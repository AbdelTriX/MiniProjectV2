package com.example.minipojectv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.minipojectv2.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesDbOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quotes.db";
    public static final String SQL_CREATE_FAVORITE_QUOTES = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            FavoriteQuotesContract.infos.TABLE_NAME,
            FavoriteQuotesContract.infos.COLUMN_NAME_ID,
            FavoriteQuotesContract.infos.COLUMN_NAME_QUOTE,
            FavoriteQuotesContract.infos.COLUMN_NAME_AUTHOR);
    public static final String SQL_DELETE_FAVORITE_QUOTES =
            "DROP TABLE IF EXISTS " + FavoriteQuotesContract.infos.TABLE_NAME;

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

    public void add(int id,String quote,String author ){
        SQLiteDatabase db= FavoriteQuotesDbOpenHelper.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteQuotesContract.infos.COLUMN_NAME_ID,id);
        values.put(FavoriteQuotesContract.infos.COLUMN_NAME_QUOTE,quote);
        values.put(FavoriteQuotesContract.infos.COLUMN_NAME_AUTHOR,author);

        db.insert(FavoriteQuotesContract.infos.TABLE_NAME,null,values);
    }
    public void Add(Quote quote){
        add(quote.getId(), quote.getQuote(), quote.getAuthor());
    }

    public void delete(int id){
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getWritableDatabase();
        String selection =  FavoriteQuotesContract.infos.COLUMN_NAME_ID + " = ? ";
        String[] selectionArgs = {Integer.toString(id)};
        db.delete(FavoriteQuotesContract.infos.TABLE_NAME, selection,selectionArgs
        );
    }
    public ArrayList<Quote> getAll(){
        ArrayList<Quote> quotes = new ArrayList<>();
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getReadableDatabase();
        String Cursour ;
        String [] project = {
                FavoriteQuotesContract.infos.COLUMN_NAME_ID,
                FavoriteQuotesContract.infos.COLUMN_NAME_QUOTE,
                FavoriteQuotesContract.infos.COLUMN_NAME_AUTHOR
        };
        Cursor cursour = db.query(
                FavoriteQuotesContract.infos.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursour.moveToNext()){
            int id = cursour.getInt(cursour.getColumnIndexOrThrow(FavoriteQuotesContract.infos.COLUMN_NAME_ID));
            String quote = cursour.getString(cursour.getColumnIndexOrThrow(FavoriteQuotesContract.infos.COLUMN_NAME_QUOTE));
            String author = cursour.getString(cursour.getColumnIndexOrThrow(FavoriteQuotesContract.infos.COLUMN_NAME_AUTHOR));

            quotes.add(new Quote(id,quote,author));
        }
        cursour.close();
        return quotes;
    }
    public boolean isFavorite (int id){
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getReadableDatabase();
        String [] project = {
                FavoriteQuotesContract.infos.COLUMN_NAME_ID,
        };
        String selection = FavoriteQuotesContract.infos.COLUMN_NAME_ID +"= ?";
        String [] selectionArgs = {Integer.toString(id)};
        Cursor cursour = db.query(
                FavoriteQuotesContract.infos.TABLE_NAME,
                project,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
        );
        boolean state = cursour.moveToNext();
        cursour.close();
        return state;
    }



}

