package com.ev.volleyimooc.http;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ev.volleyimooc.MyApplication;

/**
 * Created by tyoun on 2016-09-26.
 */

public class VolleyRequest {

    public static StringRequest request;

    public static void volley_gets(String url, String tag, VolleyCallback volleyCallback){
        MyApplication.getInstance().getHttpQueue().cancelAll(tag);
        request = new StringRequest
                (Request.Method.GET,url, volleyCallback.loadingListener(), volleyCallback.errorListener());
        request.setTag(tag);
        MyApplication.getInstance().getHttpQueue().add(request);
        MyApplication.getInstance().getHttpQueue().start();
    }




}
