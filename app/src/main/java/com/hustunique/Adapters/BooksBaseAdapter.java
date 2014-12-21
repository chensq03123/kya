package com.hustunique.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hustunique.myapplication.R;
import com.hustunique.Views.Pointwithcolor;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by chensq-ubuntu on 10/25/14.
 */
public class BooksBaseAdapter extends BaseAdapter {

    private ArrayList<Map<String,String>> mlist;
    private Context mcontext;

    public BooksBaseAdapter(Context context,ArrayList<Map<String,String>> list){
        this.mcontext=context;
        this.mlist=list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoler holder;
        if(view==null){
            holder=new ViewHoler();
            view= LayoutInflater.from(mcontext).inflate(R.layout.bookslist_item,null);
            holder.bookname=(TextView)view.findViewById(R.id.bookslist_bookname);
            holder.booknamechar=(TextView)view.findViewById(R.id.bookslist_booknamechar);
            holder.point=(Pointwithcolor)view.findViewById(R.id.bookslist_point);
            holder.progress=(TextView)view.findViewById(R.id.bookslist_progress);
            view.setTag(holder);
        }else{
            holder=(ViewHoler)view.getTag();
        }

        holder.bookname.setText(mlist.get(i).get("bookname"));
        holder.booknamechar.setText(mlist.get(i).get("bookname").substring(0,1));
        holder.point.setColor(Integer.parseInt(mlist.get(i).get("color")));

        return view;
    }

    private class ViewHoler{
        TextView bookname,progress,booknamechar;
        Pointwithcolor point;
    }

    public void UpdateData(ArrayList<Map<String,String>> list){
        this.mlist=list;
    }
}
