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

    private ArrayList<String> AllKategories = new ArrayList<>();

    private ArrayList<String> AllKategoriesProfit = new ArrayList<>();

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

    public void addIntoAllKategoryProfit(String kategory){
        this.AllKategoriesProfit.add(kategory);
    }

    public ArrayList<String> getAllKategoriesProfit(){
        return this.AllKategoriesProfit;
    }

    public void addIntoAllKategory(String kategory, String array){
        if(array.equals(COST)) {
            this.AllKategories.add(kategory);
        }
        else {
            this.AllKategoriesProfit.add(kategory);

        }
    }

    public ArrayList<String> getAllKategories(){
        return this.AllKategories;
    }

    public void addLine(Date date, HistoryClass historyClass) {
        this.Data.put(date, historyClass);
    }

    public void UpdateLine(Date whereDate, HistoryClass historyClass) {
        this.Data.put(whereDate, historyClass);
    }

    public void DeleteLine(Date whereDate ) {
        this.Data.remove(whereDate);
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
    public void clearAll() {
        this.Data.clear();
        this.cost.clear();
        this.profit.clear();
        this.AllKategoriesProfit.clear();
        this.AllKategories.clear();

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
        cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME1, null, null, null, null, null, null);


        if (cursor.moveToFirst()) {
            nameid = cursor.getColumnIndex(DBhelp.NAMES_COLUMS);

            do {

                name = cursor.getString(nameid);
                AllKategories.add(name);

            }

            while (cursor.moveToNext());
            cursor.close();

        }

        cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME2, null, null, null, null, null, null);


        if (cursor.moveToFirst()) {
            nameid = cursor.getColumnIndex(DBhelp.NAMES_COLUMS2);

            do {

                name = cursor.getString(nameid);
                AllKategoriesProfit.add(name);
            }

            while (cursor.moveToNext());
            cursor.close();

        }
    }


}
