package com.hustunique.Utils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustunique.kyplanningapp.MyBroadcastReciever;
import com.hustunique.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chensq-ubuntu on 10/25/14.
 */
public class MyApplication extends Application {

    private ArrayList<Map<String,String>> qresult;
    public static TextView rightview;
    public static int currsize=0;

    private BroadcastReceiver mreceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };



    @Override
    public void onCreate() {
        super.onCreate();
        Dbhelper.path=this.getFilesDir().toString();
        SharedPreferences sh=getSharedPreferences("mykyapp",0);
        boolean Isfirstrun=sh.getBoolean("ISFIRSTRUN",true);
        if(Isfirstrun){
             Dbhelper.createTable();
            sh.edit().putBoolean("ISFIRSTRUN",false).commit();
        }
        rightview=(TextView)LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_rightfling,null);

        MyBroadcastReciever reciever=new MyBroadcastReciever();
        IntentFilter filter=new IntentFilter();
        filter.addAction(DataConstances.ADDPLAN_ACTION);
        filter.addAction(DataConstances.POPULIST_ACTION);
        registerReceiver(reciever,filter);



        qresult=Dbhelper.querymaintable("select * from maintable",null);
        Log.i("tag",String.valueOf(qresult.size()));
        Main_item p1,p2;
        DataConstances.header=new Main_item();
        p1=DataConstances.header;
        for(int i=0;i<qresult.size();i++){
            if(i==0) {
                p1.item=(HashMap<String, String>) (qresult.get(i));
                Log.i("tag",p1.item.get("bookname"));
            }else {
                p2=new Main_item();
                p2.item=(HashMap<String, String>) (qresult.get(i));
                p1.next=p2;
                Log.i("tag",p1.item.get("bookname"));
                p1=p2;
            }
        }
    }



}
