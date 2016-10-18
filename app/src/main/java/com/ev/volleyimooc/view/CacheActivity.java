package com.ev.volleyimooc.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ev.volleyimooc.R;
import com.ev.volleyimooc.adapter.MyAdapter;
import com.ev.volleyimooc.model.NewsBean;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.ev.volleyimooc.view.MainActivity.db;

/**
 * Created by tyoun on 2016-10-11.
 */

public class CacheActivity extends AppCompatActivity {
    private ListView mListView;
    private List<NewsBean> newsBeanList;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        mListView = (ListView) findViewById(R.id.lv_cache);
        getData();
        myAdapter = new MyAdapter(this, newsBeanList);
        mListView.setAdapter(myAdapter);
        //ListView的点击事件d
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CacheActivity.this,NewsDetailActivity.class);
                intent.putExtra("news_contentUrl", newsBeanList.get(i).contentURL);
                startActivity(intent);
            }
        });
        //长按item弹出上下文菜单
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.add(0,0,0,"取消离线");
            }
        });


    }
    //上下文菜单点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Object itemValue = myAdapter.getItemId(itemInfo.position);
        int i = Integer.parseInt(itemValue.toString());
        switch (item.getItemId()){
            case 0:
                String itemTitle = newsBeanList.get(i).title;
                db.delete("NewsCache","news_title = ?",new String[]{itemTitle});
                newsBeanList.remove(i);
                myAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    //从数据库中获取MainActivity的“收藏”数据
    public void getData() {
        Cursor cursor = db.query("NewsCache", null, null, null, null, null, null);
        newsBeanList = new ArrayList<NewsBean>();
        if (cursor.moveToFirst()) {
            do {
                NewsBean newsBean = new NewsBean();
                newsBean.title = cursor.getString(cursor.getColumnIndex("news_title"));
                newsBean.contentURL = cursor.getString(cursor.getColumnIndex("news_contentUrl"));
                newsBean.time = cursor.getString(cursor.getColumnIndex("news_time"));
                newsBean.imageURL = cursor.getString(cursor.getColumnIndex("news_imageUrl"));
                newsBeanList.add(newsBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cache,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //menu点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clearCahce:
                db.execSQL("delete from NewsCache");
                Toast.makeText(CacheActivity.this, "清除webView缓存逻辑 还没有", Toast.LENGTH_SHORT).show();
                mListView.setVisibility(GONE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
