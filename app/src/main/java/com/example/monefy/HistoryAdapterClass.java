package com.example.monefy;

public class HistoryAdapterClass {
   private String suma;
   private String check;
   private String name;

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
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
}
