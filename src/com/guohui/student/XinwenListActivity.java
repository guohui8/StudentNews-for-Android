package com.guohui.student;


import com.guohui.student.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.guohui.db.DBUtils;
import com.guohui.loader.AsyncBitmapLoader;
import com.guohui.model.Xinwen;
import com.guohui.network.DownXinwenListTask;
import com.guohui.util.ConstUtils;
import com.guohui.util.DateUtil;
import com.guohui.util.MyApplication;
import com.guohui.util.Tools;
import com.guohui.widget.PullToRefreshListView;
import com.guohui.widget.PullToRefreshListView.OnRefreshListener;

import android.os.Handler;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

public class XinwenListActivity extends ZYActivity {
	private static final int[] CHANNEL_BTN_ID;
	private static final int[] CHANNEL_BTN_ID_NORMAL;
	private static final int[] CHANNEL_BTN_ID_NEWS;
	private final TextView[] channelBtn = new TextView[CHANNEL_BTN_ID.length];
	Intent intent = new Intent();
	private Button NavigateHome;
	TextView top_main_text;
	private List<Xinwen> contentList;
	private ListAdapter lAdapter;
	private boolean scrollDown = true;
	private boolean needUpdate = true;
	protected HorizontalScrollView columnscrollview;
	private int page = 0;
	public int screenheight = 800;
	public int screentype = 3;
	public int screenwidth = 480;
	private static int curScreenIndex = 0;
	private int channel_id = 0;
	private String fid, cid;
	private View loadItemView;
	AsyncBitmapLoader asyncImageLoader;
	private PullToRefreshListView list_refreshView;
	private boolean isPullToRefresh = false;
	int testPoints;
	Handler splashHandler;
	static {
		int[] arrbtn1 = new int[4];
		arrbtn1[0] = R.id.new1Icon;
		arrbtn1[1] = R.id.new2Icon;
		arrbtn1[2] = R.id.new3Icon;
		arrbtn1[3] = R.id.new4Icon;
		CHANNEL_BTN_ID = arrbtn1;
		int[] arrbtn2 = new int[4];
		arrbtn2[0] = R.string.news_title1;
		arrbtn2[1] = R.string.news_title2;
		arrbtn2[2] = R.string.news_title3;
		arrbtn2[3] = R.string.news_title4;
		CHANNEL_BTN_ID_NORMAL = arrbtn2;
		// 新闻分类指定ID
		int[] arrbtn3 = new int[4];
		arrbtn3[0] = 1;// 世界杯新闻
		arrbtn3[1] = 2;// 赛场内外
		arrbtn3[2] = 3;// 历届世界杯
		arrbtn3[3] = 4;// 球迷观点
		CHANNEL_BTN_ID_NEWS = arrbtn3;
	}

	private void clearButton() {
		for (int i = 0; i < CHANNEL_BTN_ID_NORMAL.length; i++) {
			channelBtn[i].setBackgroundResource(R.drawable.channel_bg);
			channelBtn[i].setEnabled(true);
			channelBtn[i].setSelected(false);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.xinwenlist);
		MyApplication.getInstance().addActivity(this);
		// 实例化各个组件
		initList();
		initChannelButton();
		getUpdateTime(curScreenIndex);
		loadChannelNewsList(1);		
	}

	private void initList() {		
		top_main_text = (TextView) findViewById(R.id.top_main_text);
		top_main_text.setText("新闻中心");
		NavigateHome = (Button) findViewById(R.id.NavigateHome);
		NavigateHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(XinwenListActivity.this, SearchResultActivity.class);
				startActivity(intent);
			}
		});
		// 加载下来刷新
		this.list_refreshView = (PullToRefreshListView) findViewById(R.id.list_refresh);
		this.list_refreshView.setPreferences(preferences());
		this.loadItemView = LayoutInflater.from(this).inflate(
				R.layout.loading_item, null);
		this.list_refreshView
				.setOnScrollListener(new AbsListView.OnScrollListener() {
					public void onScroll(AbsListView paramAbsListView,
							int paramInt1, int paramInt2, int paramInt3) {
						XinwenListActivity.this.list_refreshView
								.setFirstItemIndex(paramInt1);
						if ((XinwenListActivity.this.contentList != null)
								&& (XinwenListActivity.this.contentList.size() != 0)) {
							if ((paramInt1 + paramInt2 == paramInt3)
									&& (paramInt3 >= 10)
									&& (XinwenListActivity.this.scrollDown)) {
								if (isConnectNetWork()) {
									XinwenListActivity.this.scrollDown = false;
									XinwenListActivity.this.needUpdate = false;
									XinwenListActivity.this.isPullToRefresh = false;
									XinwenListActivity localCommentActivity = XinwenListActivity.this;
									localCommentActivity.page = (1 + localCommentActivity.page);
									list_refreshView.addFooterView(loadItemView);
									XinwenListActivity.this.getComment();
								} else {
									XinwenListActivity.this.list_refreshView
											.removeFooterView(loadItemView);
									ConstUtils.showToast(XinwenListActivity.this,
											R.string.Net_Failure);
								}
							}
						}
					}

					public void onScrollStateChanged(
							AbsListView paramAbsListView, int paramInt) {
					}
				});
		// 下拉刷新
		this.list_refreshView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Object localObj = lAdapter.getItem(position - 1);
						if (localObj instanceof Xinwen) {
							Xinwen xw = (Xinwen) localObj;
							Bundle bundle = new Bundle();
							Intent intent = new Intent(XinwenListActivity.this,
									XinwenShowActivity.class);
							bundle.putString("newsId", xw.getId());
							//Log.e("articleid",news.getId());
							bundle.putString("newsTitle", xw.getTitle());
							intent.putExtras(bundle);
							startActivityForResult(intent, 0);
						}
					}
				});

		this.list_refreshView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 请求下拉刷新的数据
				if (isConnectNetWork()) {
					XinwenListActivity.this.page = 0;
					XinwenListActivity.this.isPullToRefresh = true;
					// XinwenListActivity.this.loadHeader.setVisibility(0);
					XinwenListActivity.this.getComment();
				} else {
					list_refreshView.onRefreshComplete();
					ConstUtils.showToast(XinwenListActivity.this,
							R.string.Net_Failure);
				}
			}
		});

		this.lAdapter = new ListAdapter(this);
		this.list_refreshView.addFooterView(this.loadItemView);
		this.list_refreshView.setAdapter(this.lAdapter);
	}

	private void getComment() {
		if (!this.isPullToRefresh)
			this.loadItemView.setVisibility(0);
		DownXinwenListTask localDownXinwenListTask = new DownXinwenListTask(this);
		String[] arrayOfString = new String[5];
		arrayOfString[0] = String.valueOf(channel_id);// 类别
		arrayOfString[1] = String.valueOf(this.needUpdate);
		arrayOfString[2] = "3";
		arrayOfString[3] = "4";
		arrayOfString[4] = String.valueOf(this.page);
		localDownXinwenListTask.execute(arrayOfString);
	}

	public void setComment(List<Xinwen> paramList) {
		this.list_refreshView.removeFooterView(loadItemView);
		if (this.isPullToRefresh) {
			if ((this.contentList != null) && (this.contentList.size() > 0)) {
				this.contentList.clear();// 清空适配器
			}
			this.contentList = paramList;
			this.lAdapter.notifyDataSetChanged();
			// Last Update Time
			SharedPreferences.Editor editor = preferences().edit();
			editor.putString("newsPullTime",
					String.valueOf(System.currentTimeMillis()));
			editor.commit();
			list_refreshView.onRefreshComplete();
		} else {
			this.loadItemView.setVisibility(8);
			if ((paramList != null) && (paramList.size() != 0)) {
				this.scrollDown = true;
				if ((this.contentList == null)
						|| (this.contentList.size() == 0)) {
					this.contentList = paramList;
					setUpdateTime(curScreenIndex);// 更新上次刷新的时间
				} else {
					if (needUpdate) {
						this.contentList.clear();// 清空适配器
						this.contentList = paramList;
						setUpdateTime(curScreenIndex);// 更新上次刷新的时间
					} else {
						this.contentList.addAll(paramList);
					}
				}
				this.lAdapter.notifyDataSetChanged();
			} else {
				this.scrollDown = false;
				this.list_refreshView.removeFooterView(loadItemView);
				ConstUtils.showToast(XinwenListActivity.this,
						R.string.No_morenews);
				return;
			}
		}
	}

	private void initChannelButton() {
		columnscrollview = (HorizontalScrollView) findViewById(R.id.column_scrollview);
		this.screentype = Tools.getScreenMetrics(this);
		HashMap<String, Integer> obj = Tools.getWidth_Height(this);
		this.screenwidth = obj.get("w").intValue();
		this.screenheight = obj.get("h").intValue();
		int channelBtnwith = (screenwidth - columnscrollview.getPaddingLeft() - columnscrollview
				.getPaddingRight()) / 3;
		for (int i = 0; i < channelBtn.length; i++) {
			this.channelBtn[i] = ((TextView) findViewById(CHANNEL_BTN_ID[i]));
			this.channelBtn[i].setWidth(channelBtnwith);
			final int j = i;
			this.channelBtn[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					XinwenListActivity.this.list_refreshView.post(new Runnable() {
						public void run() {
							XinwenListActivity.this.list_refreshView
									.setSelection(0);
						}
					});
					if (contentList != null && contentList.size() > 0) {
						contentList.clear();
						lAdapter.notifyDataSetChanged();
					}
					clearButton();
					channelBtn[j]
							.setBackgroundResource(R.drawable.channel_bg_sel);
					channelBtn[j].setEnabled(false);
					channelBtn[j].setSelected(true);
					XinwenListActivity.this.page = 0;
					XinwenListActivity.this.scrollDown = true;
					// Log.e("channel_id", CHANNEL_BTN_ID_NEWS[j] + "");
					getUpdateTime(j + 1);
					XinwenListActivity.this.isPullToRefresh = false;
					list_refreshView.addFooterView(loadItemView);
					loadChannelNewsList(CHANNEL_BTN_ID_NEWS[j]);
				}
			});
		}
		this.channelBtn[curScreenIndex]
				.setText(CHANNEL_BTN_ID_NORMAL[curScreenIndex]);
		this.channelBtn[curScreenIndex]
				.setBackgroundResource(R.drawable.channel_bg_sel);
		this.channelBtn[curScreenIndex].setEnabled(false);
		this.channelBtn[curScreenIndex].setSelected(true);
	}

	private void loadChannelNewsList(int paramInt) {
		this.channel_id = paramInt;
		this.contentList = getCommentFromDB();
		this.list_refreshView.setVisibility(0);
		if (this.contentList != null) {
			if (this.contentList.size() > 10)
				this.contentList = this.contentList.subList(0, 10);
			this.lAdapter.notifyDataSetChanged();
			// 对比上次的刷新时间如果需要更新
			if (needUpdate) {
				if (this.isConnectNetWork()) {
					getComment();
				} else {
					this.list_refreshView.removeFooterView(loadItemView);
					scrollDown = false;
					ConstUtils.showToast(this, R.string.Net_Failure);
				}
			} else {
				loadItemView.setVisibility(4);
			}
		} else {
			if (Tools.CheckNetwork(this)) {
				getComment();
			} else {
				this.list_refreshView.removeFooterView(loadItemView);
			}
		}
	}

	private void getUpdateTime(int paramInt) {
		Long localLong;
		if (paramInt < ConstUtils.CHANNEL.length) {
			localLong = Long.valueOf(preferences().getLong(
					ConstUtils.CHANNEL[paramInt], 0L));
			if (System.currentTimeMillis() - localLong.longValue() <= 3600000L) {
				this.needUpdate = false;
			} else {
				this.needUpdate = true;
			}
		}
	}

	private void setUpdateTime(int paramInt) {
		try {
			SharedPreferences.Editor localEditor = preferences().edit();
			if (paramInt < ConstUtils.CHANNEL.length) {
				localEditor.putLong(ConstUtils.CHANNEL[paramInt],
						System.currentTimeMillis());
				localEditor.commit();
			}
		} catch (Exception localException) {

		}
	}

	@Override
	public void onDestroy() {
		if (this.splashHandler != null)
			this.splashHandler.removeMessages(0);
		super.onDestroy();
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private List<Xinwen> getCommentFromDB() {
		DBUtils db = new DBUtils(this);
		List<Xinwen> contentListDB = new ArrayList<Xinwen>();
		fid = String.valueOf(channel_id);
		cid = "(" + fid + ")";
		Cursor localCursor = DBUtils.getDataFromXinwenTable22(this, db.database,
				cid);
		while (localCursor.moveToNext()) {
			Xinwen news = new Xinwen();
			news.id = localCursor.getString(localCursor.getColumnIndex("p_id"));
			news.title = localCursor.getString(localCursor
					.getColumnIndex("p_title"));
			news.picurl = localCursor.getString(localCursor
					.getColumnIndex("p_picurl"));
			news.desc = localCursor.getString(localCursor
					.getColumnIndex("p_desc"));
			news.posttime = localCursor.getString(localCursor
					.getColumnIndex("p_date"));
			contentListDB.add(news);
		}
		localCursor.close();
		db.close();
		return (contentListDB.size() == 0) ? null : contentListDB;
	}

	private class ListAdapter extends BaseAdapter {
		Context context;
		private LayoutInflater mInflater;

		public ListAdapter(Context localContext) {
			this.context = localContext;
			this.mInflater = LayoutInflater.from(localContext);
			asyncImageLoader = new AsyncBitmapLoader();
		}

		@Override
		public int getCount() {

			if (contentList == null) {
				return 0;
			}
			return contentList.size();
		}

		@Override
		public Object getItem(int position) {
			if (contentList == null) {
				return null;
			} else {
				return contentList.get(position);
			}
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Object obj;
			if (contentList == null || contentList.size() == 0) {
				obj = new TextView(context);
				((TextView) (obj)).setText("暂无信息");
			} else {
				obj = convertView;
				new ViewHolder();
				ViewHolder viewholder;
				Xinwen xw;
				if (convertView == null) {
					obj = mInflater.inflate(R.layout.xinwen_item, null);
					viewholder = new ViewHolder();
					viewholder.title = (TextView) ((View) (obj))
							.findViewById(R.id.title);
					viewholder.time = (TextView) ((View) (obj))
							.findViewById(R.id.datetime);
					viewholder.desc = (TextView) ((View) (obj))
							.findViewById(R.id.desc);
					((View) (obj)).setTag(viewholder);
				} else {
					viewholder = (ViewHolder) convertView.getTag();
				}
				xw = (Xinwen) contentList.get(position);
				viewholder.title.setText(xw.getTitle());
				viewholder.desc.setText(xw.getDesc());
				viewholder.time.setText(xw.getPosttime());

			}
			return ((View) (obj));
		}
	}

	static class ViewHolder {
		TextView title;
		TextView desc;
		TextView time;
	}

	// 菜单项
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		super.onCreateOptionsMenu(paramMenu);

		// 关于我们
		paramMenu.add(0, ConstUtils.ABOUT_ID, 0, R.string.menu_about)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_about);
		// 更多设置
		paramMenu.add(0, ConstUtils.SETTING_ID, 0, R.string.menu_setting)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_setting);

		// 退出程序
		paramMenu.add(0, ConstUtils.EXIT_ID, 0, R.string.menu_exit)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_quit);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ConstUtils.ABOUT_ID:// 关于我们
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case ConstUtils.SETTING_ID:// 系统设置
			startActivity(new Intent(this, SystemConfigActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
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