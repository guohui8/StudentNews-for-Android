package com.guohui.student;

import java.util.Timer;
import java.util.TimerTask;

import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import com.guohui.util.MyApplication;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
public class AboutActivity extends ZYActivity {
	TextView versionText;
	TextView top_main_text,btn_home;
	Intent intent = new Intent();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		MyApplication.getInstance().addActivity(this);
		findView();		
		updateVersionText();		
	}

	private void findView(){
		//
		versionText = (TextView) findViewById(android.R.id.text1);
		versionText.setClickable(true);
		top_main_text = (TextView) findViewById(R.id.top_main_text);
		top_main_text.setText("关于我们");
		btn_home = (Button) findViewById(R.id.btn_home);		
		btn_home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
	}
	public void updateVersionText() {
		PackageInfo packageinfo;
		try {
			packageinfo = getPackageManager().getPackageInfo(getPackageName(),
					0);
			StringBuilder stringbuilder = new StringBuilder();
			stringbuilder.append("软件版本：").append(packageinfo.versionName);
			versionText.setText(stringbuilder.toString());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Boolean isExit = false;
	private static Boolean hasTask = false;

	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				ConstUtils.showToast(this, R.string.base_again_exit);
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}
}