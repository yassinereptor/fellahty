package com.fil.fellahty;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fil.fellahty.fragments.SMSCodeDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

import static com.fil.fellahty.classes.Functions.reformate_phone;
import static com.fil.fellahty.classes.Functions.verify_google_signup;
import static com.fil.fellahty.classes.Functions.verify_signup;

public class GoogleSignupActivity extends AppCompatActivity  implements DialogInterface.OnDismissListener {

    private FirebaseAuth mAuth;
    private TextInputEditText signup_email;
    private TextInputEditText signup_name;
    public TextInputEditText signup_phone;
    private TextInputLayout input_layout_password;
    private Button btn_signup;
    private TextView btn_signin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    public PhoneAuthProvider.ForceResendingToken mResendToken;
    private AuthCredential mCredential;
    public String mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        input_layout_password = findViewById(R.id.input_layout_password);
        input_layout_password.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("FB", "onVerificationCompleted:" + credential);
                mCredential = credential;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("FB", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d("FB", "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                showSMSDialog();
                Toasty.info(GoogleSignupActivity.this, "We Sent a verification code to your phone number please validate it", Toasty.LENGTH_LONG).show();
            }
        };


        btn_signin = findViewById(R.id.btn_signin);
        btn_signup = findViewById(R.id.btn_signup);
        signup_email = findViewById(R.id.signup_email);
        signup_name = findViewById(R.id.signup_name);
        signup_phone = findViewById(R.id.signup_phone);


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_signin_activity();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signup_email.getText().toString();
                String name = signup_name.getText().toString();
                String phone = signup_phone.getText().toString();

                if(verify_google_signup(GoogleSignupActivity.this, signup_email, signup_name, signup_phone))
                    signup_user(email, name, phone);

            }
        });
    }


    private void link_user_phone(AuthCredential credential)
    {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FB", "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        } else {
                            Log.w("FB", "linkWithCredential:failure", task.getException());
                            Toasty.error(GoogleSignupActivity.this, "Authentication failed.",
                                    Toasty.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void signup_user(final String email, final String name, final String phone)
    {
        FirebaseUser user = mAuth.getCurrentUser();
        save_data_db(user, email, name, phone);
    }

    private void save_data_db(FirebaseUser fb_user, final String email, String name, final String phone)
    {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("phone", phone);
        user.put("phone_valid", false);

        // Add a new document with a generated ID
        db.collection("users").document(fb_user.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        verify_phone(reformate_phone(phone));
                        if(!email.isEmpty())
                            verify_email(email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FB", "Error adding document", e);
                        Toasty.error(GoogleSignupActivity.this, "Something went wrong !", Toasty.LENGTH_LONG).show();
                    }
                });
    }

    private void verify_phone(String phone)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                GoogleSignupActivity.this,
                mCallbacks
        );
    }

    private void verify_email(String email)
    {
        mAuth.getCurrentUser().sendEmailVerification()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.info(GoogleSignupActivity.this, "We Sent a verification email please validate it", Toasty.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(GoogleSignupActivity.this, "Something went wrong !", Toasty.LENGTH_LONG).show();
            }
        });
    }
    private PhoneAuthCredential verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        return credential;
    }

    public void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);            // ForceResendingToken from callbacks
    }

    private void open_signin_activity()
    {
        startActivity(new Intent(GoogleSignupActivity.this, MainActivity.class));
    }

    private void updateUI(FirebaseUser user)
    {
        if(user != null)
            startActivity(new Intent(GoogleSignupActivity.this, MapActivity.class));

    }

    private void showSMSDialog()
    {
        final FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("fragment_edit_name");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        SMSCodeDialogFragment editNameDialog = new SMSCodeDialogFragment();
        editNameDialog.show(getSupportFragmentManager().beginTransaction(), "fragment_edit_name");

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(mCode != null && !mCode.isEmpty())
            link_user_phone(verifyPhoneNumberWithCode(mVerificationId, mCode));
    }
}
