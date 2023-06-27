package com.example.minipojectv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.example.minipojectv2.adapters.FavoriteQuotesAdapter;
import com.example.minipojectv2.db.FavoriteQuotesDbOpenHelper;
import com.example.minipojectv2.models.Quote;

import java.util.ArrayList;

public class AllFavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView rvAllFavQuotesActList;
    FavoriteQuotesDbOpenHelper db;
    TextView tvAllFavQuotesActChooseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favorite_quotes);

        rvAllFavQuotesActList = findViewById(R.id.rvAllFavQuotesActList);
        tvAllFavQuotesActChooseLayout = findViewById(R.id.tvAllFavQuotesActChooseLayout);

        //region Persistence Objects

        db = new FavoriteQuotesDbOpenHelper(this);

        //endregion

        FavoriteQuotesAdapter adapter = new FavoriteQuotesAdapter(db.getAll());
        rvAllFavQuotesActList.setAdapter(adapter);
        rvAllFavQuotesActList.setLayoutManager(new GridLayoutManager(this, 2));

        registerForContextMenu(tvAllFavQuotesActChooseLayout);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Layout Type");
        menu.add(0, v.getId(), 0, "List");
        menu.add(0, v.getId(), 0, "Grid");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "List") {
            rvAllFavQuotesActList.setLayoutManager(new LinearLayoutManager(this));
        } else if (item.getTitle() == "Grid") {
            rvAllFavQuotesActList.setLayoutManager(new GridLayoutManager(this, 2));
        }

        return true;
    }
}
