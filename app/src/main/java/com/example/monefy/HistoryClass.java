package com.example.monefy;

public class HistoryClass {
    private String name;
    private String date;
    private String suma;


    public HistoryClass(String name, String date, String suma, String check) {
        this.name = name;
        this.date = date;
        this.suma = suma;
        this.check = check;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    String check;

}
