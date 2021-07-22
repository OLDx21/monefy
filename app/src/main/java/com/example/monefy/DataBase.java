package com.example.monefy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class DataBase {
    public static final String PROFIT = "profit";
    public static final String COST = "cost";

    private static DataBase dataBase = new DataBase();

    private TreeMap<Date, HistoryClass> Data = new TreeMap<>();

    private ArrayList<String> cost = new ArrayList<>();

    private ArrayList<String> profit = new ArrayList<>();

    public static DataBase getInstance() {
        return dataBase;
    }

    public TreeMap<Date, HistoryClass> getData() {
        return this.Data;
    }

    public ArrayList<String> getArray(String array) {
        if (array.equals(PROFIT)) {
            return this.profit;
        } else {
            return this.cost;
        }
    }

    public void addLine(Date date, HistoryClass historyClass) {
        this.Data.put(date, historyClass);
    }

    public void addKategory(String kategory, String array) {
        if (array.equals(PROFIT)) {
            if (!profit.contains(kategory)) {
                profit.add(kategory);
            }
        } else {
            if (!cost.contains(kategory)) {
                cost.add(kategory);
            }
        }
    }

    public void CreateDB(SQLiteDatabase sqLiteDatabase) {

        Cursor cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME3, null, null, null, null, null, null);
        String name, sumaa, check, dates;
        int nameid, suma, checkint, date;
        Date date1;

        if (cursor.moveToFirst()) {
            nameid = cursor.getColumnIndex(DBhelp.NAME_COLUMN);
            suma = cursor.getColumnIndex(DBhelp.SUMA_COLUMN);
            checkint = cursor.getColumnIndex(DBhelp.CHECK_COLUMN);
            date = cursor.getColumnIndex(DBhelp.DATE_COLUMN);
            do {

                name = cursor.getString(nameid);
                sumaa = cursor.getString(suma);
                check = cursor.getString(checkint);
                dates = cursor.getString(date);
                date1 = new Date(dates);

                Data.put(date1, new HistoryClass(name, dates, sumaa, check));
                if (!cost.contains(name) && check.equals("minus")) {
                    cost.add(name);
                }
                if (!profit.contains(name) && check.equals("plus")) {
                    profit.add(name);
                }

            }

            while (cursor.moveToNext());
            cursor.close();

        }
    }


}
