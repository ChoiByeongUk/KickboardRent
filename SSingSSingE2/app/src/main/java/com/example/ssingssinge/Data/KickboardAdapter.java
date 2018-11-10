package com.example.ssingssinge.Data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.ssingssinge.Data.Kickboard;
import com.example.ssingssinge.KickboardList;

import java.util.ArrayList;

public class KickboardAdapter extends BaseAdapter {
    ArrayList<Kickboard> kickboards = new ArrayList();
    Context context;

    public KickboardAdapter(Context context) {
        this.context = context;
    }

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
        return kickboards.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KickboardList kickboardList = null;
        if(kickboardList == null) {
            kickboardList = new KickboardList(context);
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