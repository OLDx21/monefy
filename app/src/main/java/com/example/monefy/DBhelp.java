package com.example.monefy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelp extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "saversec";
    public static final int VERSION = 1;


    public static final String TABLE_NAME1 = "objects";
    public static final  String NAMES_COLUMS = "names";
    public static final  String VALUES_COLUMNS = "value";

    public static final String TABLE_NAME2 = "addmoney";
    public static final  String DATE_COLUMS = "date";
    public static final  String SUMA_COLUMS = "suma";
    public static final  String TAG_COLUMS = "tag";

    public static final String TABLE_NAME3 = "history";
    public static final String CHECK_COLUMN = "checked";
    public static final String NAME_COLUMN = "name2";
    public static final String DATE_COLUMN = "date2";
    public static final String SUMA_COLUMN = "sumaa";



    public DBhelp(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME1 + "(" + NAMES_COLUMS + " text," + VALUES_COLUMNS + " text"+")");
        db.execSQL("create table " + TABLE_NAME2 + "(" + DATE_COLUMS + " text," + SUMA_COLUMS + " text,"+ TAG_COLUMS + " text"+")");
        db.execSQL("create table " + TABLE_NAME3 + "(" + CHECK_COLUMN + " text," + NAME_COLUMN + " text,"+ DATE_COLUMN + " date,"+ SUMA_COLUMN+ " text"+")");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME1);
        db.execSQL("drop table if exists " + TABLE_NAME2);
        db.execSQL("drop table if exists " + TABLE_NAME3);

        onCreate(db);
    }
}
