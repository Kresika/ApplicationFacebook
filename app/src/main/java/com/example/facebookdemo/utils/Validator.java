package com.example.facebookdemo.utils;

import android.text.TextUtils;
import android.util.Patterns;

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
