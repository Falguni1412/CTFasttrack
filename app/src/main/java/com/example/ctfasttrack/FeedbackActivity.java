package com.example.ctfasttrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FeedbackActivity extends AppCompatActivity {
    Button btnsend;
    RadioGroup rgfirst,rgsecond,rgthird,rgfourth,rgfifth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();
        initlaseview();
        initialiseprogressdialog();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFeedback();
            }
        });

    }//oncreate

    SweetAlertDialog spinDialog;

    private void initialiseprogressdialog() {
        spinDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        spinDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        spinDialog.setTitleText("Please Wait");
        spinDialog.setContentText("Loading");
        spinDialog.setCancelable(false);
    }//initialiseprogressdialog

    public void setFeedback() {

       final RadioButton rbfirst =  findViewById(rgfirst.getCheckedRadioButtonId());
       final RadioButton rbsecond =  findViewById(rgsecond.getCheckedRadioButtonId());
       final RadioButton rbthird =  findViewById(rgthird.getCheckedRadioButtonId());
       final RadioButton rbfourth =  findViewById(rgfourth.getCheckedRadioButtonId());
       final RadioButton rbfifth =  findViewById(rgfourth.getCheckedRadioButtonId());


        spinDialog.show();
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.sendfeedback,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {
                            spinDialog.dismiss();
                            JSONObject response = new JSONObject(stringResponse);
                            int aa=response.getInt("status");
                            if (aa==1)
                            {
                                Toast.makeText(FeedbackActivity.this, "Thank for you valuable feedback", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(FeedbackActivity.this, LoginPage.class));
                                finish();
                            } else {
                                Toast.makeText(FeedbackActivity.this, "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // spinDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //spinDialog.dismiss();
                        Toast.makeText(FeedbackActivity.this, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", preferences.getString("userid","0"));
                params.put("first_q", rbfirst.getText().toString());
                params.put("second_q", rbsecond.getText().toString());
                params.put("third_q", rbthird.getText().toString());
                params.put("fourth_q", rbfourth.getText().toString());
                params.put("fifth_q", rbfifth.getText().toString());
                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringPostRequest.setRetryPolicy(policy);
        stringPostRequest.setShouldCache(false);
        MyApplication.getInstance().addToReqQueue(stringPostRequest);

    }


    private void initlaseview() {

        btnsend=findViewById(R.id.btnsend);
        rgfirst=findViewById(R.id.rgfirst);
        rgsecond=findViewById(R.id.rgsecond);
        rgthird=findViewById(R.id.rgthird);
        rgfourth=findViewById(R.id.rgfourth);
        rgfifth=findViewById(R.id.rgfifth);

    }//intiitlaise


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}//Actvitiy
