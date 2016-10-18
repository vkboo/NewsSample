package com.ev.volleyimooc.http;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by tyoun on 2016-09-26.
 */

public abstract class VolleyCallback {

    private Response.Listener<String> mListener;
    private Response.ErrorListener mErrorListener;


    public VolleyCallback(){}


    public Response.Listener<String> loadingListener(){
         mListener = new Response.Listener<String>() {
             @Override
             public void onResponse(String s) {
                onMySuccess(s);
             }
         };
        return mListener;
    }

    public Response.ErrorListener errorListener(){
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onMyError(volleyError);
            }
        };
        return mErrorListener;
    }

    public abstract void onMySuccess(String result);
    public abstract void onMyError(VolleyError error);
}
