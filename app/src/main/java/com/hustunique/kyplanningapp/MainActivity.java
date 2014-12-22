package com.hustunique.kyplanningapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.hustunique.Utils.Dbhelper;
import com.hustunique.Utils.MyApplication;
import com.hustunique.Views.MyCircle;
import com.hustunique.myapplication.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.hustunique.Adapters.MainListAdapter;
import com.hustunique.Utils.DataConstances;
import com.hustunique.Utils.Main_item;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MainActivity extends Activity {

	
	private SwipeMenuListView mainlist;
    private ImageView add_bookbtn;
    private Main_item listitems,head,p1,temp_head,temp_currnext,temp_currpa;
    private TextView progress,chapprogress,maindate,maindayremain;
    private MyCircle myCircle;
    private int cursize;

    private int day,month,completecount,totalcount;
    private ArrayList<Main_item> list;
    private SharedPreferences sh;
    MainListAdapter adapter;

    @Override
    protected void onRestart() {
        super.onResume();

        head=DataConstances.header;
        p1=head;
        list.clear();
        while(p1!=null&&p1.item!=null){
            list.add(p1);
            p1=p1.next;
        }
        totalcount+=list.size()- MyApplication.currsize;
        sh.edit().putInt("KYAPP_TOTALCOUNT",totalcount).commit();
        adapter.notifyDataSetChanged();
        initprogress();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.rgb(0x25,0xdc,0xca);
            tintManager.setTintColor(actionBarColor);
        }

        sh=this.getSharedPreferences("mykyapp",0);
        InitWidgets();
        InitSwipeMenuListView(MainActivity.this);
        initdate();
        initprogress();

    }
    
    private void InitWidgets(){

        maindate=(TextView)findViewById(R.id.maindate);
        maindayremain=(TextView)findViewById(R.id.main_dayremain);
        progress=(TextView)findViewById(R.id.progress);
        chapprogress=(TextView)findViewById(R.id.chprogress);
        myCircle=(MyCircle)findViewById(R.id.procircle);
    	mainlist=(SwipeMenuListView)findViewById(R.id.main_listview);
        add_bookbtn=(ImageView)findViewById(R.id.add_bookbtn);
        add_bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BooksListActivity.class);
                startActivity(intent);
            }
        });
    }
    
    /*
     * Coding onMenuItemClickListenner
     * */
    private void InitSwipeMenuListView(Context context){
    	final Context mcontext=context;
    	final SwipeMenuCreator creator=new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
				// TODO Auto-generated method stub
				SwipeMenuItem deleteitem=new SwipeMenuItem(mcontext);
				deleteitem.setBackground(R.drawable.trashcan);
				deleteitem.setWidth(dp2px(100));
                deleteitem.setHeight(dp2px(72));
				menu.addMenuItem(deleteitem);

				SwipeMenuItem poptopitem=new SwipeMenuItem(mcontext);
				poptopitem.setBackground(R.drawable.tothetop);
				poptopitem.setWidth(dp2px(100));
                poptopitem.setHeight(dp2px(72));
				menu.addMenuItem(poptopitem);	
			}
		};
		
		 list=new ArrayList<Main_item>();
            head=DataConstances.header;
            p1=head;
            while(p1!=null&&p1.item!=null){
                list.add(p1);
                p1=p1.next;
            }
        MyApplication.currsize=list.size();
        adapter=new MainListAdapter(MainActivity.this, list);
	        mainlist.setAdapter(adapter);
	        mainlist.setMenuCreator(creator);
	        mainlist.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				// TODO Auto-generated method stub
                switch(index){
                    case 1:Popup(position);break;
                    case 0:Delete(position);break;
                }

				return false;
			}
		});
        mainlist.setOnRightFlingListener(new SwipeMenuListView.OnrightFlingListener() {
            @Override
            public boolean onRightFling(int i, float v, float v2, float v3) {

                if(i==mainlist.pointToPosition((int)v2,(int)v3)&&v>300&&!SwipeMenuListView.IsOpen){
                    //Toast.makeText(MainActivity.this,list.get(i).item.get("chapname"),Toast.LENGTH_LONG).show();
                    completeplan(i);
                }

                return false;
            }
        });
    }

    private void Popup(int index){
        if(index!=0) {
            temp_currpa = list.get(index - 1);
            temp_currnext = list.get(index).next;
            temp_currpa.next = temp_currnext;
            temp_head = list.get(index);
            temp_head.next = head;
            head = temp_head;
            p1 = head;
            DataConstances.header = head;
            list.clear();
            while (p1 != null) {
                list.add(p1);
                p1 = p1.next;
            }
            adapter.notifyDataSetChanged();
            Intent intent = new Intent(DataConstances.POPULIST_ACTION);
            sendBroadcast(intent);
        }else
            Toast.makeText(MainActivity.this,"已经是最顶项了",Toast.LENGTH_SHORT).show();
    }

    private void Delete(int index){
        Dbhelper.updatechaptag(0,Integer.parseInt(list.get(index).item.get("chapid").toString()));
        if(index==0){
            DataConstances.header=DataConstances.header.next;
        }else {
            Main_item temp = list.get(index - 1);
            temp.next = list.get(index).next;
        }
            p1 =DataConstances.header;
            list.clear();
            while (p1 != null) {
                list.add(p1);
                p1 = p1.next;
            }
            adapter.notifyDataSetChanged();
            MyApplication.currsize--;
            sh.edit().putInt("KYAPP_TOTALCOUNT",totalcount-1).commit();
            initprogress();
            Intent intent = new Intent(DataConstances.POPULIST_ACTION);
            sendBroadcast(intent);
    }

    private void completeplan(int index){

       int id=Integer.parseInt(list.get(index).item.get("chapid"));
        if(index==0){
            DataConstances.header=DataConstances.header.next;
        }else {
            Main_item temp = list.get(index - 1);
            temp.next = list.get(index).next;
        }
        p1 =DataConstances.header;
        list.clear();
        while (p1 != null) {
            list.add(p1);
            p1 = p1.next;
        }
        adapter.notifyDataSetChanged();
        MyApplication.currsize--;
        Dbhelper.updatechaptag(2,id);
        sh.edit().putInt("KYAPP_COMPLETECOUNT",completecount+1).commit();
        initprogress();
        Intent intent = new Intent(DataConstances.POPULIST_ACTION);
        sendBroadcast(intent);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void initdate(){
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        maindate.setText(DataConstances.getMonth(month)+day);
    }

    private void initprogress(){
        int curday=sh.getInt("KYAPP_CURRENTDAY",0);

        if(curday==0||curday!=day){
            sh.edit().putInt("KYAPP_CURRENTDAY",day).commit();
            totalcount=list.size();
            completecount=0;
            sh.edit().putInt("KYAPP_TOTALCOUNT",totalcount).commit();
            sh.edit().putInt("KYAPP_COMPLETECOUNT",0).commit();
        }else{
            totalcount=sh.getInt("KYAPP_TOTALCOUNT",0);
            completecount=sh.getInt("KYAPP_COMPLETECOUNT",0);
        }

        if(totalcount==0) {
            progress.setText("0%");
            chapprogress.setText("0/0");
        }else{
            int pro=(int)(((float)completecount/(float)totalcount)*100);
            progress.setText(String.valueOf(pro)+"%");
            String cp=completecount+"/"+totalcount;
            chapprogress.setText(cp);
            myCircle.setProgress((float)completecount/(float)totalcount);
        }

    }

}
