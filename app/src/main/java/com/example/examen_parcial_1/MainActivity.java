package com.example.examen_parcial_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private EditText etCedula;
    private Button btnComprobar;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etCedula = findViewById(R.id.txt_ingresar);
        btnComprobar = findViewById(R.id.button);
        tvResultado = findViewById(R.id.txt_resultado);


        btnComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cedula = etCedula.getText().toString().trim();


                new ComprobarCedulaTask().execute(
                        String.format(
                                "http://10.10.18.90:3000/validar?cedula=%s",
                                cedula
                        )
                );
            }
        });
    }

    private class ComprobarCedulaTask extends AsyncTask<String, Void, String> {
        private final OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Request request = new Request.Builder()
                        .url(urls[0])
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            if (resultado.startsWith("Error:")) {
                tvResultado.setText(resultado);
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(resultado);
                String message = jsonObject.getString("message");

                // Mostrar el mensaje en el TextView
                tvResultado.setText(message);

            } catch (Exception e) {
                tvResultado.setText("Error al procesar la respuesta");
            }
        }
    }
}