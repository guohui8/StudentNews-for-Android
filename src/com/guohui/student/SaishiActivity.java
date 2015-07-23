package com.guohui.student;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.guohui.util.ConstUtils;
import com.guohui.util.DataString;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SaishiActivity extends ZYActivity implements
ExpandableListView.OnChildClickListener,
ExpandableListView.OnGroupClickListener{
	private ExpandableListView expandableListView;
	private ArrayList<String> groupList;
	private ArrayList<List<String>> childList;
	TextView top_main_text,txt_date;
	private MyexpandableListAdapter adapter;
	Intent intent = new Intent();
	Bundle bundle = new Bundle();
	private Button NavigateHome;
	String[] names;
	ArrayAdapter<String> adapter_ss;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saishi);
		findview();
	}
	public void findview(){	
		NavigateHome = (Button) findViewById(R.id.NavigateHome);
		NavigateHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(SaishiActivity.this, SearchResultActivity.class);
				startActivity(intent);
			}
		});
		top_main_text = (TextView) findViewById(R.id.top_main_text);
		top_main_text.setText("赛程安排");
		txt_date = (TextView) findViewById(R.id.txt_date);
		txt_date.setText("当前时间："+DataString.StringData());
		expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);
		InitData();
		adapter = new MyexpandableListAdapter(this);
		expandableListView.setAdapter(adapter);
		// 去掉系统自带的分隔线
		expandableListView.setDivider(null);
		// 展开所有二级列表
		final int groupCount = adapter.getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			expandableListView.expandGroup(i);
		}
		adapter.notifyDataSetChanged();
		// 只展开一个group,点第一个gorup后，再点第二个group时，第一个会自动收缩　
		expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				for (int i = 0; i < groupCount; i++) {
					if (groupPosition != i) {
						expandableListView.collapseGroup(i);
					}
				}
			}
		});
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnGroupClickListener(this);
	}
	/***
	 * InitData
	 */
	void InitData() {
		groupList = new ArrayList<String>();
		groupList.add("A组");
		groupList.add("B组");
		groupList.add("C组");
		groupList.add("D组");
		groupList.add("E组");
		groupList.add("F组");
		groupList.add("G组");
		childList = new ArrayList<List<String>>();
		for (int i = 0; i < groupList.size(); i++) {
			ArrayList<String> childTemp;
			if (i == 0) {//A
				childTemp = new ArrayList<String>();
				childTemp.add("06月13日 星期五 03:00  西班牙 VS 荷兰  B1-B2	萨尔瓦多");
				childTemp.add("06月14日 星期六 06:00  智利 VS 澳大利亚	B3-B4	库亚巴");
				childTemp.add("06月18日 星期三 03:00  西班牙 VS 智利	B1-B3	里约热内卢");
				childTemp.add("06月19日 星期四 00:00  澳大利亚 VS 荷兰	B4-B2	阿雷格里港");
				childTemp.add("06月24日 星期二 00:00  澳大利亚 VS 西班牙	B4-B1	库里奇巴");
				childTemp.add("06月24日 星期二 00:00  荷兰 VS 智利	B2-B3	圣保罗");
			} else if (i == 1) {//B
				childTemp = new ArrayList<String>();
				childTemp.add("06月13日 星期五 03:00  西班牙 VS 荷兰  B1-B2	萨尔瓦多");
				childTemp.add("06月14日 星期六 06:00  智利 VS 澳大利亚	B3-B4	库亚巴");
				childTemp.add("06月18日 星期三 03:00  西班牙 VS 智利	B1-B3	里约热内卢");
				childTemp.add("06月19日 星期四 00:00  澳大利亚 VS 荷兰	B4-B2	阿雷格里港");
				childTemp.add("06月24日 星期二 00:00  澳大利亚 VS 西班牙	B4-B1	库里奇巴");
				childTemp.add("06月24日 星期二 00:00  荷兰 VS 智利	B2-B3	圣保罗");
			}else if (i == 2) {//C
				childTemp = new ArrayList<String>();
				childTemp.add("06月15日 星期日	00:00	哥伦比亚 VS 希腊	C1-C2	贝洛奥里藏特");
				childTemp.add("06月15日 星期日	09:00	科特迪瓦 VS 日本	C3-C4	累西腓");
				childTemp.add("06月20日 星期五	00:00	哥伦比亚 VS 科特迪瓦	C1-C3	巴西利亚");
				childTemp.add("06月20日 星期五	06:00	日本 VS 希腊	C4-C2	纳塔尔");
				childTemp.add("06月25日 星期三	04:00	日本 VS 哥伦比亚	C4-C1	库亚巴");
				childTemp.add("06月25日 星期三	04:00	希腊 VS 科特迪瓦	C2-C3	福塔莱萨");
			}else if (i == 3) {//D
				childTemp = new ArrayList<String>();
				childTemp.add("06月15日 星期日	03:00	乌拉圭 VS 哥斯达黎加	D1-D2	福塔莱萨");
				childTemp.add("06月15日 星期日	06:00	英格兰 VS 意大利	D3-D4	马瑙斯");
				childTemp.add("06月20日 星期五	03:00	乌拉圭 VS 英格兰	D1-D3	圣保罗");
				childTemp.add("06月21日 星期六	00:00	意大利 VS 哥斯达黎加	D4-D2	累西腓");
				childTemp.add("06月25日 星期三	00:00	意大利 VS 乌拉圭	D4-D1	纳塔尔");
				childTemp.add("06月25日 星期三	00:00	哥斯达黎加 VS 英格兰	D2-D3	贝洛奥里藏特");
		
			}else if (i == 4) {//E
				childTemp = new ArrayList<String>();
				childTemp.add("06月16日 星期一	00:00	瑞士 VS 厄瓜多尔	E1-E2	巴西利亚");
				childTemp.add("06月16日 星期一	03:00	法国 VS 洪都拉斯	E3-E4	阿雷格里港");
				childTemp.add("06月21日 星期六	03:00	瑞士 VS 法国	E1-E3	萨尔瓦多");
				childTemp.add("06月21日 星期六	06:00	洪都拉斯 VS 厄瓜多尔	E4-E2	库里奇巴");
				childTemp.add("06月26日 星期四	04:00	洪都拉斯 VS 瑞士	E4-E1	马瑙斯");
				childTemp.add("06月26日 星期四	04:00	厄瓜多尔 VS 法国	E2-E3	里约热内卢");
			}else if (i == 5) {//F
				childTemp = new ArrayList<String>();
				childTemp.add("06月16日 星期一	06:00	阿根廷 VS 波黑	F1-F2	里约热内卢");
				childTemp.add("06月17日 星期二	03:00	伊朗 VS 尼日利亚	F3-F4	库里奇巴");
				childTemp.add("06月22日 星期日	00:00	阿根廷 VS 伊朗	F1-F3	贝洛奥里藏特");
				childTemp.add("06月22日 星期日	06:00	尼日利亚 VS 波黑	F4-F2	库亚巴");
				childTemp.add("06月26日 星期四	00:00	尼日利亚 VS 阿根廷	F4-F1	阿雷格里港");
				childTemp.add("06月26日 星期四	00:00	波黑 VS 伊朗	F2-F3	萨尔瓦多");
			}else if (i == 6) {//G
				childTemp = new ArrayList<String>();
				childTemp.add("06月17日 星期二	00:00	德国 VS 葡萄牙	G1-G2	萨尔瓦多");
				childTemp.add("06月17日 星期二	06:00	加纳 VS 美国	G3-G4	纳塔尔");
				childTemp.add("06月22日 星期日	03:00	德国 VS 加纳	G1-G3	福塔莱萨");
				childTemp.add("06月23日 星期一	06:00	美国 VS 葡萄牙	G4-G2	马瑙斯");
				childTemp.add("06月27日 星期五	00:00	美国 VS 德国	G4-G1	累西腓");
				childTemp.add("06月27日 星期五	00:00	葡萄牙 VS 加纳	G2-G3	巴西利亚");
			}else {//H
				childTemp = new ArrayList<String>();
				childTemp.add("06月18日 星期三	00:00	比利时 VS 阿尔及利亚	H1-H2	贝洛奥里藏特");
				childTemp.add("06月18日 星期三	06:00	俄罗斯 VS 韩国	H3-H4	库亚巴");
				childTemp.add("06月23日 星期一	00:00	比利时 VS 俄罗斯	H1-H3	里约热内卢");
				childTemp.add("06月23日 星期一	03:00	韩国 VS 阿尔及利亚	H4-H2	阿雷格里港");
				childTemp.add("06月27日 星期五	04:00	韩国 VS 比利时	H4-H1	圣保罗");
				childTemp.add("06月27日 星期五	04:00	阿尔及利亚 VS 俄罗斯	H2-H3	库里奇巴");
			}
			childList.add(childTemp);
		}
	}
	
	/***
	 * 数据源
	 * 
	 * @author Administrator
	 * 
	 */
	class MyexpandableListAdapter extends BaseExpandableListAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyexpandableListAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		// 返回父列表个数
		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		// 返回子列表个数
		@Override
		public int getChildrenCount(int groupPosition) {
			return childList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {

			return groupList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {

			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder groupHolder = null;
			if (convertView == null) {
				groupHolder = new GroupHolder();
				convertView = inflater.inflate(R.layout.fenlei_group, null);
				groupHolder.textView = (TextView) convertView
						.findViewById(R.id.group);
				groupHolder.imageView = (ImageView) convertView
						.findViewById(R.id.image);
				groupHolder.textView.setTextSize(16);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}

			groupHolder.textView.setText(getGroup(groupPosition).toString());
			if (isExpanded)// ture is Expanded or false is not isExpanded
				groupHolder.imageView.setImageResource(R.drawable.expanded);
			else
				groupHolder.imageView.setImageResource(R.drawable.collapse);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.fenlei_child, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.item);
			textView.setTextSize(15);
			textView.setText(getChild(groupPosition, childPosition).toString());
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public boolean onGroupClick(final ExpandableListView parent, final View v,
			int groupPosition, final long id) {

		return false;
	}
	

	class GroupHolder {
		TextView textView;
		ImageView imageView;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		return false;
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
