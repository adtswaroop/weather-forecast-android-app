package com.example.hw9weathersearch;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;
import java.util.List;

public class graphTab  extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.graph_tab, container, false);
        detailactivity activity = (detailactivity) getActivity();

        //daily summary card
        ImageView dailySummaryIcon = (ImageView) root.findViewById(R.id.dailySummaryIcon);
        EditText dailySummary = (EditText) root.findViewById(R.id.dailySummary);
        dailySummary.setText(activity.getDailySummary());
        String dailySummIcon = activity.getDailySummaryIcon();
        switch (dailySummIcon){
            case "clear-day":
                dailySummaryIcon.setImageResource(R.drawable.clearday);
                break;
            case "clear-night":
                dailySummaryIcon.setImageResource(R.drawable.clearnight);
                break;
            case "rain":
                dailySummaryIcon.setImageResource(R.drawable.rainy);
                break;
            case "snow":
                dailySummaryIcon.setImageResource(R.drawable.snowy);
                break;
            case "sleet":
                dailySummaryIcon.setImageResource(R.drawable.sleet);
                break;
            case "wind":
                dailySummaryIcon.setImageResource(R.drawable.windy);
                break;
            case "fog":
                dailySummaryIcon.setImageResource(R.drawable.fog);
                break;
            case "cloudy":
                dailySummaryIcon.setImageResource(R.drawable.cloudy);
               break;
            case "partly-cloudy-day":
                dailySummaryIcon.setImageResource(R.drawable.partlycloudyday);
                break;
            case "partly-cloudy-night":
                dailySummaryIcon.setImageResource(R.drawable.partlycloudynight);
                break;
            default:
                dailySummaryIcon.setImageResource(R.drawable.clearday);
                break;
        }


        //line chart
        LineChart lineChart = (LineChart) root.findViewById(R.id.linechart);
        lineChart.getLegend().setTextColor(Color.WHITE);
        lineChart.getLegend().setTextSize(16);

        YAxis y = lineChart.getAxisLeft();
        YAxis x = lineChart.getAxisRight();
        y.setGridColor(ContextCompat.getColor(getContext(), R.color.commonColor));
        y.setTextColor(ContextCompat.getColor(getContext(), R.color.commonColor));
        x.setGridColor(ContextCompat.getColor(getContext(), R.color.commonColor));
        x.setTextColor(ContextCompat.getColor(getContext(), R.color.commonColor));
        //y.setDrawGridLines(false);
        //x.setDrawGridLines(false);

        XAxis xx = lineChart.getXAxis();
        xx.setGranularity(1F);
        xx.setDrawGridLines(false);
        xx.setTextColor(ContextCompat.getColor(getContext(), R.color.commonColor));


        List<Entry> lowTemps = new ArrayList<Entry>();
        List<Entry> highTemps = new ArrayList<Entry>();

        String[] lowTemperatures = activity.getMinTemperatures();
        String[] highTemperatures = activity.getMaxTemperatures();

        for(int i =0; i<8; i++){
            lowTemps.add(new Entry(i, Integer.parseInt(lowTemperatures[i])));
            highTemps.add(new Entry(i, Integer.parseInt(highTemperatures[i])));
        }

        LineDataSet dataSetMin = new LineDataSet(lowTemps, "Minimum Temperature");
        dataSetMin.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet dataSetMax = new LineDataSet(highTemps, "Maximum Temperature");
        dataSetMax.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSetMin.setColors(new int[] {R.color.minTempColor} , getContext());
        dataSetMin.setCircleColor(Color.WHITE);
        dataSetMin.setCircleHoleColor(Color.WHITE);
        dataSetMin.setDrawValues(false);

        dataSetMax.setColors(new int[] {R.color.maxTempColor} , getContext());
        dataSetMax.setCircleColor(Color.WHITE);
        dataSetMax.setCircleHoleColor(Color.WHITE);
        dataSetMax.setDrawValues(false);

        List<ILineDataSet> chartData = new ArrayList<>();
        chartData.add(dataSetMin);
        chartData.add(dataSetMax);
        LineData lineData = new LineData(chartData);
        lineChart.setData(lineData);
        lineChart.invalidate();
        System.out.println("Graphs have been made....");
        return root;
    }
}
