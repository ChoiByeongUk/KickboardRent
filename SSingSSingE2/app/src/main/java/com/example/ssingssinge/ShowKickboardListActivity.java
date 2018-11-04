package com.example.ssingssinge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssingssinge.Data.Kickboard;
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

        Toast.makeText(getApplicationContext(), "구현예정\n예약시간 : " + getIntent().getIntExtra("hour", 0), Toast.LENGTH_LONG).show();

        selectedKickboard = -1;
        String location;
        location = getIntent().getStringExtra("location");
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(location + "지역내의 킥보드 검색 결과");
        kickboardListAdapter = new KickboardAdapter();

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
                intent.putExtra("SerialNumber", kickboard.getSerial());
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
        DB.add(new Kickboard("de2000", 1, "able", "경북대학교 정문"));
        DB.add(new Kickboard("mijia", 2, "able", "경북대학교 북문"));
        DB.add(new Kickboard("de2000", 3, "able", "경북대학교 북문"));
        DB.add(new Kickboard("mijia", 4, "able", "경북대학교 북문"));
        DB.add(new Kickboard("de2000", 5, "able", "경북대학교 정문"));
        DB.add(new Kickboard("mijia", 6, "able", "경북대학교 정문"));
        DB.add(new Kickboard("de2000", 7, "able", "경북대학교 정문"));
        DB.add(new Kickboard("mijia", 8, "able", "경북대학교 북문"));
        DB.add(new Kickboard("de2000", 9, "able", "경북대학교 북문"));
        DB.add(new Kickboard("mijia", 10, "able", "경북대학교 북문"));
    }

    class KickboardAdapter extends BaseAdapter {
        ArrayList<Kickboard> kickboards = new ArrayList();

        @Override
        public int getCount() {
            return kickboards.size();
        }

        @Override
        public Object getItem(int position) {
            return kickboards.get(position);
        }

        @Override
        public long getItemId(int position) {
            return kickboards.get(position).getSerial();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            KickboardList kickboardList = null;
            if(kickboardList == null) {
                kickboardList = new KickboardList(getApplicationContext());
            } else {
                kickboardList = (KickboardList) convertView;
            }

            Kickboard kickboard = kickboards.get(position);
            kickboardList.setKickboardName(kickboard.getModel_name());
            kickboardList.setKickboardImage(kickboard.getModel_name());
            return kickboardList;
        }

        public void addItem(Kickboard item) {
            kickboards.add(item);
        }

        public void clear() {
            kickboards.clear();
        }
    }
}

