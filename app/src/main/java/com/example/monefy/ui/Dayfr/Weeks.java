package com.example.monefy.ui.Dayfr;

import android.annotation.SuppressLint;
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

public class Weeks extends Fragment {

    static ArrayList<String> arrayList = new ArrayList<>();
    static ArrayList<String> arrayList2 = new ArrayList<>();
    static TreeMap<Date, ArrayList<Date>> MndWeek = new TreeMap<>();

    TreeMap<Date, HistoryClass> Data = new TreeMap<>();
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", new Locale("uk", "UA"));
    ViewPager viewPager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        setData();
        viewPager = root.findViewById(R.id.view_pager);
        Weeks.MyAdapter adapter = new Weeks.MyAdapter(getChildFragmentManager(), Data, viewPager);

        viewPager.setAdapter(adapter);


        viewPager.setCurrentItem(MndWeek.size() - 1);


        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        TreeMap<Date, HistoryClass> Data;
        ViewPager viewPager;
        public SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM", Locale.getDefault());

        MyAdapter(@NonNull FragmentManager fm, TreeMap<Date, HistoryClass> Data, ViewPager viewPager) {
            super(fm);
            this.Data = Data;
            this.viewPager = viewPager;
        }

        @Override
        public int getCount() {
            if (MndWeek.isEmpty())
                return 1;
            return MndWeek.size();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Fragment getItem(int position) {
            int count = 0;

            for (Map.Entry<Date, ArrayList<Date>> s : MndWeek.entrySet()) {
                if (count == position) {
                    double result = 0;
                    LinkedHashMap<String, Double> names = new LinkedHashMap<>();
                    LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        names.put(arrayList.get(i), 0.0);
                    }
                    for (int i = 0; i < arrayList2.size(); i++) {
                        stonks.put(arrayList2.get(i), 0.0);
                    }
                    for (int i = 0; i < s.getValue().size(); i++) {
                        if (Action.NamesAndValuesForWeeks.containsKey(format.format(s.getValue().get(i)))) {

                            result += Action.NamesAndValuesForWeeks.get(format.format(s.getValue().get(i))).getResult();

                            for (Map.Entry<String, Double> s2 : Action.NamesAndValuesForWeeks.get(format.format(s.getValue().get(i))).getNames().entrySet()) {
                                names.put(s2.getKey(), names.get(s2.getKey()) + s2.getValue());
                            }
                            for (Map.Entry<String, Double> s2 : Action.NamesAndValuesForWeeks.get(format.format(s.getValue().get(i))).getStonks().entrySet()) {
                                stonks.put(s2.getKey(), stonks.get(s2.getKey()) + s2.getValue());
                            }

                        }
                    }
                    return new SelWeek(formatter2.format(s.getValue().get(0)) + " - " + formatter2.format(s.getValue().get(s.getValue().size() - 1)),
                            new NamesAndValues(names, result, stonks), Data, s.getValue());

                }
                count += 1;
            }

            return new SelWeek(viewPager.getContext().getResources().getString(R.string.wthtran), new NamesAndValues(), Data, new ArrayList<>());
        }
        @Override
        public CharSequence getPageTitle(int position) {
            int i = 0;

            for (Map.Entry<Date, ArrayList<Date>> s : MndWeek.entrySet()) {
                if (i == position) {
                    return formatter2.format(s.getValue().get(0)) + " - " + formatter2.format(s.getValue().get(s.getValue().size() - 1));
                }
                i += 1;
            }
            return viewPager.getContext().getResources().getString(R.string.wthtran);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData( ) {
        arrayList.clear();
        MndWeek.clear();
        Action.NamesAndValuesForWeeks.clear();
        DataBase dataBase = DataBase.getInstance();
        Data = dataBase.getData();

        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<String> names2 = dataBase.getArray(DataBase.COST);
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> stonks2 = dataBase.getArray(DataBase.PROFIT);

        String  dates;
        int i = 0;
        Date date1;
        for (int in = 0; in < names2.size(); in++) {
            names.put(names2.get(in), 0.0);
        }
        for (int in = 0; in < stonks2.size(); in++) {
            stonks.put(stonks2.get(in), 0.0);
        }

            if (Data.isEmpty()) {
                return;
            }
            double result = 0;
            String lastdate = format.format(Data.keySet().iterator().next());
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
                    Action.NamesAndValuesForWeeks.put(lastdate, new NamesAndValues(names, result, stonks));
                    result = 0;
                    stonks = new LinkedHashMap<>();
                    names = new LinkedHashMap<>();
                    lastdate  = format.format(s.getKey());
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

                    Action.NamesAndValuesForWeeks.put(lastdate, new NamesAndValues(names, result, stonks));
                    lastdate = s.getKey().toString();
                    break;

                }

                lastdate = format.format(s.getKey());
                i += 1;
            }
            arrayList.addAll(names2);
            arrayList2.addAll(stonks2);


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(lastdate));

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date(Data.keySet().iterator().next().toString()));

            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(calendar1.getTime());
            int weeks = calendar.get(Calendar.WEEK_OF_YEAR) - calendar1.get(Calendar.WEEK_OF_YEAR);
            ArrayList<Date> arrayList;

            for (int in = 1; in <= weeks + 2; in++) {

                LocalDate mondayOfWeek = LocalDate.of(calendar3.get(Calendar.YEAR), Month.JUNE, 1)
                        .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - in)
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                calendar3.setTime(Date.from(mondayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                date1 = calendar3.getTime();
                arrayList = new ArrayList<>();

                for (int g = 0; g < 7; g++) {
                    calendar3.setTime(date1);
                    calendar3.add(Calendar.DATE, +g);
                    arrayList.add(calendar3.getTime());
                }
                MndWeek.put(date1, arrayList);
            }



    }


}