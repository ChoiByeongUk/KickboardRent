package com.example.ssingssinge;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.ssingssinge.Data.Kickboard;
import com.example.ssingssinge.Data.KickboardAdapter;
import com.example.ssingssinge.Data.Location;
import com.example.ssingssinge.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/*
    TODO : DB에서 킥보드 정보를 읽어와야 함
    TODO : 장소, 시간에 따라 예약가능한 킥보드 정보만 출력해야 함
 */
public class ShowKickboardListActivity extends AppCompatActivity {

    private KickboardAdapter kickboardListAdapter;
    private ArrayList<Kickboard> kickboardList = new ArrayList<>();
    private ListView listView;
    public static final String webserver = "http://192.168.0.17:8080/api/reservations";
    private String location;
    public TextView textView;
    public Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kickboard_list);
        location = getIntent().getStringExtra("location");
        textView = (TextView)findViewById(R.id.textView);
        listView = findViewById(R.id.listView);
        kickboardListAdapter = new KickboardAdapter(getApplicationContext());
        handler = new Handler();

        kickboardList.clear();
        kickboardListAdapter.clear();
        init();
        Log.d("대여가능 킥보드 수 : ", kickboardList.size()+"");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Kickboard kickboard = (Kickboard)kickboardListAdapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra("kickboard_id", kickboard.getKickboard_id());
                intent.putExtra("return_location_latitude", kickboard.getKickboard_location().getLatitude());
                intent.putExtra("return_location_longitude", kickboard.getKickboard_location().getLongitude());
                intent.putExtra("return_location_location_name", kickboard.getKickboard_location().getLocationName());
                intent.putExtra("reservation_time_start_time", getIntent().getStringExtra("startDate"));
                intent.putExtra("reservation_time_end_time", getIntent().getStringExtra("endDate"));

                setResult(MainActivity.OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(MainActivity.BACK_PRESSED);
        finish();
    }

    void init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String requestUrl = webserver + "?start=" + getIntent().getStringExtra("startDate")
                        + "&end=" + getIntent().getStringExtra("endDate");
                if(!location.equals("전체")) {
                    requestUrl += "&location=" + location;
                }

                try {
                    URL url = new URL(requestUrl);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    if (conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Authorization", getSharedPreferences("globalPref", MODE_PRIVATE).getString("accessToken", null));
                        Log.d("access token", getSharedPreferences("globalPref", MODE_PRIVATE).getString("accessToken", null));
                        conn.setDoInput(true);

                        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(isr);
                        String tempStr;
                        StringBuilder builder = new StringBuilder();

                        while((tempStr = reader.readLine()) != null) {
                            builder.append(tempStr + "\n");
                        }

                        isr.close();
                        reader.close();

                        String result = builder.toString();
                        Log.d("result : ", result);

                        JSONArray jsonArray = new JSONArray(result);

                        for(int i=0; i<jsonArray.length() ; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject locationObject = jsonObject.getJSONObject("kickboard_location");

                            Location kickboardLocation = new Location(locationObject.getDouble("latitude"),
                                        locationObject.getDouble("longitude"), locationObject.getString("location_name"));

                            Kickboard kickboard = new Kickboard(jsonObject.getInt("id"), jsonObject.getString("kickboard_manufacture"),
                                    jsonObject.getString("kickboard_modelname"), jsonObject.getString("kickboard_serial"),
                                    jsonObject.getString("kickboard_state"), kickboardLocation);

                            kickboardList.add(kickboard);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                   public void run() {
                        textView.setText(location + "지역내의 킥보드 검색 결과");
                        kickboardListAdapter.addItems(kickboardList);
                        listView.setAdapter(kickboardListAdapter);
                    }
                });
            }
        });
        thread.start();
    }
}