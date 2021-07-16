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
import com.example.monefy.ui.gallery.GalleryFragment;
import com.example.monefy.ui.slideshow.SlideshowFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BottomSheetForWeeks extends BottomSheetDialogFragment {
    private BottomSheet.BottomSheetListener mListener;
    public static BottomSheetForWeeks bottomSheet = new BottomSheetForWeeks();
    public static TreeMap<Date, HistoryClass> Data = new TreeMap<>(Collections.reverseOrder());
    public static ArrayList<Date> CheckDate = new ArrayList<>();

    public static BottomSheetForWeeks getInstance() {
        return bottomSheet;
    }

    TextView textView;
    ExpandableListView expandableListView;
    List<String> listDataParent = new ArrayList<String>();
    LinkedHashMap<String, List<HistoryAdapterClass>> listDataChild = new LinkedHashMap<>();
    ArrayList<List<HistoryAdapterClass>> arrayList = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom, container, false);
        listDataParent.clear();
        listDataChild.clear();
        arrayList.clear();
        textView = v.findViewById(R.id.textView);
        expandableListView = v.findViewById(R.id.expanded_menu);
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

                if (i == Data.size() - 1&&!arrayList.isEmpty()) {

                    arrayList.add(colors);
                }
                i+=1;
                continue;

            }
            if (bool) {

                bool = false;
                colors = new ArrayList<>();
                date = Action.formatter.format(s.getKey());
                listDataParent.add(date);
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName()));

                if (i == Data.size() - 1) {

                    arrayList.add(colors);
                }
                i += 1;
                continue;
            }
            if (date.equals(Action.formatter.format(s.getKey()))) {
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName()));

            } else {

                arrayList.add(colors);
                colors = new ArrayList<>();
                date = Action.formatter.format(s.getKey());
                listDataParent.add(date);
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName()));
            }
            if (i == Data.size() - 1) {

                arrayList.add(colors);

            }
            i += 1;

        }
        for (int b = 0; b < arrayList.size(); b++) {
            listDataChild.put(listDataParent.get(b), arrayList.get(b));

        }


        ExpandbleListAdapter listAdapter = new ExpandbleListAdapter(listDataChild, getActivity());
        expandableListView.setAdapter(listAdapter);
        if (listDataParent.size() == 0) {
            textView.setText("Жодної транзакції");
        }

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheet.BottomSheetListener) context;
        } catch (ClassCastException e) {

        }
    }

}