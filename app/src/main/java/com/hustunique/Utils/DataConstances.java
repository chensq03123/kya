package com.hustunique.Utils;

import android.graphics.Color;

public class DataConstances {
	
	public static int colors[]={Color.rgb(0xf2,0xb0,0xa4),Color.rgb(0xe9,0xce,0xb1),Color.rgb(0xb5,0xee,0xce),Color.rgb(0xb1,0xd6,0xd6),Color.rgb(0x90,0xcc,0xc0),Color.rgb(0xb1,0xd6,0xd6),Color.rgb(0xe7,0xb5,0xce),Color.rgb(0xc5,0xb3,0xcb)};
	public static int REQUEST_CODE=1;
    public final static String ADDPLAN_ACTION="ADD_PLAN";
    public final static String POPULIST_ACTION="POP_LIST";
    public static Main_item header;
    private static final String[] months={"Jan.","Feb.","Mar.","Apr.","May.","Jun.","Jul.","Aug.","Sep.","Oct.","Nov.","Dec."};

    public static String getMonth(int month){
        return months[month+1];
    }



}
