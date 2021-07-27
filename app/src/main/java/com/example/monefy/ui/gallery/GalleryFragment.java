package com.example.monefy.ui.gallery;

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
import com.example.monefy.ui.Dayfr.SelectedDay;
import com.example.monefy.ui.Dayfr.SelectedYears;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// STOPSHIP: 07.07.2021
public class GalleryFragment extends Fragment {

    TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy", new Locale("uk", "UA"));
    ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        setData();
        viewPager = root.findViewById(R.id.view_pager);
        GalleryFragment.MyAdapter adapter = new GalleryFragment.MyAdapter(getChildFragmentManager(), Data, viewPager);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Action.NamesAndValuesForYears.size() - 1);

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
            if (Action.NamesAndValuesForYears.isEmpty())
                return 1;
            return Action.NamesAndValuesForYears.size();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Fragment getItem(int position) {
            int i = 0;
            Date date;
            for (Map.Entry<String, NamesAndValues> s : Action.NamesAndValuesForYears.entrySet()) {
                if (i == position) {

                    date = new Date(s.getKey());
                    return new SelectedYears(format.format(date), s.getValue(), Data);
                }
                i += 1;
            }

            return new SelectedYears(viewPager.getContext().getResources().getString(R.string.wthtran), new NamesAndValues(), Data);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int i = 0;
            for (Map.Entry<String, NamesAndValues> s : Action.NamesAndValuesForYears.entrySet()) {
                if (i == position) {
                    return format.format(new Date(s.getKey()));
                }
                i += 1;
            }
            return viewPager.getContext().getResources().getString(R.string.wthtran);
        }
    }

    public void setData() {
        Action.NamesAndValuesForYears.clear();
        DataBase dataBase = DataBase.getInstance();
        Data = dataBase.getData();
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> names2 = dataBase.getArray(DataBase.COST);
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
                Action.NamesAndValuesForYears.put(lastdate, new NamesAndValues(names, result));
                result = 0;
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
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                    result += Double.parseDouble(s.getValue().getSuma());

                } else {
                    result -= Double.parseDouble(s.getValue().getSuma());
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                }
            }
            if (i == Data.size() - 1) {

                Action.NamesAndValuesForYears.put(lastdate, new NamesAndValues(names, result, stonks));
                break;

            }

            lastdate = s.getKey().toString();
            i += 1;
        }


    }


}
