package com.example.food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Jeez on 2018/10/22.
 */

public class WebviewActivity extends ListviewActivity{

    WebView mWebView;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 設定這個頁面XML的layout名稱
        setContentView(R.layout.kkkkkk);

        ggaa();
        ggaa1();
        // 設定要顯示回上一頁的按鈕
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // 取得從 Intent 傳來的資料，改成文章網址存為 url
        Bundle args = this.getIntent().getExtras();
        String url = args.getString("aa");
        latitude=args.getDouble("bb");
        longitude=args.getDouble("cc");

        Log.v("aq",url);



        // 取得XML中的TextView，設定文字為 url
        //urlTextView = (TextView) findViewById(R.id.url_textview);
        //urlTextView.setText(url);

        // 取得XML中的WebView
        mWebView = (WebView) findViewById(R.id.wv1);

        // WebView的設定選項
        WebSettings webSettings = mWebView.getSettings();
        // Enable Javascript
        webSettings.setJavaScriptEnabled(true);
        // Enable LocalStorage
        webSettings.setDomStorageEnabled(true);
        //webSettings.setAllowUniversalAccessFromFileURLs(true);

        // 加這行以避免跳出APP用瀏覽器開啟
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        // 載入網址
        mWebView.loadUrl(url);

    }
    private double ggaa(){
        Intent i=this.getIntent();
        latitude=i.getDoubleExtra("bb",latitude);
        return latitude;
    }
    private double ggaa1(){
        Intent i=this.getIntent();
        longitude= i.getDoubleExtra("cc", longitude);
        return longitude;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i=new Intent(this,ListviewActivity.class);
        i.putExtra("latitude",latitude);
        i.putExtra("longitude",longitude);
        Log.v("aaaaa",""+longitude);
        startActivity(i);
        return super.onOptionsItemSelected(item);

    }
}