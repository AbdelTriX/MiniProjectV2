package com.example.minipojectv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvSartActQuote, tvStartActAuthor;
    Button btnStartActPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvActAuthor);
        btnStartActPass = findViewById(R.id.btnStartActPass);

//        tvSartActQuote.setText("You miss 100% of the shots you don’t take.");
//        tvStartActAuthor.setText("Wayne Gretzky");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvSartActQuote.setText(jsonObject.getString("quote"));
                            tvStartActAuthor.setText(jsonObject.getString("author"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        tvSartActQuote.setText("That didn't work!");
                    }
        });
        // Set the tag on the request.
        stringRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        btnStartActPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}