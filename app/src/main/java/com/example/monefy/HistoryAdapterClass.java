package com.example.monefy;

import java.util.Date;

public class HistoryAdapterClass {
   private String suma;
   private String check;
   private String name;
   private Date realDate;

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
    }

    public Date getRealDate() {
        return realDate;
    }

    public void setRealDate(Date realDate) {
        this.realDate = realDate;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HistoryAdapterClass(String suma, String check, String name) {
        this.suma = suma;
        this.check = check;
        this.name = name;
    }
    public HistoryAdapterClass(String suma, String check, String name, Date realDate) {
        this.suma = suma;
        this.check = check;
        this.name = name;
        this.realDate = realDate;
    }
}
