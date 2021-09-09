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
        public static SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM (yyyy)", Locale.getDefault());

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
                    return new SelWeek(new NamesAndValues(names, result, stonks), Data, s.getValue());

                }
                count += 1;
            }

            return new SelWeek(new NamesAndValues(), Data, new ArrayList<>());
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
    public void setData() {
        arrayList.clear();
        arrayList2.clear();
        MndWeek.clear();
        Action.NamesAndValuesForWeeks.clear();
        DataBase dataBase = DataBase.getInstance();
        Data = dataBase.getData();
        FacadeMethods.getInstance().setDataWeeks(Data, MndWeek);

        arrayList.addAll(dataBase.getArray(DataBase.COST));
        arrayList2.addAll(dataBase.getArray(DataBase.PROFIT));

    }
}