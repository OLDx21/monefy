package com.example.monefy.ui.home;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;
import com.example.monefy.bottoms.BottomSheet;
import com.example.monefy.interfacee.DataChange;
import com.example.monefy.interfacee.Method;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.Date;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment implements DataChange {

    SQLiteDatabase sqLiteDatabase = Action.getSqLiteDatabase();
    BottomSheet bottomSheets = BottomSheet.getInstance();
    Controller controller = new Controller();

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.R)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Action.checked = 0;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Action.display = display;

        if (Action.ishave) {
            DataBase dataBase = DataBase.getInstance();
            dataBase.CreateDB(sqLiteDatabase);
            Action.ishave = false;
        }
        controller.setContextAndInit(getContext(), root, new Date(), this, bottomSheets,true,new Method() {
            @Override
            public void setDate() {
            }
        });
        new Thread(this::addDataSet).start();

        controller.getTextView().setText(Action.formatter2.format(new Date()));

        return root;
    }

    private void addDataSet() {
        String name, value;
        int i = 0;
        double sum2 = 0, values = 0;
        for (Map.Entry<Date, HistoryClass> s : DataBase.getInstance().getData().entrySet()) {
            if (s.getValue().getCheck().equals("minus")) {

                name = s.getValue().getName();
                value = s.getValue().getSuma();
                sum2 -= Double.parseDouble(value);
                values = controller.getCostmap().containsKey(name) ? controller.getCostmap().get(name) : 0;
                controller.getCostmap().put(name, Double.parseDouble(value) + values);
            }
            else {
                value = s.getValue().getSuma();
                name = s.getValue().getName();
                sum2 += Double.parseDouble(value);


                if (!controller.getStonksmap().containsKey(name)) {
                    controller.getStonksmap().put(name, Double.parseDouble(value));
                } else {
                    controller.getStonksmap().put(name, Double.parseDouble(value) + controller.getStonksmap().get(name));
                }
            }
        }


        for (Map.Entry<String, Double> s : controller.getCostmap().entrySet()) {
            if (Float.parseFloat(String.valueOf(s.getValue())) > 0) {
                controller.getyEntrys().add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                controller.getKategoriesCost().add(s.getKey());
                i += 1;
            }
        }
         i=0;
        for (Map.Entry<String, Double> s : controller.getStonksmap().entrySet()) {
            if (s.getValue() > 0) {
            controller.getArraystonks().add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
            controller.getKategoriesStonks().add(s.getKey());
            i += 1;
        }
        }


        controller.setPieDataSet(new PieDataSet(controller.getyEntrys(), ""));
        controller.setPieData(new PieData());
        Action.drawChart(controller.getPieChart(), controller.getPieData(), controller.getPieDataSet());
        controller.setNamesAndValues(new NamesAndValues(controller.getCostmap(), sum2,controller.getStonksmap()));

    }

    @Override
    public void Update(HistoryAdapterClass historyAdapterClass) {
     controller.Update(historyAdapterClass);
    }

}