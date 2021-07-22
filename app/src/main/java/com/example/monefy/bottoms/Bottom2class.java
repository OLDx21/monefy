package com.example.monefy.bottoms;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.monefy.*;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bottom2class extends BottomSheetDialogFragment {

    private BottomSheet.BottomSheetListener mListener;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
  public  String value;
    DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    Date date;
    public int check;
public Bottom2class(String value, Date date, int check){
    this.value = value;
    this.date = date;
    this.check = check;

}

    Button deposite, zaosh,salary;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.selectedkategorylay, container, false);
       deposite = v.findViewById(R.id.depo);
       zaosh = v.findViewById(R.id.zao);
       salary = v.findViewById(R.id.salary);
        Button[] buttons = {deposite, zaosh, salary};
        sqLiteDatabase = Action.getSqLiteDatabase();

        for(int i = 0; i<buttons.length; i++){
            int finalI = i;
            buttons[i].setOnClickListener(v1 -> {
            contentValues = new ContentValues();

            contentValues.put(DBhelp.DATE_COLUMS, format.format(date));
            contentValues.put(DBhelp.SUMA_COLUMS, value);
            contentValues.put(DBhelp.TAG_COLUMS, buttons[finalI].getText().toString());

            sqLiteDatabase.insert(DBhelp.TABLE_NAME2, null, contentValues);

                contentValues = new ContentValues();

                contentValues.put(DBhelp.CHECK_COLUMN, "plus");
                contentValues.put(DBhelp.NAME_COLUMN, buttons[finalI].getText().toString());
                contentValues.put(DBhelp.DATE_COLUMN, date.toString());
                contentValues.put(DBhelp.SUMA_COLUMN, value);
                sqLiteDatabase.insert(DBhelp.TABLE_NAME3, null, contentValues);


            DataBase dataBase = DataBase.getInstance();
            dataBase.addLine(date,new HistoryClass(buttons[finalI].getText().toString(), date.toString(), value, "plus"));
            dataBase.addKategory(buttons[finalI].getText().toString(), DataBase.PROFIT);

            DoIntent doIntent  = DoIntent.getInstance();
            doIntent.setDoIntent(getContext(), com.example.monefy.activitys.MainActivity.class);
            Intent intent = doIntent.getDoIntent();
            intent.putExtra("fragmentNumber",check); //for example
            startActivity(intent);








            });

        }



        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
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
