package com.ev.volleyimooc.view;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ev.volleyimooc.MyApplication;
import com.ev.volleyimooc.R;
import com.ev.volleyimooc.adapter.MyAdapter;
import com.ev.volleyimooc.http.VolleyCallback;
import com.ev.volleyimooc.http.VolleyRequest;
import com.ev.volleyimooc.util.DBControler;
import com.ev.volleyimooc.util.Parse;
import com.ev.volleyimooc.util.PathConfig;

import static com.ev.volleyimooc.util.Parse.newsBeanList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ListView mListView;
    private ProgressDialog dialog;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    //倒数第二次点击返回键的系统时间
    private long exitTime = 0;
    //网络接口参数
    PathConfig pathConfig = new PathConfig("keji");
    private String URL = pathConfig.FINAL_PATH;
    //SQLite数据库
    private DBControler dbControler;//News表
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //进入时加载Dialog
        dialog = new ProgressDialog(this);
        dialog.setTitle("正在加载数据^_^");
        dialog.setCancelable(false);
        dialog.show();
        //设置下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipely_main);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        mListView = (ListView)findViewById(R.id.lv_main);
        doVolley();
        dbControler = new DBControler(MainActivity.this,"NewsDB.db",1);
        db = dbControler.openDB();
        //item的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,NewsDetailActivity.class);
                intent.putExtra("news_title",Parse.newsBeanList.get(i).title);
                intent.putExtra("news_time",Parse.newsBeanList.get(i).time);
                intent.putExtra("news_imageUrl",Parse.newsBeanList.get(i).imageURL);
                intent.putExtra("news_contentUrl",Parse.newsBeanList.get(i).contentURL);
                startActivity(intent);
            }
        });
        //长按item弹出上下文菜单
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.add(0,0,0,"收藏");
                menu.add(0,1,0,"加入离线阅读");
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().getHttpQueue().cancelAll("myTag");
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2 * 1000){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    //上下文菜单点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Object itemValue = myAdapter.getItemId(itemInfo.position);
        int i = Integer.parseInt(itemValue.toString());
        switch (item.getItemId()){
            //item加入收藏
            case 0:
                ContentValues values0 = new ContentValues();
                values0.put("news_title",Parse.newsBeanList.get(i).title);
                values0.put("news_time",Parse.newsBeanList.get(i).time);
                values0.put("news_imageUrl",Parse.newsBeanList.get(i).imageURL);
                values0.put("news_contentUrl",Parse.newsBeanList.get(i).contentURL);
                db.insert("News",null,values0);
                Toast.makeText(MainActivity.this, "已加入收藏:)", Toast.LENGTH_SHORT).show();
                break;
            //item进行离线阅读
            case 1:
                /*NewsDetailActivity newsDetailActivity = new NewsDetailActivity();
                newsDetailActivity.url = Parse.newsBeanList.get(i).imageURL;
                newsDetailActivity.cacheWebView();*/
                ContentValues values1 = new ContentValues();
                values1.put("news_title",Parse.newsBeanList.get(i).title);
                values1.put("news_time",Parse.newsBeanList.get(i).time);
                values1.put("news_imageUrl",Parse.newsBeanList.get(i).imageURL);
                values1.put("news_contentUrl",Parse.newsBeanList.get(i).contentURL);
                db.insert("NewsCache",null,values1);
                Toast.makeText(MainActivity.this, "这段代码没写完整：" + "\n" +
                        "只是将item的各项加入NewsCache表，网页没有实现真正的缓存", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //Menu的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.collect:
                Intent intent0 = new Intent(MainActivity.this,CollectActivity.class);
                startActivity(intent0);
                break;
            case R.id.cache:
                Intent intent1 = new Intent(MainActivity.this,CacheActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doVolley(){
        VolleyRequest.volley_gets(URL, "abcTag", new VolleyCallback() {
            @Override
            public void onMySuccess(String result) {
                swipeRefreshLayout.setRefreshing(false);
                dialog.dismiss();
                Parse.parseJson(result);
                myAdapter = new MyAdapter(MainActivity.this, newsBeanList);
                mListView.setAdapter(myAdapter);
            }

            @Override
            public void onMyError(VolleyError error) {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("无法连接到互联网 :)");
                builder.setMessage("请打开WiFi或手机数据网络在线浏览或进入离线模式");
                builder.setCancelable(false);
                builder.setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent settingIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        startActivity(settingIntent);
                    }
                });
                builder.setNegativeButton("进入离线模式", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(MainActivity.this,CacheActivity.class));
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onRefresh() {
        doVolley();
    }
}
