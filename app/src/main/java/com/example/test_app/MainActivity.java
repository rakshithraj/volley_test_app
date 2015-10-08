package com.example.test_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.io.*;

import webService.ConnectWebService;
import webService.CustomJsonObjectRequest;
import webService.MultipartRequest;


public class MainActivity extends Activity {
    String line="";
  String response;
    ProgressDialog pDialog;
    String URL = "http://app.mangalorecitypolice.com/fir_api";
    MyAsynTask myAsynTask;
    StringBuilder content = new StringBuilder();
    File file;
    ImageLoader imageLoader;
    RequestQueue mRequestQueue;
    ConnectWebService connectWebService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         connectWebService=new ConnectWebService();

        //connectWebService.postRequest_File("http://192.168.2.238/UploadToServer.php", this, params);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 123);


        /*MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


        FormBodyPart bodyPart=new FormBodyPart("action", new StringBody("registerme"));
        reqEntity.addPart(bodyPart);
        bodyPart=new FormBodyPart("username", new StringBody(sRegistrationInfo.getEmail()));
        reqEntity.addPart(bodyPart);*/
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(this.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

boolean falg=true;
    String result;
    @Override
    protected void onResume() {
        super.onResume();
        //MyAsynTask myAsynTask=new MyAsynTask(this,URL);
        //myAsynTask.execute();
        //urlConnection();
        //volleyGet();
        //volleyPost();
      // volleySendFile();
        //volleyDownloadImage
        //volleyNetworkInageLoader();
        



    }

    private void volleyNetworkInageLoader() {
        mRequestQueue = getRequestQueue();
        imageLoader=new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
        NetworkImageView networkImageView=(NetworkImageView)this.findViewById(R.id.imageButton);
        networkImageView.setImageUrl("http://192.168.2.238/Uploads/images-6.jpeg",imageLoader);
    }

    public void volleyDownloadImage( View view) {
        final ImageView vi=(ImageView)view;
        final String url="http://192.168.2.238/Uploads/images-6.jpeg";
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override public void onResponse(Bitmap response) {

                 vi.setImageBitmap(response);
                pDialog.hide();
            } }
                , 0, 0, null, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"response="+error,Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        });
        queue.add(ir);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 123){
            if(resultCode == RESULT_OK){
                final Uri imageUri = data.getData();
                 file=new File(getRealPathFromURI(imageUri,this));
                if(file!=null){
                    connectWebService.postRequest_File("http://192.168.2.238/UploadToServer.php", this, file);
                }

            }
        }

    }
    public static String getRealPathFromURI(Uri contentUri,Activity ativity)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = ativity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void urlConnection() {
        final String url = "http://app.mangalorecitypolice.com/fir_api";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                URLConnection Connection=null;
                try {
                    URL url1 = new URL(url);

                    Connection = url1.openConnection();


                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Connection.getInputStream()));



                    // read from the urlconnection via the bufferedreader
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        content.append(line);
                    }
                    bufferedReader.close();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,content.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("result", content.toString());
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (MalformedURLException e) {

                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("result", "error="+e.toString());
                    message.setData(bundle);
                    handler.sendMessage(message);

                    e.printStackTrace();
                } catch (IOException e) {
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("result",e.toString());
                    message.setData(bundle);
                    handler.sendMessage(message);


                    e.printStackTrace();
                }finally {

                }
            }
        }).start();

    }

    public void volleySendFile(View view) {

        final String url="http://192.168.2.238/UploadToServer.php";


        String tag_json_obj = "json_obj_req";



        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        MultipartRequest jsonObjReq = new MultipartRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(MainActivity.this,"response="+response,Toast.LENGTH_LONG).show();
                        Log.d("tag",response);
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"response="+error,Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        },file) ;

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pDialog.hide();
           // Toast.makeText(MainActivity.this,msg.getData(),Toast.LENGTH_LONG).show();
        }
    };

    private void volleyPost() {
        // Tag used to cancel the request
        Map<String, String> params = new HashMap<String, String>();

        params.put("fir_no", "1");
        params.put("station", "Mulki");
        params.put("year", "2015");

        String tag_json_obj = "json_obj_req";

        String url = "http://app.mangalorecitypolice.com/fir_api";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this,"response="+response,Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"response="+error,Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("fir_no", "1");
                params.put("station", "Mulki");
                params.put("year", "2015");

                return params;
            }

        };


        queue.add(jsonObjReq);

    }


    private void volleyGet() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
              String url="http://api.androidhive.info/volley/person_object.json";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this,"response="+response,Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"response="+error,Toast.LENGTH_LONG).show();
                // hide the progress dialog
                pDialog.hide();
            }
        }){

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                return super.parseNetworkResponse(response);
            }
        };
        queue.add(jsonObjReq);

    }

}
