package com.fil.fellahty.classes;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Functions {
    public static Boolean verify_signup(Context context, TextInputEditText signup_email, TextInputEditText signup_name, TextInputEditText signup_phone, TextInputEditText signup_password)
    {
        Boolean flag = true;
        String msg  = "";

        String email = signup_email.getText().toString();
        String name = signup_name.getText().toString();
        String phone = signup_phone.getText().toString();
        String password = signup_password.getText().toString();

        String email_req_msg = "Email is required !\n";
        String name_req_msg = "Name is required !\n";
        String phone_req_msg = "Phone is required !\n";
        String password_req_msg = "Password is required !\n";
        String email_valid_msg = "Email is unvalid !\n";
        String phone_valid_msg = "Phone is unvalid !\n";
        String password_valid_msg = "Password must be 6 - 25 !\n";

        if(email.isEmpty()) {
            msg += email_req_msg;
            flag = false;
        }
        if(name.isEmpty()) {
            msg += name_req_msg;
            flag = false;
        }
        if(phone.isEmpty()) {
            msg += phone_req_msg;
            flag = false;
        }
        if(password.isEmpty()) {
            msg += password_req_msg;
            flag = false;
        }
        if(flag)
        {
            if(!isEmailValid(email)){
                msg += email_valid_msg;
                flag = false;
                signup_email.setText("");
            }
            if(!isMobileValid(phone)){
                msg += phone_valid_msg;
                flag = false;
                signup_phone.setText("");
            }
            if(!isPasswordLenValid(password)){
                msg += password_valid_msg;
                flag = false;
            }
            if(!flag)
                Toasty.error(context, removeLastChar(msg), Toasty.LENGTH_LONG).show();
            return (flag);
        }
        Toasty.error(context, removeLastChar(msg), Toasty.LENGTH_LONG).show();
        return (flag);
    }

    public static Boolean verify_signin(Context context, TextInputEditText signup_email, TextInputEditText signup_password)
    {
        Boolean flag = true;
        String msg  = "";

        String email = signup_email.getText().toString();
        String password = signup_password.getText().toString();

        String email_req_msg = "Email is required !\n";
        String password_req_msg = "Password is required !\n";
        String email_valid_msg = "Email is unvalid !\n";
        String password_valid_msg = "Password must be 6 - 25 !\n";

        if(email.isEmpty()) {
            msg += email_req_msg;
            flag = false;
        }
        if(password.isEmpty()) {
            msg += password_req_msg;
            flag = false;
        }
        if(flag)
        {
            if(!isEmailValid(email)){
                msg += email_valid_msg;
                flag = false;
                signup_email.setText("");
            }
            if(!isPasswordLenValid(password)){
                msg += password_valid_msg;
                flag = false;
            }
            if(!flag)
                Toasty.error(context, removeLastChar(msg), Toasty.LENGTH_LONG).show();
            return (flag);
        }
        Toasty.error(context, removeLastChar(msg), Toasty.LENGTH_LONG).show();
        return (flag);
    }

    public static String reformate_phone(String phone)
    {
        return phone;
    }

    public static boolean isPasswordLenValid(String pass){
        if(pass.length() >=  6 && pass.length() <= 25)
            return true;
        return false;
    }

    public static boolean isMobileValid(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
