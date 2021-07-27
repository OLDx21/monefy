package com.example.monefy.activitys;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.example.monefy.*;
import com.example.monefy.bottoms.BottomSheet;
import com.example.monefy.ui.Dayfr.SelectedDay;
import com.example.monefy.ui.Dayfr.Weeks;
import com.example.monefy.ui.home.HomeFragment;
import com.example.monefy.ui.intervdays.IntervalDays;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import static android.content.res.Resources.getSystem;
import static com.example.monefy.DBhelp.*;

public class MainActivity extends AppCompatActivity {
    Button btnall, btnyear, btnday, allday, allweeks, month, interval, settings;

    private AppBarConfiguration mAppBarConfiguration;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;



    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Натисніть ще раз для виходу", Toast.LENGTH_LONG).show();
        if (Action.bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            Action.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            Action.bottomSheetBehavior.setDraggable(true);
        }

    }


    @SuppressLint({"NewApi", "WrongConstant"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Action.CheckLang) {
            read();
            Action.formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
            Action.formatter3 = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
            Action.format = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
            IntervalDays.formatter2 = new SimpleDateFormat("dd MMMM (yyyy)", Locale.getDefault());
            Weeks.MyAdapter.formatter2 = new SimpleDateFormat("dd MMMM", Locale.getDefault());
            Action.CheckLang = false;
        }

        Action.Createdb(this);
        sqLiteDatabase = Action.getSqLiteDatabase();
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#666666")));

        Action.setFontActionBar(getSupportActionBar(), getApplicationContext(), getResources().getString(R.string.app_name));


        sqLiteDatabase = Action.getSqLiteDatabase();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.dayselected, R.id.intervaldays, R.id.motnhselected, R.id.weekselected, R.id.settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        btnall = findViewById(R.id.all);
        btnday = findViewById(R.id.day);
        btnyear = findViewById(R.id.year);
        allday = findViewById(R.id.allday);
        allweeks = findViewById(R.id.week);
        month = findViewById(R.id.month);
        interval = findViewById(R.id.interval);
        settings = findViewById(R.id.settings);
        Button[] buttons = {btnall, btnday, btnyear, allday, allweeks, month, interval, settings};


        setlistener(R.id.settings, drawer, settings, buttons);
        setlistener(R.id.nav_home, drawer, btnall, buttons);
        setlistener(R.id.nav_gallery, drawer, btnyear, buttons);
        setlistener(R.id.nav_slideshow, drawer, allday, buttons);
        setlistener(R.id.weekselected, drawer, allweeks, buttons);
        setlistener(R.id.motnhselected, drawer, month, buttons);
        btnall.setClickable(false);
        if (getIntent().getIntExtra("fragmentNumber", 0) == 1) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.dayselected);
            ControlButtons(btnday, buttons);

        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 3) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_slideshow);
            ControlButtons(allday, buttons);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 4) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.weekselected);
            ControlButtons(allweeks, buttons);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 5) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_gallery);
            ControlButtons(btnyear, buttons);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 6) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.motnhselected);
            ControlButtons(month, buttons);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 7) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.intervaldays);
            ControlButtons(interval, buttons);

        }
        btnday.setClickable(true);
        interval.setClickable(true);
        interval.setOnClickListener(v -> {
            ControlButtons(interval, buttons);
            interval.setClickable(true);

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.datesdia, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);


                mDialogBuilder.setView(promptsView);

                final Spinner spinner = promptsView.findViewById(R.id.spinner);
                final MaterialCalendarView datePicker = promptsView.findViewById(R.id.calendarView);



                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (spinner.getSelectedItemPosition()) {
                            case 0:
                                datePicker.setSelectionMode(3);
                                break;
                            case 1:
                                datePicker.setSelectionMode(2);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                mDialogBuilder
                        .setCancelable(false)

                        .setNegativeButton(getResources().getString(R.string.skas),
                                (dialog, id) -> {
                                    interval.setClickable(true);
                                    dialog.cancel();
                                }

                        )

                        .setPositiveButton(getResources().getString(R.string.okay),
                                (dialog, id) -> {

                                    if (datePicker.getSelectedDates().isEmpty()) {
                                        return;
                                    }
                                    TreeMap<Date, Date> dateTreeMap = new TreeMap<>();
                                    Calendar calendar = Calendar.getInstance();
                                    CalendarDay calendarDay;
                                    for (int i = 0; i < datePicker.getSelectedDates().size(); i++) {
                                        calendarDay = datePicker.getSelectedDates().get(i);
                                        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay());
                                        dateTreeMap.put(calendar.getTime(), calendar.getTime());


                                    }
                                    IntervalDays.dateTreeMap = dateTreeMap;
                                    drawer.closeDrawer(GravityCompat.START);
                                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.intervaldays);
                                    interval.setClickable(true);
                                    dialog.cancel();
                                }
                        );
                AlertDialog alertDialog = mDialogBuilder.create();

                alertDialog.show();
                alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1565C0"));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));


        });


        btnday.setOnClickListener(v -> {
            ControlButtons(btnday, buttons);
            btnday.setClickable(true);

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.dialogaddtovar, null);
                @SuppressLint("ResourceType") AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);


                mDialogBuilder.setView(promptsView);


                final DatePicker datePicker = promptsView.findViewById(R.id.datapicker);


                mDialogBuilder
                        .setCancelable(false)

                        .setNegativeButton(getResources().getString(R.string.skas),
                                (dialog, id) -> {
                                    btnday.setClickable(true);
                                    dialog.cancel();
                                });


                AlertDialog alertDialog = mDialogBuilder.create();
                datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    SelectedDay.calendar = calendar;
                    SelectedDay.date = calendar.getTime();
                    System.out.println(calendar.getTime());
                    drawer.closeDrawer(GravityCompat.START);
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.dayselected);
                    btnday.setClickable(true);
                    alertDialog.cancel();

                });

                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));

        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                createAlertDialog(TABLE_NAME1, NAMES_COLUMS, DataBase.COST);
                break;
            case R.id.addkategoryprof:
                createAlertDialog(TABLE_NAME2, NAMES_COLUMS2, DataBase.PROFIT);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item;
        SpannableString s;
        for(int i = 0; i<menu.size(); i++){
            item = menu.getItem(i);
            s = new SpannableString(menu.getItem(i).getTitle());
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
            item.setTitle(s);

        }




        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    void setlistener(int id, DrawerLayout drawer, Button button, Button[] buttons) {
        button.setOnClickListener(v -> {
            Action.position = null;
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(id);
            drawer.closeDrawer(GravityCompat.START);
            ControlButtons(button, buttons);
        });

    }

    public void ControlButtons(Button button, Button[] buttons) {

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(getResources().getDrawable(R.drawable.calccustom));
            buttons[i].setClickable(true);
        }
        if(button.getId()!=btnday.getId()) {
            button.setClickable(false);
        }
        button.setBackground(getResources().getDrawable(R.drawable.calccustomvtwo));

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void read() {
        String output = "";

        try {
            FileInputStream fileinput = this.openFileInput("saveinfo");
            InputStreamReader reader = new InputStreamReader(fileinput);

            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            output = stringBuffer.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (output.isEmpty()) {
            return;
        }

        Locale locale = new Locale(output.trim());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    public AlertDialog createAlertDialog(String TableName, String RowName, String array){
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.dialogaddkomp, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);

        mDialogBuilder.setView(promptsView);


        TextInputLayout textInputLayout = promptsView.findViewById(R.id.nametext);


        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.okay),
                        (dialog, id) -> {
                            if (textInputLayout.getEditText().getText().toString().isEmpty()) {
                                return;
                            }
                            if (DataBase.getInstance().getAllKategories().contains(textInputLayout.getEditText().getText().toString())) {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.havektg), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (DataBase.getInstance().getAllKategoriesProfit().contains(textInputLayout.getEditText().getText().toString())) {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.havektg), Toast.LENGTH_LONG).show();
                                return;
                            }

                            sqLiteDatabase = Action.getSqLiteDatabase();
                            contentValues = new ContentValues();
                            contentValues.put(RowName, textInputLayout.getEditText().getText().toString());


                            sqLiteDatabase.insert(TableName, null, contentValues);
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.greatjob), Toast.LENGTH_LONG).show();
                            DataBase.getInstance().addIntoAllKategory(textInputLayout.getEditText().getText().toString(), array);

                            DataBase.getInstance().addKategory(textInputLayout.getEditText().getText().toString(), array);

                            DoIntent doIntent = DoIntent.getInstance();
                            doIntent.setDoIntent(MainActivity.this, MainActivity.class);
                            Intent intent1 = doIntent.getDoIntent();
                            intent1.putExtra("fragmentNumber", Action.checked); //for example
                            startActivity(intent1);

                        })
                .setNegativeButton(getResources().getString(R.string.skas),
                        (dialog, id) -> dialog.cancel());


        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.customdialog));

        alertDialog.show();

        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1565C0"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));
        return alertDialog;
    }


}