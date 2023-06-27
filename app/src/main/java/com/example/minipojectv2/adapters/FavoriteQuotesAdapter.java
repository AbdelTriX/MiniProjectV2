package com.example.minipojectv2.adapters;

import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minipojectv2.R;
import com.example.minipojectv2.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder> {
    private ArrayList<Quote> quotes;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFavQuoteItemInfos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFavQuoteItemInfos = itemView.findViewById(R.id.tvFavQuoteItemInfos);
        }
    }

    public FavoriteQuotesAdapter(ArrayList<Quote> quotes) {
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_quote, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote quote = quotes.get(position);

        if (position % 2 == 0)
            holder.itemView.setBackgroundColor(Color.parseColor("#B799FF"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#E6FFFD"));

        holder.tvFavQuoteItemInfos.setText(quote.infos());
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }


}