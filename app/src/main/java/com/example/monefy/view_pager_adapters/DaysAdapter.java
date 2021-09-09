package com.example.monefy.view_pager_adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.monefy.DataBase;
import com.example.monefy.FacadeMethods;
import com.example.monefy.HistoryClass;
import com.example.monefy.NamesAndValues;
import com.example.monefy.interfacee.ViewPagerHelp;
import com.github.mikephil.charting.data.Entry;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class DaysAdapter extends FragmentStatePagerAdapter implements ViewPagerHelp {
    public static TreeMap<Date, NamesAndValues> NamesAndValuesListD = new TreeMap<>();
    TreeMap<Date, HistoryClass> Data;
    double days;

    ArrayList<String> xLabel = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy", new Locale("uk", "UA"));
    public String mode = ALL_MODE;

    public DaysAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
        setData();

    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        calendar.setTime(Data.keySet().iterator().next());
        calendar.add(Calendar.DATE, position * 12);
        double result;
        List<Entry> lineEntries = new ArrayList<>();
        NamesAndValues o;

        for (int i = 0; i < 12; i++) {
            o = (NamesAndValues) containsKey(calendar.getTime());
            result = 0;

            if (o != null) {

                switch (mode) {
                    case PROFIT_MODE:
                        for (Map.Entry<String, Double> s : o.getStonks().entrySet()) {
                            result += s.getValue();
                        }
                        break;

                    case COST_MODE:
                        for (Map.Entry<String, Double> s : o.getNames().entrySet()) {
                            result += s.getValue();
                        }
                        break;
                    case ALL_MODE:
                        result = o.getResult();
                        break;
                }

                lineEntries.add(new Entry(i, (float) result));
            } else {
                lineEntries.add(new Entry(i, 0));
            }
            xLabel.add(format.format(calendar.getTime()));
            calendar.add(Calendar.DATE, +1);
        }

        return new MainStatFragment(lineEntries, xLabel, position);
    }

    @Override
    public int getCount() {
        if (!NamesAndValuesListD.isEmpty() && days < 12) {
            return 1;
        }
        return (int) Math.ceil(days / 12.0);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setData() {
        NamesAndValuesListD.clear();
        xLabel.clear();
        DataBase dataBase = DataBase.getInstance();

        Data = dataBase.getData();
        days = FacadeMethods.getInstance().setDataDays(NamesAndValuesListD);
        if (NamesAndValuesListD.isEmpty()) {
            return;
        }
    }

    @Override
    public void changeMode(String mode, boolean notifyChart) {

        this.mode = mode;
        if (notifyChart) {
            notifyDataSetChanged();
        }
    }


    Object containsKey(Date date) {
        for (Map.Entry<Date, NamesAndValues> s : NamesAndValuesListD.entrySet()) {
            if (format.format(s.getKey()).equals(format.format(date))) {
                return s.getValue();
            }
        }
        return null;
    }
    @Override
    public FragmentStatePagerAdapter getInstance(){
        return this;
    }

}
