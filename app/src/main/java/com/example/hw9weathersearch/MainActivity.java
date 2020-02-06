package com.example.hw9weathersearch;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.SearchAutoComplete;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;


public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    //declarations
    CardView topcard, bottomcard;
    ListView weeklylistview;
    TextView waitingtext;
    private TextView txt_topCardTemp, txt_topCardLocation , txt_topCardSummary , txt_scndCrdHumidity, txt_scndCrdWindSpeed, txt_scndCrdVisibility, txt_scndCrdPressure;
    private ImageView img_topCrdIcon;
    String lat, lng, topCardTemp, topCardSummary, topCardLocation = "", topCardIcon;

    //weekly table
    private ListView listView;
    String[] dates = new String[8];
    String[] minTemp = new String[8];
    String[] maxTemp = new String[8];
    String[] dailyIcons = new String[8];

    //for the today tab
    String today_windSpeed, today_pressure, today_precipitation, today_temperature, today_humidity, today_visibility, today_cloudCover, today_ozone;
    String today_icon;

    //for the graph tab
    String dailySummary, dailySummaryIcon;

    //for pictures tab
    String[] imgURLS = new String[8];

    //autocomplete
    List<String> autocompleteSuggestions = new ArrayList<String>();

    //common
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    RequestQueue rq;
    String city;
    ProgressBar pb;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rq = Volley.newRequestQueue(getApplicationContext());
        rq.start();

        //progress bar
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        waitingtext = (TextView) findViewById(R.id.waitingText);
        waitingtext.setVisibility(View.VISIBLE);
        topcard = (CardView) findViewById(R.id.topcard);
        topcard.setVisibility(View.INVISIBLE);
        bottomcard = (CardView) findViewById(R.id.bottomcard);
        bottomcard.setVisibility(View.INVISIBLE);
        weeklylistview = (ListView) findViewById(R.id.weeklylistview);
        weeklylistview.setVisibility(View.INVISIBLE);

        //code
        String ipURL = "http://ip-api.com/json";
        final JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, ipURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //pb.setVisibility(View.INVISIBLE);
                    System.out.println("IP Data is here. Parsing...");
                    city = response.getString("city");
                    topCardLocation += response.getString("city") + ", " + response.getString("region") + ", " +response.getString("countryCode");
                    txt_topCardLocation = (TextView) findViewById(R.id.topCardLocation);
                    txt_topCardLocation.setText(topCardLocation);
                    lat = Double.toString(response.getDouble("lat"));
                    lng = Double.toString(response.getDouble("lon"));

                    callDarkSky(lat, lng);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError err){
                        err.printStackTrace();
                    }
                }
        );

        rq.add(jsonReq);
        //pb.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("Searchhhh....");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            final SearchAutoComplete srchAutoComplete = (SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

            @Override
            public boolean onQueryTextSubmit(String s) {
                ComponentName cmn = new ComponentName(getApplicationContext(), SearchResultsActivity.class);
                searchView.setSearchableInfo( searchManager.getSearchableInfo(cmn));
                return false;
            }


            @Override
            public boolean onQueryTextChange(final String s) {
                System.out.println("Called query text change......");
                if(!s.isEmpty())
                {
                    //api call
                    String autocompleteURL = "http://10.0.2.2:8081/autocomplete/" + Uri.encode(s);
                    final JsonObjectRequest autocompleteJSONRequest = new JsonObjectRequest(Request.Method.GET, autocompleteURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                srchAutoComplete.setDropDownBackgroundResource(R.color.white);
                                JSONArray autoresponse = response.getJSONArray("predictions");
                                int forlooptill = Math.min(autoresponse.length(), 5);
                                autocompleteSuggestions.clear();
                                for (int i = 0; i < forlooptill; i++) {
                                    JSONObject ith = autoresponse.getJSONObject(i);
                                    autocompleteSuggestions.add(ith.getString("description"));
                                }

                            ArrayAdapter<String> ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, autocompleteSuggestions);
                            srchAutoComplete.setDropDownHeight(1100);
                                srchAutoComplete.setAdapter(ad);
                            //textView.setAdapter(ad);

                            srchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String str = (String) adapterView.getItemAtPosition(i);
                                    srchAutoComplete.setText("" + str);

                                    }
                                });
                            } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            err.printStackTrace();
                        }
                    });
                    rq.add(autocompleteJSONRequest);
                }
                return true;
        }});

        return true;
    }

    protected void callDarkSky(String lati, String lngi) {

        String darkskyURL = "http://10.0.2.2:8081/getWeather/" + lati + "/" + lngi;
        final JsonObjectRequest darkskyRequest = new JsonObjectRequest(Request.Method.GET, darkskyURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    System.out.println("Darksky data is here. Parsing...");

                    JSONObject currently = response.getJSONObject("currently");
                    JSONObject daily = response.getJSONObject("daily");

                    img_topCrdIcon = (ImageView) findViewById(R.id.topCrdIcon);
                    String icon = currently.getString("icon");
                    txt_topCardTemp = (TextView) findViewById(R.id.topCardTemperature);
                    txt_topCardTemp.setText(Integer.toString((int) currently.getDouble("temperature")) + (char) 0x00B0 + "F");
                    txt_topCardSummary = (TextView) findViewById(R.id.topCardSummary);
                    txt_topCardSummary.setText(currently.getString("summary"));
                    txt_scndCrdHumidity = (TextView) findViewById(R.id.scndCard_humidity);
                    txt_scndCrdHumidity.setText(Integer.toString((int) (currently.getDouble("humidity") * 100)) + "%");
                    txt_scndCrdWindSpeed = (TextView) findViewById(R.id.scndCrd_WindSpeed);
                    txt_scndCrdWindSpeed.setText(String.format("%.2f", currently.getDouble("windSpeed")) + " mph");
                    txt_scndCrdVisibility = (TextView) findViewById(R.id.scndCrd_Visibility);
                    txt_scndCrdVisibility.setText(String.format("%.2f", currently.getDouble("visibility")) + " km");
                    txt_scndCrdPressure = (TextView) findViewById(R.id.scndCrd_Pressure);
                    txt_scndCrdPressure.setText(String.format("%.2f", currently.getDouble("pressure")) + " mb");
                    listView = (ListView) findViewById(R.id.weeklylistview);
                    //top card icon
                    switch (icon) {
                        case "clear-day":
                            img_topCrdIcon.setImageResource(R.drawable.clearday);
                            break;
                        case "clear-night":
                            img_topCrdIcon.setImageResource(R.drawable.clearnight);
                            break;
                        case "rain":
                            img_topCrdIcon.setImageResource(R.drawable.rainy);
                            break;
                        case "snow":
                            img_topCrdIcon.setImageResource(R.drawable.snowy);
                            break;
                        case "sleet":
                            img_topCrdIcon.setImageResource(R.drawable.sleet);
                            break;
                        case "wind":
                            img_topCrdIcon.setImageResource(R.drawable.windy);
                            break;
                        case "fog":
                            img_topCrdIcon.setImageResource(R.drawable.fog);
                            break;
                        case "cloudy":
                            img_topCrdIcon.setImageResource(R.drawable.cloudy);
                            break;
                        case "partly-cloudy-day":
                            img_topCrdIcon.setImageResource(R.drawable.partlycloudyday);
                            break;
                        case "partly-cloudy-night":
                            img_topCrdIcon.setImageResource(R.drawable.partlycloudynight);
                            break;
                        default:
                            img_topCrdIcon.setImageResource(R.drawable.clearday);
                            break;
                    }

                    //weekly table
                    JSONArray dailyDataArray = daily.getJSONArray("data");
                    System.out.println(dailyDataArray);

                    for (int i = 0; i < 8; i++) {
                        JSONObject dailyObjI = dailyDataArray.getJSONObject(i);
                        Long timestamp = dailyObjI.getLong("time");
                        Date dd = new Date(timestamp * 1000);
                        dates[i] = formatter.format(dd);
                        minTemp[i] = Integer.toString((int) dailyObjI.getDouble("temperatureLow"));
                        maxTemp[i] = Integer.toString((int) dailyObjI.getDouble("temperatureHigh"));
                        dailyIcons[i] = dailyObjI.getString("icon");
                    }

                    ItemAdapter itemAdapter = new ItemAdapter(getApplicationContext(), dates, minTemp, maxTemp, dailyIcons);
                    listView.setAdapter(itemAdapter);

                    //today tab data
                    today_windSpeed = String.format("%.2f", currently.getDouble("windSpeed"));
                    today_pressure = String.format("%.2f", currently.getDouble("pressure"));
                    today_precipitation = String.format("%.2f", currently.getDouble("precipIntensity"));
                    today_temperature = String.format("%.2f", currently.getDouble("temperature"));
                    today_humidity = Integer.toString((int) (currently.getDouble("humidity") * 100));
                    today_visibility = String.format("%.2f", currently.getDouble("visibility"));
                    today_cloudCover = Integer.toString((int) (currently.getDouble("cloudCover") * 100));
                    today_ozone = String.format("%.2f", currently.getDouble("ozone"));
                    today_icon = currently.getString("icon");

                    //graph tab data
                    dailySummary = daily.getString("summary");
                    dailySummaryIcon = daily.getString("icon");

                    //picturestab
                    getPictures();
                    pb.setVisibility(View.INVISIBLE);
                    topcard.setVisibility(View.VISIBLE);
                    bottomcard.setVisibility(View.VISIBLE);
                    weeklylistview.setVisibility(View.VISIBLE);
                    waitingtext.setVisibility(View.INVISIBLE);

                    intent = new Intent(getApplicationContext(), detailactivity.class);
                    intent.putExtra("location", topCardLocation);
                    intent.putExtra("windSpeed", today_windSpeed);
                    intent.putExtra("pressure", today_pressure);
                    intent.putExtra("precipitation", today_precipitation);
                    intent.putExtra("temperature", today_temperature);
                    intent.putExtra("humidity", today_humidity);
                    intent.putExtra("visibility", today_visibility);
                    intent.putExtra("cloudCover", today_cloudCover);
                    intent.putExtra("ozone", today_ozone);
                    intent.putExtra("icon", today_icon);
                    intent.putExtra("minTemps", minTemp);
                    intent.putExtra("maxTemps", maxTemp);
                    intent.putExtra("dailySummary", dailySummary);
                    intent.putExtra("dailySummaryIcon", dailySummaryIcon);
                    intent.putExtra("imageURLS", imgURLS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                err.printStackTrace();
            }
        });

        rq.add(darkskyRequest);
    }


    public void callDetails(View v)
    {
        System.out.println("Calling details activity...");
        startActivity(intent);
    }

    public void getPictures()
    {
        try{
            String googlesearchurl = "http://10.0.2.2:8081/getImage/"+ URLEncoder.encode(city, "UTF-8");
            final JsonObjectRequest imgResponse = new JsonObjectRequest(Request.Method.GET, googlesearchurl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray imageRes = response.getJSONArray("items");
                        for(int i =0; i<8;i++){
                            JSONObject temp = imageRes.getJSONObject(i);
                            imgURLS[i] = temp.getString("link");
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError err){
                    err.printStackTrace();
                }
            });
            rq.add(imgResponse);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
