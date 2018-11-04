package com.example.ssingssinge;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ssingssinge.R;

// ListView에 킥보드 정보를 보여주기 위한 Class
public class KickboardList extends LinearLayout {
    TextView textView;
    ImageView imageView;

    public KickboardList(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_kickboard_list, this, true);

        textView = (TextView)findViewById(R.id.kickboardName);
        imageView = (ImageView)findViewById(R.id.kickboardImage);
    }

    public void setKickboardName(String name) {
        textView.setText(name);
    }

    public void setKickboardImage(String name) {
        imageView.setImageResource(getResources().getIdentifier(name, "drawable", "com.example.ssingssinge"));
    }
}

