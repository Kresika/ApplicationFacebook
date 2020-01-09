package com.example.facebook.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

public class Validator {

    public static boolean validateFields(String field){

        if (TextUtils.isEmpty(field)) {
            return false;
        }

        return true;
    }

    public static boolean validateEmail(String email){
        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else{
            return true;
        }


    }





}
