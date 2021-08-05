package com.example.monefy.bottoms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.monefy.*;
import com.example.monefy.interfacee.BottomSheet;
import com.example.monefy.interfacee.DataChange;
import com.example.monefy.ui.slideshow.SlideshowFragment;

import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BottomSheetForWeeks implements BottomSheet {

    public static BottomSheetForWeeks bottomSheet = new BottomSheetForWeeks();
    public static TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
    public static ArrayList<Date> CheckDate = new ArrayList<>();

    public static BottomSheetForWeeks getInstance() {
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

        List<HistoryAdapterClass> colors = new ArrayList<HistoryAdapterClass>();
        ArrayList<String> strings = new ArrayList<>();


        for(int in = 0; in<CheckDate.size(); in++){
            strings.add(SlideshowFragment.format.format(CheckDate.get(in)));
        }

        for (Map.Entry<Date, HistoryClass> s : Data.entrySet()) {
            if(!strings.contains(SlideshowFragment.format.format(s.getKey()))){

                if (i == Data.size() - 1&&!colors.isEmpty()) {

                    arrayList.add(colors);
                }
                i+=1;
                continue;

            }
            if (bool) {

                bool = false;
                colors = new ArrayList<>();
                date = Action.formatter3.format(s.getKey());
                listDataParent.add(date);
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName(), s.getKey()));

                if (i == Data.size() - 1) {

                    arrayList.add(colors);
                }
                i += 1;
                continue;
            }
            if (date.equals(Action.formatter3.format(s.getKey()))) {
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName(), s.getKey()));

            } else {

                arrayList.add(colors);
                colors = new ArrayList<>();
                date = Action.formatter3.format(s.getKey());
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
        if (listDataParent.size() == 0) {
            textView.setText(context.getResources().getString(R.string.wthtran));
        }


    }

}
