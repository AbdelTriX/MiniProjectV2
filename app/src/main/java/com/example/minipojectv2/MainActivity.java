package com.example.minipojectv2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.minipojectv2.db.FavoriteQuotesDbOpenHelper;
import com.example.minipojectv2.db.SettingsDbHelper;
import com.example.minipojectv2.models.Colors;
import com.example.minipojectv2.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    TextView tvStartActQuote,tvStartActAuthor,tvStartActid;
    Button pass;
    ToggleButton tbStartActPinUnpin;
    private final static int INVALIDE_ID = -1;
    SharedPreferences sharedPreferences;
    ImageView ivStartActIsFavorite;
    boolean isFavorite = false;
    FavoriteQuotesDbOpenHelper db ;
    SettingsDbHelper db1;
    ArrayList<Colors> colors=new ArrayList<>();
    Spinner spinner;
    View bg;
    int selectedSpinnerPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvStartActAuthor);
        pass = findViewById(R.id.pass);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);
        ivStartActIsFavorite = findViewById(R.id.ivStartActIsFavorite);
        tvStartActid = findViewById(R.id.tvStartActid);
        spinner=findViewById(R.id.spinner);
        bg= findViewById(R.id.bg);
        ////////////
        db = new FavoriteQuotesDbOpenHelper(this);
        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        ////////////
        db1 =new SettingsDbHelper(this);
        //region insert to table color
        if (db1.isEmpty()) {
            db1.AddCOLOR(new Colors("Default       ", "#FFFFFF"));
            db1.AddCOLOR(new Colors("LightSalmon   ", "#FFA07A"));
            db1.AddCOLOR(new Colors("Plum          ", "#DDA0DD"));
            db1.AddCOLOR(new Colors("PaleGreen     ", "#98FB98"));
            db1.AddCOLOR(new Colors("CornflowerBlue", "#6495ED"));
        }
        //endregion
        //region spinner
        colors=db1.getAll();
        ArrayAdapter<Colors> adapter=new ArrayAdapter<>(MainActivity.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,colors);
        spinner.setAdapter(adapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int p = 0;
                switch (position){
                    case 0:
                        bg.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        p=0;
                        break;
                    case 1:
                        bg.setBackgroundColor(Color.parseColor("#FFA07A"));
                        p=1;

                        break;
                    case 2:
                        bg.setBackgroundColor(Color.parseColor("#DDA0DD"));
                        p=2;

                        break;
                    case 3:
                        bg.setBackgroundColor(Color.parseColor("#98FB98"));
                        p=3;

                        break;
                    case 4:
                        bg.setBackgroundColor(Color.parseColor("#6495ED"));
                        p=4;

                        break;
                }
                db1.addbgcolor("Bgcolor", String.valueOf(p));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //endregion

        if (db1.isEmptyBg()){
            bg.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {
            String colorbg=db1.getBg();
            Integer p = Integer.valueOf(colorbg);
            spinner.setSelection(p);
        }

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

            ivStartActIsFavorite.setImageResource(db.isFavorite(pinnedQuoteid)?R.drawable.baseline_favorite_24 :R.drawable.dislike);
            tbStartActPinUnpin.setChecked(false);
        }

        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int id = INVALIDE_ID;
                String quote = null;
                String author = null;


                if (!isChecked){
                    id = Integer.valueOf(tvStartActid.getText().toString());
                    quote= tvStartActQuote.getText().toString();
                    author= tvStartActAuthor.getText().toString();

                    if (!db.isFavorite(id)){
                        ivStartActIsFavorite.setImageResource(R.drawable.baseline_favorite_24);
                        db.Add(new Quote(id,quote,author));
                        logFavoriteQuotes();
                    }
                }else {
                    getRundomQuote();
                }
                editor.putInt("id",id);
                editor.putString("quote",quote);
                editor.putString("author",author);
                editor.commit();
            }
        });
        //endregion
        pass.setOnClickListener(v -> {
            finish();
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
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;
        String url = "https://dummyjson.com/quotes/"+randomNumber;

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
                                ivStartActIsFavorite.setImageResource(R.drawable.baseline_favorite_24);
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