package com.hustunique.Utils;

import android.graphics.Color;

public class DataConstances {
	
	public static int colors[]={Color.rgb(0xfc,0x8b,0x81),Color.rgb(0xf2,0xb5,0x73),Color.rgb(0x7f,0xd6,0x8b),Color.rgb(0x25,0xdc,0xca),Color.rgb(0x86,0xa8,0xf8),Color.rgb(0xcb,0x8e,0xf6)};
	public static int REQUEST_CODE=1;
    public final static String ADDPLAN_ACTION="ADD_PLAN";
    public final static String POPULIST_ACTION="POP_LIST";
    public static final String ADDBOOK_ACTION="ADD_BOOK";
    public static Main_item header;
    private static final String[] months={"Jan.","Feb.","Mar.","Apr.","May.","Jun.","Jul.","Aug.","Sep.","Oct.","Nov.","Dec."};
    public static String getMonth(int month){
        return months[month];
    }



}
