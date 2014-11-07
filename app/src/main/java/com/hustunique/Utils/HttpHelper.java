package com.hustunique.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
		public static String Httpget(String isbn){
			  String urlstr="https://api.douban.com/v2/book/isbn/"+isbn;
			     String content = "";
					try {
						URL getUrl = new URL(urlstr);
				        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
				        connection.connect();
				        // 取得输入流，并使用Reader读取
				        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				        String lines="";
				        while ((lines = reader.readLine()) != null) {
				        	content+=lines;
				        }
				        reader.close();
				        connection.disconnect();	        
					} catch (Exception e) {
						e.printStackTrace();
					}
					return content;
		}
}
