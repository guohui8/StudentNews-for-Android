package com.guohui.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

import com.guohui.service.AppUpdateService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

public class UpdateUtils {

	private Activity activity;
	private ProgressDialog dialog;
	private static UpdateUtils instance;
	
	
	public static UpdateUtils getInstance() {
		if (instance == null) {
			instance	=	new UpdateUtils();
		}
		return instance;
	}
	
	/**
	 * 检测更新
	 */
	public void checkUpdate(Activity paramActivity) {
		if (paramActivity == null) return;
		
		this.activity	=	paramActivity;
		if (ConstUtils.CheckNetwork(this.activity)) {
			//开启线程去检测更新
			new CheckUpdate().execute();
		} else {
			//check network!
			new AlertDialog.Builder(this.activity)
			.setMessage("网络不通,请检查网络设置！")
			.setPositiveButton("网络设置",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							activity.startActivityForResult(
									new Intent(
											android.provider.Settings.ACTION_WIRELESS_SETTINGS),
									0);
						}

					})
			.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					}).show();
		}
	}
	
	/**
	 * 获取当前应用的版本号
	 * @return int versioncode
	 */
	public int getAppVersionCode(Context paramContext) {
		int versionCode = 0;
		try {
			PackageManager pm = paramContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(paramContext.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * 定义状态
	 * @author Administrator
	 */
	public enum UpdateStatus {
		WRONG, NONE, HAVE
	}
	
	private class CheckUpdate extends AsyncTask<Void, Void, UpdateStatus> {

		// 版本号
		private String remoteVersionName = "";
		// 下载链接
		private String downloadURL = "";
		// 更新信息
		private String description = "";
		
		
		@Override
		protected UpdateStatus doInBackground(Void... arg0) {
			try {
				//1)创建一个URL对象
				URL url = new URL(Constant.UPDATEURL);
				//Log.e("url",Constant.UPDATEURL);
				//利用HttpURLConnection对象从网络中获取网页数据
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				//设置连接超时
				connection.setConnectTimeout(5 * 1000);
				connection.setReadTimeout(5 * 1000);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream is = connection.getInputStream();
					byte[] data		=	new	byte[1024];
					int length		=	is.read(data);
					String result	=	new String(data, 0, length);
					return _parseUpdateJson(result);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return UpdateStatus.WRONG;
		}
		
		
		private UpdateStatus _parseUpdateJson(String result) {
			int remoteVersionCode = 0;
			try {
				JSONObject localObj	=	new JSONObject(result);
				remoteVersionCode	=	localObj.getInt("versionCode");
				remoteVersionName	=	localObj.getString("versionName");
				description			=	localObj.getString("description");
				downloadURL			=	localObj.getString("downloadURL");
			} catch (Exception e) {
				e.printStackTrace();
				return UpdateStatus.WRONG;
			}
			int currentVersionCode = getAppVersionCode(activity);
			if (currentVersionCode <= 0 || remoteVersionCode <= 0)
				return UpdateStatus.NONE;
			if (remoteVersionCode > currentVersionCode) {
				return UpdateStatus.HAVE;
			} else {
				return UpdateStatus.NONE;
			}
		}
		
		@Override
		protected void onPreExecute() {
				dialog = ProgressDialog.show(activity, "", "正在检查最新版本···", true);
			super.onPreExecute();
		}
		
		
		@Override
		protected void onPostExecute(UpdateStatus result) {
				dialog.dismiss();
			switch (result) {
			case NONE:
					new AlertDialog.Builder(activity)
							.setMessage("您所使用的版本为最新版本,不需要更新!")
							.setPositiveButton("确定", null).show();
				break;
			case HAVE:
				new AlertDialog.Builder(activity)
						.setMessage(remoteVersionName + description)
						.setPositiveButton("更新", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(activity, AppUpdateService.class);
								i.putExtra("url", downloadURL);
								activity.startService(i);
							}
						}).setNegativeButton("取消", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
									dialog.dismiss();
							}
						}).setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								dialog.dismiss();
							}
						}).show();
				break;
			case WRONG:
					ConstUtils.showToast(activity, "网络连接错误,稍候再试", 0);
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			dialog.dismiss();
			super.onCancelled();
		}
	}
}