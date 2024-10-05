package com.example.ctfasttrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class ForgetPasswordPage extends AppCompatActivity {

    EditText Username, Password, confirm_password;
    CheckBox Show_Hide;
    Button btn_update_password;
    SharedPreferences preferences;
    ProgressBar progress;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_page);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        Username = (EditText) findViewById(R.id.edt_username);
        Password = (EditText) findViewById(R.id.edt_password);
        confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        btn_update_password = (Button) findViewById(R.id.btn_update_forget_password);
        progress = (ProgressBar) findViewById(R.id.progress);
        Show_Hide = (CheckBox) findViewById(R.id.chk_show_hide);

        Show_Hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Username.getText().toString().isEmpty() || Password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill all Fields Correctly", Toast.LENGTH_SHORT).show();
                } else if (!confirm_password.getText().toString().equals(Password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password is not matching", Toast.LENGTH_SHORT).show();
                } else {
                    updatePassword();
                }
            }
        });


    }

    public void updatePassword() {

        progress.setVisibility(View.VISIBLE);

        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.urlUpdatePassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {
                            progress.setVisibility(View.GONE);
                            JSONObject response = new JSONObject(stringResponse);

                            int aa = response.getInt("status");

                            if (aa == 1) {
                                startActivity(new Intent(ForgetPasswordPage.this, LoginPage.class));
                                finish();

                            } else {
                                Toast.makeText(ForgetPasswordPage.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ForgetPasswordPage.this, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Username.getText().toString());
                params.put("password", Password.getText().toString());
                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringPostRequest.setRetryPolicy(policy);
        stringPostRequest.setShouldCache(false);
        MyApplication.getInstance().addToReqQueue(stringPostRequest);
    }
}
