package com.wching.coolweather.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	/**
	​ * Province表建表语句
	​ */
	public static final String CREATE_PROVINCE = "create table "+CoolWeatherDB.TABLE_PROVINCE+" ("
			+ "id integer primary key autoincrement, " + "province_name text, "
			+ "province_code text)";
	
	/**
	 * City建表语句
	 */
	public static final String CREATE_CITY = "create table "+CoolWeatherDB.TABLE_CITY+" ("
			+ "id integer primary key autoincrement," + "city_name text, "
			+ "city_code text,"
			+ "province_id integer)";
	
	/**
	 * Country表建表语句
	 */
	public static final String CREATE_COUNTRY = "create table "+CoolWeatherDB.TABLE_COUNTRY+" ("
			+ "id integer primary key autoincrement," + "country_name text, "
			+ "country_code text, "
			+ "city_id integer)";

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
