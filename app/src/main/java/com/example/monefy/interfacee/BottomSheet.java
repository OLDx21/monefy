package com.example.monefy.interfacee;

import android.content.Context;
import android.graphics.Color;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.Calendar;

public interface BottomSheet {
     void setDataList(TextView textView, ExpandableListView expandableListView, Context context, DataChange dataChange);
}
