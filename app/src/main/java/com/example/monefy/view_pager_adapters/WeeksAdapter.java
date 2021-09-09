package com.example.monefy.view_pager_adapters;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.monefy.*;
import com.example.monefy.interfacee.ViewPagerHelp;
import com.example.monefy.ui.Dayfr.Weeks;
import com.github.mikephil.charting.data.Entry;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class WeeksAdapter extends FragmentStatePagerAdapter implements ViewPagerHelp {


    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();
    TreeMap<Date, ArrayList<Date>> MndWeek = new TreeMap<>();
    TreeMap<Date, HistoryClass> Data;
    LinkedHashMap<ArrayList<Date>, NamesAndValues> allInfo = new LinkedHashMap<>();
    ArrayList<String> xLabel = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy", new Locale("uk", "UA"));
    public String mode = ALL_MODE;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public WeeksAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
        setData();


    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        if (allInfo.size() < 2) {
            return new MainStatFragment(new ArrayList<>(), xLabel, position);
        }

        int count = 0, i = 0;
        List<Entry> lineEntries = new ArrayList<>();
        NamesAndValues o;
        double result;

        for (Map.Entry<ArrayList<Date>, NamesAndValues> s : allInfo.entrySet()) {
            result = 0;
            if (count >= (position * 12) && count < ((position * 12) + 12)) {
                o = s.getValue();
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
                i += 1;
            }
            count += 1;
        }
        return new MainStatFragment(lineEntries, xLabel, position);
    }

    @Override
    public int getCount() {
        if (!allInfo.isEmpty() && allInfo.size() < 12) {
            return 1;
        }
        return (int) Math.ceil(allInfo.size() / 12.0);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setData() {
        arrayList.clear();
        arrayList2.clear();
        MndWeek.clear();
        xLabel.clear();
        Action.NamesAndValuesForWeeks.clear();
        DataBase dataBase = DataBase.getInstance();
        Data = dataBase.getData();
        FacadeMethods.getInstance().setDataWeeks(Data, MndWeek);
        if (MndWeek.isEmpty()) {
            return;
        }
        arrayList.addAll(dataBase.getArray(DataBase.COST));
        arrayList2.addAll(dataBase.getArray(DataBase.PROFIT));

        LinkedHashMap<String, Double> names, stonks;
        NamesAndValues namesAndValues;
        double result;

        for (Map.Entry<Date, ArrayList<Date>> s : MndWeek.entrySet()) {
            names = new LinkedHashMap<>();
            stonks = new LinkedHashMap<>();
            result = 0;
            for (int in = 0; in < arrayList.size(); in++) {
                names.put(arrayList.get(in), 0.0);
            }

            for (int in = 0; in < arrayList2.size(); in++) {
                stonks.put(arrayList2.get(in), 0.0);
            }

            for (int i = 0; i < s.getValue().size(); i++) {
                if (Action.NamesAndValuesForWeeks.containsKey(Weeks.format.format(s.getValue().get(i)))) {
                    namesAndValues = Action.NamesAndValuesForWeeks.get(Weeks.format.format(s.getValue().get(i)));
                    result += namesAndValues.getResult();
                    for (Map.Entry<String, Double> s1 : namesAndValues.getNames().entrySet()) {
                        names.put(s1.getKey(), (names.get(s1.getKey()) + s1.getValue()));
                    }
                    for (Map.Entry<String, Double> s1 : namesAndValues.getStonks().entrySet()) {
                        stonks.put(s1.getKey(), (stonks.get(s1.getKey()) + s1.getValue()));
                    }

                }

            }

            allInfo.put(s.getValue(), new NamesAndValues(names, result, stonks));
            xLabel.add(format.format(s.getValue().get(0)) + " - " + format.format(s.getValue().get(s.getValue().size() - 1)));
        }
    }

    @Override
    public void changeMode(String mode, boolean notifyChart) {
        this.mode = mode;
        if (notifyChart) {
            notifyDataSetChanged();
        }
    }

    @Override
    public FragmentStatePagerAdapter getInstance() {
        return this;
    }
}
