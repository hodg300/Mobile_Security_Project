package com.example.mobile_security_project.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.example.authenticationlibrary.Authentication;
import com.example.authenticationlibrary.retrofit.CallBack;
import com.example.mobile_security_project.R;
import com.example.mobile_security_project.utils.Functions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Response;
import okhttp3.ResponseBody;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private MaterialButton btnLogin, btnLinkToRegister;
    private TextInputLayout inputEmail, inputPassword;

    private Authentication auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = new Authentication(Functions.IP);
        inputEmail = findViewById(R.id.edit_email);
        inputPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.button_login);
        btnLinkToRegister = findViewById(R.id.button_register);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        checkIfLoggedIn();
    }

    private void checkIfLoggedIn() {
        String accessToken = auth.checkLoggedIn(this);
        System.out.println(accessToken);
        if(accessToken != null){
            auth.verifyToken(this, accessToken, new CallBack() {
                @Override
                public void onSuccess(@NonNull @NotNull Response value) {
                    ResponseBody responseData = value.body();
                    Log.e(TAG, "onResponse: " + responseData);
                    try {
                        JSONObject jsonObject = new JSONObject(responseData.string());
                        System.out.println("json: " + jsonObject);
                        boolean isVerified = jsonObject.getBoolean("isVerified");
                        if(isVerified){
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    } catch (IOException | JSONException e) {
                        System.out.println(e);
                    }
                }

                @Override
                public void onError(@NonNull @NotNull Throwable throwable) {

                }
            });
        }

    }

    private void init() {
        // Login button Click Event
        btnLogin.setOnClickListener(view -> {
            // Hide Keyboard
            Functions.hideSoftKeyboard(LoginActivity.this);

            String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                if (Functions.isValidEmailAddress(email)) {
                    // login user
                    login(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
            }
        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

    }


    void login(String email, String password){

        auth.login(this, email, password, new CallBack() {
            @Override
            public void onSuccess(@NonNull @NotNull Response value) {
                System.out.println(value.toString());
                ResponseBody responseData = value.body();
                Log.e(TAG, "onResponse: " + responseData);
                try {

                    JSONObject jsonObject = new JSONObject(responseData.string());
                    System.out.println("json: " + jsonObject);
                    String accessToken = Functions.convertJSONObjectToAccessToken(jsonObject);
                    System.out.println("login: " + accessToken);
                    Functions.saveAccessToken(LoginActivity.this, accessToken);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    // Do something here
                } catch (IOException | JSONException e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onError(@NonNull @NotNull Throwable throwable) {
                System.out.println("failed");
                return;
            }
        });
    }

}
