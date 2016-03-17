package com.wching.coolweather.activity;

import com.wching.coolweather.R;
import com.wching.coolweather.util.HttpCallbackListener;
import com.wching.coolweather.util.HttpUtil;
import com.wching.coolweather.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private LinearLayout weatherInfoLayout;
	
	/**用于显示城市名*/
	private TextView cityNameText;
	
	/**用于显示发布时间*/
	private TextView publishText;
	
	/**用于显示天气描述信息*/
	private TextView weatherDespText;
	
	/**用于显示气温1*/
	private TextView temp1Text;
	
	/**用于显示气温2*/
	private TextView temp2Text;
	
	/**用于显示当前时间*/
	private TextView currentDateText;
	
	/**切换城市按钮*/
	private Button switchCity;
	
	/**更新天气按钮*/
	private Button refreshWeather;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather_layout);
		
		initView();
		
		String countryCode = getIntent().getStringExtra("contry_code");
		if(!TextUtils.isEmpty(countryCode)){
			//有县级代号就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}else{
			//没有县级代号时就直接显示本地天气
			showWeather();
		}
	}
	
	private void initView(){
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.title_text);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
	}


	@Override
	public void onClick(View v) {
		
	}
	
	/**
	 * 查询县级代号所对应的天气代号
	 * @param countryCode
	 */
	private void queryWeatherCode(String countryCode){
		String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
		queryFromServer(address,"countryCode");
	}
	
	/**
	 * 查询天气代号所对应的天气
	 * @param weatherCode
	 */
	private void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".xml";
		queryFromServer(address,"weatherCode");
	}
	
	
	/**
	 * 从服务器查询数据
	 * @param address
	 * @param type
	 */
	private void queryFromServer(final String address , final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if("countryCode".endsWith(type)){
					if(!TextUtils.isEmpty(response)){
						//从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".endsWith(type)){
					//处理服务器返回的天气信息
					Utility.handlerWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						publishText.setTag("同步失败...");
					}
				});
			}
		});
	}

	/***
	 * 显示天气信息在界面上
	 */
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}
