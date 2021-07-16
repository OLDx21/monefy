package com.example.monefy;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
public class ExpandbleListAdapter extends BaseExpandableListAdapter {
 public LinkedHashMap<String, List<HistoryAdapterClass>> Child;
 private String[] header;
 Context context;
    public ExpandbleListAdapter(LinkedHashMap<String, List<HistoryAdapterClass>> mapa, Context context){
    this.Child = mapa;
    this.header = mapa.keySet().toArray(new String[0]);
    this.context = context;

    }

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
        return groupPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listgroup, parent, false);
        }
        double sum = 0;
        TextView textView = convertView.findViewById(R.id.groupview);

        for(HistoryAdapterClass s : Child.get(header[groupPosition])){

            if(s.getCheck().equals("plus")){
                sum+=Double.parseDouble(s.getSuma());
            }
            else {
                sum-=Double.parseDouble(s.getSuma());
            }

        }
        TextView textView1 = convertView.findViewById(R.id.groupview2);
        TextView textView2 = convertView.findViewById(R.id.kolvobtn);

        if(sum<0){textView1.setTextColor(Color.RED);}
        else {textView1.setTextColor(Color.GREEN);}
        textView2.setText(String.valueOf(Child.get(header[groupPosition]).size()));
        textView1.setText(String.valueOf(sum));
        textView.setText(String.valueOf(getGroup(groupPosition)));


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textviewvery);
        TextView textView1 = convertView.findViewById(R.id.textviewvery2);
        Button button = convertView.findViewById(R.id.checkbtn);
        if(Child.get(header[groupPosition]).get(childPosition).getCheck().equals("plus")){
            button.setBackground(context.getResources().getDrawable(R.drawable.greenoval));
        }else {
            button.setBackground(context.getResources().getDrawable(R.drawable.redoval));
        }
        textView.setText(String.valueOf(getChild(groupPosition, childPosition)));
        textView1.setText(Child.get(header[groupPosition]).get(childPosition).getSuma());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}