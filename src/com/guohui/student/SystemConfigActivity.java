package com.guohui.student;


import java.io.File;

import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import com.guohui.util.FileUtil;
import com.guohui.util.MyApplication;
import com.guohui.util.UpdateUtils;
import com.guohui.widget.ZYBasicItem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CacheManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SystemConfigActivity extends ZYActivity implements
		View.OnClickListener {
	static boolean FACTORY_RESET_WHEN_RESUME = false;
	CheckBox cb, cb2,cb3;
	Intent intent = new Intent();
	private ProgressDialog dialog;
	TextView top_main_text;
	private Button NavigateHome;
	private TextView cacheSize;
	private Handler myHandler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case 1:
				cacheSize.setText("当前缓存大小：" + (CharSequence) message.obj);
				cacheSize.setTextSize(12);
				break;
			case 2:
				dialog.dismiss();
				cacheSize.setText("当前缓存大小：0k");
				ConstUtils.showToast(SystemConfigActivity.this, "缓存清理完毕", 0);
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sysconfig);
		MyApplication.getInstance().addActivity(this);
		setupView();
		findView();
	}

	private void setupView() {
		((ZYBasicItem) findViewById(R.id.more_push)).setOnClickListener(this);
		ZYBasicItem localDPBasicItem1 = (ZYBasicItem) findViewById(R.id.more_push);
		NavigateHome = (Button) findViewById(R.id.NavigateHome);
		NavigateHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(SystemConfigActivity.this, SearchResultActivity.class);
				startActivity(intent);
			}
		});
		TextView localTextView = (TextView) localDPBasicItem1
				.findViewById(R.id.itemCount);
		String localVersion = "";
		// 获取当前版本信息
		try {
			localVersion = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		localTextView.setText("当前版本：" + localVersion);
		localTextView.setTextSize(12);		
		// 是否启用推送服务
		ZYBasicItem localDPBasicItem3 = (ZYBasicItem) findViewById(R.id.more_show_tuisong);
		localDPBasicItem3.setClickable(true);
		localDPBasicItem3.setOnClickListener(this);
		boolean flag3 = preferences().getBoolean("isShowTuisong", true);
		this.cb3 = (CheckBox) (localDPBasicItem3.findViewById(R.id.itemCheckBox));
		this.cb3.setChecked(flag3);
		this.cb3.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton compoundbutton,
					boolean flag3) {
				android.content.SharedPreferences.Editor editor = preferences()
						.edit();
				editor.putBoolean("isShowTuisong", flag3);
				editor.commit();
			}
		});
		((ZYBasicItem) findViewById(R.id.more_fuwu)).setOnClickListener(this);		
		
		// 清除缓存
		ZYBasicItem localClearCache = (ZYBasicItem) findViewById(R.id.more_clear);
		this.cacheSize = (TextView) localClearCache
				.findViewById(R.id.itemCount);
		UpdateCacheSize();
		localClearCache.setOnClickListener(this);
		//分享
		((ZYBasicItem) findViewById(R.id.more_share)).setOnClickListener(this);
		//关于世界杯
		((ZYBasicItem) findViewById(R.id.more_about)).setOnClickListener(this);
		//巴西简介
		((ZYBasicItem) findViewById(R.id.more_jianjie)).setOnClickListener(this);
		//巴西交通
		((ZYBasicItem) findViewById(R.id.more_jiaotong)).setOnClickListener(this);
		//购物攻略
		((ZYBasicItem) findViewById(R.id.more_shop)).setOnClickListener(this);
		//巴西美食		
		((ZYBasicItem) findViewById(R.id.more_food)).setOnClickListener(this);
		//巴西旅游
		((ZYBasicItem) findViewById(R.id.more_tour)).setOnClickListener(this);
		
	}

	public void UpdateCacheSize() {
		new Thread() {
			public void run() {
				File cacheFile = new File(FileUtil.CACHEPATH);
				File cacheFile2 = new File(FileUtil.CACHEPATH2);
				try {
					long filesize = FileUtil.getFileSize(cacheFile);
					long filesize2 = FileUtil.getFileSize(cacheFile2);
					long filesize_total=filesize+filesize2;
					String filelength = FileUtil
							.getSoftwareSize(filesize_total);
					Message msg = myHandler.obtainMessage(1, filelength);
					myHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onClick(View view) {
		// 自动更新
		if (view.getId() == R.id.more_push) {
			UpdateUtils.getInstance().checkUpdate(this);
		}
		//分享
		if (view.getId() == R.id.more_share) {
			Intent intent = new Intent("android.intent.action.SEND");
			intent.setType("text/plain");
			intent.putExtra("android.intent.extra.SUBJECT", "您的朋友为您推荐一款手机软件");
			intent.putExtra("android.intent.extra.TEXT",
					"我正在使用世界杯2014Android客户端，看看网友的点评和推荐，你也试试哈：http://www.guohui1999.com/apk/sport.apk");
			startActivity(Intent.createChooser(intent, "告诉朋友"));
		}
		
		//是否启用推送 
		if (view.getId() == R.id.more_show_tuisong) {
			this.cb3.toggle();
		}
		//购买程序
		if (view.getId() == R.id.more_fuwu) {
			Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse("http://www.guohui1999.com"));
			startActivity(viewIntent);
		}
		//关于世界杯
		if (view.getId() == R.id.more_about) {
			intent.setClass(SystemConfigActivity.this, AboutActivity.class);
			startActivity(intent);
		}
		//巴西简介
		if (view.getId() == R.id.more_jianjie) {
			intent.setClass(SystemConfigActivity.this, JianjieActivity.class);
			startActivity(intent);
		}
		//交通介绍
		if (view.getId() == R.id.more_jiaotong) {
			intent.setClass(SystemConfigActivity.this, TrafficActivity.class);
			startActivity(intent);
		}
		//购物攻略
		if (view.getId() == R.id.more_shop) {
			intent.setClass(SystemConfigActivity.this, ShoppingActivity.class);
			startActivity(intent);
		}
		//巴西美食
		if (view.getId() == R.id.more_food) {
			intent.setClass(SystemConfigActivity.this, FoodActivity.class);
			startActivity(intent);
		}
		//旅游简介
		if (view.getId() == R.id.more_tour) {
			intent.setClass(SystemConfigActivity.this, TourActivity.class);
			startActivity(intent);
		}
		//清理缓存
		if (view.getId() == R.id.more_clear) {
			this.dialog = ProgressDialog.show(this, "", "缓存清理中,请稍候···", true);
			new Thread() {
				@Override
				public void run() {
					File file = CacheManager.getCacheFileBaseDir();
					if (file != null && file.exists() && file.isDirectory()) {
						for (File item : file.listFiles()) {
							item.delete();
						}
						file.delete();
					}
					File cacheFile = new File(FileUtil.CACHEPATH);					
					FileUtil.deleteFile(cacheFile);
					File cacheFile2 = new File(FileUtil.CACHEPATH2);
					FileUtil.deleteFile(cacheFile2);
				   // SystemConfigActivity.this.deleteDatabase("sport.db");
					Message msg = myHandler.obtainMessage(2);
					myHandler.sendMessage(msg);
				}
			}.start();
		}
		
	}

	protected void onResume() {
		super.onResume();
	}

	// 加载菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// 返回首页
		menu.add(0, ConstUtils.BACK_INDEX, 0, R.string.menu_index)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_default);
		
		return true;
	}

	// 处理菜单操作
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 返回首页
		case ConstUtils.BACK_INDEX:
			intent.setClass(SystemConfigActivity.this, MainActivity.class);
			startActivity(intent);
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}

	private void findView() {
		top_main_text = (TextView) findViewById(R.id.top_main_text);
		top_main_text.setText("关于我们");
	}

	// 屏蔽返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			intent.setClass(SystemConfigActivity.this, MainActivity.class);
			startActivity(intent);
			SystemConfigActivity.this.finish();
			return true;
		case KeyEvent.KEYCODE_CALL:
			return true;
		case KeyEvent.KEYCODE_SYM:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_STAR:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}