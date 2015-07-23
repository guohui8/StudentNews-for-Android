package com.guohui.util;


import java.util.LinkedList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class MyApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;

	private MyApplication() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	
	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * @param VersionCode
	 * @return
	 */
	@SuppressLint("NewApi")
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}
	
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}