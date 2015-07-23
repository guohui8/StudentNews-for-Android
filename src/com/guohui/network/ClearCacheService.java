package com.guohui.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ClearCacheService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		stopSelf();
	}	
}