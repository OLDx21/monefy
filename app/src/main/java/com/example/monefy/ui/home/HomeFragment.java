package com.example.monefy.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
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
import com.example.monefy.bottoms.BottomSheet;
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
import org.jetbrains.annotations.NotNull;

import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment implements DataChange {

    HomeFragment context = this;
    SQLiteDatabase sqLiteDatabase = Action.getSqLiteDatabase();
    ExpandableListView expandableListView;
    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    BottomSheet bottomSheets = BottomSheet.getInstance();
    TextView textView, textViewlist;
    DoIntent doIntent = DoIntent.getInstance();
    RadioButton stonks, cost;
    ArrayList<String> KategoriesStonks = new ArrayList<>();
    ArrayList<String> KategoriesCost = new ArrayList<>();
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    HashMap<String, Double> stonksmap = new HashMap<>();
    ArrayList<PieEntry> arraystonks = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;
    double summa=0;
    BottomSheetBehavior<View> bottomSheetBehavior;
    boolean check = true, ishave = true;



    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.R)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ConstraintLayout constraintLayout = root.findViewById(R.id.bottomSheet);


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
        textViewlist = root.findViewById(R.id.textViewlist);
        expandableListView = root.findViewById(R.id.expanded_menu);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        textView.setText(Action.formatter2.format(new Date()));

        Action.checked = 0;


        center.startAnimation(animAlpha);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Action.display = display;
        center.setWidth((display.getWidth()) - (display.getWidth() / 2));
        if (Action.ishave) {

            DataBase dataBase = DataBase.getInstance();
            dataBase.CreateDB(sqLiteDatabase);
            Action.ishave = false;
        }
        addDataSet();

        cost.setChecked(true);
        Action.bottomSheetBehavior = bottomSheetBehavior;


        stonks.setOnClickListener(v -> {
            if (!check) return;

            stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
            cost.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(arraystonks);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);
            pieChart.setCenterText("");
            check = false;
        });
        cost.setOnClickListener(v -> {
            if (check) return;

            cost.setBackground(getActivity().getResources().getDrawable(R.drawable.selectedbtn));
            stonks.setBackground(getActivity().getResources().getDrawable(R.drawable.staybtn));
            pieDataSet.setValues(yEntrys);
            pieChart.notifyDataSetChanged();
            pieChart.animateY(1000, Easing.EaseInOutCubic);
            pieChart.setCenterText("");
            check = true;

        });

        plusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            PlusActivity.check = 0;
            PlusActivity.date = new Date();
            doIntent.setDoIntent(getContext(), PlusActivity.class);
            Intent intent1 = doIntent.getDoIntent();
            startActivity(intent1);

        });
        minusbtn.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            MinusActivity.date = new Date();
            MinusActivity.check = 0;
            doIntent.setDoIntent(getContext(), MinusActivity.class);
            Intent intent1 = doIntent.getDoIntent();
            startActivity(intent1);
        });



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {


                if (ishave) {
                    bottomSheets.setDataList(textViewlist, expandableListView, getActivity(), context);
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


        return root;
    }


    void setActionBtns() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        bottomSheetBehavior.setDraggable(true);

    }

    private void addDataSet() {
        Action.NamesAndValues.clear();
        String name, value;
        int i = 0;
        double sum2 = 0, values = 0;



        for (Map.Entry<Date, HistoryClass> s : DataBase.getInstance().getData().entrySet()) {
            if (s.getValue().getCheck().equals("minus")) {

                name = s.getValue().getName();
                value = s.getValue().getSuma();
                sum2 -= Double.parseDouble(value);
                values = Action.NamesAndValues.containsKey(name) ? Double.parseDouble(Action.NamesAndValues.get(name)) : 0;
                Action.NamesAndValues.put(name, String.valueOf(Double.parseDouble(value) + values));
            }
            else {
                value = s.getValue().getSuma();
                name = s.getValue().getName();
                sum2 += Double.parseDouble(value);


                if (!stonksmap.containsKey(name)) {
                    stonksmap.put(name, Double.parseDouble(value));
                } else {
                    stonksmap.put(name, Double.parseDouble(value) + stonksmap.get(name));
                }
            }
        }


        for (Map.Entry<String, String> s : Action.NamesAndValues.entrySet()) {
            if (Float.parseFloat(s.getValue()) > 0) {
                yEntrys.add(new PieEntry(Float.parseFloat(s.getValue()), s.getKey(), i));
                KategoriesCost.add(s.getKey());
                i += 1;
            }
        }
         i=0;
        for (Map.Entry<String, Double> s : stonksmap.entrySet()) {
            if (s.getValue() > 0) {
            arraystonks.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
            KategoriesStonks.add(s.getKey());
            i += 1;
        }
        }

        if (sum2<0) {

            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(Objects.requireNonNull(getActivity()).getResources().getString(R.string.balance) + " " + String.valueOf(sum2));
        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance) + " " + String.valueOf(sum2));

        }
        summa = sum2;


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
            value = summa+Double.parseDouble(historyAdapterClass.getSuma());
            Action.NamesAndValues.put(historyAdapterClass.getName(), String.valueOf(Double.parseDouble(Action.NamesAndValues.get(historyAdapterClass.getName()))
                    -Double.parseDouble(historyAdapterClass.getSuma())));
            summa+=Double.parseDouble(historyAdapterClass.getSuma());

        }
        else {
            value = summa-Double.parseDouble(historyAdapterClass.getSuma());
            stonksmap.put(historyAdapterClass.getName(), stonksmap.get(historyAdapterClass.getName())-Double.parseDouble(historyAdapterClass.getSuma()));
            summa-=Double.parseDouble(historyAdapterClass.getSuma());
        }



        if (value<0) {

            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(Objects.requireNonNull(getActivity()).getResources().getString(R.string.balance) + " " + String.valueOf(value));

        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance) + " " + String.valueOf(value));
        }

        for (Map.Entry<String, String> s : Action.NamesAndValues.entrySet()) {
            if (Float.parseFloat(s.getValue()) > 0) {
                yEntrys.add(new PieEntry(Float.parseFloat(s.getValue()), s.getKey(), i));
                KategoriesCost.add(s.getKey());
                i += 1;
            }
        }
        i=0;
        for (Map.Entry<String, Double> s : stonksmap.entrySet()) {
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