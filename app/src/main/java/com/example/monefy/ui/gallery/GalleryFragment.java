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
    SQLiteDatabase sqLiteDatabase;
    TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
  public static SimpleDateFormat format = new SimpleDateFormat("yyyy", new Locale("uk", "UA"));
    ViewPager viewPager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        sqLiteDatabase = Action.getSqLiteDatabase();
        setData(sqLiteDatabase);

        MyAdapter adapter = new MyAdapter(getChildFragmentManager(), Data);
         viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Action.NamesAndValuesForYears.size());

        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        TreeMap<Date, HistoryClass> Data;
        MyAdapter(@NonNull FragmentManager fm,  TreeMap<Date, HistoryClass> Data) {
            super(fm);
            this.Data = Data;
        }

        @Override
        public int getCount() {
            return Action.NamesAndValuesForYears.size();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Fragment getItem(int position) {
            int i = 0;
            Date date;
           for(Map.Entry<String, NamesAndValues> s : Action.NamesAndValuesForYears.entrySet()){
               if(i==position){
                  date = new Date(s.getKey());
                   return new SelectedYears(format.format(date), s.getValue(), Data);
               }
            i+=1;
           }

            return new SelectedYears("Жодної транзакції", new NamesAndValues(),Data );
        }
    }

    public void setData(SQLiteDatabase sqLiteDatabase) {
        Action.NamesAndValuesForYears.clear();
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<String> names2 = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME3, null, null, null, null, null, null);
        String name, sumaa, check, dates;
        int nameid, suma, checkint, date, i = 0;
        boolean b = true;
        Date date1;

        if (cursor.moveToFirst()) {
            nameid = cursor.getColumnIndex(DBhelp.NAME_COLUMN);
            suma = cursor.getColumnIndex(DBhelp.SUMA_COLUMN);
            checkint = cursor.getColumnIndex(DBhelp.CHECK_COLUMN);
            date = cursor.getColumnIndex(DBhelp.DATE_COLUMN);
            do {

                name = cursor.getString(nameid);
                sumaa = cursor.getString(suma);
                check = cursor.getString(checkint);
                dates = cursor.getString(date);
                date1 = new Date(dates);

                Data.put(date1, new HistoryClass(name, dates, sumaa, check));
                if (!names.containsValue(name) && !check.equals("plus")) {
                    names2.add(name);
                    names.put(name, 0.0);
                }

            }

            while (cursor.moveToNext());
            cursor.close();

            if (Data.isEmpty()) {
                return;
            }
            double result = 0;
            String lastdate = format.format(Data.keySet().iterator().next());
            dates = format.format(Data.keySet().iterator().next());


            for (Map.Entry<Date, HistoryClass> s : Data.entrySet()) {


                if (dates.equals(format.format(s.getKey()))) {
                    if (s.getValue().getCheck().equals("plus")) {
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


                    if (s.getValue().getCheck().equals("plus")) {
                        result += Double.parseDouble(s.getValue().getSuma());

                    } else {
                        result -= Double.parseDouble(s.getValue().getSuma());
                        names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                    }
                }
                if (i == Data.size() - 1) {

                    Action.NamesAndValuesForYears.put(lastdate, new NamesAndValues(names, result));
                    break;

                }

                lastdate = s.getKey().toString();
                i += 1;
            }


        }
    }


}
