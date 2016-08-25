package com.example.weather.activity;

import java.util.ArrayList;
import java.util.List;


import com.example.weather.R;
import com.example.weather.db.WeatherDB;
import com.example.weather.model.City;
import com.example.weather.model.County;
import com.example.weather.model.Province;
import com.example.weather.util.HttpCallbackListener;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.Utility;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	public static final int LEVEL_PROVINCE = 0 ;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String>adapter;
	private WeatherDB weatherDB;
	private List<String>dataList = new ArrayList<String>();
	
	//province list
	private List<Province>provinceList;
	//city list
	private List<City>cityList;
	//county list
	private List<County>countyList;
	
	//selected province
	private Province selectedProvince;
	//selected city
	private City selectedCity;
	//selected level
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView)findViewById(R.id.list_view);
		titleText= (TextView)findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		weatherDB = WeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
		          if(currentLevel == LEVEL_PROVINCE){
		        	  selectedProvince = provinceList.get(index);
		        	  queryCities();
		          }else if(currentLevel == LEVEL_CITY){
		        	  selectedCity = cityList.get(index);
		        	  queryCounties();
		          }	
			}
		});
		queryProvinces();
	}
	
	//query province from db first then query from server
	private void queryProvinces(){
		provinceList =weatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province:provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
	}
	
	
	//query city from db first then query from server
	private void queryCities(){
		cityList =weatherDB.loadCities(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	
	
	//query county from db first then query from server
	private void queryCounties(){
		countyList =weatherDB.loadCounties(selectedCity.getId());
		if(countyList.size()>0){
			dataList.clear();
			for(County county:countyList){
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		}else{
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}
	
	//
	private void queryFromServer(final String code, final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
			public void onFinish(String response){
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvincesResponse(weatherDB, response);
				}else if("city".equals(type)){
					result = Utility.handleCitiesResponse(weatherDB, response, selectedProvince.getId());
				}else if("county".equals(type)){
					result = Utility.handleCountiesResponse(weatherDB, response, selectedCity.getId());
				}
				
				if(result){
					runOnUiThread(new Runnable(){
						public void run(){
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
					});			
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){
					public void run(){
					closeProgressDialog();
					Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show(); 
					}
				});	
			}	
		});	
	}

	private void showProgressDialog() {
	  if(progressDialog==null){
		  progressDialog = new ProgressDialog(this);
		  progressDialog.setMessage("正在加载.......");
		  progressDialog.setCanceledOnTouchOutside(false);
	  }
		progressDialog.show();
	}
	

	private void closeProgressDialog() {
		if(progressDialog!=null){
			progressDialog.dismiss();
		}	
	}
	
	public void onBackPressed(){
		if(currentLevel == LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
	
	

}


