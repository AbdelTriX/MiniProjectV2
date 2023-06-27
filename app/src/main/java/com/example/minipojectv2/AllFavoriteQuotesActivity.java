package com.example.minipojectv2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.RecoverySystem;

import com.example.minipojectv2.adapters.FavoriteQuotesAdapter;
import com.example.minipojectv2.db.FavoriteQuotesDbOpenHelper;
import com.example.minipojectv2.models.Quote;

import java.util.ArrayList;

public class AllFavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView rvAllFavQuotesActList;
    FavoriteQuotesDbOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favorite_quotes);

        rvAllFavQuotesActList = findViewById(R.id.rvAllFavQuotesActList);

        //region Persistence Objects

        db = new FavoriteQuotesDbOpenHelper(this);

        //endregion

        FavoriteQuotesAdapter adapter = new FavoriteQuotesAdapter(db.getAll());
        rvAllFavQuotesActList.setAdapter(adapter);
        rvAllFavQuotesActList.setLayoutManager(new GridLayoutManager(this, 2));
    }
}