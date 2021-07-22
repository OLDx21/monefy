package com.example.monefy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import com.example.monefy.activitys.MainActivity;
import com.example.monefy.activitys.MinusActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Action {

    public static HashMap<Integer, AlertDialog> alertDialog = new HashMap<>();

    public static Integer position;
    public static Date date;
    public static int checked;
    public static boolean ishave = true;
    public static Display display;

    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    public static SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());

    public static HashMap<String, String> NamesAndValues = new HashMap<>();
    public static TreeMap<Date, HistoryClass> SelectedDay = new TreeMap<Date, HistoryClass>(Collections.reverseOrder());
    public static HashMap<String, Double> NamesAndValuesForSelectedDay = new HashMap<>();
    public static LinkedHashMap<String, NamesAndValues> NamesAndValuesForYears = new LinkedHashMap<>();
    public static TreeMap<Date, NamesAndValues> NamesAndValuesForDays = new TreeMap<Date, NamesAndValues>();
    public static HashMap<String, Double> StonksNamesAndValuesForSelectedDay = new HashMap<>();
    public static LinkedHashMap<String, NamesAndValues> NamesAndValuesForWeeks = new LinkedHashMap<>();
    public static LinkedHashMap<Date, NamesAndValues> NamesAndValuesForMonth = new LinkedHashMap<>();

    public static ArrayList<Integer> colors = new ArrayList<>();

    static SQLiteDatabase sqLiteDatabase;
    static DBhelp dBhelp;

    static {

        colors.add(Color.parseColor("#FFFF66"));
        colors.add(Color.parseColor("#FF6600"));
        colors.add(Color.parseColor("#FF33CC"));
        colors.add(Color.parseColor("#9933FF"));
        colors.add(Color.parseColor("#6633FF"));
        colors.add(Color.parseColor("#3366CC"));
        colors.add(Color.parseColor("#33FFCC"));
        colors.add(Color.parseColor("#33FF66"));
        colors.add(Color.parseColor("#00FF00"));
        colors.add(Color.parseColor("#CCFF33"));
        colors.add(Color.parseColor("#66FFFF"));
        colors.add(Color.parseColor("#6666CC"));
        colors.add(Color.parseColor("#CC00CC"));
        colors.add(Color.parseColor("#0000FF"));
        colors.add(Color.parseColor("#0099FF"));
    }

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

    public static void drawChart(PieChart pieChart, PieData pieData, PieDataSet pieDataSet) {
        pieDataSet.setDrawValues(true);
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(15);


        pieDataSet.setValueFormatter(new MyValueFormatter(new DecimalFormat("0"), pieChart));
        pieDataSet.setValueLinePart1Length(0.3f);
        pieDataSet.setValueLinePart2Length(0.4f);
        pieDataSet.setValueLineWidth(2f);
        pieDataSet.setValueLinePart1OffsetPercentage(80); // Line starts outside of chart
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

        pieDataSet.setColors(Action.colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(20);


        //create pie data object
        pieData.setDataSet(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public static void setFontActionBar(ActionBar actionBar, Context context) {
        TextView tv = new TextView(context);
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        tv.setTextColor(Color.WHITE);
        tv.setLayoutParams(lp);
        tv.setText(context.getString(R.string.app_name));
        tv.setTextSize(20f);
        tv.setTypeface(Typeface.MONOSPACE);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);
    }


}
