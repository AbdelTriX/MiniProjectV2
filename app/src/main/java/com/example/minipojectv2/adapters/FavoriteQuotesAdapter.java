package com.example.minipojectv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minipojectv2.R;
import com.example.minipojectv2.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder> {
    private ArrayList<Quote> quotes;

    public FavoriteQuotesAdapter(ArrayList<Quote> quotes) {
        this.quotes = quotes;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        private  final TextView tvFavQuoteItemInfo  ;
        private  final TextView tvAuthor;
        private final ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFavQuoteItemInfo= itemView.findViewById(R.id.tvQuote);
            tvAuthor= itemView.findViewById(R.id.tvAuthore);
            imageView= itemView.findViewById(R.id.imageView);

        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_quote,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote quote = quotes.get(position);
        holder.tvFavQuoteItemInfo.setText(quote.info());
        holder.tvAuthor.setText(quote.infoA());
        holder.imageView.setImageResource(R.drawable.baseline_format_quote_24);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }



}
