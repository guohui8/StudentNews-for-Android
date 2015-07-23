package com.guohui.student;

import java.util.List;
import java.util.Map;
import com.guohui.student.R;
import com.guohui.model.Xinwen;
import com.guohui.network.DownSearchResultTask;
import com.guohui.network.DownSearchTextInfoTask;
import com.guohui.util.ConstUtils;
import com.guohui.util.Tools;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResultActivity extends ZYActivity implements
		OnScrollListener, OnClickListener {
	Intent intent = new Intent();
	private List<Xinwen> contentList;
	private ListAdapter lAdapter;
	protected ListView listView;
	private boolean scrollDown = true;
	private boolean needUpdate = true;
	protected HorizontalScrollView columnscrollview;
	private int page = 0;
	private View loadItemView;
	Handler splashHandler;
	private String searchText, searchKeys;
	AutoCompleteTextView actv;
	ImageButton searchClearBtn, backButton;
	Button searchBtn;
	String[] names;
	ArrayAdapter<String> adapter;
	TextView top_main_text;
	LinearLayout screen;

	private void findView() {
		screen = (LinearLayout) findViewById(R.id.screen);
		searchClearBtn = (ImageButton) findViewById(R.id.searchClearBtn);
		actv = (AutoCompleteTextView) findViewById(R.id.searchText);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		top_main_text = (TextView) findViewById(R.id.top_main_text);
		top_main_text.setText("搜索");
		backButton = (ImageButton) findViewById(R.id.backButton);
		screen.setOnClickListener(this);
		actv.setOnClickListener(this);
		actv.setOnItemSelectedListener(null);
		// 添加按钮事件
		searchClearBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		backButton.setOnClickListener(this);
		this.listView = ((ListView) findViewById(R.id.list));
		this.loadItemView = LayoutInflater.from(this).inflate(
				R.layout.loading_item, null);

		actv.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				searchText = arg0.toString();
				searchClearBtn.setVisibility(0);
				// Log.e("searchKey3",searchKey);
				init();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}
		});
	}

	public void init() {
		DownSearchTextInfoTask searchTask = new DownSearchTextInfoTask(
				SearchResultActivity.this);
		searchTask.execute(searchText);
	}

	public void setSearchList(List<Map<String, String>> paramList) {
		if ((paramList != null) && (paramList.size() != 0)) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < paramList.size(); i++) {
				sb.append(paramList.get(i).get("title"));
				sb.append("/");
			}
			String tempString = sb.toString().substring(0, sb.length() - 1);

			names = tempString.split("/");
			adapter = new ArrayAdapter<String>(getApplicationContext(),
					R.layout.autotextview_item, names);
			actv.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// ListView listview = (ListView) parent;
					// ArrayAdapter<String> adapter22 = (ArrayAdapter<String>)
					// parent.getAdapter();
					TextView textview = (TextView) view;
					searchText = textview.getText().toString();
					searchInit();
				}
			});

		} else {
			// Log.e("null","null");
			// ConstUtils.showToast(CategoryActivity.this,
			// R.string.No_morenews);
			return;
		}
	}

	private void initList() {
		this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScroll(AbsListView paramAbsListView, int paramInt1,
					int paramInt2, int paramInt3) {
				if ((SearchResultActivity.this.contentList != null)
						&& (SearchResultActivity.this.contentList.size() != 0)) {
					if ((paramInt1 + paramInt2 == paramInt3)
							&& (paramInt3 < 100) && (paramInt3 >= 10)
							&& (SearchResultActivity.this.scrollDown)) {
						if (isConnectNetWork()) {
							SearchResultActivity.this.scrollDown = false;
							SearchResultActivity.this.needUpdate = false;
							SearchResultActivity localCommentActivity = SearchResultActivity.this;
							localCommentActivity.page = (1 + localCommentActivity.page);
							listView.addFooterView(loadItemView);
							SearchResultActivity.this.getNewsList();
						} else {
							SearchResultActivity.this.listView
									.removeFooterView(loadItemView);
							ConstUtils.showToast(SearchResultActivity.this,
									R.string.Net_Failure);
						}
					}
				}
			}

			public void onScrollStateChanged(AbsListView paramAbsListView,
					int paramInt) {
			}
		});
		this.listView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Object localObj = lAdapter.getItem(arg2);
						if (localObj instanceof Xinwen) {
							Xinwen news = (Xinwen) localObj;
							Bundle bundle = new Bundle();
							Intent intent = new Intent(SearchResultActivity.this,
									XinwenShowActivity.class);
							bundle.putString("newsId", news.getId());
							//Log.e("articleid",news.getId());
							bundle.putString("newsTitle", news.getTitle());
							intent.putExtras(bundle);
							startActivityForResult(intent, 0);
						}
					}
				});
		this.lAdapter = new ListAdapter(this);
		this.listView.addFooterView(loadItemView);
	}

	private void getNewsList() {
		DownSearchResultTask localDownSearchResultTask = new DownSearchResultTask(
				this);
		String[] arrayOfString = new String[2];
		arrayOfString[0] = String.valueOf(searchText);// 关键词
		arrayOfString[1] = String.valueOf(this.page);// 分页
		localDownSearchResultTask.execute(arrayOfString);
	}

	public void setComment(List<Xinwen> paramList) {
		this.listView.removeFooterView(loadItemView);
		if ((paramList != null) && (paramList.size() != 0)) {
			this.scrollDown = true;
			if ((this.contentList == null) || (this.contentList.size() == 0)) {
				this.contentList = paramList;
			} else {
				if (needUpdate) {
					this.contentList.clear();// 清空适配器
					this.contentList = paramList;
				} else {
					this.contentList.addAll(paramList);
				}
				this.lAdapter.notifyDataSetChanged();
			}
		} else {
			this.listView.removeFooterView(loadItemView);
			this.scrollDown = false;
			ConstUtils.showToast(SearchResultActivity.this,
					R.string.No_morenews);
			return;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.searchresult);
		findView();
		initList();
		searchKeys = actv.getText().toString();
		if (!searchKeys.equals("")) {
			searchText = searchKeys;
			searchText = getSearchText(searchText);
			initShow();
		} 		
	}

	public void initShow() {
		this.listView.setAdapter(this.lAdapter);
		loadChannelNewsList(searchText);
	}

	private void loadChannelNewsList(String paramString) {
		this.searchText = paramString;
		if (this.contentList != null) {
			if (this.contentList.size() > 10)
				this.contentList = this.contentList.subList(0, 10);
			this.lAdapter.notifyDataSetChanged();
		}
		if (Tools.CheckNetwork(this)) {
			getNewsList();
		} else {
			this.listView.removeFooterView(loadItemView);
			scrollDown = false;
			ConstUtils.showToast(this, R.string.Net_Failure);
		}
	}

	private class ListAdapter extends BaseAdapter {
		Context context;
		private LayoutInflater mInflater;

		public ListAdapter(Context localContext) {
			this.context = localContext;
			this.mInflater = LayoutInflater.from(localContext);
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
					viewholder.time = (TextView) ((View) (obj))
							.findViewById(R.id.datetime);
					viewholder.id = (TextView) ((View) (obj))
							.findViewById(R.id.tx_id);
					viewholder.title = (TextView) ((View) (obj))
							.findViewById(R.id.title);	
					viewholder.desc = (TextView) ((View) (obj))
							.findViewById(R.id.desc);
					((View) (obj)).setTag(viewholder);
				} else {
					viewholder = (ViewHolder) convertView.getTag();
				}
				xw = (Xinwen) contentList.get(position);
				viewholder.id.setText(xw.getId());
				viewholder.title.setText(xw.getTitle());
				viewholder.time.setText(xw.getPosttime());
				viewholder.desc.setText(xw.getDesc());
			}
			return ((View) (obj));
		}
	}

	static class ViewHolder {
		TextView id;
		TextView title;
		TextView time;
		TextView desc;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	// 按钮点击事件
	@Override
	public void onClick(View v) {
		InputMethodManager imm;
		switch (v.getId()) {
		case R.id.searchClearBtn:
			// 清空文本输入
			actv.setText("");
			break;
		case R.id.searchBtn:
			// 搜索按钮
			searchInit();
			break;
		case R.id.backButton:
			// 返回按钮
			SearchResultActivity.this.finish();
			break;
		case R.id.screen:
			imm = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			actv.setCursorVisible(false);// 失去光标
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.searchText:
			imm = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			actv.setCursorVisible(true);// 有光标
			break;
		}
	}

	private String getSearchText(String mString){
		mString = mString.replaceAll("\'", "\"");
		mString = mString.replaceAll("\"", "tt");
		mString = mString.replaceAll(" ", "%20");
		return mString;
	}
	
	private void searchInit() {
		searchText = getSearchText(actv.getText().toString());		
		page = 0;
		if (searchText.equals("")) {
			ConstUtils.showToast(SearchResultActivity.this, R.string.nokeys);
		} else {
			if (this.contentList != null) {
				this.contentList.clear();
			}
			initList();
			initShow();
		}
	}
}