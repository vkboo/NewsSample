package com.ev.volleyimooc.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tyoun on 2016-10-13.
 */

public class DBControler {

    private MyDBHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;


    public DBControler(Context context,String tableName,int version){
        this.mContext = context;
        dbHelper = new MyDBHelper(mContext,tableName,null,version);
    }


    public SQLiteDatabase openDB(){
        db = dbHelper.getWritableDatabase();
        return db;
    }

}
