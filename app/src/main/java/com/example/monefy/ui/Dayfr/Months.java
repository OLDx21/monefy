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
        months = FacadeMethods.getInstance().setDataMonth(Data);
    }
}


