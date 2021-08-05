package com.example.monefy;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.monefy.activitys.MinusActivity;
import com.example.monefy.activitys.PlusActivity;
import com.example.monefy.interfacee.BottomSheet;
import com.example.monefy.interfacee.DataChange;
import com.example.monefy.interfacee.Method;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Controller {
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    View root;
    BottomSheetBehavior<View> bottomSheetBehavior;
    Context context;
    DataChange dataChange;
    Animation animAlpha;
    Animation animbeta;
    ExpandableListView expandableListView;
    Button center, left, right, minusbtn, plusbtn;
    TextView textView, textViewlist;
    boolean check = true, ishave = true, activeMainButton;
    DoIntent doIntent = DoIntent.getInstance();
    RadioButton stonks, cost;
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<PieEntry> arraystonks = new ArrayList<>();
    NamesAndValues namesAndValues;
    ArrayList<String> KategoriesStonks = new ArrayList<>();
    ArrayList<String> KategoriesCost = new ArrayList<>();
    LinkedHashMap<String, Double> stonksmap = new LinkedHashMap<>();
    LinkedHashMap<String, Double> costmap = new LinkedHashMap<>();
    BottomSheet bottomSheets;


    public Controller setContextAndInit(Context context, View root, Date date, DataChange dataChange, BottomSheet bottomSheet,boolean activeMainButton,Method method) {
        this.context = context;
        this.root = root;
        this.dataChange = dataChange;
        this.bottomSheets = bottomSheet;
        this.activeMainButton = activeMainButton;

        animAlpha = AnimationUtils.loadAnimation(context, R.anim.animka3);
        animbeta = AnimationUtils.loadAnimation(context, R.anim.animka);
        ConstraintLayout constraintLayout = root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        pieChart = root.findViewById(R.id.idPieChart);
        Action.SettingsPieChart(pieChart);
        center = root.findViewById(R.id.center);
        right = root.findViewById(R.id.right);
        left = root.findViewById(R.id.left);
        minusbtn = root.findViewById(R.id.minusbtn);
        plusbtn = root.findViewById(R.id.plusbtn);
        textView = root.findViewById(R.id.textView);
        textViewlist = root.findViewById(R.id.textViewlist);
        stonks = root.findViewById(R.id.offer);
        cost = root.findViewById(R.id.search);
        expandableListView = root.findViewById(R.id.expanded_menu);
        center.startAnimation(animAlpha);
        cost.setChecked(true);
        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));

        stonks.setOnClickListener(v -> {
            if (!check) return;

            stonks.setBackground(context.getResources().getDrawable(R.drawable.selectedbtn));
            cost.setBackground(context.getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(arraystonks);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);
            pieChart.setCenterText("");
            check = false;
        });
        cost.setOnClickListener(v -> {
            if (check) return;

            cost.setBackground(context.getResources().getDrawable(R.drawable.selectedbtn));
            stonks.setBackground(context.getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(yEntrys);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);
            pieChart.setCenterText("");
            check = true;

        });
        plusbtn.setOnClickListener(v -> {
                v.startAnimation(animbeta);

            if(activeMainButton) {
                if(DataBase.getInstance().getAllKategoriesProfit().isEmpty()){
                    Snackbar.make(v, context.getResources().getString(R.string.importantmessage), Snackbar.LENGTH_LONG).show();
                    return;
                }
                PlusActivity.check = Action.checked;
                PlusActivity.date = date;
                doIntent.setDoIntent(context, PlusActivity.class);
                Intent intent1 = doIntent.getDoIntent();
                context.startActivity(intent1);
            }
            else {
                Snackbar.make(v, context.getResources().getString(R.string.onlyview), Snackbar.LENGTH_LONG).show();
            }

        });
        minusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            if(activeMainButton) {
                if(DataBase.getInstance().getAllKategories().isEmpty()){
                    Snackbar.make(v, context.getResources().getString(R.string.importantmessage2), Snackbar.LENGTH_LONG).show();
                    return;
                }
                MinusActivity.date = date;
                MinusActivity.check = Action.checked;
                doIntent.setDoIntent(context, MinusActivity.class);
                Intent intent1 = doIntent.getDoIntent();
                context.startActivity(intent1);
            }
            else {
                Snackbar.make(v, context.getResources().getString(R.string.onlyview), Snackbar.LENGTH_LONG).show();
            }
        });
        ishave = true;
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                if (ishave) {
                    Action.bottomSheetBehavior = bottomSheetBehavior;
                    method.setDate();
                    bottomSheets.setDataList(textViewlist, expandableListView, context, dataChange);
                    ishave = false;
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
        center.setOnClickListener(v -> {
            v.startAnimation(animAlpha);
            setActionBtns();
        });
        left.setOnClickListener(v -> {
            setActionBtns();
        });
        right.setOnClickListener(v -> {
            setActionBtns();

        });


        return this;
    }

    void setActionBtns() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        bottomSheetBehavior.setDraggable(true);

    }

    public void setPieData(PieData pieData) {
        this.pieData = pieData;
    }

    public void setPieDataSet(PieDataSet pieDataSet) {
        this.pieDataSet = pieDataSet;
    }

    public void setNamesAndValues(NamesAndValues namesAndValues) {
        this.namesAndValues = namesAndValues;
        if (namesAndValues.getResult() < 0) {
            center.setBackground(context.getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(Objects.requireNonNull(context).getResources().getString(R.string.balance) + " " + String.valueOf(namesAndValues.getResult()));
        } else {
            center.setBackground(context.getResources().getDrawable(R.drawable.custombtn));
            center.setText(context.getResources().getString(R.string.balance) + " " + String.valueOf(namesAndValues.getResult()));
        }
        Action.drawChart(pieChart, pieData, pieDataSet);
    }

    public LinkedHashMap<String, Double> getCostmap() {
        return costmap;
    }

    public LinkedHashMap<String, Double> getStonksmap() {
        return stonksmap;
    }

    public ArrayList<PieEntry> getyEntrys() {
        return yEntrys;
    }

    public ArrayList<PieEntry> getArraystonks() {
        return arraystonks;
    }

    public ArrayList<String> getKategoriesStonks() {
        return KategoriesStonks;
    }

    public ArrayList<String> getKategoriesCost() {
        return KategoriesCost;
    }

    public PieChart getPieChart() {
        return pieChart;
    }

    public PieDataSet getPieDataSet() {
        return pieDataSet;
    }

    public PieData getPieData() {
        return pieData;
    }

    public TextView getTextView() {
        return textView;
    }

    public void Update(HistoryAdapterClass historyAdapterClass) {
        KategoriesCost.clear();
        KategoriesStonks.clear();
        arraystonks.clear();
        yEntrys.clear();

        Action.DataChanged(namesAndValues, center, yEntrys, arraystonks, KategoriesCost, KategoriesStonks, pieChart, pieDataSet, historyAdapterClass, context);

        cost.setBackground(context.getResources().getDrawable(R.drawable.selectedbtn));
        stonks.setBackground(context.getResources().getDrawable(R.drawable.staybtn));
        check = true;
        cost.setChecked(true);
    }

}
