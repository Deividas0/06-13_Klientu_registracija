package com.example.a06_10_klientu_registracija;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button registruoti;
    private EditText nameText, lastNameText, miestasText, phoneNumberText;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        registruoti = findViewById(R.id.registerButton);
        nameText = findViewById(R.id.nameText);
        lastNameText = findViewById(R.id.lastNameText);
        miestasText = findViewById(R.id.miestasText);
        phoneNumberText = findViewById(R.id.phoneNumber);

        registruoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String lastName = lastNameText.getText().toString();
                String miestas = miestasText.getText().toString();
                String phoneNumber = phoneNumberText.getText().toString();

                Klientas klientas = new Klientas(name, lastName, miestas, phoneNumber);
                Gson gson = new Gson();
                String json = gson.toJson(klientas);

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, json);
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/klientoDuomenuRegistracijaSqlPost")
                                .post(body)
                                .addHeader("Content-Type", "application/json")
                                .build();

                        try {
                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Handle the response if needed, e.g., show a Toast
                                        // Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Handle the error, e.g., show a Toast
                                        // Toast.makeText(MainActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Handle the exception, e.g., show a Toast
                                    // Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}