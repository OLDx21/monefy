package com.example.monefy.ui.intervdays;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;
import com.example.monefy.bottoms.BottomSheetForIntervals;
import com.example.monefy.interfacee.DataChange;
import com.example.monefy.interfacee.Method;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class IntervalDays extends Fragment implements DataChange {

    public static SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM (yyyy)", Locale.getDefault());
    BottomSheetForIntervals bottomSheets = BottomSheetForIntervals.getInstance();
    public TextView textView;
    Date from;
    Date to;
    ArrayList<String> arrayList = new ArrayList<>();
    public static TreeMap<Date, Date> dateTreeMap;

    Controller controller = new Controller();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Action.checked = 7;
        from = dateTreeMap.keySet().iterator().next();
        to = (Date) dateTreeMap.keySet().toArray()[dateTreeMap.size() - 1];

        for (Map.Entry<Date, Date> s : dateTreeMap.entrySet()) {
            arrayList.add(Action.formatter.format(s.getValue()));
        }

        controller.setContextAndInit(getContext(), root, null, this, bottomSheets, false, new Method() {
            @Override
            public void setDate() {
                BottomSheetForIntervals.CheckDate = arrayList;
            }
        });
        new Thread(this::setData).start();

        controller.getTextView().setText(formatter2.format(from) + " - " + formatter2.format(to));

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData() {
        TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
        String name, sumaa, check, dates;
        int i = 0;
        Date date1;
        double results = 0;


        for (Map.Entry<Date, HistoryClass> s : DataBase.getInstance().getData().entrySet()) {
            name = s.getValue().getName();
            sumaa = s.getValue().getSuma();
            check = s.getValue().getCheck();
            dates = s.getValue().getDate();
            date1 = new Date(dates);

            if (arrayList.contains(Action.formatter.format(date1))) {
                Data.put(date1, new HistoryClass(name, dates, sumaa, check));
                if (check.equals("minus")) {
                    results -= Double.parseDouble(sumaa);
                    if (!controller.getCostmap().containsKey(name)) {
                        controller.getCostmap().put(name, Double.parseDouble(sumaa));
                    } else {
                        controller.getCostmap().put(name, Double.parseDouble(sumaa) + controller.getCostmap().get(name));
                    }

                } else {
                    if (!controller.getStonksmap().containsKey(name)) {
                        controller.getStonksmap().put(name, Double.parseDouble(sumaa));
                    } else {
                        controller.getStonksmap().put(name, Double.parseDouble(sumaa) + controller.getStonksmap().get(name));
                    }
                    results += Double.parseDouble(sumaa);
                }
            }
        }

        BottomSheetForIntervals.Data = Data;
        for (Map.Entry<String, Double> map : controller.getCostmap().entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                controller.getyEntrys().add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                controller.getKategoriesCost().add(map.getKey());
                i += 1;
            }
        }
        i = 0;
        for (Map.Entry<String, Double> map : controller.getStonksmap().entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                controller.getArraystonks().add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                controller.getKategoriesStonks().add(map.getKey());
                i += 1;
            }
        }

        controller.setPieDataSet(new PieDataSet(controller.getyEntrys(), ""));
        controller.setPieData(new PieData());
        controller.setNamesAndValues(new NamesAndValues(controller.getCostmap(), results, controller.getStonksmap()));
    }

    @Override
    public void Update(HistoryAdapterClass historyAdapterClass) {
        controller.Update(historyAdapterClass);
    }
}