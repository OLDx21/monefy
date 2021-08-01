package com.example.monefy.ui.Dayfr;

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
import com.example.monefy.bottoms.BottomSheetDayHistory;
import com.example.monefy.interfacee.DataChange;
import com.example.monefy.interfacee.Method;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SelectedDay extends Fragment implements DataChange {
    public static Calendar calendar;
    public static Date date;
    BottomSheetDayHistory bottomSheets = BottomSheetDayHistory.getInstance();

    Controller controller = new Controller();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        date = calendar.getTime();
        date.setSeconds(new Date().getSeconds());
        date.setMinutes(new Date().getMinutes());
        Action.checked = 1;

        controller.setContextAndInit(getContext(), root, date, this, bottomSheets, true, new Method() {
            @Override
            public void setDate() {

            }
        });
        new Thread(this::addDataSet).start();
        controller.getTextView().setText(Action.formatter2.format(date));
        return root;
    }


    private void addDataSet() {
        double result = 0;
        Action.SelectedDay.clear();

        String name, sumaa, check, dates;
        int i = 0;

        Date date1;

        DataBase dataBase = DataBase.getInstance();

        for (Map.Entry<Date, HistoryClass> s : dataBase.getData().entrySet()) {

            name = s.getValue().getName();
            sumaa = s.getValue().getSuma();
            check = s.getValue().getCheck();
            dates = s.getValue().getDate();
            date1 = new Date(dates);

            if (Action.formatter.format(date1).equals(Action.formatter.format(SelectedDay.date))) {
                Action.SelectedDay.put(date1, new HistoryClass(name, dates, sumaa, check));
                if (check.equals("minus")) {
                    if (!controller.getCostmap().containsKey(name)) {
                        controller.getCostmap().put(name, Double.parseDouble(sumaa));
                    } else {

                        controller.getCostmap().put(name, (Double.parseDouble(sumaa) + controller.getCostmap().get(name)));
                    }
                } else {
                    if (!controller.getStonksmap().containsKey(name)) {
                        controller.getStonksmap().put(name, Double.parseDouble(sumaa));
                    } else {

                        controller.getStonksmap().put(name, (Double.parseDouble(sumaa) + controller.getStonksmap().get(name)));
                    }
                    result += Double.parseDouble(sumaa);
                }
            }
        }

        for (Map.Entry<String, Double> map : controller.getCostmap().entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                controller.getyEntrys().add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                controller.getKategoriesCost().add(map.getKey());
                i += 1;
            }
            result -= map.getValue();

        }
        i = 0;
        for (Map.Entry<String, Double> map : controller.getStonksmap().entrySet()) {
            if (Float.parseFloat(String.valueOf(map.getValue())) > 0) {
                controller.getArraystonks().add(new PieEntry(Float.parseFloat(String.valueOf(map.getValue())), map.getKey(), i));
                controller.getKategoriesStonks().add(map.getKey());
                i += 1;
            }
        }

        //create the data set
        controller.setPieDataSet(new PieDataSet(controller.getyEntrys(), ""));
        controller.setPieData(new PieData());
        controller.setNamesAndValues(new NamesAndValues(controller.getCostmap(), result, controller.getStonksmap()));
    }

    @Override
    public void Update(HistoryAdapterClass historyAdapterClass) {
        controller.Update(historyAdapterClass);
    }
}
