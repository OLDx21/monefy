package com.example.monefy.ui.intervdays;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;
import com.example.monefy.activitys.MinusActivity;
import com.example.monefy.activitys.PlusActivity;
import com.example.monefy.bottoms.BottomSheet;
import com.example.monefy.bottoms.BottomSheetDayHistory;
import com.example.monefy.bottoms.BottomSheetForIntervals;
import com.example.monefy.ui.Dayfr.SelectedDay;
import com.example.monefy.ui.Dayfr.Weeks;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class IntervalDays extends Fragment {
    public SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM (yyyy)", new Locale("uk", "UA"));

    SQLiteDatabase sqLiteDatabase;


    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    BottomSheetForIntervals bottomSheet = BottomSheetForIntervals.getInstance();
    public TextView textView;
    Date from;
    Date to;
    ArrayList<String> arrayList = new ArrayList<>();
    public static TreeMap<Date, Date> dateTreeMap;
    ArrayList<String> Kategories = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        sqLiteDatabase = Action.getSqLiteDatabase();

        pieChart = root.findViewById(R.id.idPieChart);
        Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.animka3);
        Animation animbeta = AnimationUtils.loadAnimation(getContext(), R.anim.animka);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextTypeface(Typeface.SERIF);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(30);
        pieChart.getDescription().setText("");
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setTransparentCircleRadius(67f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateY(1000, Easing.EaseInOutCubic);
        pieChart.setRotationAngle(20);
        center = root.findViewById(R.id.center);
        right = root.findViewById(R.id.right);
        left = root.findViewById(R.id.left);
        textView = root.findViewById(R.id.textView);
        minusbtn = root.findViewById(R.id.minusbtn);
        plusbtn = root.findViewById(R.id.plusbtn);

        Action.checked = 3;

        center.startAnimation(animAlpha);
        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));
        from = dateTreeMap.keySet().iterator().next();
        to = (Date) dateTreeMap.keySet().toArray()[dateTreeMap.size() - 1];


        textView.setText(formatter2.format(from) + " - " + formatter2.format(to));
        for (Map.Entry<Date, Date> s : dateTreeMap.entrySet()) {
            arrayList.add(Action.formatter.format(s.getValue()));
        }
        setData(sqLiteDatabase);

        plusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            Snackbar.make(v, "Тільки перегляд", Snackbar.LENGTH_LONG).show();

        });
        minusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            Snackbar.make(v, "Тільки перегляд", Snackbar.LENGTH_LONG).show();
        });


        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                BottomSheetForIntervals.CheckDate = arrayList;
                bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetForIntervals.CheckDate = arrayList;
                bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetForIntervals.CheckDate = arrayList;
                bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
            }
        });
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChart.setCenterText(Kategories.get((Integer) e.getData()) + "\n" + e.getY());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(SQLiteDatabase sqLiteDatabase) {
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
        Cursor cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME3, null, null, null, null, null, null);
        String name, sumaa, check, dates;
        int nameid, suma, checkint, date, i = 0;
        Date date1;
        double results = 0;

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


                if (arrayList.contains(Action.formatter.format(date1))) {
                    Data.put(date1, new HistoryClass(name, dates, sumaa, check));
                    if (check.equals("minus")) {
                        results -= Double.parseDouble(sumaa);
                        if (!names.containsKey(name)) {
                            names.put(name, Double.parseDouble(sumaa));
                        } else {
                            names.put(name, Double.parseDouble(sumaa) + names.get(name));
                        }

                    } else {
                        results += Double.parseDouble(sumaa);
                    }
                }
            }

            while (cursor.moveToNext());
            cursor.close();

        }

        BottomSheetForIntervals.Data = Data;
        for (Map.Entry<String, Double> map : names.entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                Kategories.add(map.getKey());
                i += 1;
            }
        }
        center.setText(String.valueOf(results));
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
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
        ArrayList<Integer> colors = new ArrayList<>();
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
        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(20);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}