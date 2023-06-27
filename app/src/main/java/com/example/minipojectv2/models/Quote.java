package com.example.minipojectv2.models;

import android.text.Spannable;
import android.util.Log;

import androidx.annotation.NonNull;

public class Quote {
    private int id;
    private String quote;
    private String author;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Quote(int id, String quote, String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("[Quote %d] %s\n%s", getId(), getQuote(), getAuthor());
    }
}