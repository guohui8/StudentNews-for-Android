package com.guohui.student;

import java.util.ArrayList;
import java.util.List;

import com.guohui.student.R;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
/**
 * 
 */
public class MyseeActivity extends ZYActivity {
	TextView top_main_text;
	List<View> listViews;
	Context context = null;
	LocalActivityManager manager = null;
	TabHost tabHost = null;
	private ViewPager pager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.viewpager);
		
		top_main_text = (TextView) findViewById(R.id.top_main_text);
		top_main_text.setText("文章记录");
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		tabHost.setup(manager);		
		tabHost.setPadding(tabHost.getPaddingLeft(), tabHost.getPaddingTop(), tabHost.getPaddingRight(), tabHost.getPaddingBottom()-1); 
		context = MyseeActivity.this;		
		pager  = (ViewPager) findViewById(R.id.viewpager);		
		listViews = new ArrayList<View>();
		Button btn_refresh = (Button) findViewById(R.id.btn_refresh);
		btn_refresh.setVisibility(4);
		Intent i1 = new Intent(context, Xinwen1Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		listViews.add(getView("Xinwen1Activity", i1));
		Intent i2 = new Intent(context, Xinwen2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		listViews.add(getView("Xinwen2Activity", i2));
		
		RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabwidget, null);  
		TextView tvTab1 = (TextView)tabIndicator1.findViewById(R.id.tv_title);
		tvTab1.setText("最近查看");
		
		RelativeLayout tabIndicator2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabwidget,null);  
		TextView tvTab2 = (TextView)tabIndicator2.findViewById(R.id.tv_title);
		tvTab2.setText("我的收藏");
		
		tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator1).setContent(i1));
		tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator2).setContent(i2));
		//TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		//tabWidget.setStripEnabled(false);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				 tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			            @Override
			            public void onTabChanged(String tabId) {
			                if ("A".equals(tabId)) {
			                    pager.setCurrentItem(0);
			                } 
			                if ("B".equals(tabId)) {
			                    pager.setCurrentItem(1);
			                } 			                
			            }
			        });
				
			}
		});
		

		pager.setAdapter(new MyPageAdapter(listViews));
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				tabHost.setCurrentTab(position);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});	

	}

	private View getView(String id, Intent intent) {
		Log.d("EyeAndroid", "getView() called! id = " + id);
		return manager.startActivity(id, intent).getDecorView();
	}

	private class MyPageAdapter extends PagerAdapter {
		
		private List<View> list;

		private MyPageAdapter(List<View> list) {
			this.list = list;
		}

		@Override
        public void destroyItem(ViewGroup view, int position, Object arg2) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.removeView(list.get(position));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

	}

}
