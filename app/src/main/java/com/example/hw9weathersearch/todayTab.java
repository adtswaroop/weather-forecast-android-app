package com.example.hw9weathersearch;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class todayTab extends Fragment {

    TextView tv_windspeed , tv_pressure, tv_precipitation, tv_temperature, tv_humidity, tv_visibility, tv_cloudcover, tv_ozone, tv_summaryText;
    ImageView summaryIcon;

    View root;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        detailactivity activity = (detailactivity) getActivity();
        root = inflater.inflate(R.layout.today_tab, container, false);
        tv_windspeed = (TextView) root.findViewById(R.id.windSpeedVal);
        tv_windspeed.setText(activity.getTodayWindSpeed() + " mph");

        tv_pressure = (TextView) root.findViewById(R.id.pressureVal);
        tv_pressure.setText(activity.getTodayPressure() + " mb");

        tv_precipitation = (TextView) root.findViewById(R.id.precipitationVal);
        tv_precipitation.setText(activity.getTodayPrecipitation() + " mmph");

        tv_temperature = (TextView) root.findViewById(R.id.temperatureVal);
        tv_temperature.setText(activity.getTodayTemperature() + (char)0x00B0 + "F");

        tv_humidity = (TextView) root.findViewById(R.id.humidityVal);
        tv_humidity.setText(activity.getTodayHumidity() + "%");

        tv_visibility = (TextView) root.findViewById(R.id.visibilityVal);
        tv_visibility.setText(activity.getTodayVisibility() + " km");

        tv_cloudcover = (TextView) root.findViewById(R.id.cloudcoverVal);
        tv_cloudcover.setText(activity.getTodayCloudcover() + "%");

        tv_ozone = (TextView) root.findViewById(R.id.ozoneVal);
        tv_ozone.setText(activity.getTodayOzone() + " DU");

        summaryIcon = (ImageView) root.findViewById(R.id.summaryIcon);
        String icon = activity.getTodayIcon();
        String summaryText;
        switch (icon){
            case "clear-day":
                summaryIcon.setImageResource(R.drawable.clearday);
                summaryText = "clear day";
                break;
            case "clear-night":
                summaryIcon.setImageResource(R.drawable.clearnight);
                summaryText = "clear night";
                break;
            case "rain":
                summaryIcon.setImageResource(R.drawable.rainy);
                summaryText = "rain";
                break;
            case "snow":
                summaryIcon.setImageResource(R.drawable.snowy);
                summaryText = "snow";
                break;
            case "sleet":
                summaryIcon.setImageResource(R.drawable.sleet);
                summaryText = "sleet";
                break;
            case "wind":
                summaryIcon.setImageResource(R.drawable.windy);
                summaryText = "wind";
                break;
            case "fog":
                summaryIcon.setImageResource(R.drawable.fog);
                summaryText = "fog";
                break;
            case "cloudy":
                summaryIcon.setImageResource(R.drawable.cloudy);
                summaryText = "cloudy";
                break;
            case "partly-cloudy-day":
                summaryIcon.setImageResource(R.drawable.partlycloudyday);
                summaryText = "cloudy day";
                break;
            case "partly-cloudy-night":
                summaryIcon.setImageResource(R.drawable.partlycloudynight);
                summaryText = "cloudy night";
                break;
            default:
                summaryIcon.setImageResource(R.drawable.clearday);
                summaryText = "clear day";
                break;
        }
        tv_summaryText = (TextView) root.findViewById(R.id.summaryTxt);
        tv_summaryText.setText(summaryText);
        System.out.println("Today tab is complete...");
        return root;
    }
}
