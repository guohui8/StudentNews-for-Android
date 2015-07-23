package com.guohui.student;


import android.app.TabActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

public class ZYTabActivity extends TabActivity implements OnSharedPreferenceChangeListener {

	private boolean mPaused = true;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		this.prefs = preferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
	}

	public static SharedPreferences preferences(Context context) {
		return context.getSharedPreferences(context.getPackageName(), 3);
	}

	public SharedPreferences preferences() {
		return this.prefs;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

	public boolean isPaused() {
		return this.mPaused;
	}

	public void onApplyTheme() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mPaused = false;		
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.mPaused = true;
	}
}
