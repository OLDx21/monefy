package com.example.monefy.ui.Dayfr;

import android.content.Intent;
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
import com.example.monefy.bottoms.BottomSheeetForYears;
import com.example.monefy.bottoms.BottomSheetForDays;
import com.example.monefy.ui.slideshow.SlideshowFragment;
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

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Days extends Fragment {
    SQLiteDatabase sqLiteDatabase;

    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    BottomSheetForDays bottomSheet = BottomSheetForDays.getInstance();
    public TextView textView;
    Date years;
    NamesAndValues namesAndValues;
    TreeMap<Date, HistoryClass> Data;
    DoIntent doIntent = DoIntent.getInstance();
    ArrayList<String> Kategories = new ArrayList<>();

    public Days(Date years, NamesAndValues namesAndValues, TreeMap<Date, HistoryClass> Data) {
        this.years = years;
        this.namesAndValues = namesAndValues;
        this.Data = Data;

    }


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
        if (Data.isEmpty()) {

            return root;
        }


        center.setText(String.valueOf(namesAndValues.getResult()));

        if (namesAndValues.getResult() >= 0) {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText("Баланс " + namesAndValues.getResult());
        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText("Баланс " + namesAndValues.getResult());
        }
        Date date = years;
        date.setMinutes(new Date().getMinutes());
        date.setHours(new Date().getHours());
        date.setSeconds(new Date().getSeconds());
        textView.setText(Action.formatter2.format(date));

        addDataToChart();
        Action.date = date;
        plusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            PlusActivity.check = 3;
            PlusActivity.date = date;
            doIntent.setDoIntent(getContext(), PlusActivity.class);
            Intent intent1 = doIntent.getDoIntent();
            startActivity(intent1);

        });
        minusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            MinusActivity.check = 3;
            MinusActivity.date = date;
            doIntent.setDoIntent(getContext(), MinusActivity.class);
            Intent intent1 = doIntent.getDoIntent();
            startActivity(intent1);
        });
        center.setOnClickListener(v -> {
            clickbtns(date);
        });
        left.setOnClickListener(v -> {
            clickbtns(date);
        });
        right.setOnClickListener(v -> {
            clickbtns(date);
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

    void clickbtns(Date date) {
        BottomSheetForDays.Data.clear();
        BottomSheetForDays.Data.putAll(Data);
        BottomSheetForDays.CheckDate = SlideshowFragment.format.format(date);
        bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
    }

    void addDataToChart() {
        if (namesAndValues.getNames() == null || namesAndValues.getNames().isEmpty()) {
            return;
        }
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        int i = 0;

        for (Map.Entry<String, Double> s : namesAndValues.getNames().entrySet()) {
            if (Float.parseFloat(String.valueOf(s.getValue())) > 0.0) {
                yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                Kategories.add(s.getKey());
                i += 1;
            }
        }


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