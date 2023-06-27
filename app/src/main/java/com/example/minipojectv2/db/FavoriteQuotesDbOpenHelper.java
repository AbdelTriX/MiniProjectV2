package com.example.minipojectv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.minipojectv2.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesDbOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quotes.db";
    private static final String SQL_CREATE_FAVORITE_QUOTES = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY," +
                    "%s TEXT," +
                    "%s TEXT)",
            FavoriteQuotesContract.Infos.TABLE_NAME,
            FavoriteQuotesContract.Infos.COLUMN_NAME_ID,
            FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE,
            FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR);
    private static final String SQL_DELETE_FAVORITE_QUOTES = String.format("DROP TABLE IF EXISTS %s",
            FavoriteQuotesContract.Infos.TABLE_NAME);

    public FavoriteQuotesDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE_QUOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVORITE_QUOTES);
        onCreate(db);
    }

    private void add(int id, String quote, String author) {
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_ID, id);
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE, quote);
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR, author);

        db.insert(FavoriteQuotesContract.Infos.TABLE_NAME, null, values);
    }

    public void add(Quote quote) {
        add(quote.getId(), quote.getQuote(), quote.getAuthor());
    }

    public void delete(int id) {
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getWritableDatabase();

        String selection = FavoriteQuotesContract.Infos.COLUMN_NAME_ID + " = ?";

        String[] selectionArgs = {Integer.toString(id)};

        db.delete(FavoriteQuotesContract.Infos.TABLE_NAME, selection, selectionArgs);
    }

    public ArrayList<Quote> getAll() {
        ArrayList<Quote> quotes = new ArrayList<>();
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getReadableDatabase();

        String[] projection = {
                FavoriteQuotesContract.Infos.COLUMN_NAME_ID,
                FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE,
                FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR
        };

        Cursor cursor = db.query(
                FavoriteQuotesContract.Infos.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteQuotesContract.Infos.COLUMN_NAME_ID));
            String quote = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR));

            quotes.add(new Quote(id, quote, author));
        }

        cursor.close();

        return quotes;
    }

    public boolean isFavorite(int id) {
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getReadableDatabase();

        String[] projection = {FavoriteQuotesContract.Infos.COLUMN_NAME_ID};

        String selection = FavoriteQuotesContract.Infos.COLUMN_NAME_ID + " = ?";

        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = db.query(
                FavoriteQuotesContract.Infos.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean state = cursor.moveToNext();

        cursor.close();

        return state;
    }
}