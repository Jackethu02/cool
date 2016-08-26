package com.example.weather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.util.Log;

public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener){
		new Thread(new Runnable(){
			public void run(){
				HttpURLConnection connection = null;
				StringBuilder response = new StringBuilder();
				try {
					Log.w("address===========",address);
//					URL url = new URL(address);
//					
//					InputStream in = url.openStream();
//					String s = "";
//				    BufferedReader reader = new BufferedReader(new InputStreamReader(
//		                    in,"UTF-8"));
//					
//				     while ((s = reader.readLine()) != null) {
//				    	 Log.w("=======>>>>>===========", s);
//				    	 response.append(s);
//				    
//			            }
//				     reader.close();
				     
				     
				     
				     URL url = new URL(address);
			            InputStream inputstream = url.openStream();
			            String s, str = "";
			            BufferedReader in = new BufferedReader(new InputStreamReader(
			                    inputstream,"UTF-8"));
			 
//			            Writer out = new BufferedWriter(new OutputStreamWriter(
//			                    new FileOutputStream("d:/weather.xml"), "utf-8"));
			            while ((s = in.readLine()) != null) {
			                str += s;
			                Log.w("==================str================", str);
			                response.append(s);
			                
			            }
//			            out.write(str);
//			            out.close();
			            in.close();
				     
				     
				     
				     
					
					
					
					
					
//					connection = (HttpURLConnection) url.openConnection();
//					connection.setRequestMethod("GET");
//					connection.setConnectTimeout(8000);
//					connection.setReadTimeout(8000);
//					InputStream in = connection.getInputStream();
//					BufferedReader	reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
					
				
					
				
//				    String result=null;
					String line;
//					while ((line = reader.readLine()) != null) {
//						Log.w("=============>>>>==================", line);
//						response.append(line);
//						result += line;
//					}
//					line = reader.readLine();	
//					Log.w("=============>>>>==================", line);
//					if(line!=null){
//						response.append(line);
//					}else{
//						Log.w("===========line is empty========", "===========");
//					}
				
				} catch (Exception e) {
					if(listener!=null){
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
					
					if(listener!=null){
						listener.onFinish(response.toString());
					}
				}}
		}).start();	
	}}
	
	
	
	
	


