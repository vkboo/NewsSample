package com.ev.volleyimooc.util;

import com.ev.volleyimooc.model.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyoun on 2016-10-10.
 */

public class Parse {



    public static List<NewsBean> newsBeanList;

    /**
     * 解析JSON数据
     */
    public static void parseJson(String data) {
        newsBeanList = new ArrayList<>();
        try {
            JSONObject allObject;
            NewsBean newsBean;
            allObject = new JSONObject(data);
            JSONObject resultObject = allObject.getJSONObject("result");
            JSONArray dataArray = resultObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject newsObject = dataArray.getJSONObject(i);
                newsBean = new NewsBean();
                newsBean.title = newsObject.getString("title");
                newsBean.contentURL = newsObject.getString("url");
                newsBean.imageURL = newsObject.getString("thumbnail_pic_s");
                newsBean.time = newsObject.getString("date");
                newsBean.count = i;//add!!!
                newsBeanList.add(newsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
