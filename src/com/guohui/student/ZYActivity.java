package com.guohui.student;


import java.io.File;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

import com.guohui.util.Constant;
import com.guohui.util.Tools;
import com.newqm.sdkoffer.QuMiConnect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class ZYActivity extends Activity 
		implements SharedPreferences.OnSharedPreferenceChangeListener {
	protected SharedPreferences prefs;
	public int screenheight = 800;
	public int screentype = 3;
	public int screenwidth = 480;
	private boolean isShowTuisong;
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 积分墙
		QuMiConnect.ConnectQuMi(this, "5cf561b322c1b360", "50647eb30855c23b"); // 初始化 不需要重复调用,调用一次即可
	
		this.prefs = preferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);			
		this.screentype = Tools.getScreenMetrics(this);
		HashMap<String, Integer> obj = Tools.getWidth_Height(this);
		this.screenwidth = obj.get("w").intValue();
		this.screenheight = obj.get("h").intValue();
		File f1 = new File(Constant.NewsimgDir);	
		File f2 = new File(Constant.DownloadDir);
		if(!f1.exists()){
			f1.mkdirs();
		}	
		if (!f2.exists()) {
			f2.mkdirs();
		}
		init();
	}
			
	private void init(){
		JPushInterface.init(getApplicationContext());
		isShowTuisong = preferences().getBoolean("isShowTuisong", true);
		//如果不启用推送就关闭
		if (!isShowTuisong){
			JPushInterface.stopPush(getApplicationContext());
		}else{
			JPushInterface.resumePush(getApplicationContext());
		}		
	}
		
	public SharedPreferences preferences() {
		return this.prefs;
	}
	
	public boolean isWIFI() {
		boolean flag;
		if (Tools.isWifi(this)) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	public boolean isConnectNetWork() {
		boolean flag;
		if (Tools.CheckNetwork(this)) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	public static SharedPreferences preferences(Context context) {
		return context.getSharedPreferences(context.getPackageName(), MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
	}
	
	public void setLocationResult(String[] string) {}
		
}