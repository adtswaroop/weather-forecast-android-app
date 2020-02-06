package com.example.hw9weathersearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchResultsActivity extends AppCompatActivity {

    //declarations
    String city="", state="", latitude, longitude;

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    RequestQueue rq;

    CardView topcard, bottomcard;
    ListView weeklylistview;
    TextView waitingtext;
    private TextView txt_topCardTemp, txt_topCardLocation , txt_topCardSummary , txt_scndCrdHumidity, txt_scndCrdWindSpeed, txt_scndCrdVisibility, txt_scndCrdPressure, wronginput;
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

    ProgressBar pb;
    Intent intent;
    SharedPreferences sp;
    FloatingActionButton addfav, removefav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        sp = this.getPreferences(Context.MODE_PRIVATE);
        addfav = (FloatingActionButton) findViewById(R.id.addfav);
        removefav = (FloatingActionButton) findViewById(R.id.removefav);

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
        wronginput = (TextView) findViewById(R.id.wronginput);
        wronginput.setVisibility(View.INVISIBLE);

        rq = Volley.newRequestQueue(getApplicationContext());
        rq.start();

        String[] params = handleIntent(getIntent());

        System.out.println("wapas");
        if (params.length > 0) {
            if(params.length == 1)
            {
                city = params[0].trim();
                state = city;
            }
            else {
                city = params[0].length() > 0 ? params[0].trim() : "";
                state = params[1].length() > 0 ? params[1].trim() : "";
            }
            System.out.println("City..." +city);
            System.out.println("State...."+state);
            if (city.isEmpty() || state.isEmpty()) {
                wronginput.setVisibility(View.VISIBLE);
            }

            if(sp.contains(city))
            {
                System.out.println("Already present " + city);
                removefav.show();
                addfav.hide();
            }
            else
            {
                System.out.println("Not present " + city);
                removefav.hide();
                addfav.show();
            }
            System.out.println("Searching.... " + city + state);

            String geocodeURL = "http://10.0.2.2:8080/getGeoLocation/" + Uri.encode(city) + "/" + Uri.encode(city) + "/" + Uri.encode(state);
            JsonObjectRequest geocodeRequest = new JsonObjectRequest(Request.Method.GET, geocodeURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("Calling geocode for searched city...");
                        latitude = response.getString("latitude");
                        longitude = response.getString("longitude");
                        callDarksky(latitude, longitude);
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
            rq.add(geocodeRequest);
        }
        else{
            System.out.println("Input is not valid...");
            wronginput.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private String[] handleIntent(Intent intent) {
        String[] params;
        String query="";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(query); //change this according to the city fetched
            params = query.split(",");
            System.out.println("Query..." + query);
            topCardLocation = query;
            txt_topCardLocation = (TextView) findViewById(R.id.topCardLocation);
            txt_topCardLocation.setText(topCardLocation);
        }
        else
        {
            System.out.println("In else...");
             params = new String[2];
        }

        return params;
    }

    public void callDarksky(String lat, String lon)
    {
        System.out.println("Calling darksky for searched city...");
        String darkskyURL = "http://10.0.2.2:8080/getWeather/" + lat + "/" + lon;
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

    public void getPictures()
    {
        try{
            System.out.println("Getting pictures for..." + city);
            String googlesearchurl = "http://10.0.2.2:8080/getImage/"+ URLEncoder.encode(city, "UTF-8");
            final JsonObjectRequest imgResponse = new JsonObjectRequest(Request.Method.GET, googlesearchurl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray imageRes = response.getJSONArray("items");
                        for(int i =0; i<8;i++){
                            JSONObject temp = imageRes.getJSONObject(i);
                            imgURLS[i] = temp.getString("link");
                            System.out.println(imgURLS[i]);
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

    public void callDetails(View v)
    {
        System.out.println("Calling details activity...");
        startActivity(intent);
    }

    public void showRemoveFavIcon(View v){
        removefav.show();
        addfav.hide();
        Context context = getApplicationContext();
        String str = city + " was added to Favorites";
        int duration = Toast.LENGTH_SHORT;

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(city , city);
        ed.commit();
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

    public void showAddFavIcon(View v) {
        addfav.show();
        removefav.hide();
        Context context = getApplicationContext();
        String str = city + " was removed from Favorites";
        int duration = Toast.LENGTH_SHORT;
        SharedPreferences.Editor ed = sp.edit();
        ed.remove(city);
        ed.commit();
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
}
