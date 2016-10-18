package com.ev.volleyimooc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ev.volleyimooc.MyApplication;
import com.ev.volleyimooc.model.NewsBean;
import com.ev.volleyimooc.R;
import com.ev.volleyimooc.http.MyImageCache;

import java.util.List;


/**
 * Created by tyoun on 2016-10-10.
 */

public class MyAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<NewsBean> newsBeanList;
    private ImageLoader imageLoader;

    public MyAdapter(Context context, List<NewsBean> newsBeanList) {
        inflater = LayoutInflater.from(context);
        this.newsBeanList = newsBeanList;
        imageLoader = new ImageLoader(MyApplication.getInstance().getHttpQueue(),new MyImageCache());
    }

    @Override
    public int getCount() {
        return newsBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return newsBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        ImageLoader.ImageListener listener = null;
        String url = "";
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.titleItem);
            viewHolder.timeView = (TextView) convertView.findViewById(R.id.timeItem);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //使用ImageLoader加载网络图片并缓存（注意把IamgeLoader的实例化放在构造方法里面）
        listener = ImageLoader.getImageListener(viewHolder.imageView,
                R.drawable.loading, R.drawable.fail);
        url = newsBeanList.get(i).imageURL;
        imageLoader.get(url, listener);

        viewHolder.titleView.setText(newsBeanList.get(i).title);
        viewHolder.timeView.setText(newsBeanList.get(i).time);
        return convertView;
    }

    class ViewHolder {
        TextView titleView, timeView;
        ImageView imageView;
    }
}
