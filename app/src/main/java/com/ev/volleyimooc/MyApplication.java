package com.ev.volleyimooc;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tyoun on 2016-09-23.
 */

public class MyApplication extends Application {

    private static RequestQueue queues;
    private static MyApplication myApplication = null;

    public static MyApplication getInstance(){
        if (myApplication == null){
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public RequestQueue getHttpQueue(){
        return queues;
    }
}
