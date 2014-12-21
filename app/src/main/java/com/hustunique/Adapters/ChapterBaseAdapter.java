package com.hustunique.Adapters;

import java.util.ArrayList;
import java.util.Map;

import com.hustunique.myapplication.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustunique.myapplication.R;
import com.hustunique.Utils.DataConstances;
import com.hustunique.Views.Pointwithcolor;

public class ChapterBaseAdapter  extends BaseAdapter{

	private Context mcontext;
	private ArrayList<Map<String,String>> mlist;
	private int color= DataConstances.colors[0];
    private boolean[] tags;

	public ChapterBaseAdapter(Context context,ArrayList<Map<String,String>> list,int color){
		this.mcontext=context;
		this.mlist=list;
        this.color=color;
        tags=new boolean[mlist.size()];
        for(int i=0;i<mlist.size();i++)
            tags[i]=false;

	}

    public void setColor(int color){
        this.color=color;
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
       final Animation animationforward= AnimationUtils.loadAnimation(mcontext,R.anim.btn_rotate);
       final Animation animationback=AnimationUtils.loadAnimation(mcontext,R.anim.btn_rotateback);
		ViewHolder holder;
		if(arg1==null){
			holder=new ViewHolder();
			arg1=LayoutInflater.from(mcontext).inflate(R.layout.chapterlist_item, null);
			holder.chapname=(TextView)arg1.findViewById(R.id.chapname);
			holder.img=(ImageView)arg1.findViewById(R.id.deletebtn);
			holder.point=(Pointwithcolor)arg1.findViewById(R.id.chap_point);
            holder.tagtext=(TextView)arg1.findViewById(R.id.tagtext);
			arg1.setTag(holder);
		}else{
			holder=(ViewHolder) arg1.getTag();
		}

        animationforward.setDuration(200);
        animationback.setDuration(200);
        holder.chapname.setText(mlist.get(arg0).get("chapname"));
        holder.img.setImageResource(R.drawable.rotate);
        holder.point.setColor(this.color);
        holder.img.setVisibility(View.VISIBLE);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.bringToFront();
                if(!tags[arg0]) {
                    view.setAnimation(animationforward);
                    animationforward.setFillAfter(true);
                    animationforward.setFillEnabled(true);
                    view.startAnimation(animationforward);
                    animationforward.startNow();
                    tags[arg0] = true;
                }else{
                    view.setAnimation(animationback);
                    animationback.setFillAfter(true);
                    animationback.setFillEnabled(true);
                    view.startAnimation(animationback);
                    animationback.startNow();
                    tags[arg0] = false;
                }
            }
        });

        holder.img.clearAnimation();

        int tag=Integer.parseInt(mlist.get(arg0).get("tag"));
        Log.i("tag",String.valueOf(tag));
        if(tag==2){
            holder.tagtext.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);
            holder.point.setColor(Color.GRAY);
        }else if(tag==1){
            holder.img.setVisibility(View.GONE);
            holder.tagtext.setVisibility(View.VISIBLE);
        }else{
            holder.tagtext.setVisibility(View.GONE);
            if(tags[arg0]){
                holder.img.setImageResource(R.drawable.rotatelater);
            }else
                holder.img.setImageResource(R.drawable.rotate);
        }

		return arg1;
	}

    public boolean[] getSelections(){
        return tags;
    }

	private class ViewHolder{
		Pointwithcolor point;
		TextView chapname,tagtext;
		ImageView img;
	}
}
