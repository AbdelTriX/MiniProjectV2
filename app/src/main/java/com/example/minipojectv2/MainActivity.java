package com.example.minipojectv2;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.minipojectv2.db.FavoriteQuotesDbOpenHelper;
import com.example.minipojectv2.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    TextView tvStartActQuote,tvStartActAuthor,tvStartActid;
    Button ShowAllFav;
    ToggleButton tbStartActPinUnpin;
    private final static int INVALIDE_ID = -1;
    SharedPreferences sharedPreferences;
    ImageView ivStartActIsFavorite;
    boolean isFavorite = false;
    FavoriteQuotesDbOpenHelper db ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvStartActAuthor);
        ShowAllFav = findViewById(R.id.btnStartActShowAllFavQuotes);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);
        ivStartActIsFavorite = findViewById(R.id.ivStartActIsFavorite);
        tvStartActid = findViewById(R.id.tvStartActId);
        ////////////
        db = new FavoriteQuotesDbOpenHelper(this);
        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        ////////////

        ivStartActIsFavorite.setOnClickListener(v-> {
            int id=-1;
            id =Integer.parseInt(tvStartActid.getText().toString());
            boolean isFavorite = db.isFavorite(id);

            if(isFavorite) {
                tbStartActPinUnpin.setChecked(true);

                ivStartActIsFavorite.setImageResource(R.drawable.dislike);

                db.delete(id);

            }
            else{
                ivStartActIsFavorite.setImageResource(R.drawable.like);
                String quote =tvStartActQuote.getText().toString();
                String author =tvStartActAuthor.getText().toString();
                db.Add(new Quote(id,quote,author));

            }
            logFavoriteQuotes();

        });


        //region pin AND unpin

        int pinnedQuoteid = sharedPreferences.getInt("id",INVALIDE_ID);

        if (pinnedQuoteid == INVALIDE_ID) {
            getRundomQuote();
        }else {
            String author = sharedPreferences.getString("author",null);
            String quote = sharedPreferences.getString("quote",null);


            tvStartActQuote.setText(quote);
            tvStartActAuthor.setText(author);
            tvStartActid.setText(String.format("#%d",pinnedQuoteid));

            ivStartActIsFavorite.setImageResource(db.isFavorite(pinnedQuoteid)?R.drawable.like :R.drawable.dislike);
            tbStartActPinUnpin.setChecked(false);
        }

        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int id = INVALIDE_ID;
                String quote = null;
                String author = null;


                if (isChecked) {
                    id = Integer.valueOf(tvStartActid.getText().toString());
                    quote = tvStartActQuote.getText().toString();
                    author = tvStartActAuthor.getText().toString();

                    if (!db.isFavorite(id)) {
                        ivStartActIsFavorite.setImageResource(R.drawable.like);
                        db.Add(new Quote(id, quote, author));
                        logFavoriteQuotes();
                    }
                } else {
                    getRundomQuote();
                }
                editor.putInt("id",id);
                editor.putString("quote",quote);
                editor.putString("author",author);
                editor.commit();
            }
        });
        //endregion
        ShowAllFav.setOnClickListener(v -> {
            Intent intent= new Intent(this,AllFavoriteQuotesActivity.class);
            startActivity(intent);
        });
    }

    private void logFavoriteQuotes() {
        ArrayList<Quote> quotes= db.getAll();
        for (Quote q : quotes){
            Log.e("SQLITE",q.toString());
        }
    }

    private  void getRundomQuote() {
        RequestQueue queue = Volley.newRequestQueue(this);
//        Random random = new Random();
//        int randomNumber = random.nextInt(3) + 1;
        String url = "https://dummyjson.com/quotes/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tvStartActid.setText(String.format("%d",response.getInt("id")));
                            tvStartActQuote.setText(response.getString("quote"));
                            tvStartActAuthor.setText(response.getString("author"));
                            int id = response.getInt("id");

                            if (db.isFavorite(id)){
                                ivStartActIsFavorite.setImageResource(R.drawable.like);
                            }else
                                ivStartActIsFavorite.setImageResource(R.drawable.dislike);

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