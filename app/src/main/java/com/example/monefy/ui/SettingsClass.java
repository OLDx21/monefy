package com.example.monefy.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.monefy.*;
import com.example.monefy.activitys.MainActivity;
import org.jetbrains.annotations.NotNull;
import androidx.appcompat.app.AlertDialog;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsClass extends Fragment {

    Spinner spinner;
    Button deleteall;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragmentsettings, container, false);
        spinner = root.findViewById(R.id.spinnerlang);
        deleteall = root.findViewById(R.id.deleteallbtn);
        Animation animbeta = AnimationUtils.loadAnimation(getContext(), R.anim.animka);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    return;
                }
                String lang = "";

                switch (spinner.getSelectedItem().toString()){
                    case "English":
                        lang = "en";
                        break;
                    case "Українська":
                        lang = "uk";
                        break;
                    case "Русский":
                        lang = "ru";
                        break;


                }


                write(lang);

                Action.CheckLang = true;
                DoIntent doIntent  = DoIntent.getInstance();
                doIntent.setDoIntent(getContext(), MainActivity.class);
                Intent intent = doIntent.getDoIntent();
                startActivity(intent);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deleteall.setOnClickListener(v -> {
            v.startAnimation(animbeta);
            AlertDialog alertDialog = CreateDialog();
            alertDialog.show();


        });
        return root;
    }
    public void write(String sb1){
        try {

            FileOutputStream fileOutputStream = getActivity().openFileOutput("saveinfo", Context.MODE_PRIVATE);
            fileOutputStream.write(sb1.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public AlertDialog CreateDialog(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialogdeleteallorno, null);
        @SuppressLint("ResourceType") AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        mDialogBuilder.setView(promptsView);

        mDialogBuilder
                .setCancelable(false)

                .setNegativeButton(getResources().getString(R.string.skas),
                        (dialog, id) -> {

                            dialog.cancel();
                        }).setPositiveButton(getResources().getString(R.string.okay),
                (dialog, id) -> {
                    SQLiteDatabase sqLiteDatabase = Action.getSqLiteDatabase();

                    sqLiteDatabase.execSQL("drop table if exists " + DBhelp.TABLE_NAME1);
                    sqLiteDatabase.execSQL("drop table if exists " + DBhelp.TABLE_NAME2);
                    sqLiteDatabase.execSQL("drop table if exists " + DBhelp.TABLE_NAME3);

                    sqLiteDatabase.execSQL("create table " + DBhelp.TABLE_NAME1 + "(" + DBhelp.NAMES_COLUMS + " text" + ")");
                    sqLiteDatabase.execSQL("create table " + DBhelp.TABLE_NAME2 + "(" + DBhelp.NAMES_COLUMS2 + " text" + ")");
                    sqLiteDatabase.execSQL("create table " + DBhelp.TABLE_NAME3 + "(" + DBhelp.CHECK_COLUMN + " text," + DBhelp.NAME_COLUMN + " text,"+ DBhelp.DATE_COLUMN + " date,"+ DBhelp.SUMA_COLUMN+ " text"+")");
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.transuc), Toast.LENGTH_SHORT).show();

                    DataBase.getInstance().clearAll();

                    DoIntent doIntent = DoIntent.getInstance();
                    doIntent.setDoIntent(getActivity(), MainActivity.class);
                    Intent intent = doIntent.getDoIntent();
                    startActivity(intent);

                });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.customdialog));
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1565C0"));

        return alertDialog;
    }



}
