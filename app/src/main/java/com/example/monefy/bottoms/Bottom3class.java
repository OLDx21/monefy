package com.example.monefy.bottoms;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.monefy.Action;
import com.example.monefy.activitys.MainActivity;
import com.example.monefy.DBhelp;
import com.example.monefy.DoIntent;
import com.example.monefy.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Bottom3class extends BottomSheetDialogFragment {
    private BottomSheet.BottomSheetListener mListener;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    public  String value;
    public Date date;
    public int check;

    public Bottom3class(String value, Date date, int check){
        this.value = value;
        this.date = date;
        this.check = check;

    }


    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.selectkategoryminus, container, false);
        @SuppressLint("ResourceType") LinearLayout  layout = v.findViewById(R.id.bottom_sheet);
        sqLiteDatabase = Action.getSqLiteDatabase();
        Animation animBeta = AnimationUtils.loadAnimation(getActivity(), R.anim.animka2);
        GridLayout gridLayout = new GridLayout(getContext());
        gridLayout.setOrientation(0);
        gridLayout.setColumnCount(3);
        ArrayList<Button> buttons = new ArrayList<>();

        for(Map.Entry<String, String> s : Action.NamesAndValues.entrySet()){
            Button button = new Button(getActivity());
            button.setWidth((Action.display.getWidth()/3));
            button.setText(s.getKey());
            button.setBackground(getActivity().getResources().getDrawable(R.drawable.customselectedbtn));
            buttons.add(button);

            gridLayout.addView(button);
        }

        layout.addView(gridLayout);

        for(int i = 0; i<buttons.size(); i++){
            int finalI = i;
            buttons.get(i).setOnClickListener(v1 -> {
                v1.startAnimation(animBeta);
                contentValues = new ContentValues();
                contentValues.put(DBhelp.NAMES_COLUMS, buttons.get(finalI).getText().toString());
                contentValues.put(DBhelp.VALUES_COLUMNS, (Double.parseDouble(Action.NamesAndValues.get(buttons.get(finalI).getText().toString()))+Double.parseDouble(value)));

                sqLiteDatabase.update(DBhelp.TABLE_NAME1, contentValues, "names = ?", new String[]{buttons.get(finalI).getText().toString()});

                contentValues = new ContentValues();

                contentValues.put(DBhelp.CHECK_COLUMN, "minus");
                contentValues.put(DBhelp.NAME_COLUMN, buttons.get(finalI).getText().toString());
                contentValues.put(DBhelp.DATE_COLUMN, date.toString());
                contentValues.put(DBhelp.SUMA_COLUMN, value);
                sqLiteDatabase.insert(DBhelp.TABLE_NAME3, null, contentValues);

                Toast.makeText(getActivity(), "Операція успішна!", Toast.LENGTH_SHORT).show();

                DoIntent doIntent  = DoIntent.getInstance();
                doIntent.setDoIntent(getContext(), MainActivity.class);
                Intent intent = doIntent.getDoIntent();
                intent.putExtra("fragmentNumber",check);
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
