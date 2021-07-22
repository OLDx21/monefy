package com.example.monefy.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.monefy.*;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CostMinus extends AppCompatActivity {
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonpoint, deletebtn, selectedbtm;
    EditText textView;
    Pattern pattern = Pattern.compile(".*\\..*\\..*");
    public static String kategory;
    Matcher matcher;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    String check;
    TextView datetext;
    public static Date date;
    public static int checked;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minuscost);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.textview2);
        button0 = findViewById(R.id.btn0);
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        button4 = findViewById(R.id.btn4);
        button5 = findViewById(R.id.btn5);
        button6 = findViewById(R.id.btn6);
        button7 = findViewById(R.id.btn7);
        button8 = findViewById(R.id.btn8);
        button9 = findViewById(R.id.btn9);
        buttonpoint = findViewById(R.id.btnpoint);
        deletebtn = findViewById(R.id.delete);
        selectedbtm = findViewById(R.id.selectedbtn);
        selectedbtm.setText(getResources().getString(R.string.add)+" "+kategory);
        datetext = findViewById(R.id.date);
        textView.setEnabled(false);
         Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.animka);
        Animation animBeta = AnimationUtils.loadAnimation(this, R.anim.animka2);
        Button[] buttons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonpoint};
     datetext.setText(Action.formatter.format(date));




        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                matcher = pattern.matcher(textView.getText().toString());
                if (textView.getText().toString().length() == 1 && textView.getText().toString().equals(".") || textView.getText().toString().equals("0")) {
                    textView.setText("");
                    return;
                }

                if (matcher.find()) {

                    textView.setText(textView.getText().toString().substring(0, textView.getText().toString().length() - 1));
                    return;
                }
                try {


                    if (textView.getText().toString().contains(".") && textView.getText().toString().charAt(textView.getText().toString().length() - 4) == '.') {
                        textView.setText(textView.getText().toString().substring(0, textView.getText().toString().length() - 1));
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    return;
                }
            }
        });

        selectedbtm.setOnClickListener(v -> {
            if(textView.getText().toString().isEmpty()) return;
            v.startAnimation(animAlpha);
            check = Action.NamesAndValues.get(kategory);
            sqLiteDatabase = Action.getSqLiteDatabase();
            contentValues = new ContentValues();

            contentValues.put(DBhelp.NAMES_COLUMS, kategory);
            contentValues.put(DBhelp.VALUES_COLUMNS, (Double.parseDouble(Action.NamesAndValues.get(kategory))+Double.parseDouble(textView.getText().toString())));

            sqLiteDatabase.update(DBhelp.TABLE_NAME1, contentValues, "names = ?", new String[]{kategory});


            contentValues = new ContentValues();

            contentValues.put(DBhelp.CHECK_COLUMN, "minus");
            contentValues.put(DBhelp.NAME_COLUMN, kategory);
            contentValues.put(DBhelp.DATE_COLUMN, date.toString());
            contentValues.put(DBhelp.SUMA_COLUMN, textView.getText().toString());
            sqLiteDatabase.insert(DBhelp.TABLE_NAME3, null, contentValues);

            Toast.makeText(CostMinus.this, "Успіх!", Toast.LENGTH_SHORT).show();

            DataBase dataBase = DataBase.getInstance();
            dataBase.addLine(date, new HistoryClass(kategory, date.toString(), textView.getText().toString(), "minus"));
            dataBase.addKategory(kategory, DataBase.COST);

            DoIntent doIntent  = DoIntent.getInstance();
            doIntent.setDoIntent(CostMinus.this, MainActivity.class);
            Intent intent = doIntent.getDoIntent();
            intent.putExtra("fragmentNumber",checked); //for example
            startActivity(intent);


        });

        deletebtn.setOnClickListener(v -> {
            v.startAnimation(animBeta);
            if(textView.getText().toString().isEmpty()) return;
            textView.setText(textView.getText().toString().substring(0, textView.getText().toString().length()-1));
        });
        for(int i = 0; i<buttons.length; i++){


            int finalI = i;
            buttons[i].setOnClickListener(v -> {
                v.startAnimation(animAlpha);

                textView.setText(textView.getText().toString()+buttons[finalI].getText().toString());
            });

        }


    }

}
