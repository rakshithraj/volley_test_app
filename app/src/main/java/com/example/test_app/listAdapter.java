package com.example.test_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import webService.AppController;

/**
 * Created by Rakshith on 8/6/2015.
 */
public class listAdapter extends BaseAdapter {
    Activity activity;
    List<AppController.ItemInfo> listItemInfo;
    LayoutInflater layoutInflater=null;
    public listAdapter(Activity activity, List<AppController.ItemInfo> listItemInfo) {
        this.activity = activity;
        this.listItemInfo = listItemInfo;
        layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(listItemInfo.size()<=0)
            return 1;
        return listItemInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView textView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi;
        vi=convertView;
        ViewHolder holder;
        if(convertView==null){
            vi=layoutInflater.inflate(R.layout.row_item,null);
            holder=new ViewHolder();
        }else{
            holder=(ViewHolder)vi.getTag();
        }




        return vi;
    }
}
