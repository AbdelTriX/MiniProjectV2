package com.example.minipojectv2.models;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
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
        return String.format("Quote %d %s\n%s", getId(), getQuote(), getAuthor());
    }

    public Spannable infos() {
        SpannableStringBuilder spannable = new SpannableStringBuilder(quote);
        Spannable spannableAuthor = new SpannableString("\n→" + author);

        spannable.setSpan(
                new ForegroundColorSpan(Color.BLUE),
                0,
                spannable.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableAuthor.setSpan(new BackgroundColorSpan(Color.GRAY),
                2,
                spannableAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.insert(spannable.length(), spannableAuthor);

        return spannable;
    }
}