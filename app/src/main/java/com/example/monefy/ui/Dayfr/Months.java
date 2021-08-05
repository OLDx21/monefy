package com.example.monefy.ui.Dayfr;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.monefy.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Months extends Fragment {


    TreeMap<Date, HistoryClass> Data;
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", new Locale("uk", "UA"));
    ViewPager viewPager;
    public int months;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        setData();
        viewPager = root.findViewById(R.id.view_pager);
        Months.MyAdapter adapter = new Months.MyAdapter(getChildFragmentManager(), Data, viewPager, months);

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(months);

        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        TreeMap<Date, HistoryClass> Data;
        ViewPager viewPager;
        int month;
        Calendar calendar = Calendar.getInstance();

        MyAdapter(@NonNull FragmentManager fm, TreeMap<Date, HistoryClass> Data, ViewPager viewPager, int month) {
            super(fm);
            this.Data = Data;
            this.viewPager = viewPager;
            this.month = month;

        }

        @Override
        public int getCount() {
            if (month == 0)
                return 1;
            return month + 1;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (Data.isEmpty())
                return new SelMonth(new Date(), new NamesAndValues(), Data);
            calendar.setTime(Data.keySet().iterator().next());
            calendar.add(Calendar.MONTH, position);

            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForMonth.entrySet()) {
                if (format.format(s.getKey()).equals(format.format(calendar.getTime()))) {
                    return new SelMonth(s.getKey(), s.getValue(), Data);
                }
            }
            return new SelMonth(calendar.getTime(), new NamesAndValues(), Data);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (Data.isEmpty())
                return viewPager.getContext().getString(R.string.wthtran);
            calendar.setTime(Data.keySet().iterator().next());
            calendar.add(Calendar.MONTH, position);

            if (!Action.NamesAndValuesForMonth.isEmpty()) {
                return Action.format.format(calendar.getTime());
            }
            return viewPager.getContext().getString(R.string.wthtran);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData() {
        Action.NamesAndValuesForMonth.clear();
        DataBase dataBase = DataBase.getInstance();
        Data = dataBase.getData();
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<String> names2 = dataBase.getArray(DataBase.COST);

        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> stonks2 = dataBase.getArray(DataBase.PROFIT);

        for (int in = 0; in < names2.size(); in++) {
            names.put(names2.get(in), 0.0);
        }
        for (int in = 0; in < stonks2.size(); in++) {
            stonks.put(stonks2.get(in), 0.0);
        }

        String dates;
        int i = 0;


        if (Data.isEmpty()) {
            return;
        }

        double result = 0;
        String lastdate = Data.keySet().iterator().next().toString();
        dates = format.format(Data.keySet().iterator().next());


        for (Map.Entry<Date, HistoryClass> s : Data.entrySet()) {


            if (dates.equals(format.format(s.getKey()))) {
                if (s.getValue().getCheck().equals("plus")) {
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                    result += Double.parseDouble(s.getValue().getSuma());
                } else {
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                    result -= Double.parseDouble(s.getValue().getSuma());
                }


            } else {
                Action.NamesAndValuesForMonth.put(new Date(lastdate), new NamesAndValues(names, result, stonks));
                result = 0;
                stonks = new LinkedHashMap<>();
                names = new LinkedHashMap<>();
                lastdate = s.getKey().toString();
                dates = format.format(s.getKey());
                for (int in = 0; in < names2.size(); in++) {
                    names.put(names2.get(in), 0.0);
                }
                for (int in = 0; in < stonks2.size(); in++) {

                    stonks.put(stonks2.get(in), 0.0);
                }

                if (s.getValue().getCheck().equals("plus")) {
                    result += Double.parseDouble(s.getValue().getSuma());
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                } else {
                    result -= Double.parseDouble(s.getValue().getSuma());
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                }
            }

            if (i == Data.size() - 1) {
                Action.NamesAndValuesForMonth.put(new Date(lastdate), new NamesAndValues(names, result, stonks));
                break;
            }

            lastdate = s.getKey().toString();
            i += 1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(lastdate));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(Data.keySet().iterator().next().toString()));

        months = (int) ChronoUnit.MONTHS.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis()), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(calendar.getTimeInMillis()), ZoneId.systemDefault()));
        calendar = null;
        calendar1 = null;
    }
}


