package com.example.hw9weathersearch;

import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.SyncFailedException;
import java.net.URLEncoder;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw9weathersearch.ui.main.SectionsPagerAdapter;

import java.net.URI;

public class detailactivity extends AppCompatActivity {

    //today tab
    String today_windSpeed, today_pressure, today_precipitation, today_temperature, today_humidity, today_visibility, today_cloudCover, today_ozone, today_icon;
    String[] minTemp, maxTemp;

    //graph tab
    String dailySummary, dailySummaryIcon;

    //pictures
    String[] imageLinks = new String[8];

    //common
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailactivity);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.calendartoday);
        tabs.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).setIcon(R.drawable.weeklytrending);
        tabs.getTabAt(2).setIcon(R.drawable.googlephotos);

        tabs.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab.getIcon().clearColorFilter();
                        tab.getIcon().setColorFilter(Color.parseColor("#f7f7f7"), PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        tab.getIcon().clearColorFilter();
                        //tab.getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        //super.onTabReselected(tab);
                    }
                });

        Intent ii = getIntent();
        //location
        location = ii.getStringExtra("location");
        setTitle(location);
        //today
        today_windSpeed = ii.getStringExtra("windSpeed");
        today_pressure = ii.getStringExtra("pressure");
        today_precipitation = ii.getStringExtra("precipitation");
        today_temperature = ii.getStringExtra("temperature");
        today_humidity = ii.getStringExtra("humidity");
        today_visibility = ii.getStringExtra("visibility");
        today_cloudCover = ii.getStringExtra("cloudCover");
        today_ozone = ii.getStringExtra("ozone");
        today_icon = ii.getStringExtra("icon");
        //graph
        minTemp = ii.getStringArrayExtra("minTemps");
        maxTemp = ii.getStringArrayExtra("maxTemps");
        dailySummary = ii.getStringExtra("dailySummary");
        dailySummaryIcon = ii.getStringExtra("dailySummaryIcon");
        //pictures
        imageLinks = ii.getStringArrayExtra("imageURLS");
        System.out.println(imageLinks[1]);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.tweet:
                try {
                    String twitterURL = "https://twitter.com/intent/tweet?text=" + URLEncoder.encode("Check Out " + location + "'s Weather! It is " + today_temperature + (char)0x00B0 + "F! #CSCI571WeatherSearch", "UTF-8");
                    if (item.getItemId() == R.id.tweet) {
                        System.out.println("Tweeting...");
                        Intent ii = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterURL));
                        startActivity(ii);
                    }
                    else
                    {
                        System.out.println("Going back...");
                        Intent ii = new Intent(this, MainActivity.class);
                        startActivity(ii);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }

    }

    public String getTodayWindSpeed(){
        return today_windSpeed;
    }

    public String getTodayPressure(){
        return today_pressure;
    }

    public String getTodayPrecipitation(){
        return today_precipitation;
    }

    public String getTodayTemperature(){
        return today_temperature;
    }

    public String getTodayHumidity(){
        return today_humidity;
    }

    public String getTodayVisibility(){
        return today_visibility;
    }

    public String getTodayCloudcover(){
        return today_cloudCover;
    }

    public String getTodayOzone(){
        return today_ozone;
    }

    public String getTodayIcon(){
        return today_icon;
    }

    public String[] getMinTemperatures() { return minTemp; }

    public String[] getMaxTemperatures() { return maxTemp; }

    public String getDailySummary() {return dailySummary;}

    public String getDailySummaryIcon() {return  dailySummaryIcon;}

    public String[] getImageURLS() {return imageLinks; }
}