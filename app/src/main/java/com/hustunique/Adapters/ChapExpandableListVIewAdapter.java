package com.hustunique.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hustunique.myapplication.R;
import com.hustunique.Utils.DataConstances;
import com.hustunique.Views.Pointwithcolor;

public class ChapExpandableListVIewAdapter  extends BaseExpandableListAdapter{
		
		Context mcontext;
		ArrayList<String> mgrouplist;
		ArrayList<ArrayList<String>> mchildlist;
        private int color=DataConstances.colors[0];
	
	public ChapExpandableListVIewAdapter(Context context,ArrayList<String> grouplist,ArrayList<ArrayList<String>> chilelist,int color){
				this.mchildlist=chilelist;
				this.mgrouplist=grouplist;
				this.mcontext=context;
                this.color=color;
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
/////////////////////////////////////////////Modify this method to make a specific chile view/////////////////////////////////////////////////////////////
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		if(arg3==null){
			holder=new ViewHolder();
			arg3=LayoutInflater.from(mcontext).inflate(R.layout.addbooklist_item, null);
			holder.addpoint=(Pointwithcolor) arg3.findViewById(R.id.add_pointwithcolor);
			holder.addtext=(TextView) arg3.findViewById(R.id.add_name);
			arg3.setTag(holder);
		}else
			holder=(ViewHolder) arg3.getTag();
			
			holder.addtext.setText(mchildlist.get(arg0).get(arg1));
		    holder.addpoint.setColor(color);
		
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return mchildlist.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mgrouplist.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
///////////////////////////////////////Modify this method to make a specific group view /////////////////////////////////////////////////////////////////////////////////
	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(arg2==null){
			holder=new ViewHolder();
			arg2=LayoutInflater.from(mcontext).inflate(R.layout.addbooklist_item, null);
			holder.addpoint=(Pointwithcolor) arg2.findViewById(R.id.add_pointwithcolor);
			holder.addtext=(TextView) arg2.findViewById(R.id.add_name);
			arg2.setTag(holder);
		}else
			holder=(ViewHolder) arg2.getTag();
			
			holder.addtext.setText(mgrouplist.get(arg0));
			holder.addpoint.setColor(color);
		
		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

    public void setColor(int color){
        this.color=color;
    }

	private class ViewHolder{
		TextView addtext;
		Pointwithcolor addpoint;
	}

}
