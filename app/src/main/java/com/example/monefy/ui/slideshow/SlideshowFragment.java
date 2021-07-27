package com.example.monefy.ui.slideshow;

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
import com.example.monefy.bottoms.BottomSheetForDays;
import com.example.monefy.ui.Dayfr.Days;
import com.example.monefy.ui.Dayfr.SelectedYears;
import com.example.monefy.ui.gallery.GalleryFragment;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class SlideshowFragment extends Fragment {



    TreeMap<Date, HistoryClass> Data = new TreeMap<>();
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", new Locale("uk", "UA"));
    ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        setData();
        viewPager = root.findViewById(R.id.view_pager);
        SlideshowFragment.MyAdapter adapter = new SlideshowFragment.MyAdapter(getChildFragmentManager(), Data, viewPager);

        viewPager.setAdapter(adapter);

        if (Action.position == null) {
            viewPager.setCurrentItem(Action.NamesAndValuesForDays.size() - 1);
        } else {
            viewPager.setCurrentItem(Action.position);
        }


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
            if (Action.NamesAndValuesForDays.isEmpty())
                return 1;
            return Action.NamesAndValuesForDays.size();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Fragment getItem(int position) {
            int i = 0;

            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForDays.entrySet()) {
                if (i == position) {
                    Action.position = viewPager.getCurrentItem();
                    return new Days(s.getKey(), s.getValue(), Data);
                }
                i += 1;
            }

            return new Days(new Date(), new NamesAndValues(), Data);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            int i = 0;

            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForDays.entrySet()) {
                if (i == position) {
                    return Action.formatter2.format(s.getKey());
                }
                i += 1;
            }
            return  viewPager.getContext().getString(R.string.wthtran);
        }


    }

    public void setData( ) {
        Action.NamesAndValuesForDays.clear();
        DataBase dataBase = DataBase.getInstance();
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<String> names2 = dataBase.getArray(DataBase.COST);
        Data = dataBase.getData();
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> stonks2 = dataBase.getArray(DataBase.PROFIT);


        String dates;
        int i = 0;
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
                Action.NamesAndValuesForDays.put(new Date(lastdate), new NamesAndValues(names, result, stonks));
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

                Action.NamesAndValuesForDays.put(new Date(lastdate), new NamesAndValues(names, result, stonks));

            }

            lastdate = s.getKey().toString();
            i += 1;
        }

        long milliseconds = new Date(lastdate).getTime() - Action.NamesAndValuesForDays.keySet().iterator().next().getTime();
        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));

        Calendar c = Calendar.getInstance();

        for (int in = 0; in <= days; in++) {

            c.setTime(new Date(lastdate));
            c.add(Calendar.DATE, -in);

            if (!containsKey(format.format(c.getTime()))) {
                Action.NamesAndValuesForDays.put(c.getTime(), new NamesAndValues());
            }
        }


    }


    boolean containsKey(String date) {
        for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForDays.entrySet()) {
            if (date.equals(format.format(s.getKey()))) {
                return true;
            }

        }
        return false;
    }

}