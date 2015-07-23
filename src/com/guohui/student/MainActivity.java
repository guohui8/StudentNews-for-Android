package com.guohui.student;

import java.util.Timer;
import java.util.TimerTask;

import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;

public class MainActivity extends ZYTabActivity implements OnClickListener {
	private TabHost tabHost;
	private RelativeLayout[] c = new RelativeLayout[5];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.main);
		this.tabHost = getTabHost();
		setUpTabHost();
	}

	private void setUpTabHost() {
		this.c[0] = ((RelativeLayout) findViewById(R.id.tab_home));
		this.c[1] = ((RelativeLayout) findViewById(R.id.tab_at));
		this.c[2] = ((RelativeLayout) findViewById(R.id.tab_msg));
		this.c[3] = ((RelativeLayout) findViewById(R.id.tab_hall));
		this.c[4] = ((RelativeLayout) findViewById(R.id.tab_jilu));

		RelativeLayout[] arrayOfRelativeLayout = this.c;
		int i1 = arrayOfRelativeLayout.length;
		for (int i = 0; i < i1; i++)
			arrayOfRelativeLayout[i].setOnClickListener(this);
		TabHost.TabSpec localTabSpec1 = this.tabHost.newTabSpec("tab1")
				.setIndicator("tab1");
		localTabSpec1.setContent(new Intent(this, SaishiActivity.class));
		this.tabHost.addTab(localTabSpec1);
		TabHost.TabSpec localTabSpec2 = this.tabHost.newTabSpec("tab2")
				.setIndicator("tab2");
		localTabSpec2.setContent(new Intent(this, XinwenListActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		this.tabHost.addTab(localTabSpec2);
		
		TabHost.TabSpec localTabSpec3 = this.tabHost.newTabSpec("tab3")
				.setIndicator("tab3");
		localTabSpec3.setContent(new Intent(this, NewsListActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		this.tabHost.addTab(localTabSpec3);
		
		TabHost.TabSpec localTabSpec4 = this.tabHost.newTabSpec("tab4")
				.setIndicator("tab4");
		localTabSpec4.setContent(new Intent(this, SystemConfigActivity.class));
		this.tabHost.addTab(localTabSpec4);
		
		TabHost.TabSpec localTabSpec5 = this.tabHost.newTabSpec("tab5")
				.setIndicator("tab5");
		localTabSpec5.setContent(new Intent(this, MyseeActivity.class));
		this.tabHost.addTab(localTabSpec5);
		this.tabHost.getCurrentTabTag();
		this.tabHost.setCurrentTab(0);
		this.c[0].setSelected(true);
	}

	@Override
	public void onClick(View v) {
		int i1 = this.c.length;
		for (int i = 0; i < i1; i++) {
			if (this.c[i] == ((RelativeLayout) findViewById(v.getId()))) {
				this.c[i].setSelected(true);
				this.tabHost.setCurrentTab(i);
			} else {
				this.c[i].setSelected(false);
			}
		}
		switch (v.getId()) {
		case R.id.tab_home:
			this.tabHost.setCurrentTabByTag("tab1");
			break;
		case R.id.tab_at:
			this.tabHost.setCurrentTabByTag("tab2");
			break;
		case R.id.tab_msg:
			this.tabHost.setCurrentTabByTag("tab3");
			break;
		case R.id.tab_hall:
			this.tabHost.setCurrentTabByTag("tab4");
			break;
		case R.id.tab_jilu:
			this.tabHost.setCurrentTabByTag("tab5");
			break;	
		}
	}

	public boolean onPrepareOptionsMenu(Menu paramMenu) {
		paramMenu.clear();
		// 文章记录
		paramMenu.add(0, ConstUtils.ABOUT_ID, 0, R.string.menu_about)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_comment);
		// 图片记录
		paramMenu.add(0, ConstUtils.SETTING_ID, 0, R.string.menu_setting)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_guide);

		return super.onPrepareOptionsMenu(paramMenu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case ConstUtils.ABOUT_ID:
			startActivity(new Intent(this, MyseeActivity.class));
			break;
		case ConstUtils.SETTING_ID:
			startActivity(new Intent(this, ViewPagerActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
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
