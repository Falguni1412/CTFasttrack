package com.example.ctfasttrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
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

public class LoginPage extends AppCompatActivity {

    EditText Username, Password;
    CheckBox Show_Hide;
    Button Login;
    TextView newuser,forget_password;

    boolean doubletap = false;

    SharedPreferences preferences;
    ProgressBar progress;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();

        Username = (EditText) findViewById(R.id.edt_contactno);
        Password = (EditText) findViewById(R.id.edt_password);
        Login = (Button) findViewById(R.id.btn_login);
        newuser = (TextView) findViewById(R.id.txt_newuser);
        forget_password = (TextView) findViewById(R.id.txt_forget_password);
        progress = (ProgressBar)findViewById(R.id.progress);
        Show_Hide = (CheckBox) findViewById(R.id.chk_show_hide);

        Show_Hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {

                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

                else
                {
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        if(preferences.getBoolean("islogin",false))
        {
            startActivity(new Intent(LoginPage.this,HomeActivity.class));
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Username.getText().toString().isEmpty() || Password.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Please Fill all Fields Correctly", Toast.LENGTH_SHORT).show();
                else
                    login();
            }
        });

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,RegistrationPage.class));
                finish();
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,ForgetPasswordPage.class));
                finish();
            }
        });
    }

    public void login()
    {

        progress.setVisibility(View.VISIBLE);

        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.urllogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {
                            progress.setVisibility(View.GONE);
                            editor.putString("username",Username.getText().toString()).commit();
                            editor.putString("password",Password.getText().toString()).commit();

                            JSONObject response=new JSONObject(stringResponse);

                            int aa=response.getInt("status");

                            if (aa==1){

                                JSONArray jsonArray=response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject j1 = jsonArray.getJSONObject(i);

                                    editor.putString("userid",j1.getString("id")).commit();

                                }

                                editor.putBoolean("islogin",true).commit();

                                startActivity(new Intent(LoginPage.this,HomeActivity.class));
                                finish();

                            }
                            else {
                                Toast.makeText(LoginPage.this,"Incorrect Id or Branch", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginPage.this, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",Username.getText().toString());
                params.put("password",Password.getText().toString());
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

        if (doubletap)
        {
            super.onBackPressed();
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Double Tap to Exit", Toast.LENGTH_SHORT).show();

            doubletap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubletap = false;
                }
            },2000);
        }
    }
}
