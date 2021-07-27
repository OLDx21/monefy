package com.example.monefy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.*;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import com.example.monefy.activitys.CostUpdate;
import com.example.monefy.interfacee.DataChange;

import java.util.*;

public class ExpandbleListAdapter extends BaseExpandableListAdapter {
    public LinkedHashMap<String, List<HistoryAdapterClass>> Child;
    private String[] header;
    Context context;
    public DataChange action;
    int act;
    ExpandableListView expandableListView;
    MenuBuilder menuBuilder;
    MenuInflater inflater;
    MenuPopupHelper optionsMenu;
    @SuppressLint("ResourceType")


    public ExpandbleListAdapter(LinkedHashMap<String, List<HistoryAdapterClass>> mapa, Context context, DataChange dataChange, ExpandableListView expandableListView) {
        this.Child = mapa;
        this.header = mapa.keySet().toArray(new String[0]);
        this.context = context;
        this.action = dataChange;
        this.expandableListView = expandableListView;

    }

    Context wrapper;

    @Override
    public int getGroupCount() {
        return header.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Child.get(header[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Child.get(header[groupPosition]).get(childPosition).getName();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listgroup, parent, false);
        }
        double sum = 0;
        TextView textView = convertView.findViewById(R.id.groupview);

        for (HistoryAdapterClass s : Objects.requireNonNull(Child.get(header[groupPosition]))) {

            if (s.getCheck().equals("plus")) {
                sum += Double.parseDouble(s.getSuma());
            } else {
                sum -= Double.parseDouble(s.getSuma());
            }

        }
        TextView textView1 = convertView.findViewById(R.id.groupview2);
        TextView textView2 = convertView.findViewById(R.id.kolvobtn);

        if (sum < 0) {
            textView1.setTextColor(Color.parseColor("#FF6666"));
        } else {
            textView1.setTextColor(Color.parseColor("#66CC66"));
        }
        textView2.setText(String.valueOf(Child.get(header[groupPosition]).size()));
        textView1.setText(String.valueOf(sum));
        textView.setText(String.valueOf(getGroup(groupPosition)));

        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                act = event.getAction();
                switch (act) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return false;
            }
        });


        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        wrapper = new ContextThemeWrapper(context, R.style.AppTheme);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textviewvery);
        TextView textView1 = convertView.findViewById(R.id.textviewvery2);
        Button button = convertView.findViewById(R.id.checkbtn);
        if (Child.get(header[groupPosition]).get(childPosition).getCheck().equals("plus")) {
            button.setBackground(context.getResources().getDrawable(R.drawable.greenovalcheck));
        } else {
            button.setBackground(context.getResources().getDrawable(R.drawable.redovalcheck));
        }
        textView.setText(String.valueOf(getChild(groupPosition, childPosition)));
        textView1.setText(Child.get(header[groupPosition]).get(childPosition).getSuma());


        convertView.setOnClickListener((v) -> {

            CostUpdate.historyAdapterClass = Child.get(header[groupPosition]).get(childPosition);
            CostUpdate.checked = 0;

            DoIntent doIntent = DoIntent.getInstance();
            doIntent.setDoIntent(context, CostUpdate.class);
            Intent intent = doIntent.getDoIntent();
            context.startActivity(intent);
        });
        menuBuilder = new MenuBuilder(wrapper);
        inflater = new MenuInflater(wrapper);
        inflater.inflate(R.menu.popup, menuBuilder);
        convertView.setOnLongClickListener(v -> {
             optionsMenu = new MenuPopupHelper(wrapper, menuBuilder, v);
            optionsMenu.setForceShowIcon(true);

// Set Item Click Listener
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.opt1:
                            Action.getSqLiteDatabase().delete(DBhelp.TABLE_NAME3, "date2 = ?", new String[]{Child.get(header[groupPosition]).get(childPosition).getRealDate().toString()});
                            DataBase.getInstance().DeleteLine(Child.get(header[groupPosition]).get(childPosition).getRealDate());
                            action.Update(Child.get(header[groupPosition]).get(childPosition));
                            Child.get(header[groupPosition]).remove(childPosition);

                            if (Child.get(header[groupPosition]).size() == 0) {
                                Child.remove(header[groupPosition]);
                                header = Child.keySet().toArray(new String[0]);
                            }
                            notifyDataSetChanged();

                            return false;

                        default:
                            return true;
                    }
                }

                @Override
                public void onMenuModeChange(MenuBuilder menu) {

                }
            });

            optionsMenu.show();

            return false;
        });

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                act = event.getAction();
                switch (act) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        expandableListView.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow NestedScrollView to intercept touch events.
                        expandableListView.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return false;
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}