package com.example.monefy.view_pager_adapters;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.monefy.*;
import com.example.monefy.interfacee.ViewPagerHelp;
import com.github.mikephil.charting.data.Entry;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class YearsAdapter extends FragmentStatePagerAdapter implements ViewPagerHelp {
    TreeMap<Date, HistoryClass> Data;
    double years;
    ArrayList<String> xLabel = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy", new Locale("uk", "UA"));
    public String mode = ALL_MODE;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public YearsAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
        setData();

    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        calendar.setTime(Data.keySet().iterator().next());
        calendar.add(Calendar.YEAR, position * 12);

        List<Entry> lineEntries = new ArrayList<>();
        NamesAndValues o;
        double result;
        for (int i = 0; i < 12; i++) {
            result = 0;
            o = (NamesAndValues) containsKey(calendar.getTime());
            if (o != null) {
                switch (mode) {
                    case PROFIT_MODE:
                        for (Map.Entry<String, Double> s1 : o.getStonks().entrySet()) {
                            result += s1.getValue();
                        }
                        break;

                    case COST_MODE:
                        for (Map.Entry<String, Double> s1 : o.getNames().entrySet()) {
                            result += s1.getValue();
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
            calendar.add(Calendar.YEAR, +1);
        }

        return new MainStatFragment(lineEntries, xLabel, position);
    }

    @Override
    public int getCount() {

        if (!Action.NamesAndValuesForYears.isEmpty() && years < 12) {
            return 1;
        }
        return (int) Math.ceil(years / 12.0);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setData() {

        xLabel.clear();
        DataBase dataBase = DataBase.getInstance();

        Data = dataBase.getData();
        years = FacadeMethods.getInstance().setDataYears(Data);

        if (Action.NamesAndValuesForYears.size() == 0) {
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
        for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForYears.entrySet()) {
            if (format.format(s.getKey()).equals(format.format(date))) {
                return s.getValue();
            }
        }
        return null;
    }

    @Override
    public FragmentStatePagerAdapter getInstance() {
        return this;
    }
}
