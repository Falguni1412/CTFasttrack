package com.example.ctfasttrack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ViewAllBookingAdapter extends BaseAdapter {

    List<POJOViewAllBooking> list;
    Activity activity;
    TextView tv_no_records;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public ViewAllBookingAdapter(Activity activity, List<POJOViewAllBooking> list, TextView tv_no_records) {
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

        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (v == null) {

            holder = new ViewHolder();
            v = inflater.inflate(R.layout.lv_view_all_my_booking, null);

            holder.bus_id = (TextView) v.findViewById(R.id.rv_tv_all_booking_bus_id);
            holder.bus_number = (TextView) v.findViewById(R.id.rv_tv_all_booking_bus_no);
            holder.bus_from = (TextView) v.findViewById(R.id.rv_tv_all_booking_bus_from);
            holder.bus_to = (TextView) v.findViewById(R.id.rv_tv_all_booking_bus_to);
            holder.user_name = (TextView) v.findViewById(R.id.rv_tv_all_booking_user_name);
            holder.btn_view_in_details = (Button) v.findViewById(R.id.rv_btn_my_booking_view_my_booking);
            v.setTag(holder);

        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        final POJOViewAllBooking obj = list.get(position);
        holder.bus_id.setText("BUS NO:"+obj.getBus_id());
        holder.bus_number.setText(obj.getBus_number());
        holder.bus_from.setText("Bus From: "+obj.getBus_from());
        holder.bus_to.setText("Bus To: "+obj.getBus_to());
        holder.user_name.setText("Customer Name: "+obj.getUser_name());

//        holder.btn_view_in_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, ViewBookingDetailsActivity.class);
//                editor.putString("id", obj.id).commit();
//                editor.putString("bus_id", obj.getBus_id()).commit();
//                editor.putString("bus_number", obj.getBus_number()).commit();
//                editor.putString("bus_from", obj.getBus_from()).commit();
//                editor.putString("bus_to", obj.getBus_to()).commit();
//                editor.putString("date", obj.getDate()).commit();
//                editor.putString("time", obj.getTime()).commit();
//                editor.putString("user_name", obj.getUser_name()).commit();
//                editor.putString("user_mobile_no", obj.getUser_mobile_no()).commit();
//                editor.putString("user_address", obj.getUser_address()).commit();
//                activity.startActivity(intent);
//            }
//        });
        return v;
    }

    class ViewHolder {
        TextView bus_id,bus_number,bus_from, bus_to,user_name;
        Button btn_view_in_details;
//        ProgressDialog progressDialog;
    }
}