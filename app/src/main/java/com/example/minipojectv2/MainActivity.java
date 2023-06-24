package com.example.minipojectv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    TextView tvSartActQuote, tvStartActAuthor;
    Button btnStartActPass;
    ToggleButton tbStartActPinUnpin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvActAuthor);
        btnStartActPass = findViewById(R.id.btnStartActPass);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);

        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        String quote = sharedPreferences.getString("quote",null);
        String author = sharedPreferences.getString("author", null);



        if (quote == null) {
            getRandomQuote();
        } else {
            tvSartActQuote.setText(quote);
            tvStartActAuthor.setText(author);
            tbStartActPinUnpin.setChecked(true);

        }


        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*
                Checked -> PIN
                UnChecked -> UNPIN
                 */

                String quote = null;
                String author = null;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    // Store quote somewhere
                    editor.putString("quote", tvSartActQuote.getText().toString());
                    editor.putString("author", tvStartActAuthor.getText().toString());
                }else {
                    // Remove the stored quote
                    getRandomQuote();
                }
                editor.putString("quote",quote);
                editor.putString("author",author);
                editor.apply();
            }
        });

        btnStartActPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getRandomQuote() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tvSartActQuote.setText(response.getString("quote"));
                    tvStartActAuthor.setText(response.getString("author"));

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}