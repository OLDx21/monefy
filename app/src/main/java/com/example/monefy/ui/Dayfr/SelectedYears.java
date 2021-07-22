package com.example.monefy.ui.Dayfr;

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
import com.example.monefy.bottoms.BottomSheeetForYears;
import com.example.monefy.bottoms.BottomSheetForDays;
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

import java.text.DecimalFormat;
import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SelectedYears extends Fragment {
    SQLiteDatabase sqLiteDatabase;

    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    BottomSheeetForYears bottomSheet = BottomSheeetForYears.getInstance();
    public TextView textView;
    String years;
    NamesAndValues namesAndValues;
    TreeMap<Date, HistoryClass> Data;
    ArrayList<String> KategoriesCost = new ArrayList<>();
    ArrayList<String> KategoriesStonks = new ArrayList<>();
    RadioButton stonks, cost;
    PieDataSet pieDataSet;
    PieData pieData;
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<PieEntry> arraystonks = new ArrayList<>();

    public SelectedYears(String years, NamesAndValues namesAndValues, TreeMap<Date, HistoryClass> Data) {
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
        stonks = root.findViewById(R.id.offer);
        cost = root.findViewById(R.id.search);
        Action.checked = 2;

        center.startAnimation(animAlpha);
        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));

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
            System.out.println(cost.isSelected()+" "+stonks.isSelected());

        });
        if (namesAndValues.getResult() >= 0) {
            center.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance)+" "+namesAndValues.getResult());
        } else {
            center.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(getActivity().getResources().getString(R.string.balance)+" "+ namesAndValues.getResult());
        }
        textView.setText(years);
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
            v.startAnimation(animAlpha);
            setckickbtn();
        });
        left.setOnClickListener(v -> setckickbtn());
        right.setOnClickListener(v -> setckickbtn());
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

    void setckickbtn() {
        BottomSheeetForYears.Data.clear();
        BottomSheeetForYears.Data.putAll(Data);
        BottomSheeetForYears.CheckDate = textView.getText().toString();
        bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
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

}
