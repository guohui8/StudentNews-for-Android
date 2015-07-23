package com.guohui.student;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.guohui.network.DownMobileTask;
import com.guohui.service.Download;
import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import com.guohui.util.Constant;
import com.guohui.util.ShortcutUtil;
import com.guohui.util.UpdateUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class WelComeActivity extends ZYActivity {
	private boolean isShowTuisong;	
	SharedPreferences.Editor localEditor;
	private int currentVersion;	//当前版本
	private int serverVersion;	//服务器版本
	private String remoteVersionName = "";
	private String description = "";
	private String downloadURL = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
		init();		
	}
	
	Handler splashHandler = new Handler(){
    	public void handleMessage(Message message) {
    		switch(message.what) {
    			case 2:
    				WelComeActivity.this.startActivity(new Intent(WelComeActivity.this, MainActivity.class));
    				localEditor.commit();
    				WelComeActivity.this.finish();
    	            System.gc();
    	            break;
    		}
    	}
    };	
	//
	public void init(){
	  localEditor = preferences().edit();
	     // 创建快捷方式
 		if (preferences().getBoolean("createshortcut", true)) {
 			if (ShortcutUtil.hasShortCut(this, R.string.app_name)) {
 				ShortcutUtil.deleteShortCut(this, R.string.app_name);
 			}
 			ShortcutUtil.createShortCut(this, R.drawable.ic_launcher,
 					R.string.app_name);
 			localEditor.putBoolean("createshortcut", false);
 			localEditor.commit();
 		}
 		this.currentVersion = UpdateUtils.getInstance().getAppVersionCode(this);
 		if(isConnectNetWork()){
 			//
 			//Log.e("insert","insert mobile");
 			init_tuisong();
        	new DownMobileTask().execute(Constant.getMobilePhoneInformation(this));
        	new CheckUpdate().execute();
 		} else {
        	this.splashHandler.sendEmptyMessageDelayed(2, 2000L);
        }	
	}
	
	private void init_tuisong(){
		JPushInterface.init(getApplicationContext());
		isShowTuisong = preferences().getBoolean("isShowTuisong", true);		
		//Log.e("isShowTuisong", "22");
		//如果不启用推送就关闭
		if (!isShowTuisong){
			JPushInterface.stopPush(getApplicationContext());
		}else{
			JPushInterface.resumePush(getApplicationContext());
		}		
	}
	class CheckUpdate extends AsyncTask<Void, Void, Integer> {
		private int versionCode = 0;		
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				//1)创建一个URL对象
				URL url = new URL(Constant.UPDATEURL);
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
					try {
						JSONObject localObj	=	new JSONObject(result);
						versionCode	=	localObj.getInt("versionCode");
						remoteVersionName	=	localObj.getString("versionName");
						description			=	localObj.getString("description");
						downloadURL			=	localObj.getString("downloadURL");
						//Log.i("vserInfo:", versionCode + remoteVersionName + description + downloadURL);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return versionCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			//Log.e("VersionCode:", result+"");
			setVersionResult(result);
		}
	}
	
	public void setVersionResult(Integer result) {
		if(result > 0) {
			this.serverVersion = result;
			if(this.serverVersion > this.currentVersion) {
				new AlertDialog.Builder(this)
				.setMessage(remoteVersionName + description)
				.setPositiveButton("更新", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						SharedPreferences.Editor localEditor = preferences().edit();
						localEditor.putString("UpdateMessage", description);
						localEditor.commit();
						Intent i = new Intent(WelComeActivity.this, Download.class);
						i.putExtra("url", downloadURL);
						WelComeActivity.this.startService(i);
						WelComeActivity.this.splashHandler.sendEmptyMessageDelayed(2, 0L);
					}
				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						WelComeActivity.this.splashHandler.sendEmptyMessageDelayed(2, 0L);
					}
				}).setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						WelComeActivity.this.splashHandler.sendEmptyMessageDelayed(2, 0L);
					}
				}).show();
	        } else {
	        	this.splashHandler.sendEmptyMessageDelayed(2, 0L);
	        }
		} else {
			this.splashHandler.sendEmptyMessageDelayed(2, 0L);
		}
	}
	
	@Override
	protected void onDestroy() {
		this.splashHandler.removeMessages(2);
	    super.onDestroy();
	}
}