package com.example.monefy;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.monefy.ui.Dayfr.Months;
import com.example.monefy.ui.gallery.GalleryFragment;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.monefy.ui.slideshow.SlideshowFragment.format;

public class FacadeMethods {
    private static FacadeMethods facadeMethods = new FacadeMethods();

    public static FacadeMethods getInstance() {
        return facadeMethods;
    }


    public int setDataDays(TreeMap<Date, NamesAndValues> treeMap) {
        DataBase dataBase = DataBase.getInstance();
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<String> names2 = dataBase.getArray(DataBase.COST);
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> stonks2 = dataBase.getArray(DataBase.PROFIT);


        String dates;
        int i = 0;
        for (int in = 0; in < names2.size(); in++) {
            names.put(names2.get(in), 0.0);
        }
        for (int in = 0; in < stonks2.size(); in++) {
            stonks.put(stonks2.get(in), 0.0);
        }

        if (dataBase.getData().isEmpty()) {
            return 0;
        }
        double result = 0;
        String lastdate = dataBase.getData().keySet().iterator().next().toString();
        dates = format.format(dataBase.getData().keySet().iterator().next());
        for (Map.Entry<Date, HistoryClass> s : dataBase.getData().entrySet()) {


            if (dates.equals(format.format(s.getKey()))) {
                if (s.getValue().getCheck().equals("plus")) {
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                    result += Double.parseDouble(s.getValue().getSuma());
                } else {
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                    result -= Double.parseDouble(s.getValue().getSuma());
                }


            } else {
                treeMap.put(new Date(lastdate), new NamesAndValues(names, result, stonks));
                result = 0;
                stonks = new LinkedHashMap<>();
                names = new LinkedHashMap<>();
                lastdate = s.getKey().toString();
                dates = format.format(s.getKey());
                for (int in = 0; in < names2.size(); in++) {
                    names.put(names2.get(in), 0.0);

                }
                for (int in = 0; in < stonks2.size(); in++) {

                    stonks.put(stonks2.get(in), 0.0);
                }

                if (s.getValue().getCheck().equals("plus")) {
                    result += Double.parseDouble(s.getValue().getSuma());
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                } else {
                    result -= Double.parseDouble(s.getValue().getSuma());
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                }
            }
            if (i == dataBase.getData().size() - 1) {

                treeMap.put(new Date(lastdate), new NamesAndValues(names, result, stonks));

            }

            lastdate = s.getKey().toString();
            i += 1;
        }
        return calculateNumberOfDaysBetween(treeMap.keySet().iterator().next(), new Date(lastdate));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDataWeeks(TreeMap<Date, HistoryClass> Data, TreeMap<Date, ArrayList<Date>> MndWeek) {
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<String> names2 = DataBase.getInstance().getArray(DataBase.COST);
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> stonks2 = DataBase.getInstance().getArray(DataBase.PROFIT);

        String dates;
        int i = 0;
        Date date1;
        for (int in = 0; in < names2.size(); in++) {
            names.put(names2.get(in), 0.0);
        }
        for (int in = 0; in < stonks2.size(); in++) {
            stonks.put(stonks2.get(in), 0.0);
        }

        if (Data.isEmpty()) {
            return;
        }
        double result = 0;
        String lastdate = format.format(Data.keySet().iterator().next());
        dates = format.format(Data.keySet().iterator().next());


        for (Map.Entry<Date, HistoryClass> s : Data.entrySet()) {


            if (dates.equals(format.format(s.getKey()))) {
                if (s.getValue().getCheck().equals("plus")) {
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                    result += Double.parseDouble(s.getValue().getSuma());
                } else {
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                    result -= Double.parseDouble(s.getValue().getSuma());
                }


            } else {
                Action.NamesAndValuesForWeeks.put(lastdate, new NamesAndValues(names, result, stonks));
                result = 0;
                stonks = new LinkedHashMap<>();
                names = new LinkedHashMap<>();
                lastdate = format.format(s.getKey());
                dates = format.format(s.getKey());
                for (int in = 0; in < names2.size(); in++) {
                    names.put(names2.get(in), 0.0);
                }
                for (int in = 0; in < stonks2.size(); in++) {
                    stonks.put(stonks2.get(in), 0.0);
                }

                if (s.getValue().getCheck().equals("plus")) {
                    result += Double.parseDouble(s.getValue().getSuma());
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                } else {
                    result -= Double.parseDouble(s.getValue().getSuma());
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                }
            }
            if (i == Data.size() - 1) {

                Action.NamesAndValuesForWeeks.put(lastdate, new NamesAndValues(names, result, stonks));
                lastdate = s.getKey().toString();
                break;

            }

            lastdate = format.format(s.getKey());
            i += 1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(lastdate));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(Data.keySet().iterator().next().toString()));

        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(calendar.getTime());

        LocalDate monday = new LocalDate(calendar.getTime()).withDayOfWeek(DateTimeConstants.MONDAY);

        int weeks = (int) ChronoUnit.WEEKS.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(calendar1.getTimeInMillis()), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(calendar.getTimeInMillis()), ZoneId.systemDefault()));

        ArrayList<Date> arrayList;

        for (int in = 0; in <= weeks + 1; in++) {

            if (in != 0) {
                monday = monday.minusDays(7);

            }
            calendar.setTime(monday.toDate());
            date1 = calendar.getTime();
            arrayList = new ArrayList<>();

            for (int g = 0; g < 7; g++) {
                calendar3.setTime(date1);
                calendar3.add(Calendar.DATE, +g);
                arrayList.add(calendar3.getTime());
            }

            MndWeek.put(date1, arrayList);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int setDataMonth(TreeMap<Date, HistoryClass> Data) {
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        ArrayList<String> names2 = DataBase.getInstance().getArray(DataBase.COST);
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> stonks2 = DataBase.getInstance().getArray(DataBase.PROFIT);

        for (int in = 0; in < names2.size(); in++) {
            names.put(names2.get(in), 0.0);
        }
        for (int in = 0; in < stonks2.size(); in++) {
            stonks.put(stonks2.get(in), 0.0);
        }

        String dates;
        int i = 0;


        if (Data.isEmpty()) {
            return 0;
        }

        double result = 0;
        String lastdate = Data.keySet().iterator().next().toString();
        dates = Months.format.format(Data.keySet().iterator().next());


        for (Map.Entry<Date, HistoryClass> s : Data.entrySet()) {


            if (dates.equals(Months.format.format(s.getKey()))) {
                if (s.getValue().getCheck().equals("plus")) {
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                    result += Double.parseDouble(s.getValue().getSuma());
                } else {
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                    result -= Double.parseDouble(s.getValue().getSuma());
                }


            } else {
                Action.NamesAndValuesForMonth.put(new Date(lastdate), new NamesAndValues(names, result, stonks));
                result = 0;
                stonks = new LinkedHashMap<>();
                names = new LinkedHashMap<>();
                lastdate = s.getKey().toString();
                dates = Months.format.format(s.getKey());
                for (int in = 0; in < names2.size(); in++) {
                    names.put(names2.get(in), 0.0);
                }
                for (int in = 0; in < stonks2.size(); in++) {

                    stonks.put(stonks2.get(in), 0.0);
                }

                if (s.getValue().getCheck().equals("plus")) {
                    result += Double.parseDouble(s.getValue().getSuma());
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                } else {
                    result -= Double.parseDouble(s.getValue().getSuma());
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                }
            }

            if (i == Data.size() - 1) {
                Action.NamesAndValuesForMonth.put(new Date(lastdate), new NamesAndValues(names, result, stonks));
                break;
            }

            lastdate = s.getKey().toString();
            i += 1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(lastdate));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(Data.keySet().iterator().next().toString()));

        return (int) ChronoUnit.MONTHS.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis()), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(calendar.getTimeInMillis()), ZoneId.systemDefault()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int setDataYears(TreeMap<Date, HistoryClass> Data) {
        LinkedHashMap<String, Double> names = new LinkedHashMap<>();
        LinkedHashMap<String, Double> stonks = new LinkedHashMap<>();
        ArrayList<String> names2 = DataBase.getInstance().getArray(DataBase.COST);
        ArrayList<String> stonks2 = DataBase.getInstance().getArray(DataBase.PROFIT);

        for (int in = 0; in < names2.size(); in++) {
            names.put(names2.get(in), 0.0);
        }
        for (int in = 0; in < stonks2.size(); in++) {
            stonks.put(stonks2.get(in), 0.0);
        }

        String dates;
        int i = 0;

        if (Data.isEmpty()) {
            return 0;
        }

        double result = 0;
        String lastdate = Data.keySet().iterator().next().toString();
        dates = GalleryFragment.format.format(Data.keySet().iterator().next());


        for (Map.Entry<Date, HistoryClass> s : Data.entrySet()) {


            if (dates.equals(GalleryFragment.format.format(s.getKey()))) {
                if (s.getValue().getCheck().equals("plus")) {
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                    result += Double.parseDouble(s.getValue().getSuma());
                } else {
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                    result -= Double.parseDouble(s.getValue().getSuma());
                }


            } else {
                Action.NamesAndValuesForYears.put(new Date(lastdate), new NamesAndValues(names, result));
                result = 0;
                names = new LinkedHashMap<>();
                lastdate = s.getKey().toString();
                dates = GalleryFragment.format.format(s.getKey());
                for (int in = 0; in < names2.size(); in++) {
                    names.put(names2.get(in), 0.0);
                }
                for (int in = 0; in < stonks2.size(); in++) {
                    stonks.put(stonks2.get(in), 0.0);
                }


                if (s.getValue().getCheck().equals("plus")) {
                    stonks.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + stonks.get(s.getValue().getName()));
                    result += Double.parseDouble(s.getValue().getSuma());

                } else {
                    result -= Double.parseDouble(s.getValue().getSuma());
                    names.put(s.getValue().getName(), Double.parseDouble(s.getValue().getSuma()) + names.get(s.getValue().getName()));
                }
            }
            if (i == Data.size() - 1) {
                Action.NamesAndValuesForYears.put(new Date(lastdate), new NamesAndValues(names, result, stonks));
                break;
            }

            lastdate = s.getKey().toString();
            i += 1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(lastdate));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(Data.keySet().iterator().next().toString()));

        int year = (int) ChronoUnit.YEARS.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis()), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(calendar.getTimeInMillis()), ZoneId.systemDefault()));

        for (int in = 0; in <= year; in++) {

            calendar.setTime(new Date(lastdate));
            calendar.add(Calendar.YEAR, -in);

            if (!containsKey(GalleryFragment.format.format(calendar.getTime()))) {
                Action.NamesAndValuesForYears.put(calendar.getTime(), new NamesAndValues());
            }
        }


        return year;
    }

    boolean containsKey(String date) {
        for (Map.Entry<Date, NamesAndValues> s : Action.NamesAndValuesForYears.entrySet()) {
            if (date.equals(GalleryFragment.format.format(s.getKey()))) {
                return true;
            }
        }
        return false;
    }

    private int calculateNumberOfDaysBetween(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("End date should be grater or equals to start date");
        }

        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long milPerDay = 1000 * 60 * 60 * 24;

        int numOfDays = (int) ((endDateTime - startDateTime) / milPerDay); // calculate vacation duration in days

        return (numOfDays + 1); // add one day to include start date in interval
    }
}
