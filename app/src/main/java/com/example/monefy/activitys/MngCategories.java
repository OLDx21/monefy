package com.example.monefy.activitys;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.monefy.Action;
import com.example.monefy.ExpandbleListAdapter;
import com.example.monefy.ListViewAdapter;
import com.example.monefy.R;

public class MngCategories extends AppCompatActivity {
    ExpandableListView expandableListView;

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
        setContentView(R.layout.categorymng);
        ActionBar actionBar = getSupportActionBar();
        Action.setFontActionBar(actionBar, getApplicationContext(), getResources().getString(R.string.controlcateg));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.barcolor)));
        expandableListView = findViewById(R.id.expandedcategorylist);
        ListViewAdapter listAdapter = new ListViewAdapter(this);
        expandableListView.setAdapter(listAdapter);

    }
}
