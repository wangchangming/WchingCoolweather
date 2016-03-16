package com.wching.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.wching.coolweather.model.City;
import com.wching.coolweather.model.Country;
import com.wching.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	
	private static final String DATABASE_NAME = "cool_weather.db";
	
	private static final int DATEBASE_VERSION = 1;
	
	public static final String TABLE_PROVINCE = "Province";
	
	public static final String TABLE_CITY = "City";
	
	public static final String TABLE_COUNTRY = "Country";
	
	
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DATABASE_NAME, null, DATEBASE_VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * 将Province实例存储到数据库
	 * @param province
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert(TABLE_PROVINCE, null, values);
		}
	}
	
	/**
	 * 读取全国所有省份信息
	 * @return
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query(TABLE_PROVINCE, null, null, null, null, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				do {
					Province province = new Province();
					province.setId(cursor.getInt(cursor.getColumnIndex("id")));
					province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
					province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
					list.add(province);
				} while (cursor.moveToNext());
			}
		}
		return list;
	}

	/**
	 * 将City实例存储到数据库
	 * @param city
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode()); 
			values.put("province_id", city.getProvinceId());
			db.insert(TABLE_CITY, null, values);
		}
	}
	
	/**
	 * 读取某省份的所有城市信息
	 * @param provinceId
	 * @return
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query(TABLE_CITY, null, "province_id = ?", new String[]{provinceId+""}, null, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				do {
					City city = new City();
					city.setId(cursor.getInt(cursor.getColumnIndex("id")));
					city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
					city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
					city.setProvinceId(provinceId);
					list.add(city);
				} while (cursor.moveToNext());
			}
		}
		return list;
	}

	/**
	 * 将Country实例存储到数据库
	 * @param city
	 */
	public void saveCountry(Country country){
		if(country != null){
			ContentValues values = new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert(TABLE_COUNTRY, null, values);
		}
	}
	
	/**
	 * 读取某城市下所有的县城信息
	 * @param cityId
	 * @return
	 */
	public List<Country> loadCountries(int cityId){
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query(TABLE_COUNTRY, null, "city_id = ?", new String[]{cityId+""}, null, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				do {
					Country country = new Country();
					country.setId(cursor.getInt(cursor.getColumnIndex("id")));
					country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
					country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
					country.setCityId(cityId);
					list.add(country);
				} while (cursor.moveToNext());
			}
		}
		return list;
	}
}
