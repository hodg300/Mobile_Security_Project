package com.example.mobile_security_project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;


public class Functions {

    public static String IP = "192.168.1.4";
    public static String ZOO_ROUT = "http://" + IP + ":3006/";


    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void hideProgressDialog(Context context) {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }


    public static void saveAccessToken(Context context, String accessToken){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("access_token", accessToken);
        editor.commit();
    }


    public static String getAccessToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        return pref.getString("access_token", null);
    }

    public static void deleteAccessToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("access_token");
        editor.commit();
    }

    public static String convertJSONObjectToAccessToken(JSONObject jsonObject) {
        String accessToken = null;
        try {
            accessToken = jsonObject.getString("accessToken");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return accessToken;
    }


}
