package com.example.ctfasttrack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

public class AllBusesAdapter extends BaseAdapter {

    List<POJOAllBuses> list;
    Activity activity;
    TextView tv_no_records;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public AllBusesAdapter(Activity activity,List<POJOAllBuses> list, TextView tv_no_records) {
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

        final AllBusesAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (v == null) {

            holder = new AllBusesAdapter.ViewHolder();
            v = inflater.inflate(R.layout.lv_all_buses, null);

            holder.bus_id = (TextView) v.findViewById(R.id.txt_bus_id);
            holder.bus_number = (TextView) v.findViewById(R.id.txt_bus_number);
            holder.bus_from = (TextView) v.findViewById(R.id.txt_bus_source);
            holder.bus_to = (TextView) v.findViewById(R.id.txt_bus_destination);
            holder.bus_date = (TextView) v.findViewById(R.id.txt_bus_date);
            holder.bus_time = (TextView) v.findViewById(R.id.txt_bus_time);
            holder.bus_seats = (TextView) v.findViewById(R.id.txt_bus_seats);
            holder.btn_view_in_map = (Button) v.findViewById(R.id.btn_view_in_map);
            holder.btn_book_my_bus = (Button) v.findViewById(R.id.btn_book_my_bus);
            v.setTag(holder);

        }
        else {
            holder = (AllBusesAdapter.ViewHolder) v.getTag();
        }

        final POJOAllBuses obj = list.get(position);
        holder.bus_id.setText(obj.getBus_id());
        holder.bus_number.setText(obj.getNumber());
        holder.bus_from.setText(obj.getFrom());
        holder.bus_to.setText(obj.getTo());
        holder.bus_date.setText(obj.getDate());
        holder.bus_time.setText(obj.getTime());
        holder.bus_seats.setText(obj.getSeats());

        holder.btn_view_in_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SingleBusLocationActivity.class);
                i.putExtra("busid", obj.bus_id);
                i.putExtra("busnumber", obj.getNumber());
                i.putExtra("lati", obj.getLati());
                i.putExtra("longi", obj.getLongi());
                i.putExtra("date", obj.getDate());
                i.putExtra("time", obj.getTime());
                activity.startActivity(i);
            }
        });

        holder.btn_book_my_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.urlBookMyBus,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String success = obj.getString("status");

                                    if (success.equals("1")) {

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(activity, AllBuses.class);
                                                activity.startActivity(i);
                                                activity.finish();
                                                Toast.makeText(activity, "Booking Successfully Done", Toast.LENGTH_SHORT).show();
                                            }
                                        },2000);

//                                progress.setVisibility(View.GONE);
                                    } else {
                                        String message = obj.getString("message");
                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
//                                progress.setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
//                        progress.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", preferences.getString("userid", ""));
                        params.put("bus_id", obj.getBus_id());
                        params.put("bus_no", obj.getNumber());
                        params.put("bus_from",obj.getFrom() );
                        params.put("bus_to",obj.getTo());
                        params.put("date",obj.getDate());
                        params.put("time",obj.getTime());
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                requestQueue.add(stringRequest);
            }
        });

        return v;
    }

    class ViewHolder {
        TextView bus_id,bus_number,bus_from, bus_to,bus_date,bus_time,bus_seats;
        Button btn_view_in_map,btn_book_my_bus;
//        ProgressDialog progressDialog;
    }
}