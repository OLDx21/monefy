package com.example.monefy.activitys;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.monefy.Action;
import com.example.monefy.DataBase;
import com.example.monefy.R;
import com.example.monefy.ui.Dayfr.SelectedDay;
import com.example.monefy.ui.Dayfr.Weeks;
import com.example.monefy.ui.intervdays.IntervalDays;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import static com.example.monefy.DBhelp.*;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    MenuItem[] items;
    private static long back_pressed;

    @Override
    public void onBackPressed() {

        if (Action.bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            Action.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            Action.bottomSheetBehavior.setDraggable(true);
            return;
        }
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            System.exit(1);
        } else {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.clickagain), Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();


    }

    @SuppressLint({"NewApi", "WrongConstant"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Action.CheckLang) {
            SharedPreferences sharedPreferences = getSharedPreferences("AppSettingPrefs", 0);
            if (sharedPreferences.getBoolean("NightMode", false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            Locale locale = new Locale(sharedPreferences.getString("Languages", "en"));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            Action.formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
            Action.formatter3 = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
            Action.format = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
            IntervalDays.formatter2 = new SimpleDateFormat("dd MMMM (yyyy)", Locale.getDefault());
            Weeks.MyAdapter.formatter2 = new SimpleDateFormat("dd MMMM (yyyy)", Locale.getDefault());
            Action.CheckLang = false;
        }


        Action.Createdb(this);
        sqLiteDatabase = Action.getSqLiteDatabase();
        setContentView(R.layout.activity_main);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.barcolor)));
        Action.setFontActionBar(getSupportActionBar(), getApplicationContext(), getResources().getString(R.string.app_name));


        sqLiteDatabase = Action.getSqLiteDatabase();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.dayselected, R.id.intervaldays, R.id.motnhselected, R.id.weekselected, R.id.settings, R.id.stat)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        @SuppressLint("RestrictedApi") Menu menu = navigationView.getMenu();
        MenuItem item;
        SpannableString s;
        for (int i = 0; i < menu.size(); i++) {
            if (i != 6 && i != 8) {
                item = menu.getItem(i);

                s = new SpannableString(menu.getItem(i).getTitle());
                s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
                item.setTitle(s);
            }
        }
        items = new MenuItem[7];
        items[0] = navigationView.getMenu().getItem(0);
        items[1] = navigationView.getMenu().getItem(1);
        items[2] = navigationView.getMenu().getItem(2);
        items[3] = navigationView.getMenu().getItem(3);
        items[4] = navigationView.getMenu().getItem(4);
        items[5] = navigationView.getMenu().getItem(7);
        items[6] = navigationView.getMenu().getItem(8);

        setListener();

        navigationView.getMenu().getItem(6).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for(int i = 0; i<items.length; i++){
                    items[i].setEnabled(true);
                }
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.dialogaddtovar, null);
                @SuppressLint("ResourceType") AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);


                mDialogBuilder.setView(promptsView);


                final DatePicker datePicker = promptsView.findViewById(R.id.datapicker);


                mDialogBuilder
                        .setCancelable(false)

                        .setNegativeButton(getResources().getString(R.string.skas),
                                (dialog, id) -> {

                                    dialog.cancel();
                                });


                AlertDialog alertDialog = mDialogBuilder.create();
                datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    SelectedDay.calendar = calendar;
                    SelectedDay.date = calendar.getTime();

                    drawer.closeDrawer(GravityCompat.START);
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.dayselected);
                    item.setChecked(true);
                    alertDialog.cancel();

                });
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));
                return true;
            }
        });
        navigationView.getMenu().getItem(5).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for(int i = 0; i<items.length; i++){
                    items[i].setEnabled(true);
                }
                LayoutInflater li2 = LayoutInflater.from(MainActivity.this);
                View promptsView2 = li2.inflate(R.layout.datesdia, null);
                AlertDialog.Builder mDialogBuilder2 = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);


                mDialogBuilder2.setView(promptsView2);

                final Spinner spinner = promptsView2.findViewById(R.id.spinner);
                final MaterialCalendarView datePicker2 = promptsView2.findViewById(R.id.calendarView);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (spinner.getSelectedItemPosition()) {
                            case 0:
                                datePicker2.setSelectionMode(3);
                                break;
                            case 1:
                                datePicker2.setSelectionMode(2);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                mDialogBuilder2
                        .setCancelable(false)

                        .setNegativeButton(getResources().getString(R.string.skas),
                                (dialog, id) -> {

                                    dialog.cancel();
                                }
                        )

                        .setPositiveButton(getResources().getString(R.string.okay),
                                (dialog, id) -> {

                                    if (datePicker2.getSelectedDates().isEmpty()) {
                                        return;
                                    }
                                    TreeMap<Date, Date> dateTreeMap = new TreeMap<>();
                                    Calendar calendar = Calendar.getInstance();
                                    CalendarDay calendarDay;
                                    for (int i = 0; i < datePicker2.getSelectedDates().size(); i++) {
                                        calendarDay = datePicker2.getSelectedDates().get(i);
                                        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay());
                                        dateTreeMap.put(calendar.getTime(), calendar.getTime());
                                    }
                                    IntervalDays.dateTreeMap = dateTreeMap;
                                    drawer.closeDrawer(GravityCompat.START);
                                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.intervaldays);
                                    item.setChecked(true);
                                    dialog.cancel();
                                }
                        );
                AlertDialog alertDialog2 = mDialogBuilder2.create();
                alertDialog2.show();
                alertDialog2.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1565C0"));
                alertDialog2.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));
                return true;
            }
        });

        if (getIntent().getIntExtra("fragmentNumber", 0) == 1) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.dayselected);
            navigationView.getMenu().getItem(5).setChecked(true);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 3) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_slideshow);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 4) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.weekselected);
            navigationView.getMenu().getItem(1).setChecked(true);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 5) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_gallery);
            navigationView.getMenu().getItem(3).setChecked(true);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 6) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.motnhselected);
            navigationView.getMenu().getItem(2).setChecked(true);
        }
        if (getIntent().getIntExtra("fragmentNumber", 0) == 7) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.intervaldays);
            navigationView.getMenu().getItem(6).setChecked(true);
        }


    }

    public void setListener() {

        for (int i = 0; i < items.length; i++) {
            items[i].setOnMenuItemClickListener(item -> {
                Action.position = null;
                for (int g = 0; g < items.length; g++) {
                    items[g].setEnabled(true);
                }
                item.setEnabled(false);
                return false;
            });
        }
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
        for (int i = 0; i < menu.size(); i++) {
            item = menu.getItem(i);
            s = new SpannableString(menu.getItem(i).getTitle());
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textcolor)), 0, s.length(), 0);
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


    public AlertDialog createAlertDialog(String TableName, String RowName, String array) {
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
                            new Thread(() -> {
                                sqLiteDatabase = Action.getSqLiteDatabase();
                                contentValues = new ContentValues();
                                contentValues.put(RowName, textInputLayout.getEditText().getText().toString());
                                sqLiteDatabase.insert(TableName, null, contentValues);
                                DataBase.getInstance().addIntoAllKategory(textInputLayout.getEditText().getText().toString(), array);
                                DataBase.getInstance().addKategory(textInputLayout.getEditText().getText().toString(), array);
                            }).start();
                            Snackbar.make(mAppBarConfiguration.getDrawerLayout(), getResources().getString(R.string.greatjob), Snackbar.LENGTH_LONG).show();
                            dialog.cancel();

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