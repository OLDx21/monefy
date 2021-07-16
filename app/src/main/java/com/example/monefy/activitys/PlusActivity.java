package com.example.monefy.activitys;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.monefy.Action;
import com.example.monefy.bottoms.Bottom2class;
import com.example.monefy.DoIntent;
import com.example.monefy.R;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlusActivity extends AppCompatActivity {
   public static int check;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonpoint, deletebtn, selectedbtm;
    EditText textView;
    Pattern pattern = Pattern.compile(".*\\..*\\..*");
    Matcher matcher;
    Bottom2class bottom2class;
    TextView datetext;
    public static Date date;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)

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
        datetext = findViewById(R.id.date);

        textView.setEnabled(false);
        Button[] buttons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonpoint};

        Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.animka);
        Animation animBeta = AnimationUtils.loadAnimation(this, R.anim.animka2);
        Animation animSeta = AnimationUtils.loadAnimation(this, R.anim.error);

        selectedbtm.setText("ВИБІР КАТЕГОРІЇ");
        datetext.setText(Action.formatter.format(date));

        selectedbtm.setOnClickListener(v -> {
            v.startAnimation(animAlpha);
            if (textView.getText().toString().isEmpty()) {
                textView.startAnimation(animSeta);
                deletebtn.startAnimation(animSeta);
                new Thread(() -> {
                    textView.setBackground(getResources().getDrawable(R.drawable.textview2));

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    textView.setBackground(getResources().getDrawable(R.drawable.customtextview));
                }).start();
                return;
            }
            bottom2class = new Bottom2class(textView.getText().toString(), date, check);
            bottom2class.show(getSupportFragmentManager(), "exampleBottomSheet");
            System.out.println(date);

        });

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
                } catch (StringIndexOutOfBoundsException ignored) {

                }
            }
        });
        deletebtn.setOnClickListener(v -> {
            v.startAnimation(animBeta);
            if (textView.getText().toString().isEmpty()) return;
            textView.setText(textView.getText().toString().substring(0, textView.getText().toString().length() - 1));
        });

        for (int i = 0; i < buttons.length; i++) {


            int finalI = i;
            buttons[i].setOnClickListener(v -> {
                v.startAnimation(animAlpha);
                textView.setText(textView.getText().toString() + buttons[finalI].getText().toString());
            });

        }
    }
}
