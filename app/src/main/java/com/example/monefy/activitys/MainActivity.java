package com.example.monefy.activitys;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import com.example.monefy.*;
import com.example.monefy.ui.Dayfr.SelectedDay;
import com.example.monefy.ui.home.HomeFragment;
import com.example.monefy.ui.intervdays.IntervalDays;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import static com.example.monefy.DBhelp.*;

public class MainActivity extends AppCompatActivity {
    Button btnall, btnyear, btnday, allday, allweeks, month, interval;
    private AppBarConfiguration mAppBarConfiguration;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onLongPress(MotionEvent e) {
            showPopupMenu();
        }
    });

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Натисніть ще раз для виходу", Toast.LENGTH_LONG).show();
    }

    @SuppressLint({"NewApi", "WrongConstant"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66CC66")));
        sqLiteDatabase = Action.getSqLiteDatabase();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.dayselected, R.id.intervaldays, R.id.motnhselected, R.id.weekselected)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (getIntent().getIntExtra("fragmentNumber", 0) == 1) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.dayselected);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 3) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_slideshow);
        }
        btnall = findViewById(R.id.all);
        btnday = findViewById(R.id.day);
        btnyear = findViewById(R.id.year);
        allday = findViewById(R.id.allday);
        allweeks = findViewById(R.id.week);
        month = findViewById(R.id.month);
        interval = findViewById(R.id.interval);

        setlistener(R.id.nav_home, drawer, btnall);
        setlistener(R.id.nav_gallery, drawer, btnyear);
        setlistener(R.id.nav_slideshow, drawer, allday);
        setlistener(R.id.weekselected, drawer, allweeks);
        setlistener(R.id.motnhselected, drawer, month);


        interval.setOnClickListener(v -> {
            if (!Action.alertDialog.containsKey(2)) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.datesdia, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);


                mDialogBuilder.setView(promptsView);

                final Spinner spinner = promptsView.findViewById(R.id.spinner);
                final MaterialCalendarView datePicker = promptsView.findViewById(R.id.calendarView);

                ArrayAdapter<?> adapter =
                        ArrayAdapter.createFromResource(this, R.array.mode, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

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
                                (dialog, id) -> dialog.cancel())

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
                                        System.out.println(calendar.getTime());

                                    }
                                    IntervalDays.dateTreeMap = dateTreeMap;
                                    drawer.closeDrawer(GravityCompat.START);
                                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.intervaldays);
                                    dialog.cancel();
                                }
                        );
                AlertDialog alertDialog = mDialogBuilder.create();

                alertDialog.show();

                Action.alertDialog.put(2, alertDialog);
            } else {

                Action.alertDialog.get(2).show();

            }

        });


        btnday.setOnClickListener(v -> {

            if (!Action.alertDialog.containsKey(1)) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.dialogaddtovar, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);


                mDialogBuilder.setView(promptsView);


                final DatePicker datePicker = promptsView.findViewById(R.id.datapicker);


                mDialogBuilder
                        .setCancelable(false)

                        .setNegativeButton(getResources().getString(R.string.skas),
                                (dialog, id) -> dialog.cancel());


                AlertDialog alertDialog = mDialogBuilder.create();
                datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    SelectedDay.date = calendar.getTime();
                    drawer.closeDrawer(GravityCompat.START);
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.dayselected);
                    alertDialog.hide();

                });
                alertDialog.show();
                Action.alertDialog.put(1, alertDialog);
            } else {
                Action.alertDialog.get(1).show();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.dialogaddkomp, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);


                mDialogBuilder.setView(promptsView);


                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);


                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (userInput.getText().toString().isEmpty()) {
                                            Toast.makeText(MainActivity.this, "даун", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        sqLiteDatabase = Action.getSqLiteDatabase();
                                        contentValues = new ContentValues();
                                        contentValues.put(DBhelp.NAMES_COLUMS, userInput.getText().toString());
                                        contentValues.put(DBhelp.VALUES_COLUMNS, "0");

                                        sqLiteDatabase.insert(TABLE_NAME1, null, contentValues);
                                        Toast.makeText(MainActivity.this, "Успішно додано!", Toast.LENGTH_LONG).show();


                                        DoIntent doIntent = DoIntent.getInstance();
                                        doIntent.setDoIntent(MainActivity.this, MainActivity.class);
                                        Intent intent1 = doIntent.getDoIntent();
                                        startActivity(intent1);

                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                AlertDialog alertDialog = mDialogBuilder.create();

                alertDialog.show();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showPopupMenu() {
        PopupMenu popupMenu = HomeFragment.popupMenu;


        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

    void setlistener(int id, DrawerLayout drawer, Button button) {
        button.setOnClickListener(v -> {
            Action.position = null;
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(id);
            drawer.closeDrawer(GravityCompat.START);

        });


    }
}