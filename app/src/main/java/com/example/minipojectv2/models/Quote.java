package com.example.minipojectv2.models;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.webkit.WebSettings;

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
        return String.format("Quote %d , %s , %s",id,quote,author);
    }
    public Spannable info(){

        SpannableStringBuilder spannable = new SpannableStringBuilder(quote);
        spannable.setSpan(
                new AbsoluteSizeSpan(30,true),
                0,
                1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannable;
    }
    public Spannable infoA(){

        SpannableStringBuilder spannable = new SpannableStringBuilder(author);
        spannable.setSpan(
                new UnderlineSpan(),
                0,
                author.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannable;
    }
}
