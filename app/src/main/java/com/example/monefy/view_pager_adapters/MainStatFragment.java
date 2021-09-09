package com.example.monefy.view_pager_adapters;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.monefy.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainStatFragment extends Fragment {
    List<Entry> lineEntries;
    ArrayList<String> xLabel;
    int position;

    MainStatFragment(List<Entry> lineEntries, ArrayList<String> xLabel, int position) {
        this.lineEntries = lineEntries;
        this.xLabel = xLabel;
        this.position = position;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.statistfragment, container, false);
        LineChart lineChart = root.findViewById(R.id.linechart);
        lineChart.getDescription().setText("");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setTextColor(getActivity().getResources().getColor(R.color.textcolor));
        xAxis.setDrawGridLines(false);
        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.textcolor));
        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.textcolor));
        drawLineChart(lineChart);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value<0){
                    value = 0;
                }

                return xLabel.get((int)value + (12*position));
            }
        });



        return root;
    }
    private void drawLineChart(LineChart lineChart) {
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.itemcolorback));
        lineDataSet.setCircleColor(Color.YELLOW);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(getActivity().getResources().getColor(R.color.textcolor));

        lineChart.getDescription().setText("");
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());


    }
}
