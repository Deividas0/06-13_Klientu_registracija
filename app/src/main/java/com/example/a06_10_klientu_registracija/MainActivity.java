package com.example.a06_10_klientu_registracija;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public Button registruotiButtonMainLangas;
    public Button prisijungtiButtonMainLangas;
    public Button registerButtonRegisterLayout;
    public EditText usernameRegisterLayout, passwordRegisterLayout, nameTextRegisterLayout, lastNameTextRegisterLayout,
            miestasTextRegisterLayout, phoneNumberRegisterLayout;

    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        prisijungtiButtonMainLangas = findViewById(R.id.prisijungimoLayoutPrisijungti);
        registruotiButtonMainLangas = findViewById(R.id.registerButtonMainLayout);

        registruotiButtonMainLangas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.registracijos_ekranas);
                registerButtonRegisterLayout = findViewById(R.id.registerButtonRegisterLayout);

                registerButtonRegisterLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        usernameRegisterLayout = findViewById(R.id.usernameRegisterLayout);
                        passwordRegisterLayout = findViewById(R.id.passwordRegisterLayout);

                        String username = usernameRegisterLayout.getText().toString();
                        String password = passwordRegisterLayout.getText().toString();

                        Klientas klientas = new Klientas(username, password);
                        Gson gson = new Gson();
                        String json = gson.toJson(klientas);
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpClient client = new OkHttpClient();

                                MediaType mediaType = MediaType.parse("application/json");
                                RequestBody body = RequestBody.create(mediaType, json);
                                Request request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/klientoRegistracijaySqlPost")
                                        .post(body)
                                        .addHeader("Content-Type", "application/json")
                                        .build();

                                Response response = null;
                                try {
                                    response = client.newCall(request).execute();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                if (response.isSuccessful()) {
                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Handle the response if needed, e.g., show a Toast
                                            Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                            



                                            setContentView(R.layout.duomenu_atnaujinimas);
                                            Button atnaujintiButtonDuomenuNaujinimasLayout = findViewById(R.id.atnaujintiButtonDuomenuNaujinimasLayout);
                                            atnaujintiButtonDuomenuNaujinimasLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    nameTextRegisterLayout = findViewById(R.id.nameTextRegisterLayout);
                                                    lastNameTextRegisterLayout = findViewById(R.id.lastNameTextRegisterLayout);
                                                    miestasTextRegisterLayout = findViewById(R.id.miestasTextRegisterLayout);
                                                    phoneNumberRegisterLayout = findViewById(R.id.phoneNumberRegisterLayout);

                                                    String vardas = nameTextRegisterLayout.getText().toString();
                                                    String pavarde = lastNameTextRegisterLayout.getText().toString();
                                                    String miestas = miestasTextRegisterLayout.getText().toString();
                                                    String telNumeris = phoneNumberRegisterLayout.getText().toString();

                                                    Klientas klientas = new Klientas(vardas, pavarde, miestas, telNumeris);
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

                                                            Response response = null;
                                                            try {
                                                                response = client.newCall(request).execute();
                                                            } catch (IOException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                            if (response.isSuccessful()) {
                                                                mainHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        //Handle the response if needed, e.g., show a Toast
                                                                        Toast.makeText(MainActivity.this, "Duomenys sėkmingai atnaujinti!", Toast.LENGTH_SHORT).show();
                                                                        setContentView(R.layout.activity_main);
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    public void sendEmail(String subject, String content, String to_email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to_email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client:"));
    }
}





