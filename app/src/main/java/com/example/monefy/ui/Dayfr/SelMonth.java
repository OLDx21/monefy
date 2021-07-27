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
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;
import com.example.monefy.activitys.MinusActivity;
import com.example.monefy.activitys.PlusActivity;
import com.example.monefy.bottoms.BottomSheeetForYears;
import com.example.monefy.bottoms.BottomSheetForDays;
import com.example.monefy.bottoms.BottomSheetForMonths;
import com.example.monefy.bottoms.BottomSheetForWeeks;
import com.example.monefy.interfacee.DataChange;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SelMonth extends Fragment implements DataChange {

    boolean ishave = true;
    SelMonth selMonth = this;
    BottomSheetBehavior<View> bottomSheetBehavior;
    ExpandableListView expandableListView;
    SQLiteDatabase sqLiteDatabase;
    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    BottomSheetForMonths bottomSheets = BottomSheetForMonths.getInstance();
    public TextView textView;
    String years;
    NamesAndValues namesAndValues;
    TreeMap<Date, HistoryClass> Data;
    Date date;
    ArrayList<String> KategoriesCost = new ArrayList<>();
    ArrayList<String> KategoriesStonks = new ArrayList<>();
    RadioButton stonks, cost;
    PieDataSet pieDataSet;
    PieData pieData;
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<PieEntry> arraystonks = new ArrayList<>();
    boolean check = true;
    public SelMonth(Date years, NamesAndValues namesAndValues, TreeMap<Date, HistoryClass> Data) {
        this.years = Action.format.format(years);
        this.namesAndValues = namesAndValues;
        this.Data = Data;
        this.date = years;

    }

    public SelMonth(String years, NamesAndValues namesAndValues, TreeMap<Date, HistoryClass> Data) {
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
        Action.SettingsPieChart(pieChart);
        center = root.findViewById(R.id.center);
        right = root.findViewById(R.id.right);
        left = root.findViewById(R.id.left);
        textView = root.findViewById(R.id.textViewlist);
        minusbtn = root.findViewById(R.id.minusbtn);
        plusbtn = root.findViewById(R.id.plusbtn);
        stonks = root.findViewById(R.id.offer);
        cost = root.findViewById(R.id.search);
        expandableListView = root.findViewById(R.id.expanded_menu);

        TextView textViewttx = root.findViewById(R.id.textView);
        textViewttx.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,15));
        ConstraintLayout constraintLayout = root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        Action.checked = 6;


        center.startAnimation(animAlpha);
        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));
        if (namesAndValues.getResult() >= 0) {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance)+" "+namesAndValues.getResult());
        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(getActivity().getResources().getString(R.string.balance)+" "+ namesAndValues.getResult());
        }

        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));

        cost.setChecked(true);
        stonks.setOnClickListener(v -> {
            if(!check) return;
            stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
            cost.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(arraystonks);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);
            check = false;


        });
        cost.setOnClickListener(v -> {
            if(check) return;
            cost.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
            stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(yEntrys);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);
            check = true;

        });





        addDataToChart();

        plusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            Snackbar.make(v, getActivity().getResources().getString(R.string.onlyview), Snackbar.LENGTH_LONG).show();

        });
        minusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            Snackbar.make(v, getActivity().getResources().getString(R.string.onlyview), Snackbar.LENGTH_LONG).show();
        });
        center.setOnClickListener(v -> {
            clickbtns();
        });
        left.setOnClickListener(v -> {
            clickbtns();
        });
        right.setOnClickListener(v -> {
            clickbtns();
        });
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                if(date==null){
                    textView.setText(getActivity().getResources().getString(R.string.wthtran));

                }

                if (ishave && date!=null){
                    BottomSheetForMonths.Data.clear();
                    BottomSheetForMonths.Data.putAll(Data);
                    BottomSheetForMonths.CheckDate = Months.format.format(date);
                    bottomSheets.setDataList(textView, expandableListView, getActivity(), selMonth);
                    ishave= false;

                }


            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (cost.isChecked()) {
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

    void clickbtns() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    void addDataToChart() {
        arraystonks.clear();
        KategoriesStonks.clear();
        KategoriesCost.clear();
        yEntrys.clear();
        int i = 0;
        if (!(namesAndValues.getNames() == null) && !(namesAndValues.getNames().isEmpty())) {
            for (Map.Entry<String, Double> s : namesAndValues.getNames().entrySet()) {
                if (Float.parseFloat(String.valueOf(s.getValue())) > 0.0) {
                    yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                    KategoriesCost.add(s.getKey());
                    i += 1;
                }
            }
        }
        i = 0;

        if (namesAndValues.getStonks() != null && !(namesAndValues.getStonks().isEmpty())) {

            for (Map.Entry<String, Double> s : namesAndValues.getStonks().entrySet()) {
                if (Float.parseFloat(String.valueOf(s.getValue())) > 0.0) {
                    arraystonks.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                    KategoriesStonks.add(s.getKey());
                    i += 1;
                }
            }

        }


        pieDataSet = new PieDataSet(yEntrys, "");
        pieData = new PieData();
        Action.drawChart(pieChart, pieData, pieDataSet);

    }

    @Override
    public void Update(HistoryAdapterClass historyAdapterClass) {
        KategoriesCost.clear();
        KategoriesStonks.clear();
        arraystonks.clear();
        yEntrys.clear();

        Action.DataChanged(namesAndValues, center, yEntrys, arraystonks, KategoriesCost, KategoriesStonks, pieChart, pieDataSet, historyAdapterClass, getActivity());

        cost.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
        stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
        check=true;
        cost.setChecked(true);
    }
}
