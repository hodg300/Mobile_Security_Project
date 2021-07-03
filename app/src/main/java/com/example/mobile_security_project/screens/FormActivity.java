package com.example.mobile_security_project.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_security_project.R;
import com.example.mobile_security_project.utils.Functions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormActivity extends AppCompatActivity {

    private MaterialButton btnCreate;
    private TextInputLayout inputType, inputName, inputAge, inputImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        btnCreate = findViewById(R.id.button_create);
        inputType = findViewById(R.id.edit_animal_type);
        inputName = findViewById(R.id.edit_animal_name);
        inputAge = findViewById(R.id.edit_animal_age);
        inputImageUrl = findViewById(R.id.edit_animal_image_url);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

    }

    private void init() {
        // Login button Click Event
        btnCreate.setOnClickListener(view -> {


            String type = Objects.requireNonNull(inputType.getEditText()).getText().toString().trim();
            String name = Objects.requireNonNull(inputName.getEditText()).getText().toString().trim();
            String age = Objects.requireNonNull(inputAge.getEditText()).getText().toString().trim();
            String imageUrl = Objects.requireNonNull(inputImageUrl.getEditText()).getText().toString().trim();

            if (!type.isEmpty() && !name.isEmpty() && !age.isEmpty() && !imageUrl.isEmpty()) {
                int ageInInteger = Integer.parseInt(age);
                    // Add animal to DB
                    addAnimal(type, name, ageInInteger, imageUrl);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void addAnimal(String type, String name, int age, String imageUrl) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Add animal data...");
        progressDialog.show();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\n    \"animalType\": \"" + type + "\",\n    \"animalName\": \""+ name+"\",\n    \"animalAge\": \""+ age +"\"\n, \"imageUrl\": \""+ imageUrl + "\"\n   }", mediaType);
        Request request = new Request.Builder()
                .url(Functions.ZOO_ROUT_CREATE_ANIMAL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Token", Functions.getAccessToken(this))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                progressDialog.dismiss();
                if(response.isSuccessful() && response.body() != null) {
                    Functions.showToast(FormActivity.this, "The animal was added successfully");
                    Intent intent = new Intent(FormActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    FormActivity.this.finish();
                }else{
                    Functions.showToast(FormActivity.this,"Session expired");
                    Intent intent = new Intent(FormActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
