package com.example.monefy;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class NamesAndValues {

    private LinkedHashMap<String, Double> names;
    private LinkedHashMap<String, Double> stonks;
    double result;
    public NamesAndValues() {


    }
    public NamesAndValues(LinkedHashMap<String, Double> names, double result) {
        this.names = names;
        this.result = result;

    }
    public NamesAndValues(LinkedHashMap<String, Double> names, double result,LinkedHashMap<String, Double> stonks) {
        this.names = names;
        this.result = result;
        this.stonks = stonks;

    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public LinkedHashMap<String, Double> getNames() {
        return names;
    }

    public void setNames(LinkedHashMap<String, Double> names) {
        this.names = names;
    }

    public LinkedHashMap<String, Double> getStonks() {
        return stonks;
    }

    public void setStonks(LinkedHashMap<String, Double> stonks) {
        this.names = stonks;
    }


}
