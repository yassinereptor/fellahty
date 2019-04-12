package com.fil.fellahty.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fil.fellahty.MainActivity;
import com.fil.fellahty.R;
import com.fil.fellahty.SignupActivity;

import es.dmoral.toasty.Toasty;


public class SMSCodeDialogFragment extends DialogFragment {

    private TextInputEditText mEditText;
    private Button btn_valid_sms_code;
    private Button btn_skip_sms_code;
    private boolean flag;


    public interface UserNameListener {
        void onFinishUserDialog(String user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_val_dialog, container);
        mEditText = view.findViewById(R.id.signup_code_sms);
        btn_valid_sms_code = view.findViewById(R.id.btn_valid_sms_code);
        btn_skip_sms_code = view.findViewById(R.id.btn_skip_sms_code);
        flag = false;
        btn_valid_sms_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity mainActivity = (SignupActivity)getActivity();
                if(!mEditText.getText().toString().isEmpty())
                {
                    flag = true;
                    ((SignupActivity) mainActivity).mCode = mEditText.getText().toString();
                    dismiss();
                }
            }
        });

        btn_skip_sms_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                dismiss();
            }
        });
        return view;
    }

}