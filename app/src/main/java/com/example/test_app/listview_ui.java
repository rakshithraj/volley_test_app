package com.example.test_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import webService.AppController;

/**
 * Created by Rakshith on 8/6/2015.
 */
public class listview_ui extends Activity {
    ListView listView;
    List<AppController.ItemInfo> listItemInfo=new ArrayList<AppController.ItemInfo>();
    listAdapter mlistAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.listview_ui);
        intialize();
        setData();
    }

    private void setData() {
        AppController.ItemInfo itemInfo;
        for(int i=0;i<10;i++){
            itemInfo=new AppController.ItemInfo();
            itemInfo.setText(i+"0000000000000000"+i);
            listItemInfo.add(itemInfo);
        }
        mlistAdapter.notifyDataSetChanged();
    }

    private void intialize() {
        listView=(ListView)this.findViewById(R.id.listView);
        mlistAdapter=new listAdapter(this,listItemInfo);
        listView.setAdapter(mlistAdapter);
    }
}
