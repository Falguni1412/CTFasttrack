package com.example.ctfasttrack;

import static com.example.ctfasttrack.LocationTrack.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SingleBusLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap = null;

    private static final int PERMISSION_REQUEST_CODE = 200;
    SupportMapFragment mapFragment;
    Context conte;
    String busid = "0",busnumber="0",date = "0",time = "0";
    Double lati,longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Bus Location");
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        busid = getIntent().getStringExtra("busid");
        busnumber = getIntent().getStringExtra("busnumber");
        lati = Double.valueOf(getIntent().getStringExtra("lati"));
        longi = Double.valueOf(getIntent().getStringExtra("longi"));
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");


    }//oncreate


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(SingleBusLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SingleBusLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        if (location != null) {
            // Add a marker in Sydney and move the camera
//            LatLng sydney = new LatLng(lati,longi);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            LatLng latLng = new LatLng(lati, longi);
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title(busnumber);
//            markerOptions.title(busid);
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
//            mMap.addMarker(markerOptions);
//
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
////                builder.include(mylatLng1);
//            builder.include(latLng);
//            CameraUpdate cameraUpdate = CameraUpdateFactory
//                    .newLatLngBounds(builder.build(), 10);
//            mMap.moveCamera(cameraUpdate);
        }

        ct = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                LatLng latLng = new LatLng(lati, longi);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(busnumber);
                markerOptions.title(busid);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
                mMap.addMarker(markerOptions);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

//                builder.include(mylatLng1);
                builder.include(latLng);
                CameraUpdate cameraUpdate = CameraUpdateFactory
                        .newLatLngBounds(builder.build(), 10);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
//                getData();
//                LatLng latLng = new LatLng(lati, longi);
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title(busnumber);
//                markerOptions.title(busid);
//                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
//                mMap.addMarker(markerOptions);
////
//                LatLng mylatLng1 = new LatLng(location.getLatitude(), location.getLongitude());
//                MarkerOptions markerOptions1 = new MarkerOptions();
//                markerOptions1.position(mylatLng1);
//                markerOptions1.title("My Location");
//                mMap.addMarker(markerOptions1);

//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
////                builder.include(mylatLng1);
//                builder.include(latLng);
//                CameraUpdate cameraUpdate = CameraUpdateFactory
//                        .newLatLngBounds(builder.build(), 10);
//                mMap.moveCamera(cameraUpdate);
                ct.start();
            }
        };
        ct.start();


    }

    CountDownTimer ct=null;

    public void getData() {

        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.getSingleBusLocation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {

                            JSONObject response = new JSONObject(stringResponse);

                            int aa = response.getInt("status");
                            Toast.makeText(SingleBusLocationActivity.this, "" + aa, Toast.LENGTH_SHORT).show();
                            mMap.clear();
                            if (aa == 1) {

                                JSONArray jsonArray = response.getJSONArray("single_bus_location");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject j1 = jsonArray.getJSONObject(i);
                                  String id=j1.getString("id");
                                  String bus_id=j1.getString("bus_id");
                                Double lati = j1.getDouble("lati");
                                Double longi = j1.getDouble("longi");

                                LatLng latLng = new LatLng(lati, longi);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(busnumber);
                                markerOptions.title(bus_id);
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
                                mMap.addMarker(markerOptions);

                                LatLng mylatLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                                MarkerOptions markerOptions1 = new MarkerOptions();
                                markerOptions1.position(mylatLng1);
                                markerOptions1.title("My Location");
                                mMap.addMarker(markerOptions1);

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                                builder.include(mylatLng1);
                                builder.include(latLng);
                                CameraUpdate cameraUpdate = CameraUpdateFactory
                                        .newLatLngBounds(builder.build(), 10);
                                mMap.moveCamera(cameraUpdate);
                                }
                            }
//                            JSONObject jsonObject = new JSONObject(stringResponse);
//                            JSONArray response = jsonObject.getJSONArray("status");
//                            mMap.clear();
//
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject j1 = response.getJSONObject(i);
//                                //  String id=j1.getString("id");
//                                Double lati = j1.getDouble("lati");
//                                Double longi = j1.getDouble("longi");
//
//                                LatLng latLng = new LatLng(lati, longi);
//                                MarkerOptions markerOptions = new MarkerOptions();
//                                markerOptions.position(latLng);
//                                markerOptions.title(busnumber);
//                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
//                                mMap.addMarker(markerOptions);
//
//                                LatLng mylatLng1 = new LatLng(location.getLatitude(), location.getLongitude());
//                                MarkerOptions markerOptions1 = new MarkerOptions();
//                                markerOptions1.position(mylatLng1);
//                                markerOptions1.title("My Location");
//                                mMap.addMarker(markerOptions1);
//
//                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//                                builder.include(mylatLng1);
//                                builder.include(latLng);
//                                CameraUpdate cameraUpdate = CameraUpdateFactory
//                                        .newLatLngBounds(builder.build(), 10);
//                                mMap.moveCamera(cameraUpdate);
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SingleBusLocationActivity.this, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("busid",busid);
                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringPostRequest.setRetryPolicy(policy);
        stringPostRequest.setShouldCache(false);
        MyApplication.getInstance().addToReqQueue(stringPostRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}//
