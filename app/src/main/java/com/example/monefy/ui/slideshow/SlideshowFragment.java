package com.example.monefy.ui.slideshow;

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
import com.example.monefy.ui.Dayfr.Days;

import java.text.SimpleDateFormat;
import java.util.*;

public class SlideshowFragment extends Fragment {


    TreeMap<Date, HistoryClass> Data = new TreeMap<>();
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", new Locale("uk", "UA"));
    ViewPager viewPager;
    public int days;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        viewPager = root.findViewById(R.id.view_pager);

        setData();

        SlideshowFragment.MyAdapter adapter = new SlideshowFragment.MyAdapter(getChildFragmentManager(), Data, viewPager, days);

        viewPager.setAdapter(adapter);
        if (Action.position == null) {

            viewPager.setCurrentItem(days);
        } else {

            viewPager.setCurrentItem(Action.position);
        }

        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        TreeMap<Date, HistoryClass> Data;
        ViewPager viewPager;
        int days;
        Calendar calendar = Calendar.getInstance();

        MyAdapter(@NonNull FragmentManager fm, TreeMap<Date, HistoryClass> Data, ViewPager viewPager, int days) {
            super(fm);
            this.Data = Data;
            this.viewPager = viewPager;
            this.days = days;
        }

        @Override
        public int getCount() {
            if (days == 0)
                return 1;
            return days+1;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (Data.isEmpty())
                return new Days(new Date(), new NamesAndValues(), Data);
            calendar.setTime(Data.keySet().iterator().next());
            calendar.add(Calendar.DATE, position);

            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForDays.entrySet()) {
                if (format.format(s.getKey()).equals(format.format(calendar.getTime()))) {

                    Action.position = viewPager.getCurrentItem();
                    return new Days(s.getKey(), s.getValue(), Data);
                }
            }
            Action.position = viewPager.getCurrentItem();

            return new Days(calendar.getTime(), new NamesAndValues(), Data);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (Data.isEmpty())
                return viewPager.getContext().getString(R.string.wthtran);
            calendar.setTime(Data.keySet().iterator().next());
            calendar.add(Calendar.DATE, position);

            if (!Action.NamesAndValuesForDays.isEmpty()) {
                return Action.formatter2.format(calendar.getTime());
            }
            return viewPager.getContext().getString(R.string.wthtran);
        }


    }

    public void setData() {
        Action.NamesAndValuesForDays.clear();
        DataBase dataBase = DataBase.getInstance();
        Data = dataBase.getData();
        days = FacadeMethods.getInstance().setDataDays(Action.NamesAndValuesForDays);

    }
}