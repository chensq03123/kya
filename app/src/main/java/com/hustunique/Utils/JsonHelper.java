package com.hustunique.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {
			private BookInfo book;
			private String strresponse;
			public JsonHelper(String str){
				this.strresponse=str;
			}
			
			public BookInfo getBookInfor() throws JSONException{
				
				book=new BookInfo();
				JSONObject jso=new JSONObject(this.strresponse);
				book.setname(jso.getString("title"));
				book.setauthor(Parasauthor(jso.getJSONArray("author")));
				book.setpublisher(jso.getString("publisher"));
				book.setcatolog(jso.getString("catalog"));
				return book;
			}
			
			private String Parasauthor(JSONArray array){
				  StringBuffer str =new StringBuffer();
			        for(int i=0;i<array.length();i++)
			        {
			            try{
			                str=str.append(array.getString(i)).append(" ");
			            }catch (Exception e){
			                e.printStackTrace();
			            }
			        }
			        return str.toString();
			}
}

