package com.ev.volleyimooc.view;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ev.volleyimooc.R;

import static com.ev.volleyimooc.view.MainActivity.db;

/**
 * Created by tyoun on 2016-09-26.
 */

public class NewsDetailActivity extends AppCompatActivity {

    private WebView mWebView;
    public String url;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        mWebView = (WebView) findViewById(R.id.newsDetail);
        mProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        mProgressBar.setDrawingCacheBackgroundColor(Color.RED);
        doWebView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cache:
                cacheWebView();
                ContentValues values1 = new ContentValues();
                values1.put("news_title", getIntent().getStringExtra("news_title"));
                values1.put("news_time",getIntent().getStringExtra("news_time"));
                values1.put("news_imageUrl",getIntent().getStringExtra("news_imageUrl"));
                values1.put("news_contentUrl",getIntent().getStringExtra("news_contentUrl"));
                db.insert("NewsCache",null,values1);
                Toast.makeText(this, "加入离线阅读成功！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        url = getIntent().getStringExtra("news_contentUrl");
        mWebView.loadUrl(url);
        //增加进度条
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == mProgressBar.getVisibility()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }
    //缓存WebView
    public void cacheWebView(){
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+"ZhaoBoCache";
        //设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);
    }

}
