package com.example.weather.activity;

import com.example.weather.R;
import com.example.weather.util.HttpCallbackListener;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.Utility;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WeatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	//show city name
	private TextView cityNameText;
	//show publish time
	private TextView publishText;
	//show weather info
	private TextView weatherDespText;
	//show lower temp 1
	private TextView temp1Text;
	//show higher temp 2
	private TextView temp2Text;
	//show current time
	private TextView currentDateText;
	//switch city button
	private Button switchCity;
	// update weather button
	private Button refreshWeather;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText = (TextView)findViewById(R.id.city_name);
		publishText = (TextView)findViewById(R.id.publish_text);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		temp1Text = (TextView)findViewById(R.id.temp1);
		temp2Text = (TextView)findViewById(R.id.temp2);
		currentDateText = (TextView)findViewById(R.id.current_data);
		switchCity= (Button)findViewById(R.id.switch_city);
		refreshWeather = (Button)findViewById(R.id.refresh_weather);
		String countyCode = getIntent().getStringExtra("county_code");
       if(!TextUtils.isEmpty(countyCode)){
    	   //it have county code then it query weather
    	   publishText.setText("同步中....");
    	   weatherInfoLayout.setVisibility(View.INVISIBLE);
    	   cityNameText.setVisibility(View.INVISIBLE);
    	   queryWeatherCode(countyCode);
       }else{
    	   //it have no county code then show location weather
    	   showWeather();
       }
		
       switchCity.setOnClickListener(this);
       refreshWeather.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中....");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	
		
	}


	//query weather by county code
	private void queryWeatherCode(String countyCode){
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	
	
	
	
	//query weather with weather code
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+ weatherCode+".html";
		
		queryFromServer(address,"weatherCode");
		
	}
	
	
	
	


	private void queryFromServer(final String address,final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
			public void onFinish(final String response){
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//copy with weather code from server
						String []array = response.split("\\|");
						if(array!=null && array.length ==2){
							String weatherCode = array[1];
							Log.i("===========>>>>>>>====weather code =============", weatherCode);
							
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Log.i("===========>>>>>>>====address code =============", address+"------response:"+response);
					//copy with weather info
					 Utility.handleWeatherResponse(WeatherActivity.this, response);
					 runOnUiThread(new Runnable(){
						public void run(){
							showWeather();
						}
					});			
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){
					public void run(){
		             publishText.setText("同步失败");
					}
				});	
			}	
		});	

		
	}
	
	
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText("今天"+prefs.getString("weather_desp", "")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}

}
