package com.example.minipojectv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView tvSartActQuote, tvStartActAuthor;
    Button btnStartActPass;
    ToggleButton tbStartActPinUnpin;
    SharedPreferences sharedPreferences;
    Spinner spinner;
    private int selectedColorPosition = 0;

    private String[] colors = {"Default", "LightSalmon", "Plum", "PaleGreen", "CornFlowerBlue"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvActAuthor);
        btnStartActPass = findViewById(R.id.btnStartActPass);
        spinner = findViewById(R.id.spActColors);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);


        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        String quote = sharedPreferences.getString("quote",null);
        String author = sharedPreferences.getString("author", null);
        selectedColorPosition = sharedPreferences.getInt("color_position", 0);






        if (quote == null) {
            getRandomQuote();
        } else {
            tvSartActQuote.setText(quote);
            tvStartActAuthor.setText(author);
            tbStartActPinUnpin.setChecked(true);
            setSpinnerSelection(selectedColorPosition);
            setActivityBackgroundColor(selectedColorPosition);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedColorPosition); // Set the empty selection
        spinner.setOnItemSelectedListener(this);





        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*
                Checked -> PIN
                UnChecked -> UNPIN
                 */

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    // Store quote somewhere
                    editor.putString("quote", tvSartActQuote.getText().toString());
                    editor.putString("author", tvStartActAuthor.getText().toString());
                    editor.putInt("color_position", spinner.getSelectedItemPosition());

                }else {
                    // Remove the stored quote
                    editor.putString("quote",null);
                    editor.putString("author",null);
                    editor.remove("color_position");
                }

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
    private void setSpinnerSelection(int position) {
        spinner.setSelection(position);
    }
    private void setActivityBackgroundColor(int position) {
        int backgroundColor;
        switch (position) {
            case 0:
                backgroundColor = Color.WHITE;
                break;
            case 1:
                backgroundColor = getResources().getColor(R.color.LightSalmon);
                break;
            case 2:
                backgroundColor = getResources().getColor(R.color.Plum);
                break;
            case 3:
                backgroundColor = getResources().getColor(R.color.PaleGreen);
                break;
            case 4:
                backgroundColor = getResources().getColor(R.color.CornflowerBlue);
                break;
            default:
                backgroundColor = Color.WHITE;
                break;
        }
        getWindow().getDecorView().setBackgroundColor(backgroundColor);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setActivityBackgroundColor(position);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("color_position", position);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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