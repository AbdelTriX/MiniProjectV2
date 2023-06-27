package com.example.minipojectv2.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.minipojectv2.models.Colors;

import java.util.ArrayList;

public class SettingsDbHelper extends SQLiteOpenHelper {
    public final static String DATA_NAME="Settings.db";
    public final static int DATA_VERSION=1;
    //region table color
    public final static String TABLE_NAME_COLOR ="color";
    public final static String COLUMN_ID_COLOR ="id";
    public final static String COLUMN_NAME_COLOR ="name";
    public final static String COLUMN_CODE="code";
    //endregion

    //region table setting
    public final static String TABLE_NAME_SETTING="setting";
    public final static String COLUMN_ID_SETTING="id";
    public final static String COLUMN_NAME_SETTING="name";
    public final static String COLUMN_VALUE="value";
    //endregion

    //region COLOR STRING
    public static final String SQL_CREATE_COLOR = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE_NAME_COLOR, COLUMN_ID_COLOR,
            COLUMN_NAME_COLOR, COLUMN_CODE);

    public static final String SQL_DELETE_COLOR =
            "DROP TABLE IF EXISTS " + TABLE_NAME_COLOR;
    //endregion

    //region SETTING STRING
    public static final String SQL_CREATE_SETTING = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE_NAME_SETTING, COLUMN_ID_SETTING,
            COLUMN_NAME_SETTING, COLUMN_VALUE);

    public static final String SQL_DELETE_SETTING =
            "DROP TABLE IF EXISTS " + TABLE_NAME_SETTING;
    //endregion


    public SettingsDbHelper(@Nullable Context context) {
        super(context, DATA_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COLOR);
        db.execSQL(SQL_CREATE_SETTING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_COLOR);
        db.execSQL(SQL_DELETE_SETTING);
        onCreate(db);
    }
    public void AddCOLOR(Colors colors){
        addcolor(colors.getName(), colors.getCode());
    }
    public void addcolor(String name,String code ){
        SQLiteDatabase db= SettingsDbHelper.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_COLOR,name);
        values.put(COLUMN_CODE,code);
        db.insert(TABLE_NAME_COLOR,null,values);
    }
    public void addbgcolor(String name, String value) {
        SQLiteDatabase db = SettingsDbHelper.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SETTING, name);
        values.put(COLUMN_VALUE, value);

        int rowCount = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_SETTING);

        if (rowCount == 0) {
            db.insert(TABLE_NAME_SETTING, null, values);
        } else {
            db.update(TABLE_NAME_SETTING, values, null, null);
        }
    }

    public ArrayList<Colors> getAll(){
        ArrayList<Colors> colors = new ArrayList<>();
        SQLiteDatabase db = SettingsDbHelper.this.getReadableDatabase();

        String [] project = {
                COLUMN_NAME_COLOR,
                COLUMN_CODE
        };
        Cursor cursour = db.query(
                TABLE_NAME_COLOR,
                project,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursour.moveToNext()){
            String name = cursour.getString(cursour.getColumnIndexOrThrow(COLUMN_NAME_COLOR));
            String code = cursour.getString(cursour.getColumnIndexOrThrow(COLUMN_CODE));

            colors.add(new Colors(name,code));
        }
        cursour.close();
        return colors;
    }
    public boolean isEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_COLOR, null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }

        cursor.close();
        return true;
    }
    public String getBg(){
        SQLiteDatabase db = SettingsDbHelper.this.getReadableDatabase();
        String code=null;
        String [] project = {
                COLUMN_NAME_SETTING,
                COLUMN_VALUE
        };
        Cursor cursour = db.query(
                TABLE_NAME_SETTING,
                project,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursour.moveToNext()){
            code = cursour.getString(cursour.getColumnIndexOrThrow(COLUMN_VALUE));

        }
        cursour.close();
        return code;
    }
    public boolean isEmptyBg() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_SETTING, null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }

        cursor.close();
        return true;
    }
}
