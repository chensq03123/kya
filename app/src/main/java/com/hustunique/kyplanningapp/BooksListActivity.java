package com.hustunique.kyplanningapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hustunique.Utils.DataConstances;
import com.hustunique.myapplication.R;
import com.hustunique.Adapters.BooksBaseAdapter;
import com.hustunique.Utils.Dbhelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by chensq-ubuntu on 10/25/14.
 */
public class BooksListActivity extends Activity {

    private ArrayList<Map<String,String>> mlist;
    private ListView bookslistview;
    private BooksBaseAdapter madapter;
    private TextView addbtn;
    private MyAsyncTask task;
    private LinearLayout informlayout;
    private Animation animin,animaout;
    private BroadcastReceiver mreceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().compareTo(DataConstances.ADDBOOK_ACTION)==0){
                //Toast.makeText(BooksListActivity.this,DataConstances.ADDBOOK_ACTION,Toast.LENGTH_LONG).show();
                int colorseleted=intent.getIntExtra("COLOR_SELECTED",Color.rgb(0xff,0x00,0x00));
                mlist= Dbhelper.querybook("select * from book",null);
                madapter.UpdateData(mlist);
                madapter.notifyDataSetChanged();
                AnimationController(colorseleted);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.allbooks_layout);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.rgb(0x25,0xdc,0xca);
            tintManager.setTintColor(actionBarColor);
        }

        //Regist addbook receiver
        IntentFilter filter=new IntentFilter();
        filter.addAction(DataConstances.ADDBOOK_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mreceiver, filter);

        informlayout=(LinearLayout)findViewById(R.id.success);




        addbtn=(TextView)findViewById(R.id.addbook_btn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BooksListActivity.this,AddBooksActivity.class);
                startActivity(intent);
            }
        });
        mlist= Dbhelper.querybook("select * from book",null);
        madapter = new BooksBaseAdapter(BooksListActivity.this, mlist);
        bookslistview=(ListView)findViewById(R.id.allbooks_list);
        bookslistview.setAdapter(madapter);
        bookslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(BooksListActivity.this,BooksDetailActivity.class);
                intent.putExtra("BOOKSID",mlist.get(i).get("id"));
                startActivity(intent);
            }
        });

    }

    private class MyAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            mlist= Dbhelper.querybook("select * from book",null);
            Log.i("asynctast",String.valueOf(mlist.size()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(madapter==null) {
                madapter = new BooksBaseAdapter(BooksListActivity.this, mlist);
                bookslistview.setAdapter(madapter);
                Log.i("asynctasttrue",String.valueOf(mlist.size()));
            }else{
                madapter.notifyDataSetChanged();
                madapter.notifyDataSetInvalidated();
                bookslistview.setAdapter(madapter);
                Log.i("asynctastflase",String.valueOf(mlist.size()));
            }
        }
    }


    private void AnimationController(int colorselected){
        informlayout.setBackgroundColor(colorselected);
        animin= AnimationUtils.loadAnimation(BooksListActivity.this,R.anim.animation_in);
        animaout=AnimationUtils.loadAnimation(BooksListActivity.this,R.anim.animation_out);
        animin.setDuration(1000);
        animaout.setStartOffset(3000);
        animaout.setDuration(1000);
        animin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                informlayout.startAnimation(animaout);
                informlayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //informlayout.setAnimation(animaout);
        informlayout.setAnimation(animin);
        informlayout.startAnimation(animin);
        informlayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("tag","onresume");
        mlist= Dbhelper.querybook("select * from book",null);
        madapter.notifyDataSetChanged();
        madapter.notifyDataSetInvalidated();
    }
}
