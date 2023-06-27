package com.example.minipojectv2;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.example.minipojectv2.models.Quote;
import com.example.minipojectv2.db.FavoriteQuotesDbOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    private final static int INVALIDE_ID = -1;

    TextView tvStartActQuote, tvStartActAuthor;
    Button btnStartActPass;
    ToggleButton tbStartActPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView ivStartActIsFavorite;
    FavoriteQuotesDbOpenHelper db;
    TextView tvStartActId;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvStartActAuthor);
        btnStartActPass = findViewById(R.id.btnStartActPass);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);
        ivStartActIsFavorite = findViewById(R.id.ivStartActIsFavorite);
        tvStartActId = findViewById(R.id.tvStartActId);

        //region Persistence Objects

        db = new FavoriteQuotesDbOpenHelper(this);
        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);

        //endregion

        //region Pin | Unpin Quote

        int pinnedQuoteId = sharedPreferences.getInt("id", INVALIDE_ID);

        if (pinnedQuoteId == INVALIDE_ID) {
            getRandomQuote();
        } else {
            String quote = sharedPreferences.getString("quote", null);
            String author = sharedPreferences.getString("author", null);

            tvStartActId.setText(String.format("#%d", pinnedQuoteId));
            tvStartActQuote.setText(quote);
            tvStartActAuthor.setText(author);

            ivStartActIsFavorite.setImageResource(db.isFavorite(pinnedQuoteId) ? R.drawable.like : R.drawable.dislike);

            tbStartActPinUnpin.setChecked(true);
        }

        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int pinnedQuoteId = INVALIDE_ID;
                String quote = null;
                String author = null;

                if (isChecked) {
                    pinnedQuoteId = Integer.parseInt(tvStartActId.getText().toString().substring(1));
                    quote = tvStartActQuote.getText().toString();
                    author = tvStartActAuthor.getText().toString();

                    if (!db.isFavorite(pinnedQuoteId)) {
                        ivStartActIsFavorite.setImageResource(R.drawable.like);

                        db.add(new Quote(pinnedQuoteId, quote, author));

                        //region ToDo: Delete

                        logFavoriteQuotes();

                        //endregion
                    }
                } else {
//                    getRandomQuote();
                }

                editor.putInt("id", pinnedQuoteId);
                editor.putString("quote", quote);
                editor.putString("author", author);

                editor.commit();
            }
        });

        //endregion

        //region Like | Dislike Quote

        ivStartActIsFavorite.setOnClickListener(v -> {
            int id = Integer.parseInt(tvStartActId.getText().toString().substring(1));
            boolean isFavorite = db.isFavorite(id);

            if (isFavorite) {
                tbStartActPinUnpin.setChecked(false);

                ivStartActIsFavorite.setImageResource(R.drawable.dislike);

                db.delete(id);
            } else {
                ivStartActIsFavorite.setImageResource(R.drawable.like);

                String quote = tvStartActQuote.getText().toString();
                String author = tvStartActAuthor.getText().toString();

                db.add(new Quote(id, quote, author));
            }

            //region ToDo: Delete

            logFavoriteQuotes();

            //endregion
        });

        //endregion

        btnStartActPass.setOnClickListener(v -> {
            finish();
        });
    }

    //region ToDo: Delete

    private void logFavoriteQuotes() {
        ArrayList<Quote> quotes = db.getAll();
        for (Quote quote : quotes) {
            Log.e("SQLite", quote.toString());
        }
    }

    //endregion

    private void getRandomQuote() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        //region ToDo: Delete

//        int randomNumber = ThreadLocalRandom.current().nextInt(1, 5 + 1);
//        String url = String.format("https://dummyjson.com/quotes/%d", randomNumber);

        //endregion

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = response.getInt("id");
                            String quote = response.getString("quote");
                            String author = response.getString("author");

                            if (db.isFavorite(id))
                                ivStartActIsFavorite.setImageResource(R.drawable.like);
                            else
                                ivStartActIsFavorite.setImageResource(R.drawable.dislike);

                            tvStartActId.setText(String.format("#%d", id));
                            tvStartActQuote.setText(quote);
                            tvStartActAuthor.setText(author);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(jsonObjectRequest);
    }
}