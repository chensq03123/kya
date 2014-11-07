package com.hustunique.Adapters;

import java.util.ArrayList;
import java.util.Map;

import com.hustunique.myapplication.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustunique.Utils.DataConstances;
import com.hustunique.Utils.Main_item;
import com.hustunique.Views.Pointwithcolor;

public class MainListAdapter extends BaseAdapter{

	private Context mcontext;
	private ArrayList<Main_item> mlist;
	
	public MainListAdapter(Context context, ArrayList<Main_item> list){
		this.mcontext=context;
		this.mlist=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(arg1==null){
		holder=new ViewHolder();
		arg1= LayoutInflater.from(mcontext).inflate(R.layout.mainlist_item,null);
		holder.mainlist_bookname=(TextView)arg1.findViewById(R.id.nameofbook);
		holder.mainlist_chap=(TextView)arg1.findViewById(R.id.chapter);
		holder.mainpoint_text=(TextView)arg1.findViewById(R.id.mainlist_text);
		holder.mianpoint_color=(Pointwithcolor)arg1.findViewById(R.id.mainlist_point);
		arg1.setTag(holder);
		}else{
			holder=(ViewHolder)arg1.getTag();
		}

        holder.mainlist_bookname.setText(mlist.get(arg0).item.get("bookname"));
        holder.mianpoint_color.setColor(Integer.parseInt(mlist.get(arg0).item.get("color")));
        holder.mainlist_chap.setText(mlist.get(arg0).item.get("chapname"));
        holder.mainpoint_text.setText(mlist.get(arg0).item.get("bookname").substring(0,1));
		return arg1;
	}

	private class ViewHolder{
		TextView mainpoint_text,mainlist_chap,mainlist_bookname;
		Pointwithcolor mianpoint_color;
	}
		
	
}
