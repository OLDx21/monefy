package com.example.monefy;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Display;
import androidx.appcompat.app.AlertDialog;
import com.example.monefy.activitys.MainActivity;
import com.example.monefy.activitys.MinusActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class Action {
    public static HashMap<Integer, AlertDialog> alertDialog = new HashMap<>();
    public static Date date;
    public static int checked;
    public static Display display;
    public static HashMap<String, String> NamesAndValues = new HashMap<>();
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    public static SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("uk", "UA"));
    public static TreeMap<Date, HistoryClass> SelectedDay = new TreeMap<Date, HistoryClass>(Collections.reverseOrder());
    public static HashMap<String, Double> NamesAndValuesForSelectedDay = new HashMap<>();
    static SQLiteDatabase sqLiteDatabase;
    static DBhelp dBhelp;
    public static LinkedHashMap<String, NamesAndValues> NamesAndValuesForYears = new LinkedHashMap<>();
    public static TreeMap<Date, NamesAndValues> NamesAndValuesForDays = new TreeMap<Date, NamesAndValues>();
    public static LinkedHashMap<String, NamesAndValues> NamesAndValuesForWeeks = new LinkedHashMap<>();
    public static LinkedHashMap<Date, NamesAndValues> NamesAndValuesForMonth = new LinkedHashMap<>();
    public static Integer position;


    public static void Createdb(Context context) {
        dBhelp = new DBhelp(context);
        sqLiteDatabase = dBhelp.getWritableDatabase();
    }

    public static SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public static DBhelp getdBhelp() {
        return dBhelp;
    }


}
