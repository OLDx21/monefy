package com.example.monefy.interfacee;

import androidx.fragment.app.FragmentStatePagerAdapter;

public interface ViewPagerHelp {
    public static String PROFIT_MODE = "profit";
    public static String COST_MODE = "cost";
    public static String ALL_MODE = "all";

    public void setData();

    public void changeMode(String mode, boolean notifyChart);

    public FragmentStatePagerAdapter getInstance();
}
