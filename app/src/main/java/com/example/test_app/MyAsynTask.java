package com.example.test_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.webservice.WebService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rakshith on 8/5/2015.
 */
public class MyAsynTask extends AsyncTask<Context,Integer,String> {

    Activity activity;
    String URL;
    String response=null;
    ProgressDialog progressDialog;
    List<NameValuePair> nameValuePairs ;
    public MyAsynTask( Activity activity, String URL) {
        super();
        this.activity=activity;
        this.URL=URL;

        nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("fir_no",
                "1"));
        nameValuePairs.add(new BasicNameValuePair("station",
                "Mulki"));
        nameValuePairs.add(new BasicNameValuePair("year",
                "2015"));
    }


    void showProgressDialog(boolean isNew){
        if(isNew) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else{
            if (progressDialog != null) {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        }
    }
    @Override
    protected String doInBackground(Context... params) {
        // Tag used to cancel the request
        WebService ldConnectLockDownService=new WebService();
        response=ldConnectLockDownService.executePost(URL,nameValuePairs);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(true);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        Toast.makeText(activity,"response="+response,Toast.LENGTH_LONG).show();
    }

}
