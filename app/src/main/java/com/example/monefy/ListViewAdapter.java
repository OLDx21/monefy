package com.example.monefy;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    private String[] header = new String[2];
    ArrayList<ArrayList<String>> Child = new ArrayList<>();
    ContentValues contentValues;
    SQLiteDatabase sqLiteDatabase = Action.getSqLiteDatabase();
    public ListViewAdapter(Context context) {
        this.context = context;
        header[0] = context.getResources().getString(R.string.cost);
        header[1] = context.getResources().getString(R.string.stonks);
        Child.add(DataBase.getInstance().getAllKategories());
        Child.add(DataBase.getInstance().getAllKategoriesProfit());

    }


    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Child.get(groupPosition).get(childPosition);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listgroupforcategory, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.namecategory);
        textView.setText(header[groupPosition]);

        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylay, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textviewnamecategory);
        Button edit = convertView.findViewById(R.id.btnedit);
        Button delete = convertView.findViewById(R.id.btnedelete);
        textView.setText(Child.get(groupPosition).get(childPosition));

        edit.setOnClickListener(v -> {
            if (groupPosition == 0) {
                CreateDialog2(DBhelp.TABLE_NAME1, Child.get(groupPosition).get(childPosition), DBhelp.NAMES_COLUMS, this, childPosition, groupPosition);
            } else {
                CreateDialog2(DBhelp.TABLE_NAME2, Child.get(groupPosition).get(childPosition), DBhelp.NAMES_COLUMS2, this, childPosition, groupPosition);
            }
        });
        delete.setOnClickListener(v -> {
            if (groupPosition == 0) {
                CreateDialog(DBhelp.TABLE_NAME1, Child.get(groupPosition).get(childPosition), DBhelp.NAMES_COLUMS, this, childPosition, groupPosition);
            } else {
                CreateDialog(DBhelp.TABLE_NAME2, Child.get(groupPosition).get(childPosition), DBhelp.NAMES_COLUMS2, this, childPosition, groupPosition);
            }

        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlertDialog CreateDialog(String table, String CategoryName, String columnName, BaseExpandableListAdapter expandableListAdapter, int child, int group) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialogdeleteallorno, null);
        @SuppressLint("ResourceType") AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        TextView textView = promptsView.findViewById(R.id.titlemessage);
        textView.setText(context.getResources().getString(R.string.deletecategory));
        TextView textView2 = promptsView.findViewById(R.id.mainmessage);
        textView2.setText(context.getResources().getString(R.string.titledltctg));
        mDialogBuilder.setView(promptsView);

        mDialogBuilder
                .setCancelable(false)

                .setNegativeButton(context.getString(R.string.skas),
                        (dialog, id) -> {

                            dialog.cancel();
                        }).setPositiveButton(context.getString(R.string.okay),
                        (dialog, id) -> {


                            sqLiteDatabase.delete(table, columnName + " = ?", new String[]{CategoryName});
                            sqLiteDatabase.delete(DBhelp.TABLE_NAME3, "name2 = ?", new String[]{CategoryName});

                            DataBase.getInstance().clearAll();
                            DataBase.getInstance().CreateDB(sqLiteDatabase);

                            expandableListAdapter.notifyDataSetChanged();
                            Toast.makeText(context, context.getResources().getString(R.string.transuc), Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.customdialog));
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1565C0"));

        return alertDialog;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlertDialog CreateDialog2(String table, String CategoryName, String columnName, BaseExpandableListAdapter expandableListAdapter, int child, int group) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialogaddkomp, null);
        @SuppressLint("ResourceType") AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        TextInputLayout textInputLayout = promptsView.findViewById(R.id.nametext);
        textInputLayout.setHint(context.getResources().getString(R.string.newname));
        mDialogBuilder.setView(promptsView);

        mDialogBuilder
                .setCancelable(false)

                .setNegativeButton(context.getString(R.string.skas),
                        (dialog, id) -> {

                            dialog.cancel();
                        }).setPositiveButton(context.getString(R.string.okay),
                        (dialog, id) -> {
                            if (textInputLayout.getEditText().getText().toString().isEmpty()) {
                                return;
                            }
                            if (DataBase.getInstance().getAllKategories().contains(textInputLayout.getEditText().getText().toString())) {
                                Toast.makeText(context, context.getResources().getString(R.string.havektg), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (DataBase.getInstance().getAllKategoriesProfit().contains(textInputLayout.getEditText().getText().toString())) {
                                Toast.makeText(context, context.getResources().getString(R.string.havektg), Toast.LENGTH_LONG).show();
                                return;
                            }


                            contentValues = new ContentValues();
                            contentValues.put(columnName, textInputLayout.getEditText().getText().toString());

                            sqLiteDatabase.update(table, contentValues ,columnName + " = ?", new String[]{CategoryName});
                            contentValues = new ContentValues();
                            contentValues.put(DBhelp.NAME_COLUMN, textInputLayout.getEditText().getText().toString());
                            sqLiteDatabase.update(DBhelp.TABLE_NAME3,contentValues,"name2 = ?", new String[]{CategoryName});

                            DataBase.getInstance().clearAll();
                            DataBase.getInstance().CreateDB(sqLiteDatabase);

                            expandableListAdapter.notifyDataSetChanged();
                            Toast.makeText(context, context.getResources().getString(R.string.transuc), Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.customdialog));
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1565C0"));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1565C0"));

        return alertDialog;
    }
}
