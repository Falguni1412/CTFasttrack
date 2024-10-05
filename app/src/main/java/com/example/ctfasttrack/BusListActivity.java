package com.example.ctfasttrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BusListActivity extends AppCompatActivity {

    AutoCompleteTextView actto, actfrom;
    List<String> stringArrayStations;
    List<BusPojo> list_buses = new ArrayList<BusPojo>();
    RecyclerView rvbus;
    Button btnsearchbus;
    BusListAdapter mAdapter;

    List<BusStations> listbusstations = new ArrayList<>();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        initialiseView();
        initialiseprogressdialog();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        rvbus = findViewById(R.id.rvbus);
        btnsearchbus = findViewById(R.id.btnsearchbus);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvbus.setLayoutManager(mLayoutManager);
        rvbus.setItemAnimator(new DefaultItemAnimator());

        getallStations();

        btnsearchbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actfrom.getText().toString().equalsIgnoreCase("") || actto.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(BusListActivity.this, "Please select source and destination", Toast.LENGTH_SHORT).show();
                } else if (actfrom.getText().toString().equalsIgnoreCase(actto.getText().toString())) {
                    Toast.makeText(BusListActivity.this, "Source and destination should be different", Toast.LENGTH_SHORT).show();
                } else {

                    for (BusStations wp : listbusstations) {
                        if (wp.getStationname().equalsIgnoreCase(actto.getText().toString())) {
                            editor.putString("tolatitude", wp.getLatitude()).commit();
                            editor.putString("tolongitude", wp.getLongitude()).commit();
                            startService(new Intent(BusListActivity.this, LocationCheckService.class));
                        }
                    }

                    serchbuses();
                }
            }
        });

    }//oncreate

    private void serchbuses() {
        spinDialog.show();
        String url = Config.getBusesByStation + "?source=" + actfrom.getText().toString() + "&destination=" + actto.getText().toString();
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url.replace(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {

                            JSONObject response = new JSONObject(stringResponse);

                            int aa = response.getInt("status");

                            if (aa == 1) {

                                JSONArray jsonArray = response.getJSONArray("searchbus");
                                spinDialog.dismiss();
                            list_buses.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject j1 = jsonArray.getJSONObject(i);
                                    String id = j1.getString("id");
                                    String from = j1.getString("from");
                                    String to = j1.getString("to");
                                    String number = j1.getString("number");
                                    String seats = j1.getString("seats");
                                    String driver_name = j1.getString("driver_name");
                                    String driver_mobileno = j1.getString("driver_mobileno");
                                    String conductor_name = j1.getString("conductor_name");
                                    String conductor_mobileno = j1.getString("conductor_mobileno");
                                    String image_url = j1.getString("image_url");
                                    String image_name = j1.getString("image_name");
                                    String status = j1.getString("status");
                                    String updated_at = j1.getString("updated_at");
                                    Toast.makeText(BusListActivity.this, "Seats "+seats, Toast.LENGTH_SHORT).show();

                                    BusPojo busPojo = new BusPojo();
                                    busPojo.setId(id);
                                    busPojo.setFrom(from);
                                    busPojo.setTo(to);
                                    busPojo.setNumber(number);
                                    busPojo.setDriver_name(driver_name);
                                    busPojo.setDriver_mobileno(driver_mobileno);
                                    busPojo.setConductor_name(conductor_name);
                                    busPojo.setImage_url(image_url);
                                    busPojo.setImage_name(image_name);
                                    busPojo.setStatus(status);
                                    busPojo.setUpdated_at(updated_at);
                                    list_buses.add(busPojo);
                                }
                                BusListAdapter busListAdapter = new BusListAdapter(BusListActivity.this, list_buses, actfrom.getText().toString(), actto.getText().toString());
                                rvbus.setAdapter(busListAdapter);
                            }

//                            JSONArray response = new JSONArray(stringResponse);
//                            spinDialog.dismiss();
//                            list_buses.clear();
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject j1 = response.getJSONObject(i);
//                                String id = j1.getString("id");
//                                String from = j1.getString("from");
//                                String to = j1.getString("to");
//                                String number = j1.getString("number");
//                                String seats = j1.getString("seats");
//                                String driver_name = j1.getString("driver_name");
//                                String driver_mobileno = j1.getString("driver_mobileno");
//                                String conductor_name = j1.getString("conductor_name");
//                                String conductor_mobileno = j1.getString("conductor_mobileno");
//                                String image_url = j1.getString("image_url");
//                                String image_name = j1.getString("image_name");
//                                String status = j1.getString("status");
//                                String updated_at = j1.getString("updated_at");
//
//                                BusPojo busPojo = new BusPojo();
//                                busPojo.setId(id);
//                                busPojo.setFrom(from);
//                                busPojo.setTo(to);
//                                busPojo.setNumber(number);
//                                busPojo.setDriver_name(driver_name);
//                                busPojo.setDriver_mobileno(driver_mobileno);
//                                busPojo.setConductor_name(conductor_name);
//                                busPojo.setImage_url(image_url);
//                                busPojo.setImage_name(image_name);
//                                busPojo.setStatus(status);
//                                busPojo.setUpdated_at(updated_at);
//                                list_buses.add(busPojo);
//                            }
//                            BusListAdapter busListAdapter = new BusListAdapter(BusListActivity.this, list_buses,actfrom.getText().toString(),actto.getText().toString());
//                            rvbus.setAdapter(busListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BusListActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                            spinDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinDialog.dismiss();
                        error.getMessage();
                        Toast.makeText(BusListActivity.this, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringPostRequest.setRetryPolicy(policy);
        stringPostRequest.setShouldCache(false);
        MyApplication.getInstance().addToReqQueue(stringPostRequest);
    }

    SweetAlertDialog spinDialog;

    private void initialiseprogressdialog() {

        spinDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        spinDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        spinDialog.setTitleText("Please Wait");
        spinDialog.setContentText("Loading");
        spinDialog.setCancelable(false);

    }//initialiseprogressdialog

    private void getallStations() {
        spinDialog.show();
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.getStations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {

                            JSONObject response = new JSONObject(stringResponse);

                            int aa = response.getInt("status");

                            if (aa == 1) {

                                JSONArray jsonArray = response.getJSONArray("send_station");
                                spinDialog.dismiss();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject j1 = jsonArray.getJSONObject(i);
                                //  String id=j1.getString("id");
                                String station_name = j1.getString("station_name");
                                String latitude = j1.getString("latitude");
                                String longitude = j1.getString("longitude");
                                BusStations busStations = new BusStations();
                                busStations.setStationname(station_name);
                                busStations.setLatitude(latitude);
                                busStations.setLongitude(longitude);
                                listbusstations.add(busStations);
                                stringArrayStations.add(station_name);
                                    Toast.makeText(BusListActivity.this, "Station Name"+station_name, Toast.LENGTH_SHORT).show();
                            }
                            adaptorstations.notifyDataSetChanged();
                            }
//                            JSONArray response = new JSONArray(stringResponse);
//                            spinDialog.dismiss();
//
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject j1 = response.getJSONObject(i);
//                                //  String id=j1.getString("id");
//                                String station_name = j1.getString("station_name");
//                                String latitude = j1.getString("latitude");
//                                String longitude = j1.getString("longitude");
//                                BusStations busStations = new BusStations();
//                                busStations.setStationname(station_name);
//                                busStations.setLatitude(latitude);
//                                busStations.setLongitude(longitude);
//                                listbusstations.add(busStations);
//                                stringArrayStations.add(station_name);
//                            }
//                            adaptorstations.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            spinDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinDialog.dismiss();
                        Toast.makeText(BusListActivity.this, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringPostRequest.setRetryPolicy(policy);
        stringPostRequest.setShouldCache(false);
        MyApplication.getInstance().addToReqQueue(stringPostRequest);

    }//getallStations

    ArrayAdapter adaptorstations;

    private void initialiseView() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        actfrom = findViewById(R.id.actfrom);
        actto = findViewById(R.id.actto);
        stringArrayStations = new ArrayList<String>();

        adaptorstations = new ArrayAdapter<String>(BusListActivity.this, R.layout.spinner_item, R.id.tvspinneritem, stringArrayStations);
        actfrom.setThreshold(1);
        actfrom.setAdapter(adaptorstations);

        actto.setThreshold(1);
        actto.setAdapter(adaptorstations);

    }//initiliseView

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}//Activity
