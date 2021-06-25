package com.example.mobile_security_project.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authenticationlibrary.Authentication;
import com.example.authenticationlibrary.model.User;
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


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    public final static String USER = "user";

    private MaterialButton btnRegister, btnLinkToLogin;
    private TextInputLayout inputName, inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = findViewById(R.id.edit_name);
        inputEmail = findViewById(R.id.edit_email);
        inputPassword = findViewById(R.id.edit_password);
        btnRegister = findViewById(R.id.button_register);
        btnLinkToLogin = findViewById(R.id.button_login);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        // Login button Click Event
        btnRegister.setOnClickListener(view -> {

            String name = Objects.requireNonNull(inputName.getEditText()).getText().toString().trim();
            String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();

            // Check for empty data in the form
            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if (Functions.isValidEmailAddress(email)) {
                    registerUser(email, password, name);
                } else {
                    Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                    // Hide Keyboard
                    Functions.hideSoftKeyboard(RegisterActivity.this);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                // Hide Keyboard
                Functions.hideSoftKeyboard(RegisterActivity.this);
            }
        });

        // Link to Register Screen
        btnLinkToLogin.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void registerUser(String email, String password, String fullName){
        Authentication auth = new Authentication(Functions.IP);
        auth.register(this, email, password, fullName, new CallBack() {
            @Override
            public void onSuccess(@NonNull @NotNull Response value) {
                System.out.println(value.toString());
                ResponseBody responseData = value.body();
                Log.e(TAG, "onResponse: " + responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData.string());
                    User user = convertJSONObjectToUser(jsonObject);
                    auth.login(RegisterActivity.this, user.getEmail(), password, new CallBack() {
                        @Override
                        public void onSuccess(@NonNull @NotNull Response value) {
                            try {
                                JSONObject jsonObject = new JSONObject(value.body().string());
                                String accessToken = jsonObject.getString("accessToken");
                                Functions.saveAccessToken(RegisterActivity.this, accessToken);
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(@NonNull @NotNull Throwable throwable) {
                        }
                    });
                } catch (JSONException | IOException ignored) { }
            }

            @Override
            public void onError(@NonNull @NotNull Throwable throwable) {
                System.out.println("failed");
            }
        });
    }

    private User convertJSONObjectToUser(JSONObject jsonObject) {
        User user = null;
        try {
            user = new User(
                    jsonObject.getString("email"),
                    jsonObject.getString("fullName")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

}
