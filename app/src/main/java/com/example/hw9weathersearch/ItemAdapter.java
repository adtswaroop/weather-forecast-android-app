package com.example.hw9weathersearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater lyInflater;
    String[] dates;
    String[] maxTemp;
    String[] minTemp;
    String[] icons;

    public ItemAdapter(Context c, String[] dt, String[] minT, String[] maxT, String[] iconicons){
        dates = dt;
        minTemp = minT;
        maxTemp = maxT;
        icons = iconicons;
        lyInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dates.length;
    }

    @Override
    public Object getItem(int i) {
        return dates[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View nv = lyInflater.inflate(R.layout.dailydataview, null);
        TextView dateCol = (TextView) nv.findViewById(R.id.dailydate);
        TextView maxTempCol = (TextView) nv.findViewById(R.id.dailymaxtemp);
        TextView minTempCol = (TextView) nv.findViewById(R.id.dailymintemp);
        ImageView iconCol = (ImageView) nv.findViewById(R.id.dailyicon);

        dateCol.setText(dates[i].toString());
        maxTempCol.setText(maxTemp[i].toString());
        minTempCol.setText(minTemp[i].toString());
        String iconCurr = icons[i].toString();
            switch (iconCurr){
                case "clear-day":
                    iconCol.setImageResource(R.drawable.clearday);
                    break;
                case "clear-night":
                    iconCol.setImageResource(R.drawable.clearnight);
                    break;
                case "rain":
                    iconCol.setImageResource(R.drawable.rainy);
                    break;
                case "snow":
                    iconCol.setImageResource(R.drawable.snowy);
                    break;
                case "sleet":
                    iconCol.setImageResource(R.drawable.sleet);
                    break;
                case "wind":
                    iconCol.setImageResource(R.drawable.windy);
                    break;
                case "fog":
                    iconCol.setImageResource(R.drawable.fog);
                    break;
                case "cloudy":
                    iconCol.setImageResource(R.drawable.cloudy);
                    break;
                case "partly-cloudy-day":
                    iconCol.setImageResource(R.drawable.partlycloudyday);
                    break;
                case "partly-cloudy-night":
                    iconCol.setImageResource(R.drawable.partlycloudynight);
                    break;
                default:
                    iconCol.setImageResource(R.drawable.clearday);
                    break;
            }
        return nv;
    }
}
