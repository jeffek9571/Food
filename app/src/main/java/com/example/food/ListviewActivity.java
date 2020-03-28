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

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ListviewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    private ProgressDialog pDialog;
    private ListView lv;
    double latitude;
    String a;
    double longitude;
    String b;
    String c="https://www.google.com/";
    // URL to get contacts JSON
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


//            ggaa2();
        lv = (ListView) findViewById(R.id.list);
        mainAdapter = new MainAdapter(this, getLayoutInflater());
        lv.setAdapter(mainAdapter);
        lv.setOnItemClickListener(this);
//            tv2 = (TextView) findViewById(R.id.tv2);
//            tv3 = (TextView) findViewById(R.id.tv3);
//            tv2.setText("現在經度:" + a);
//            tv3.setText("現在緯度:" + b);
        ggaa();
        ggaa1();
        Log.v("QW","QQ"+a);

        mDialog = new ProgressDialog(this);

        mDialog.setMessage("Loading Data...");
        mDialog.setCancelable(false);

        loadData();
    }

    private String ggaa(){
        Intent i=this.getIntent();
        latitude=i.getDoubleExtra("latitude",latitude);
        clickLat=i.getDoubleExtra(" clickLat", clickLat);
        a=String.valueOf(latitude+ clickLat);



        return a;
    }
    private String ggaa1(){
        Intent i=this.getIntent();

        clickLon=i.getDoubleExtra(" clickLon", clickLon);
        longitude= i.getDoubleExtra("longitude", longitude);
        b=String.valueOf( clickLon+longitude);

        return b;
    }

    private void loadData(){
        String urlString   = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+a+","+b+"&rankby=distance&type=restaurant&key=AIzaSyDWF5Al83s2a9P1JctqM3um9usNXEpVa2U&language=zh-tw";
//        +"&language=zh-TW"

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
        String bbb=plus_code.optString("compound_code");
        String dddd=bbb.substring(10,13);



        String eee=jsonObject.optString("vicinity");



        Log.v("aqaa","777="+bbb);

        String zzz=jsonObject.optString("name");


        String aaa="https://www.google.com.tw/maps/search/";

        cc = aaa+dddd+eee+zzz;
        Log.v("aqaa",cc);

        Intent i = new Intent(this, WebviewActivity.class);
        i.putExtra("aa", cc);
        i.putExtra("bb",Double.valueOf(a));
        i.putExtra("cc",Double.valueOf(b));
        Log.v("aqaa1",a);


        // 使用準備好的 Intent 來開啟新的頁面
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
