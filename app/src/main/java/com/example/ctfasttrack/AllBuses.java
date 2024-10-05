package com.example.ctfasttrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class AllBuses extends AppCompatActivity {
    List<POJOAllBuses> list;
    ListView lv_all_buses;
    TextView tv_no_records;
    ProgressBar pBar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AllBusesAdapter adapter;

    SearchView searchView_bus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_buses);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();

        setTitle("Order List");

        searchView_bus= findViewById(R.id.searchview_busbysourcedestination);
        list = new ArrayList<POJOAllBuses>();
        lv_all_buses = (ListView) findViewById(R.id.lv_all_buses);
        tv_no_records = (TextView) findViewById(R.id.tv_no_records);

        pBar = (ProgressBar) findViewById(R.id.pBar);

        searchView_bus.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchbus(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchbus(newText);
                return false;
            }
        });


        getAllBuses();

    }//oncreate

    private void searchbus(String query) {

        List<POJOAllBuses> tempcenterlist = new ArrayList();
        tempcenterlist.clear();

        for (POJOAllBuses d : list) {
            if (d.getFrom().toUpperCase().contains(query.toUpperCase()) || d.getTo().toUpperCase().contains(query
                    .toUpperCase()))
                tempcenterlist.add(d);
        }

        adapter = new AllBusesAdapter(AllBuses.this,tempcenterlist ,tv_no_records);
        lv_all_buses.setAdapter(adapter);
    }

    private void getAllBuses() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Config.url_all_buses, params, new JsonHttpResponseHandler() {

            public void onStart() {
                pBar.setVisibility(View.VISIBLE);
                super.onStart();
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    pBar.setVisibility(View.GONE);

                    String aa=response.getString("status");
                    if (aa.equals("1"))
                    {
                        JSONArray jarry = response.getJSONArray("getAllStation");

                        for (int i = 0; i < jarry.length(); i++) {
                            JSONObject jsonObject = jarry.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String bus_id = jsonObject.getString("bus_id");
                            String lati = jsonObject.getString("lati");
                            String longi = jsonObject.getString("longi");
                            String date = jsonObject.getString("date");
                            String time = jsonObject.getString("time");
                            String from = jsonObject.getString("from");
                            String to = jsonObject.getString("to");
                            String number = jsonObject.getString("number");
                            String seats = jsonObject.getString("seats");

                            list.add(new POJOAllBuses(id,bus_id,lati,longi,date,time,from,to,number,seats));
                        }
                        adapter = new AllBusesAdapter(AllBuses.this, list, tv_no_records);
                        lv_all_buses.setAdapter(adapter);
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //              pbar.setVisibility(View.GONE);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Toast.makeText(AllBuses.this, "could not connect", Toast.LENGTH_LONG).show();
            }


        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(AllBuses.this, HomeActivity.class));
        finish();
    }

}//Activity
