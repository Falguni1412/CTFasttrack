package com.example.ctfasttrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView change_password;
    EditText old_password,new_password,conform_password;
    Button submit;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String oldPassword;
    String username;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();

        change_password = (TextView) findViewById(R.id.txt_change_password);
        new_password=(EditText)findViewById(R.id.edt_new_pass);
        old_password = (EditText) findViewById(R.id.edt_old_pass);
        conform_password = (EditText) findViewById(R.id.edt_conform_new_pass);
        progress=(ProgressBar)findViewById(R.id.progress);
        submit = (Button) findViewById(R.id.btn_Submit);

        username=preferences.getString("username","");
        oldPassword=preferences.getString("password","");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (old_password.getText().toString().equals(oldPassword))
                {
                    if (new_password.getText().toString().equals(conform_password.getText().toString()))
                    {
                        changepassword();
                    }
                    else
                    {
                        Toast.makeText(ChangePasswordActivity.this,"password not matched",Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(ChangePasswordActivity.this,"Incorrect old Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void changepassword()
    {
        progress.setVisibility(View.VISIBLE);
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();

        params.put("newpass",new_password.getText().toString());
        params.put("userid",preferences.getString("userid",""));

        client.post(Config.urlchangepassword,params,new JsonHttpResponseHandler(){

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progress.setVisibility(View.GONE);
                try {

                    String aa = response.getString("success");

                    if (aa.equals("1")) {

                        Toast.makeText(ChangePasswordActivity.this, "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangePasswordActivity.this, LoginPage.class));
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Could Not Connect", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                progress.setVisibility(View.GONE);
                Toast.makeText(ChangePasswordActivity.this,"Could Not Connect",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
        finish();
    }
}
