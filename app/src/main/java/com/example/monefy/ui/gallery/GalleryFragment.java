package com.example.monefy.ui.gallery;

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
import com.example.monefy.ui.Dayfr.SelectedYears;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

// STOPSHIP: 07.07.2021
public class GalleryFragment extends Fragment {

    TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy", new Locale("uk", "UA"));
    ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForYears.entrySet()) {
                if (i == position) {

                    return new SelectedYears(format.format(s.getKey()), s.getValue(), Data);
                }
                i += 1;
            }

            return new SelectedYears(viewPager.getContext().getResources().getString(R.string.wthtran), new NamesAndValues(), Data);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int i = 0;
            for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForYears.entrySet()) {
                if (i == position) {
                    return format.format(s.getKey());
                }
                i += 1;
            }
            return viewPager.getContext().getResources().getString(R.string.wthtran);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData() {
        Action.NamesAndValuesForYears.clear();
        DataBase dataBase = DataBase.getInstance();
        Data = dataBase.getData();

        FacadeMethods.getInstance().setDataYears(Data);

    }

}
