package com.example.ssingssinge;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.ssingssinge.Data.Kickboard;
import com.example.ssingssinge.Data.KickboardAdapter;
import com.example.ssingssinge.R;
import com.google.gson.Gson;
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

/*
    TODO : DB에서 킥보드 정보를 읽어와야 함
    TODO : 장소, 시간에 따라 예약가능한 킥보드 정보만 출력해야 함
 */
public class ShowKickboardListActivity extends AppCompatActivity {

    private KickboardAdapter kickboardListAdapter;
    private ArrayList<Kickboard> kickboardList = new ArrayList<>(); // DB 추가 후 삭제예정
    private ListView listView;
    private int selectedKickboard;
    public static final String webserver = "http://10.0.2.2:8080/api/";
    private static boolean waiting = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kickboard_list);
        waiting = false;
        init();

        Toast.makeText(getApplicationContext(), "구현예정\n예약시간 : " + getIntent().getIntExtra("hour", 0)
                + "시 " + getIntent().getIntExtra("minute", 0) * 10 + "분\n", Toast.LENGTH_LONG).show();

        selectedKickboard = -1;

        String location;
        location = getIntent().getStringExtra("location");

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(location + "지역내의 킥보드 검색 결과");

        kickboardListAdapter = new KickboardAdapter(getApplicationContext());
        listView = findViewById(R.id.listView);
        kickboardListAdapter.addItems(kickboardList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Kickboard kickboard = (Kickboard)kickboardListAdapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra("SerialNumber", kickboard.getKickboard_id());

                setResult(MainActivity.OK, intent);
                finish();
            }
        });
        listView.setAdapter(kickboardListAdapter);
    }

    /*
    DB추가 후 삭제할 예정
    킥보드 정보 초기화 위한 코드
     */
    void init() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String requestUrl = webserver + "scooters";
                try {
                    URL url = new URL(requestUrl);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");
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
                            Kickboard kickboard = new Kickboard(jsonObject.getInt("kickboard_id"), jsonObject.getString("kickboard_manufacture"),
                                    jsonObject.getString("kickboard_modelname"), jsonObject.getString("kickboard_serial"),
                                    jsonObject.getString("kickboard_state"), jsonObject.getString("kickboard_location"));
                            kickboardList.add(kickboard);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                waiting = true;
            }
        });

        while(waiting == false) { }
    }
}