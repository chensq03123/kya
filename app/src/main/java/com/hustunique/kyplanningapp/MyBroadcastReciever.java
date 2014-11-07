package com.hustunique.kyplanningapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hustunique.Utils.DataConstances;
import com.hustunique.Utils.Dbhelper;
import com.hustunique.Utils.Main_item;

/**
 * Created by chensq-ubuntu on 10/31/14.
 */
public class MyBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.i("tag",action);
        if(action.compareTo(DataConstances.ADDPLAN_ACTION)==0){
            Log.i("tag","debug broadcastreciever");
            Dbhelper.cleartable();
            Main_item p1=DataConstances.header;
            while(p1!=null){
                Log.i(p1.item.get("chapname"),p1.item.get("color"));
                Dbhelper.insertmainitem(p1);
                p1=p1.next;

            }
        }else if(action.compareTo(DataConstances.POPULIST_ACTION)==0){
            Dbhelper.cleartable();
            Main_item p1=DataConstances.header;
            while(p1!=null){
                Dbhelper.insertmainitem(p1);
                p1=p1.next;
            }
        }

    }
}
