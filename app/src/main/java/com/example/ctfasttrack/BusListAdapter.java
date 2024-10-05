package com.example.ctfasttrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.MyViewHolder> {
    private List<BusPojo> busPojos;
    private String answer;
    Context context;
    String from,to;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvroute, tvbusno;
        Button btntrackbus;

        public MyViewHolder(View view) {
            super(view);
            tvroute = view.findViewById(R.id.tvroute);
            tvbusno = view.findViewById(R.id.tvbusno);
            btntrackbus = view.findViewById(R.id.btntrackbus);
        }
    }

    public BusListAdapter(Context context, List<BusPojo> busPojos,String from,String to) {
        this.busPojos = busPojos;
        this.context = context;
        this.from=from;
        this.to=to;
        initialiseprogressdialog(context);
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
        editor=preferences.edit();
    }
    SweetAlertDialog spinDialog;

    private void initialiseprogressdialog(Context context) {

        spinDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        spinDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        spinDialog.setTitleText("Please Wait");
        spinDialog.setContentText("Loading");
        spinDialog.setCancelable(false);

    }//initialiseprogressdialog

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bus, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BusPojo busPojo = busPojos.get(position);
        holder.tvbusno.setText(busPojo.getNumber());
        Toast.makeText(context, "Bus No"+busPojo.getNumber(), Toast.LENGTH_SHORT).show();

        holder.tvroute.setText(busPojo.getFrom()+" -> "+busPojo.getTo());

        holder.btntrackbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackBusById(busPojo.getId(),busPojo.getNumber());
            }
        });
    }

    private void trackBusById(final String busid, final String busnumber) {
        spinDialog.show();
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Config.addhistory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {
                            spinDialog.dismiss();
                            JSONObject response = new JSONObject(stringResponse);
                            int aa=response.getInt("status");
                            if (aa==1)
                            {
                                Intent i = new Intent(context, BusLocationActivity.class);
                                i.putExtra("busid", busid);
                                i.putExtra("busnumber", busnumber);
                                context.startActivity(i);
                            } else {
                            }
                        } catch (JSONException e) {
                            spinDialog.dismiss();
                            e.printStackTrace();
                            // spinDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //spinDialog.dismiss();
                        Toast.makeText(context, "Could not connect", Toast.LENGTH_LONG).show();
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", preferences.getString("userid","0"));
                params.put("source", from);
                params.put("destination", to);
                params.put("busid", busid);
                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringPostRequest.setRetryPolicy(policy);
        stringPostRequest.setShouldCache(false);
        MyApplication.getInstance().addToReqQueue(stringPostRequest);
    }

    @Override
    public int getItemCount() {
        return busPojos.size();
    }
}





