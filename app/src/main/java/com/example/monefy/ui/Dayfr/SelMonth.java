package com.example.monefy.ui.Dayfr;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;
import com.example.monefy.bottoms.BottomSheetForMonths;
import com.example.monefy.interfacee.DataChange;
import com.example.monefy.interfacee.Method;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SelMonth extends Fragment implements DataChange {

    BottomSheetForMonths bottomSheets = BottomSheetForMonths.getInstance();
    NamesAndValues namesAndValues;
    TreeMap<Date, HistoryClass> Data;
    Date date;
    Controller controller = new Controller();

    public SelMonth(Date years, NamesAndValues namesAndValues, TreeMap<Date, HistoryClass> Data) {
        this.date = years;
        this.namesAndValues = namesAndValues;
        this.Data = Data;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textViewttx = root.findViewById(R.id.textView);
        textViewttx.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 15));
        Action.checked = 6;

        controller.setContextAndInit(getContext(), root, null, this, bottomSheets, false, new Method() {
            @Override
            public void setDate() {
                BottomSheetForMonths.Data.clear();
                BottomSheetForMonths.Data.putAll(Data);
                BottomSheetForMonths.CheckDate = Months.format.format(date);
            }
        });
        new Thread(this::addDataToChart).start();

        return root;
    }


    void addDataToChart() {
        controller.getArraystonks().clear();
        controller.getKategoriesStonks().clear();
        controller.getKategoriesCost().clear();
        controller.getyEntrys().clear();
        int i = 0;
        if (!(namesAndValues.getNames() == null) && !(namesAndValues.getNames().isEmpty())) {
            for (Map.Entry<String, Double> s : namesAndValues.getNames().entrySet()) {
                if (Float.parseFloat(String.valueOf(s.getValue())) > 0.0) {
                    controller.getyEntrys().add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                    controller.getKategoriesCost().add(s.getKey());
                    i += 1;
                }
            }
        }
        i = 0;

        if (namesAndValues.getStonks() != null && !(namesAndValues.getStonks().isEmpty())) {

            for (Map.Entry<String, Double> s : namesAndValues.getStonks().entrySet()) {
                if (Float.parseFloat(String.valueOf(s.getValue())) > 0.0) {
                    controller.getArraystonks().add(new PieEntry(Float.parseFloat(String.valueOf(s.getValue())), s.getKey(), i));
                    controller.getKategoriesStonks().add(s.getKey());
                    i += 1;
                }
            }
        }

        controller.setPieDataSet(new PieDataSet(controller.getyEntrys(), ""));
        controller.setPieData(new PieData());
        controller.setNamesAndValues(namesAndValues);
    }

    @Override
    public void Update(HistoryAdapterClass historyAdapterClass) {
        controller.Update(historyAdapterClass);
    }
}
