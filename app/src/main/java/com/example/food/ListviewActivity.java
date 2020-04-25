package com.example.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cz.msebera.android.httpclient.Header;

public class ListviewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private ListView lv;
    double latitude;
    String lat;
    double longitude;
    String lon;
    ProgressDialog mDialog;
    String cc;
    SwipeRefreshLayout mSwipeLayout;
    MainAdapter mainAdapter;
    double  clickLat;
    double  clickLon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //隱藏狀態列
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kkk);

        lv = (ListView) findViewById(R.id.list);
        mainAdapter = new MainAdapter(this, getLayoutInflater());
        lv.setAdapter(mainAdapter);
        lv.setOnItemClickListener(this);

        getLat();
        getLon();
        Log.v("QW","QQ"+lat);

        mDialog = new ProgressDialog(this);

        mDialog.setMessage("Loading Data...");
        mDialog.setCancelable(false);

        loadData();
    }

    private String getLat(){
        Intent i=this.getIntent();
        latitude=i.getDoubleExtra("latitude",latitude);
        clickLat=i.getDoubleExtra(" clickLat", clickLat);
        lat=String.valueOf(latitude+ clickLat);



        return lat;
    }
    private String getLon(){
        Intent i=this.getIntent();

        clickLon=i.getDoubleExtra(" clickLon", clickLon);
        longitude= i.getDoubleExtra("longitude", longitude);
        lon=String.valueOf( clickLon+longitude);

        return lon;
    }

    private void loadData(){
        String urlString   = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&rankby=distance&type=restaurant&key=AIzaSyDWF5Al83s2a9P1JctqM3um9usNXEpVa2U&language=zh-tw";

        mDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mDialog.dismiss();

                Toast.makeText(getApplicationContext(),
                        "Success!", Toast.LENGTH_LONG).show();
                //Log.d("Hot Text:", response.toString());

                mainAdapter.updateData(response.optJSONArray("results"));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mDialog.dismiss();
                mSwipeLayout.setRefreshing(false); //結束更新動畫
                Toast.makeText(getApplicationContext(),
                        "Error: " + statusCode + " " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("mappp", "click " + position);
        //從成員mainAdapter中用getItem取出第position項的資料，存成jsonObject
        JSONObject jsonObject = (JSONObject) mainAdapter.getItem(position);

        JSONObject plus_code=jsonObject.optJSONObject("plus_code");
        String compound_code=plus_code.optString("compound_code");
        String compound_codecut=compound_code.substring(10,13);
        String vicinity=jsonObject.optString("vicinity");

        Log.v("aqaa","777="+compound_code);
        String name=jsonObject.optString("name");
        String google="https://www.google.com.tw/maps/search/";
        cc = google+compound_codecut+vicinity+name;
        Log.v("aqaa",cc);

        Intent i = new Intent(this, WebviewActivity.class);
        i.putExtra("aa", cc);
        i.putExtra("bb",Double.valueOf(lat));
        i.putExtra("cc",Double.valueOf(lon));
        Log.v("aqaa1",lat);

        // 使用準備好的 Intent 來開啟新的頁面
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
