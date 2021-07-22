package com.example.monefy.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;

import com.example.monefy.activitys.CostMinus;
import com.example.monefy.activitys.MinusActivity;
import com.example.monefy.activitys.PlusActivity;
import com.example.monefy.bottoms.BottomSheet;
import com.example.monefy.ui.Dayfr.SelectedDay;
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
public class HomeFragment extends Fragment {

    SQLiteDatabase sqLiteDatabase=Action.getSqLiteDatabase();

    Button center, left, right, minusbtn, plusbtn;
    PieChart pieChart;
    public static PopupMenu popupMenu;
    BottomSheet bottomSheet = BottomSheet.getInstance();
    TextView textView;
    DoIntent doIntent = DoIntent.getInstance();
    RadioButton stonks, cost;
    ArrayList<String> KategoriesStonks = new ArrayList<>();
    ArrayList<String> KategoriesCost = new ArrayList<>();
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    HashMap <String, Double> stonksmap = new HashMap<>();
    ArrayList<PieEntry> arraystonks = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;
    @RequiresApi(api = Build.VERSION_CODES.R)
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
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(30);
        pieChart.setCenterTextTypeface(Typeface.SERIF);
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
        textView.setText(Action.formatter2.format(new Date()));
        Action.checked = 0;


        center.startAnimation(animAlpha);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Action.display = display;
        center.setWidth((display.getWidth()) - (display.getWidth() / 2));
        popupMenu = new PopupMenu(getContext(), textView);
        addDataSet(sqLiteDatabase);
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

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                CostMinus.kategory = item.getTitle().toString();
                switch (Action.checked) {
                    case 0:
                        CostMinus.checked = 0;
                        CostMinus.date = new Date();
                        break;
                    case 1:
                        CostMinus.checked = 1;
                        CostMinus.date = SelectedDay.date;
                        break;
                    case 2:
                        return false;
                    case 3:
                        return false;


                }


                DoIntent doIntent = DoIntent.getInstance();
                doIntent.setDoIntent(getContext(), CostMinus.class);
                Intent intent1 = doIntent.getDoIntent();
                startActivity(intent1);

                return false;
            }
        });

        setbuttoninfo(Action.NamesAndValues);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if(cost.isChecked()) {
                    pieChart.setCenterText(KategoriesCost.get((Integer) e.getData()) + "\n" + e.getY());
                }
                else {
                    pieChart.setCenterText(KategoriesStonks.get((Integer) e.getData()) + "\n" + e.getY());
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        center.setOnClickListener(v -> {
            v.startAnimation(animAlpha);
            bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
        });
        left.setOnClickListener(v -> bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet"));
        right.setOnClickListener(v -> bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet"));


        return root;
    }

    private void addDataSet(SQLiteDatabase sqLiteDatabase) {
        Action.NamesAndValues.clear();
        Cursor cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME1, null, null, null, null, null, null);
        String name, value;
        int i = 0, nameid, kolvotable;


        if (cursor.moveToFirst()) {
            nameid = cursor.getColumnIndex(DBhelp.NAMES_COLUMS);
            kolvotable = cursor.getColumnIndex(DBhelp.VALUES_COLUMNS);

            do {

                name = cursor.getString(nameid);
                value = cursor.getString(kolvotable);
                popupMenu.getMenu().add(name);
                Action.NamesAndValues.put(name, value);
                if (Float.parseFloat(value) > 0) {
                    yEntrys.add(new PieEntry(Float.parseFloat(value), name, i));
                    KategoriesCost.add(name);
                }

                i += 1;

            }

            while (cursor.moveToNext());
        }

        cursor.close();


        //create the data set
        pieDataSet = new PieDataSet(yEntrys, "");
        pieData = new PieData();
        Action.drawChart(pieChart, pieData, pieDataSet);
    }

    @SuppressLint("SetTextI18n")
    public void setbuttoninfo(HashMap<String, String> hashMap) {
        double sum = 0;
        double sum2 = 0;

        Cursor cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME2, null, null, null, null, null, null);
        String value;
        String name;

        if (cursor.moveToFirst()) {
            int valueid = cursor.getColumnIndex(DBhelp.SUMA_COLUMS);
            int nameid = cursor.getColumnIndex(DBhelp.TAG_COLUMS);


            do {

                value = cursor.getString(valueid);
                name = cursor.getString(nameid);
                sum2 += Double.parseDouble(value);

                if(!stonksmap.containsKey(name)){
                    stonksmap.put(name, Double.parseDouble(value));
                }
                else {
                    stonksmap.put(name, Double.parseDouble(value)+stonksmap.get(name));
                }

            }

            while (cursor.moveToNext());
        }

        cursor.close();


        for (Map.Entry<String, String> s : hashMap.entrySet()) {
            sum += Double.parseDouble(s.getValue());

        }
        int count=0;
        for(Map.Entry<String, Double> s: stonksmap.entrySet()){
            arraystonks.add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), count));
            KategoriesStonks.add(s.getKey());
            count+=1;
        }


        if (sum > sum2) {

            center.setBackground(getContext().getResources().getDrawable(R.drawable.redmainbtm));
            center.setText(Objects.requireNonNull(getActivity()).getResources().getString(R.string.balance)+" -"+ String.valueOf(sum - sum2));
        } else {
            center.setBackground(getContext().getResources().getDrawable(R.drawable.custombtn));
            center.setText(getActivity().getResources().getString(R.string.balance)+" "+String.valueOf(sum2 - sum));

        }

    }
}