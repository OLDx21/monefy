package com.example.monefy;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Action {

    public static HashMap<Integer, AlertDialog> alertDialog = new HashMap<>();

    public static boolean CheckLang = true;
    public static Integer position;
    public static int checked;
    public static boolean ishave = true;
    public static Display display;
    public static BottomSheetBehavior<View> bottomSheetBehavior;

    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    public static SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
    public static SimpleDateFormat formatter3 = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
    public static SimpleDateFormat format = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());



    public static TreeMap<Date, HistoryClass> SelectedDay = new TreeMap<Date, HistoryClass>(Collections.reverseOrder());
    public static TreeMap<Date, NamesAndValues> NamesAndValuesForYears = new TreeMap<>();
    public static TreeMap<Date, NamesAndValues> NamesAndValuesForDays = new TreeMap<Date, NamesAndValues>();
    public static LinkedHashMap<String, NamesAndValues> NamesAndValuesForWeeks = new LinkedHashMap<>();
    public static TreeMap<Date, NamesAndValues> NamesAndValuesForMonth = new TreeMap<>();

    public static ArrayList<Integer> colors = new ArrayList<>();

    static SQLiteDatabase sqLiteDatabase;
    static DBhelp dBhelp;

    static {
        colors.add(Color.parseColor("#FF6600"));
        colors.add(Color.parseColor("#FF33CC"));
        colors.add(Color.parseColor("#00CCFF"));
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
        pieDataSet.setValueTextColor(pieChart.getContext().getResources().getColor(R.color.textcolor));
        pieDataSet.setValueTypeface(Typeface.MONOSPACE);
        pieDataSet.setValueLinePart1OffsetPercentage(80); // Line starts outside of chart
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        pieDataSet.setColors(Action.colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();

        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(pieChart.getContext().getResources().getColor(R.color.textcolor));
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

    public static void setFontActionBar(ActionBar actionBar, Context context, String text) {
        TextView tv = new TextView(context);
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);



        tv.setTextColor(Color.WHITE);
        tv.setLayoutParams(lp);
        tv.setText(text);
        tv.setTextSize(20f);
        tv.setTypeface(Typeface.MONOSPACE);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);
    }
    public static void SettingsPieChart(PieChart pieChart){
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextColor(pieChart.getContext().getResources().getColor(R.color.textcolor));
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setEntryLabelTypeface(Typeface.MONOSPACE);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(20);
        pieChart.setCenterTextTypeface(Typeface.MONOSPACE);
        pieChart.getDescription().setText("");
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleColor(pieChart.getContext().getResources().getColor(R.color.TransparentCircleColor));
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setTransparentCircleRadius(67f);
        pieChart.setHoleColor(pieChart.getContext().getResources().getColor(R.color.maincolor));
        pieChart.animateY(1000, Easing.EaseInOutCubic);
        pieChart.setRotationAngle(20);

    }

    public static void DataChanged(NamesAndValues namesAndValues, Button center, ArrayList<PieEntry> yEntrys, ArrayList<PieEntry> arraystonks,
                                   ArrayList<String> CostK, ArrayList<String> StonksK, PieChart pieChart, PieDataSet pieDataSet,
                                   HistoryAdapterClass historyAdapterClass, Context context){

        double value =0;
        int i = 0;
        if(historyAdapterClass.getCheck().equals("minus")){
            value = namesAndValues.getResult()+Double.parseDouble(historyAdapterClass.getSuma());
            namesAndValues.getNames().put(historyAdapterClass.getName(), namesAndValues.getNames().get(historyAdapterClass.getName())-Double.parseDouble(historyAdapterClass.getSuma()));
            namesAndValues.setResult(value);

        }
        else {
            value = namesAndValues.getResult()-Double.parseDouble(historyAdapterClass.getSuma());
            namesAndValues.getStonks().put(historyAdapterClass.getName(), namesAndValues.getStonks().get(historyAdapterClass.getName())-Double.parseDouble(historyAdapterClass.getSuma()));
            namesAndValues.setResult(value);
        }
        if (value<0) {

            center.setBackground(context.getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(Objects.requireNonNull(context).getResources().getString(R.string.balance) + " -" + String.valueOf(value));
        } else {
            center.setBackground(context.getResources().getDrawable(R.drawable.custombtn));
            center.setText(context.getResources().getString(R.string.balance) + " " + String.valueOf(value));
        }

        for (Map.Entry<String, Double> s : namesAndValues.getNames().entrySet()) {
            if (s.getValue() > 0) {
                yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                CostK.add(s.getKey());
                i += 1;
            }
        }
        i=0;
        for (Map.Entry<String, Double> s : namesAndValues.getStonks().entrySet()) {
            if (s.getValue() > 0) {
                arraystonks.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                StonksK.add(s.getKey());
                i += 1;
            }
        }
        pieChart.setCenterText("");
        pieDataSet.setValues(yEntrys);
        ((Activity)context).runOnUiThread(()->{
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);
        });




    }




}
