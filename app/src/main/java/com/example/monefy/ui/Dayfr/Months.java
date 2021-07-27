package com.example.monefy.ui.Dayfr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.example.monefy.*;
import com.example.monefy.ui.Dayfr.Days;
import com.example.monefy.ui.Dayfr.SelectedYears;
import com.example.monefy.ui.gallery.GalleryFragment;
import com.example.monefy.ui.slideshow.SlideshowFragment;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Months extends Fragment {



    TreeMap<Date, HistoryClass> Data;
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", new Locale("uk", "UA"));
    ViewPager viewPager;



    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        setData();
        viewPager = root.findViewById(R.id.view_pager);
        Months.MyAdapter adapter = new Months.MyAdapter(getChildFragmentManager(), Data, viewPager);

        viewPager.setAdapter(adapter);


        viewPager.setCurrentItem(Action.NamesAndValuesForMonth.size()-1);


        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        TreeMap<Date, HistoryClass> Data;
        ViewPager viewPager;

        MyAdapter(@NonNull FragmentManager fm, TreeMap<Date, HistoryClass> Data, ViewPager viewPager) {
            super(fm);
            this.Data = Data;
            this.viewPager = viewPager;
        }

        @Override
        public int getCount() {
            if(Action.NamesAndValuesForMonth.isEmpty())
                return 1;
            return Action.NamesAndValuesForMonth.size();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Fragment getItem(int position) {
            int count = 0;

            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForMonth.entrySet()) {
                if (count == position) {
                    return new SelMonth(s.getKey(), s.getValue(), Data);

                }
                count += 1;
            }

            return new SelMonth(viewPager.getContext().getResources().getString(R.string.wthtran), new NamesAndValues(), Data);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            int i = 0;

            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForMonth.entrySet()) {
                if (i == position) {
                    return Action.format.format(s.getKey());
                }
                i += 1;
            }
            return viewPager.getContext().getResources().getString(R.string.wthtran);
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

        String  dates;
        int  i = 0;



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


        }
    }


