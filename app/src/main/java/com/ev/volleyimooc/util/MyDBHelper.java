package com.ev.volleyimooc.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by tyoun on 2016-10-12.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    public static final String CREATE_NEWS = "create table News("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_time text,"
            + "news_imageUrl text,"
            + "news_contentUrl text)";

    public static final String CREATE_NEWSCHCHE = "create table NewsCache("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_time text,"
            + "news_imageUrl text,"
            + "news_contentUrl text)";
    
    private Context mContext;
    
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        this.mContext = context;
    }
    


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
        db.execSQL(CREATE_NEWSCHCHE);
        Toast.makeText(mContext, "数据库创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
