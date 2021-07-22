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
import android.widget.RadioButton;
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
    public SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM (yyyy)", Locale.getDefault());


    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    BottomSheetForIntervals bottomSheet = BottomSheetForIntervals.getInstance();
    public TextView textView;
    Date from;
    Date to;
    ArrayList<String> arrayList = new ArrayList<>();
    public static TreeMap<Date, Date> dateTreeMap;
    ArrayList<String> KategoriesCost = new ArrayList<>();
    ArrayList<String> KategoriesStonks = new ArrayList<>();
    RadioButton stonks, cost;
    PieDataSet pieDataSet;
    PieData pieData;
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<PieEntry> arraystonks = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);




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
        stonks = root.findViewById(R.id.offer);
        cost = root.findViewById(R.id.search);
        Action.checked = 3;

        center.startAnimation(animAlpha);
        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));
        from = dateTreeMap.keySet().iterator().next();
        to = (Date) dateTreeMap.keySet().toArray()[dateTreeMap.size() - 1];


        textView.setText(formatter2.format(from) + " - " + formatter2.format(to));
        for (Map.Entry<Date, Date> s : dateTreeMap.entrySet()) {
            arrayList.add(Action.formatter.format(s.getValue()));
        }
        setData();
        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));

        cost.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
        stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
        cost.setChecked(true);
        stonks.setOnClickListener(v -> {
            stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
            cost.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(arraystonks);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);


        });

        cost.setOnClickListener(v -> {
            cost.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
            stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(yEntrys);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);

        });

        plusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            Snackbar.make(v, getActivity().getResources().getString(R.string.onlyview), Snackbar.LENGTH_LONG).show();

        });

        minusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            Snackbar.make(v, getActivity().getResources().getString(R.string.onlyview), Snackbar.LENGTH_LONG).show();
        });


        center.setOnClickListener(v -> {
            v.startAnimation(animAlpha);
            BottomSheetForIntervals.CheckDate = arrayList;
            bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
        });
        left.setOnClickListener(v -> {
            BottomSheetForIntervals.CheckDate = arrayList;
            bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
        });
        right.setOnClickListener(v -> {
            BottomSheetForIntervals.CheckDate = arrayList;
            bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
        });
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (cost.isChecked()) {
                    System.out.println(h.getAxis());
                    pieChart.setCenterText(KategoriesCost.get((Integer) e.getData()) + "\n" + e.getY());
                } else {
                    pieChart.setCenterText(KategoriesStonks.get((Integer) e.getData()) + "\n" + e.getY());
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData( ) {
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();

        TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
        String name, sumaa, check, dates;
        int i = 0;
        Date date1;
        double results = 0;


        for (Map.Entry<Date, HistoryClass> s : DataBase.getInstance().getData().entrySet()) {
            name = s.getValue().getName();
            sumaa = s.getValue().getSuma();
            check = s.getValue().getCheck();
            dates = s.getValue().getDate();
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
                    if (!stonks.containsKey(name)) {
                        stonks.put(name, Double.parseDouble(sumaa));
                    } else {
                        stonks.put(name, Double.parseDouble(sumaa) + stonks.get(name));
                    }
                    results += Double.parseDouble(sumaa);
                }
            }
        }

        BottomSheetForIntervals.Data = Data;
        for (Map.Entry<String, Double> map : names.entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                KategoriesCost.add(map.getKey());
                i += 1;
            }
        }
        i = 0;
        for (Map.Entry<String, Double> map : stonks.entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                arraystonks.add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                KategoriesStonks.add(map.getKey());
                i += 1;
            }
        }
        if (results < 0) {

            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(getActivity().getResources().getString(R.string.balance) + " " + results);
        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance) + " " + results);

        }
        pieDataSet = new PieDataSet(yEntrys, "");
        pieData = new PieData();
        Action.drawChart(pieChart, pieData, pieDataSet);
    }
}