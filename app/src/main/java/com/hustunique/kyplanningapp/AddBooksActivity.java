package com.hustunique.kyplanningapp;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hustunique.myapplication.R;
import com.hustunique.Adapters.ChapBaseAdapter;
import com.hustunique.Adapters.ChapExpandableListVIewAdapter;
import com.hustunique.Utils.BookInfo;
import com.hustunique.Utils.DataConstances;
import com.hustunique.Utils.Dbhelper;
import com.hustunique.Utils.HttpHelper;
import com.hustunique.Utils.JsonHelper;
import com.hustunique.Views.Pointwithcolor;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class AddBooksActivity extends Activity{

	private ListView chaplistview;
	private ArrayList<String> grouplist;
	private ArrayList<ArrayList<String>> childlist;
	private ImageView scanbook;
	private BookInfo book;
    private ImageView backbtn;
    private EditText newchaptext;
    private ImageView newchapbtn;
	private EditText addchaptext,bookname,author,publisher;
    private TextView completebtn;
	private ChapBaseAdapter adapter;
    private LinearLayout Colorselect_layout,Editchaplayout;
    private ArrayList<Pointwithcolor> colorlist;
    private FrameLayout actionbar;
    private boolean Isscan=false;
    private int colorselected=DataConstances.colors[0];
    private  SystemBarTintManager tintm;
    SystemBarTintManager tintManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.addbook_layout);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.rgb(0x25,0xdc,0xca);
            tintManager.setTintColor(actionBarColor);
        }
		IniteWidgets();

		scanbook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
						Intent intent=new Intent(AddBooksActivity.this,MipcaActivityCapture.class);
						startActivityForResult(intent, DataConstances.REQUEST_CODE);
			}
		});

        LinearLayout footer= (LinearLayout) LayoutInflater.from(AddBooksActivity.this).inflate(R.layout.listview_footer,null);
        Editchaplayout=(LinearLayout)footer.findViewById(R.id.editchap_layout);
        addchaptext=(EditText)footer.findViewById(R.id.addchapter_tbtn);
        addchaptext.setOnClickListener(new OnEdittextclicklistenner());
        addchaptext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER&&keyEvent.getAction()==KeyEvent.ACTION_DOWN) {
                    String chapstr = addchaptext.getText().toString().trim();
                    if (chapstr.compareTo("") == 0) {
                       // Toast.makeText(AddBooksActivity.this, "请输入文字", Toast.LENGTH_LONG).show();
                        InputMethodManager imm = (InputMethodManager) getSystemService(AddBooksActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromInputMethod(AddBooksActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    }else {
                        grouplist.add(chapstr);
                        addchaptext.setText("");
                        // Editchaplayout.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
        newchapbtn=(ImageView)footer.findViewById(R.id.newchap_btn);
        newchaptext=(EditText)footer.findViewById(R.id.addchap_text);
        newchapbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String chapstr=newchaptext.getText().toString().trim();
                if(chapstr.compareTo("")==0){
                    Toast.makeText(AddBooksActivity.this,"请输入文字",Toast.LENGTH_LONG).show();
                }else{
                    grouplist.add(chapstr);
                    newchaptext.setText("");
                   // Editchaplayout.setVisibility(View.GONE);
                }
            }
        });
       /* addchaptext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Editchaplayout.getVisibility()!=View.VISIBLE) {
                    Editchaplayout.setVisibility(View.VISIBLE);
                    newchaptext.requestFocus();
                    newchaptext.requestFocusFromTouch();
                    newchaptext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    newchaptext.setSelection(newchaptext.getText().toString().length());
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInput(newchaptext, 0);
                }else{
                    Editchaplayout.setVisibility(View.GONE);
                }
            }
        });*/
        adapter=new ChapBaseAdapter(AddBooksActivity.this, grouplist,colorselected);
		chaplistview.addFooterView(footer);
		chaplistview.setAdapter(adapter);
        completebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String booknamet=bookname.getText().toString();
                String authort=author.getText().toString();
                String publishert=publisher.getText().toString();
                if(!Isscan){
                    book.setname(booknamet);
                    book.setauthor(authort);
                    book.setpublisher(publishert);
                    book.setChapcount(grouplist.size());
                }
                if(grouplist.size()==0||book.getname().compareTo("")==0){
                    Toast.makeText(AddBooksActivity.this,"错误！还没填写书信息",Toast.LENGTH_LONG).show();
                }else{
                    book.setcolor(colorselected);
                    Dbhelper.insertbook(book);
                    int id=Integer.parseInt(Dbhelper.querybookid("select * from book where bookname=\""+book.getname()+"\"",null));
                   for(int i=0;i<grouplist.size();i++){
                       Dbhelper.insertchap(grouplist.get(i),id);
                   }
                    Intent intent=new Intent(DataConstances.ADDBOOK_ACTION);
                    intent.putExtra("COLOR_SELECTED",colorselected);
                    sendBroadcast(intent);
                    AddBooksActivity.this.finish();
                }


            }
        });

	}
	
	private void IniteWidgets(){
        book=new BookInfo();
		chaplistview=(ListView)findViewById(R.id.add_chapexplist);
		scanbook=(ImageView)findViewById(R.id.scan);
		bookname=(EditText)findViewById(R.id.add_bookname);
		author=(EditText)findViewById(R.id.add_author);
		publisher=(EditText)findViewById(R.id.add_publisher);
        bookname.setTag(1);
        author.setTag(2);
        publisher.setTag(3);
        bookname.setOnClickListener(new OnEdittextclicklistenner());
        author.setOnClickListener(new OnEdittextclicklistenner());
        publisher.setOnClickListener(new OnEdittextclicklistenner());
        bookname.setOnKeyListener(new MyOnKeyListenner());
        author.setOnKeyListener(new MyOnKeyListenner());
        publisher.setOnKeyListener(new MyOnKeyListenner());
		grouplist=new ArrayList<String>();
		childlist=new ArrayList<ArrayList<String>>();
        Colorselect_layout=(LinearLayout)findViewById(R.id.colorselect_layout);
	    colorlist=new ArrayList<Pointwithcolor>();
        actionbar=(FrameLayout)findViewById(R.id.chapterslayot_acitionbar);
        actionbar.setFocusable(true);
        actionbar.setFocusableInTouchMode(true);
        actionbar.requestFocus();
        backbtn=(ImageView)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBooksActivity.this.finish();
            }
        });
        completebtn=(TextView)findViewById(R.id.addbook_complete);
	    for(int i=0;i<DataConstances.colors.length;i++){

            Pointwithcolor point=new Pointwithcolor(AddBooksActivity.this);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(dp2px(32),dp2px(32));
            layoutParams.setMargins(dp2px(4), dp2px(4), dp2px(4), dp2px(4));
            point.setColor(DataConstances.colors[i]);
            point.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionbar.setBackgroundDrawable(new ColorDrawable(((Pointwithcolor) view).getColor()));

                    colorselected=((Pointwithcolor) view).getColor();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        tintManager.setStatusBarTintColor(colorselected);
                    }
                    adapter.setColor(colorselected);
                    adapter.notifyDataSetInvalidated();
                }
            });
            point.setOnTouchListener(new View.OnTouchListener() {
                int tempcolor;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch(motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN: {
                            tempcolor = ((Pointwithcolor) view).getColor();
                            ((Pointwithcolor) view).setColor(tempcolor-90);
                            break;
                        }
                        case MotionEvent.ACTION_UP:{
                            ((Pointwithcolor)view).setColor(tempcolor);
                            break;
                        }
                    }
                    return false;
                }
            });
            colorlist.add(point);
            Colorselect_layout.addView(point,layoutParams);
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==DataConstances.REQUEST_CODE){
			if(resultCode==RESULT_OK){
				String isbncode=data.getStringExtra("result");
				Toast.makeText(AddBooksActivity.this, isbncode, Toast.LENGTH_LONG).show();
				new HttpgetAsynTask().execute(isbncode);
			}
		}
	}

	private class HttpgetAsynTask extends AsyncTask<String, Void, Integer>{
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			bookname.setText(book.getname());
			author.setText(book.getauthor());
			publisher.setText(book.getpublisher());
			//Toast.makeText(AddBooksActivity.this, book.getcatolog(), 2000).show();
			adapter.notifyDataSetInvalidated();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
		String result=HttpHelper.Httpget(arg0[0]);
		JsonHelper jhelper=new JsonHelper(result);
		try {
			book=jhelper.getBookInfor();
			
			ArrayList<String> cl =new ArrayList<String>();
			int tag=0;
			String catalogstr=book.getcatolog();
			String[] catalarry=catalogstr.split("\n");
			for(int i=0;i<catalarry.length;i++){
				if(catalarry[i].contains("第")&&catalarry[i].contains("章"))
					grouplist.add(catalarry[i]);
			}
            book.setChapcount(grouplist.size());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
		}
		
	}	

    private class OnEdittextclicklistenner implements OnClickListener{
        @Override
        public void onClick(View view) {
            if(view instanceof  EditText) {
                EditText t = (EditText) view;
                if(!t.isCursorVisible())
                    t.setCursorVisible(true);
                t.requestFocus();
                t.setEnabled(true);
                t.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                t.setSelection(t.getText().toString().length());
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(t, 0);
            }
            }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

     class MyOnKeyListenner implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            int tag=(Integer)(v.getTag());
            if(keyCode == KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_UP) {
                switch (tag) {
                    case 1: {
                        author.requestFocus();
                        break;
                    }
                    case 2:{
                        publisher.requestFocus();
                        break;
                    }
                }
            }
            return false;
        }
    }

}
