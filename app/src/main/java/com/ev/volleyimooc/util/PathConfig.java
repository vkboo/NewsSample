package com.ev.volleyimooc.util;

/**
 * Created by tyoun on 2016-10-10.
 */

public class PathConfig {

    /*Demo:  http://v.juhe.cn/toutiao/index?type=guonei&key=c8d134ca68e5849b52f3a2a66bc8b4bb*/

    public String BASE_PATH;
    public String APPKEY_PATH;
    public String TYPE_PATH;
    public String FINAL_PATH;


    public PathConfig(String type) {
        this.TYPE_PATH = type;
        initPath();
    }

    public void initPath() {
        BASE_PATH = "http://v.juhe.cn/toutiao/index?type=";
        APPKEY_PATH = "&key=c8d134ca68e5849b52f3a2a66bc8b4bb";
        FINAL_PATH = BASE_PATH + TYPE_PATH + APPKEY_PATH;
    }
}
