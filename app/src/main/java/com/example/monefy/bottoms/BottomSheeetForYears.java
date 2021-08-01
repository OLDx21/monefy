package com.example.monefy.bottoms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.monefy.*;
import com.example.monefy.interfacee.BottomSheet;
import com.example.monefy.interfacee.DataChange;
import com.example.monefy.ui.gallery.GalleryFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BottomSheeetForYears  implements BottomSheet {

    public static BottomSheeetForYears bottomSheet = new BottomSheeetForYears();
    public static TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
    public static String CheckDate;

    public static BottomSheeetForYears getInstance() {
        return bottomSheet;
    }


    List<String> listDataParent = new ArrayList<String>();
    LinkedHashMap<String, List<HistoryAdapterClass>> listDataChild = new LinkedHashMap<>();
    ArrayList<List<HistoryAdapterClass>> arrayList = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public void setDataList(TextView textView, ExpandableListView expandableListView, Context context, DataChange dataChange) {

        listDataParent.clear();
        listDataChild.clear();
        arrayList.clear();

        int i = 0;
        String date = "";
        boolean bool = true;
        System.out.format(CheckDate);
        List<HistoryAdapterClass> colors = new ArrayList<HistoryAdapterClass>();
        for (Map.Entry<Date, HistoryClass> s : Data.entrySet()) {
            if (!CheckDate.equals(GalleryFragment.format.format(s.getKey()))) {

                if (i == Data.size() - 1 && !colors.isEmpty()) {
                    arrayList.add(colors);
                }
                i += 1;
                continue;

            }
            if (bool) {
                bool = false;
                colors = new ArrayList<>();
                date = Action.formatter.format(s.getKey());
                listDataParent.add(date);
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName(), s.getKey()));

                if (i == Data.size() - 1) {
                    arrayList.add(colors);
                }
                i += 1;
                continue;
            }
            if (date.equals(Action.formatter.format(s.getKey()))) {
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName(), s.getKey()));

            } else {
                arrayList.add(colors);
                colors = new ArrayList<>();
                date = Action.formatter.format(s.getKey());
                listDataParent.add(date);
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName(), s.getKey()));
            }
            if (i == Data.size() - 1) {
                arrayList.add(colors);

            }
            i += 1;

        }


        for (int b = 0; b < arrayList.size(); b++) {
            listDataChild.put(listDataParent.get(b), arrayList.get(b));

        }


        ExpandbleListAdapter listAdapter = new ExpandbleListAdapter(listDataChild, context, dataChange, expandableListView);
        expandableListView.setAdapter(listAdapter);
        if (listDataParent.isEmpty()) {
            textView.setText(context.getResources().getString(R.string.wthtran));
        }



    }




}
