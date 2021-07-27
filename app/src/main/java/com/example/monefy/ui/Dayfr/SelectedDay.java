package com.example.monefy.ui.Dayfr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;
import com.example.monefy.activitys.MinusActivity;
import com.example.monefy.activitys.PlusActivity;
import com.example.monefy.bottoms.BottomSheetDayHistory;
import com.example.monefy.interfacee.DataChange;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SelectedDay extends Fragment implements DataChange {

    public static Date date;
    SelectedDay selectedDay = this;
    BottomSheetBehavior<View> bottomSheetBehavior;
    ExpandableListView expandableListView;
    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    BottomSheetDayHistory bottomSheet = BottomSheetDayHistory.getInstance();
    public TextView textView, textviewlist;
    DoIntent doIntent = DoIntent.getInstance();
    ArrayList<String> KategoriesCost = new ArrayList<>();
    ArrayList<String> KategoriesStonks = new ArrayList<>();
    RadioButton stonks, cost;
    PieDataSet pieDataSet;
    PieData pieData;
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<PieEntry> arraystonks = new ArrayList<>();
    boolean check = true;
    double Summa=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);




        pieChart = root.findViewById(R.id.idPieChart);
        Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.animka3);
        Animation animbeta = AnimationUtils.loadAnimation(getContext(), R.anim.animka);
        Action.SettingsPieChart(pieChart);
        center = root.findViewById(R.id.center);
        right = root.findViewById(R.id.right);
        left = root.findViewById(R.id.left);
        textView = root.findViewById(R.id.textView);
        minusbtn = root.findViewById(R.id.minusbtn);
        plusbtn = root.findViewById(R.id.plusbtn);
        stonks = root.findViewById(R.id.offer);
        cost = root.findViewById(R.id.search);
        ConstraintLayout constraintLayout = root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        textviewlist = root.findViewById(R.id.textViewlist);
        expandableListView = root.findViewById(R.id.expanded_menu);
        Action.checked = 1;

        center.startAnimation(animAlpha);
        center.setWidth((Action.display.getWidth()) - (Action.display.getWidth() / 2));
        if (date == null) {
            textView.setText("Оберіть дату в видвижному меню!");
            return root;

        }

        textView.setText(Action.formatter2.format(date));
        addDataSet();

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

        plusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            PlusActivity.check = 1;
            PlusActivity.date = date;
            doIntent.setDoIntent(getContext(), PlusActivity.class);
            Intent intent1 = doIntent.getDoIntent();
            startActivity(intent1);

        });
        minusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            MinusActivity.check = 1;
            MinusActivity.date = date;
            doIntent.setDoIntent(getContext(), MinusActivity.class);
            Intent intent1 = doIntent.getDoIntent();
            startActivity(intent1);
        });

        bottomSheet.setDataList(textviewlist, expandableListView, getActivity(), selectedDay);


        center.setOnClickListener(v -> {
            v.startAnimation(animAlpha);
            clickbtns();
        });
        left.setOnClickListener(v -> clickbtns());
        right.setOnClickListener(v -> clickbtns());
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


    private void addDataSet( ) {
        double result = 0;
        Action.NamesAndValuesForSelectedDay.clear();
        Action.StonksNamesAndValuesForSelectedDay.clear();
        Action.SelectedDay.clear();


        String name, sumaa, check, dates;
        int i = 0;

        Date date1;

        DataBase dataBase = DataBase.getInstance();

        for (Map.Entry<Date, HistoryClass> s : dataBase.getData().entrySet()) {

            name = s.getValue().getName();
            sumaa = s.getValue().getSuma();
            check = s.getValue().getCheck();
            dates = s.getValue().getDate();
            date1 = new Date(dates);

            if (Action.formatter.format(date1).equals(Action.formatter.format(SelectedDay.date))) {
                Action.SelectedDay.put(date1, new HistoryClass(name, dates, sumaa, check));
                if (check.equals("minus")) {
                    if (!Action.NamesAndValuesForSelectedDay.containsKey(name)) {
                        Action.NamesAndValuesForSelectedDay.put(name, Double.parseDouble(sumaa));
                    } else {

                        Action.NamesAndValuesForSelectedDay.put(name, (Double.parseDouble(sumaa) + Action.NamesAndValuesForSelectedDay.get(name)));
                    }


                } else {
                    if (!Action.StonksNamesAndValuesForSelectedDay.containsKey(name)) {
                        Action.StonksNamesAndValuesForSelectedDay.put(name, Double.parseDouble(sumaa));
                    } else {

                        Action.StonksNamesAndValuesForSelectedDay.put(name, (Double.parseDouble(sumaa) + Action.StonksNamesAndValuesForSelectedDay.get(name)));
                    }

                    result += Double.parseDouble(sumaa);
                }
            }


        }

        for (Map.Entry<String, Double> map : Action.NamesAndValuesForSelectedDay.entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                KategoriesCost.add(map.getKey());
                i += 1;
            }
            result -= map.getValue();

        }
        i = 0;
        for (Map.Entry<String, Double> map : Action.StonksNamesAndValuesForSelectedDay.entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                arraystonks.add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                KategoriesStonks.add(map.getKey());
                i += 1;
            }
        }
        if (result < 0) {

            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(getActivity().getResources().getString(R.string.balance) + " " + result);
        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance) + " " + result);

        }
        Summa = result;
        //create the data set
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
        double value =0;
        int i = 0;
        if(historyAdapterClass.getCheck().equals("minus")){
            value = Summa+Double.parseDouble(historyAdapterClass.getSuma());
            Action.NamesAndValuesForSelectedDay.put(historyAdapterClass.getName(),Action.NamesAndValuesForSelectedDay.get(historyAdapterClass.getName())
                    -Double.parseDouble(historyAdapterClass.getSuma()));
            Summa+=Double.parseDouble(historyAdapterClass.getSuma());

        }
        else {
            value = Summa-Double.parseDouble(historyAdapterClass.getSuma());
            Action.StonksNamesAndValuesForSelectedDay.put(historyAdapterClass.getName(),
                    Action.StonksNamesAndValuesForSelectedDay.get(historyAdapterClass.getName())-Double.parseDouble(historyAdapterClass.getSuma()));
            Summa-=Double.parseDouble(historyAdapterClass.getSuma());
        }



        if (value<0) {

            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(Objects.requireNonNull(getActivity()).getResources().getString(R.string.balance) + " -" + String.valueOf(value));
        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance) + " " + String.valueOf(value));
        }

        for (Map.Entry<String, Double> s : Action.NamesAndValuesForSelectedDay.entrySet()) {
            if (s.getValue() > 0) {
                yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                KategoriesCost.add(s.getKey());
                i += 1;
            }
        }
        i=0;
        for (Map.Entry<String, Double> s : Action.StonksNamesAndValuesForSelectedDay.entrySet()) {
            if (s.getValue() > 0) {
                arraystonks.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                KategoriesStonks.add(s.getKey());
                i += 1;
            }
        }
        pieChart.setCenterText("");
        pieDataSet.setValues(yEntrys);
        pieChart.notifyDataSetChanged();
        pieChart.animateY(1000, Easing.EaseInOutCubic);

        cost.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
        stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
        check=true;
        cost.setChecked(true);
    }
}
