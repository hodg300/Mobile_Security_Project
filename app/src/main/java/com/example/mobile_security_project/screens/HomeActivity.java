package com.example.mobile_security_project.screens;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authenticationlibrary.Authentication;
import com.example.mobile_security_project.R;
import com.example.mobile_security_project.adapters.MyAdapter;
import com.example.mobile_security_project.utils.Functions;
import com.example.mobile_security_project.utils.ListItem;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class HomeActivity extends AppCompatActivity {
    private MaterialButton btnLogout, btnAddAnimal;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItem;
    private Authentication auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = new Authentication(Functions.IP);
        btnLogout = findViewById(R.id.logout);
        btnAddAnimal = findViewById(R.id.add_btn);
        recyclerView = findViewById(R.id.recyclerViewAnimals);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

        listItem = new ArrayList<>();

        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        getAllAnimalsFromServer(progressDialog);
    }

    private void getAllAnimalsFromServer(ProgressDialog progressDialog) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Functions.ZOO_ROUT)
                .method("GET", null)
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
                    ResponseBody responseData = response.body();
                    try {
                        JSONArray array = new JSONArray(responseData.string());
                        setRecycleView(array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void setRecycleView(JSONArray array){

            try {
                for(int i = 0; i< array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    System.out.println("obj: " + object.toString());
                    ListItem item = new ListItem(
                            object.getString("animalType"),
                            object.getString("animalName"),
                            object.getString("imageUrl"),
                            object.getInt("animalAge")
                    );
                    listItem.add(item);
                    System.out.println("animal: " + item);
                }

                adapter = new MyAdapter(listItem, getApplicationContext());
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    private void init() {
        btnLogout.setOnClickListener(v -> logoutUser());
        btnAddAnimal.setOnClickListener(v -> goToAddAnimalForm());

    }

    private void goToAddAnimalForm() {
        Intent intent = new Intent(HomeActivity.this, FormActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        auth.signOut(HomeActivity.this);

        // Sign out and launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}