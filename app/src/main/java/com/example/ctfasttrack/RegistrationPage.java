package com.example.ctfasttrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends AppCompatActivity {

    EditText NAME, CONTACTNO, EMAIL, ADDRESS, USERNAME, PASSWORD;
    ArrayAdapter<CharSequence> adapter;
    Spinner spinner;
    Button REGISTER;

    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        NAME = (EditText) findViewById(R.id.edt_register_name);
        CONTACTNO = (EditText) findViewById(R.id.edt_register_contactno);
        EMAIL = (EditText) findViewById(R.id.edt_register_email);
        ADDRESS = (EditText) findViewById(R.id.edt_register_address);
        USERNAME = (EditText) findViewById(R.id.edt_register_username);
        PASSWORD = (EditText) findViewById(R.id.edt_register_password);
        REGISTER = (Button) findViewById(R.id.btn_register);

        progress = (ProgressBar) findViewById(R.id.progress);

        spinner = (Spinner) findViewById(R.id.spinner_gender);

        adapter = adapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NAME.getText().toString().isEmpty() || EMAIL.getText().toString().isEmpty() || CONTACTNO.getText().toString().isEmpty() || ADDRESS.getText().toString().isEmpty() || USERNAME.getText().toString().isEmpty() || PASSWORD.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill all Fields Correctly", Toast.LENGTH_SHORT).show();

                } else {
//                    startActivity(new Intent(RegistrationPage.this,HomePage.class));
//                    finish();
                    registration();
                }
            }
        });

    }

    public void registration() {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.urlregister,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {
                            progress.setVisibility(View.GONE);
                            JSONObject response = new JSONObject(stringResponse);
                            int aa=response.getInt("status");
                            if (aa==1)
                            {
                                Toast.makeText(RegistrationPage.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                                finish();
                            } else {
                                Toast.makeText(RegistrationPage.this, "Already Registered", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progress.setVisibility(View.GONE);
                            e.printStackTrace();
                            // spinDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //spinDialog.dismiss();
                        Toast.makeText(RegistrationPage.this, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", NAME.getText().toString());
                params.put("gender", spinner.getSelectedItem().toString());
                params.put("contact", CONTACTNO.getText().toString());
                params.put("email", EMAIL.getText().toString());
                params.put("address", ADDRESS.getText().toString());
                params.put("username", USERNAME.getText().toString());
                params.put("password", PASSWORD.getText().toString());
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
//        super.onBackPressed();

        startActivity(new Intent(RegistrationPage.this, LoginPage.class));
        finish();
    }
}
