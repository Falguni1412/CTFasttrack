package com.example.ctfasttrack;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomeHistoryAdapter extends BaseAdapter {

    List<pojoClassHistory> list;
    Activity activity;
    TextView tv_no_records;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public CustomeHistoryAdapter(Activity activity,List<pojoClassHistory> list, TextView tv_no_records) {
        this.list = list;
        this.activity = activity;
        this.tv_no_records = tv_no_records;

        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        final CustomeHistoryAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (v == null) {

            holder = new CustomeHistoryAdapter.ViewHolder();
            v = inflater.inflate(R.layout.list_history, null);

            holder.busno = (TextView) v.findViewById(R.id.txt_bus_id1);
            holder.bus_from = (TextView) v.findViewById(R.id.txt_bus_source1);
            holder.bus_to = (TextView) v.findViewById(R.id.txt_bus_destination1);
            holder.bus_date = (TextView) v.findViewById(R.id.txt_bus_date_time1);

            v.setTag(holder);

        }
        else {
            holder = (CustomeHistoryAdapter.ViewHolder) v.getTag();
        }

        final pojoClassHistory obj = list.get(position);
        holder.busno.setText(obj.getBusno());
        holder.bus_from.setText(obj.getFrom());
        holder.bus_to.setText(obj.getTo());
        holder.bus_date.setText(obj.getDate());

        return v;
    }

    class ViewHolder {
        TextView busno, bus_from, bus_to,bus_date;

    }
}
