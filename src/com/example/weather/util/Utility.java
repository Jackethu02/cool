package com.example.weather.util;

import android.text.TextUtils;

import com.example.weather.db.WeatherDB;
import com.example.weather.model.City;
import com.example.weather.model.County;
import com.example.weather.model.Province;

public class Utility {
	
	//copy with province data from response in server and save to province table in db
	public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[]allProvinces = response.split(",");
			if(allProvinces!=null &&allProvinces.length>0){
				for(String p:allProvinces){
					String[]array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//the data save to Province table
					weatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;	
	}
	
	
	//copy with city data from response in server
	public  static boolean handleCitiesResponse(WeatherDB weatherDB,
			String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[]allCities = response.split(",");
			if(allCities!=null &&allCities.length>0){
				for(String c:allCities){
					String[]array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//the data save to City table
					weatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;	
	}
	
	
	//copy with county data from response 
	public  static boolean handleCountiesResponse(WeatherDB weatherDB,
			String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[]allCounties = response.split(",");
			if(allCounties!=null &&allCounties.length>0){
				for(String c:allCounties){
					String[]array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//the data save to City table
					weatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;	
	}
}
