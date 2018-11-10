package com.example.ssingssinge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.ssingssinge.Data.Kickboard;
import com.example.ssingssinge.Data.KickboardAdapter;
import com.example.ssingssinge.R;

import java.util.ArrayList;

/*
    TODO : DB에서 킥보드 정보를 읽어와야 함
    TODO : 장소, 시간에 따라 예약가능한 킥보드 정보만 출력해야 함
 */
public class ShowKickboardListActivity extends AppCompatActivity {

    private KickboardAdapter kickboardListAdapter;
    private ArrayList<Kickboard> DB; // DB 추가 후 삭제예정
    private ListView listView;
    private int selectedKickboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kickboard_list);
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
        if(location.equals("전체")) {
            for(int i=0; i<DB.size(); i++) {
                kickboardListAdapter.addItem(DB.get(i));
            }
        } else {
            for(int i=0; i<DB.size(); i++) {
                if(DB.get(i).getLocation().equals(location))
                    kickboardListAdapter.addItem(DB.get(i));
            }
        }
        listView.setAdapter(kickboardListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Kickboard kickboard = (Kickboard)kickboardListAdapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra("SerialNumber", kickboard.getId());

                setResult(MainActivity.OK, intent);
                finish();
            }
        });
    }

    /*
    DB추가 후 삭제할 예정
    킥보드 정보 초기화 위한 코드
     */
    void init() {
        DB = new ArrayList<Kickboard>();
        DB.add(new Kickboard(1,"de2000",  "able", "경북대학교 정문"));
        DB.add(new Kickboard(2,"mijia",  "able", "경북대학교 북문"));
        DB.add(new Kickboard(3,"de2000",  "able", "경북대학교 북문"));
        DB.add(new Kickboard(4,"mijia",  "able", "경북대학교 북문"));
        DB.add(new Kickboard(5,"de2000",  "able", "경북대학교 정문"));
        DB.add(new Kickboard(6,"mijia",  "able", "경북대학교 정문"));
        DB.add(new Kickboard(7,"de2000", "able", "경북대학교 정문"));
        DB.add(new Kickboard(8,"mijia", "able", "경북대학교 북문"));
        DB.add(new Kickboard(9,"de2000", "able", "경북대학교 북문"));
        DB.add(new Kickboard(10,"mijia",  "able", "경북대학교 북문"));
    }
}