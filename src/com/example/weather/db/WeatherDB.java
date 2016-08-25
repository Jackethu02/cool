package com.example.weather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.weather.model.City;
import com.example.weather.model.County;
import com.example.weather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class WeatherDB {
	//database name
	public static final String DB_NAME="weather";
	
	//database version
	public static final int VERSION = 1;
	private static WeatherDB weatherDB;
	private SQLiteDatabase db;
	
	//cons
	private WeatherDB(Context context){
		WeatherOpenHelper dbHelper = new WeatherOpenHelper(context, DB_NAME,null,VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	//get WeatherDB instance
	public synchronized static WeatherDB getInstance(Context context){
		if(weatherDB == null){
			weatherDB = new WeatherDB(context);
		}
		return weatherDB;
	}
	
	//store the province instance to db
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);	
		}
	}
	
	//read all province information from db
	public List<Province>loadProvinces(){
		List<Province>list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null,  null, null, null);
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}
		}
		return list;
	}
	
	
	//store the city instance to db
	public void saveCity(City city){
		if(city!=null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);	
		}
	}
	
	
	//read all city from a province in db
	public List<City>loadCities(int provinceId){
		List<City>list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[]{String.valueOf(provinceId)},  null, null, null);
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}
		}
		return list;
	}
	
	
	
	//store the city instance to db
	public void saveCounty(County county){
		if(county!=null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);	
		}
	}
	
	
	//read all county from one city in db
	public List<County>loadCounties(int cityId){
		List<County>list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[]{String.valueOf(cityId)},  null, null, null);
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
		}}
		return list;
	}
	
	
}
