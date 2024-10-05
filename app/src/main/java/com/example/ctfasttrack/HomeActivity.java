package com.example.ctfasttrack;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.ctfasttrack.LocationTrack.location;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    GoogleMap mMap = null;

    private static final int PERMISSION_REQUEST_CODE = 200;

    Context conte;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onResume() {
        super.onResume();
        new CommonMethods().EnableGPSAutoMatically(HomeActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        conte = HomeActivity.this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, BusListActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);

        boolean firsttime = sharedPreferences.getBoolean("firsttime", true);

        if (firsttime) {
            alertdialog();
        }

        initialiseprogressdialog();

    }

    public void alertdialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("CTFastrack");
        ad.setMessage("Welcome to CTFastrack");
        ad.setPositiveButton(Html.fromHtml("<font color = '#ff00ff'>OK"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firsttime", false);
        editor.apply();
    }

    SweetAlertDialog spinDialog;
    SupportMapFragment mapFragment;

    private void initialiseprogressdialog() {

        spinDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        spinDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        spinDialog.setTitleText("Please Wait");
        spinDialog.setContentText("Loading");
        spinDialog.setCancelable(false);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }//initialiseprogressdialog


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Toast.makeText(HomeActivity.this, "Location enabled", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(HomeActivity.this, "Please enable the gps", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean finelocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coarselocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (finelocation & coarselocation) {
                        new CommonMethods().initialiseprogressdialog(HomeActivity.this);
                        new LocationTrack(HomeActivity.this);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!finelocation || !coarselocation) {
                            CommonMethods.showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finishAffinity();
                                        }
                                    }, HomeActivity.this, "Ok", "Cancel");
                            return;
                        }
                    }
                }
        }
    }//onRequestPermissionsResult

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(conte, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(conte, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        mMap.setMyLocationEnabled(true);

        if (location != null) {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
        }

        getData();
    }

    public void getData() {

        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.getLocation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {
                            JSONObject response = new JSONObject(stringResponse);

                            int aa = response.getInt("status");

                            if (aa == 1) {

                                JSONArray jsonArray = response.getJSONArray("bus_location");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject j1 = jsonArray.getJSONObject(i);
//                                  String id=j1.getString("id");
                                    String bus_id = j1.getString("bus_id");
                                    Double lati = j1.getDouble("lati");
                                    Double longi = j1.getDouble("longi");

                                    LatLng latLng = new LatLng(lati, longi);
                                    MarkerOptions markerOptions = new MarkerOptions();

                                    // Setting the position for the marker
                                    markerOptions.position(latLng);

                                    // Setting the title for the marker.
                                    // This will be displayed on taping the marker
                                    markerOptions.title(bus_id);
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
                                    // Clears the previously touched position


                                    // Animating to the touched position
                                      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

                                    // Placing a marker on the touched position
                                    mMap.addMarker(markerOptions);


                                }
                            }
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject j1 = response.getJSONObject(i);
//                                //  String id=j1.getString("id");
//                                String bus_id = j1.getString("bus_id");
//                                Double lati = j1.getDouble("lati");
//                                Double longi = j1.getDouble("longi");
//
//                                LatLng latLng = new LatLng(lati, longi);
//                                MarkerOptions markerOptions = new MarkerOptions();
//
//                                // Setting the position for the marker
//                                markerOptions.position(latLng);
//
//                                // Setting the title for the marker.
//                                // This will be displayed on taping the marker
//                                markerOptions.title(bus_id);
//                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
//                                // Clears the previously touched position
//
//
//                                // Animating to the touched position
//                                //  googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                                // Placing a marker on the touched position
//                                mMap.addMarker(markerOptions);

//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Could not connect", Toast.LENGTH_LONG).show();
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            recreate();
            return true;
        } else if (id == R.id.menu_all_buses) {
            Intent intent =  new Intent(HomeActivity.this,AllBuses.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_password) {
            editor.putBoolean("islogin", false).commit();
            startActivity(new Intent(HomeActivity.this, ChangePasswordActivity.class));
            finishAffinity();
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(HomeActivity.this, History.class));
            finishAffinity();
        }
        if (id == R.id.navlogout) {
            editor.putBoolean("islogin", false).commit();
            startActivity(new Intent(HomeActivity.this, LoginPage.class));
            finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
