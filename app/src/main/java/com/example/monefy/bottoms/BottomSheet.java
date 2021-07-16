package com.example.monefy.bottoms;

import com.example.monefy.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.*;


@RequiresApi(api = Build.VERSION_CODES.O)
public class BottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    public static BottomSheet bottomSheet = new BottomSheet();

    public static BottomSheet getInstance() {
        return bottomSheet;
    }

    TextView textView;
    ExpandableListView expandableListView;
    SQLiteDatabase sqLiteDatabase;
    DBhelp dBhelp;
    TreeMap<Date, HistoryClass> treeMap = new TreeMap<Date, HistoryClass>(Collections.reverseOrder());
    Date date1;
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    List<String> listDataParent = new ArrayList<String>();
    LinkedHashMap<String, List<HistoryAdapterClass>> listDataChild = new LinkedHashMap<>();
    ArrayList<List<HistoryAdapterClass>> arrayList = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom, container, false);

        textView = v.findViewById(R.id.textview2);
        expandableListView = v.findViewById(R.id.expanded_menu);
        dBhelp = Action.getdBhelp();
        sqLiteDatabase = dBhelp.getWritableDatabase();

        treeMap.clear();
        arrayList.clear();
        listDataChild.clear();
        listDataParent.clear();
        Cursor cursor = sqLiteDatabase.query(DBhelp.TABLE_NAME3, null, null, null, null, null, null);
        String name;
        String suma;
        String date = "";
        String checked;
        int named;
        int summa;
        int datet;
        int check;
        int i = 0;
        if (cursor.moveToFirst()) {
            named = cursor.getColumnIndex(DBhelp.NAME_COLUMN);
            summa = cursor.getColumnIndex(DBhelp.SUMA_COLUMN);
            datet = cursor.getColumnIndex(DBhelp.DATE_COLUMN);
            check = cursor.getColumnIndex(DBhelp.CHECK_COLUMN);


            do {


                name = cursor.getString(named);
                suma = cursor.getString(summa);
                date = cursor.getString(datet);
                checked = cursor.getString(check);


                date1 = new Date(date);


                treeMap.put(date1, new HistoryClass(name, date, suma, checked));


            }

            while (cursor.moveToNext());
        }

        cursor.close();


        List<HistoryAdapterClass> colors = new ArrayList<HistoryAdapterClass>();
        for (Map.Entry<Date, HistoryClass> s : treeMap.entrySet()) {

            if (i == 0) {
                colors = new ArrayList<>();
                date = formatter.format(s.getKey());
                listDataParent.add(date);
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName()));
                i += 1;
                if (treeMap.size() == 1) {
                    arrayList.add(colors);
                }
                continue;
            }
            if (date.equals(formatter.format(s.getKey()))) {
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName()));

            } else {
                arrayList.add(colors);
                colors = new ArrayList<>();
                date = formatter.format(s.getKey());
                listDataParent.add(date);
                colors.add(new HistoryAdapterClass(s.getValue().getSuma(), s.getValue().getCheck(), s.getValue().getName()));
            }
            if (i == treeMap.size() - 1) {

                arrayList.add(colors);

            }
            i += 1;

        }


        for (int b = 0; b < arrayList.size(); b++) {
            listDataChild.put(listDataParent.get(b), arrayList.get(b));

        }


        ExpandbleListAdapter listAdapter = new ExpandbleListAdapter(listDataChild, getActivity());
        expandableListView.setAdapter(listAdapter);


        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {

        }
    }

}