package com.guohui.student;

import com.guohui.db.DBUtils;
import com.guohui.db.NewsSQLiteOpenHelper;
import com.guohui.student.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Xinwen1Activity extends Activity{
	TextView  empty_txt;
	private ListView list;
	private Cursor myCursor;
	Intent intent = new Intent();
	SimpleCursorAdapter adpater;
	LinearLayout loading_empty;
	DBUtils db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.sub);
		initView();		
	}
	//
	private void initView() {
		list = (ListView) findViewById(R.id.listView);
		loading_empty = (LinearLayout) findViewById(R.id.loading_empty);
		empty_txt = (TextView) findViewById(R.id.textView1);		
		showHistory();
	}
	

	// 显示历史记录列表
	private void showHistory() {		 
		 db = new DBUtils(this);
	     myCursor=db.select_history("3");
	     Integer num=myCursor.getCount();//获取数量	     
	     if(num.equals(0)){
	    	 loading_empty.setVisibility(View.VISIBLE);
	    	 empty_txt.setText("暂无浏览记录");
	    	 list.setVisibility(View.GONE);
	     }else{
	    	 list.setVisibility(View.VISIBLE);
	    	 loading_empty.setVisibility(View.GONE); 
	      adpater=new SimpleCursorAdapter(this
	        		, R.layout.history_item, myCursor,
	        		new String[]{NewsSQLiteOpenHelper.COLUMN_NEWSID,NewsSQLiteOpenHelper.COLUMN_TITLE},
	        		new int[]{R.id.id,R.id.title});
	       list.setAdapter(adpater);
	       //点击项
	       list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					myCursor.moveToPosition(arg2);
					Intent intent=new Intent();
					Bundle bundle=new Bundle();					
					bundle.putString("newsId",myCursor.getString(2));
					bundle.putString("newsTitle",myCursor.getString(1));
					intent.setClass(Xinwen1Activity.this, XinwenShowActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
	       ItemOnLongClick1();
	     }
	}
	 private void ItemOnLongClick1() {
		  list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
		        @Override
				public void onCreateContextMenu(
					ContextMenu menu,
					View v,
					ContextMenuInfo menuInfo) {
					menu.add(0,0,0,"删除");
	                menu.add(0,1,0,"删除ALL");	
	                menu.add(0,2,0,"取消");
					}
                });
		   }
		 
        // 长按菜单响应函数
        public boolean onContextItemSelected(MenuItem item) {
 
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                                .getMenuInfo();
                db = new DBUtils(this);
                switch(item.getItemId()) {                
                case 0:
                	//删除一条记录   	                	 
                	 db.delRecord(String.valueOf(info.id),"3");
	   				 Toast.makeText(Xinwen1Activity.this, "该记录删除成功！", Toast.LENGTH_SHORT).show();
	   				 showHistory();
                  break; 
                case 1:
                   // 删除ALL操作
                	 db.delHistory("3");
	   				 Toast.makeText(Xinwen1Activity.this, "所有记录删除成功！", Toast.LENGTH_SHORT).show();
	   				 showHistory();
                   break; 
                case 2:
                	//取消操作
                    showHistory();
                    break;    
                default:
                   break;
                } 
                return super.onContextItemSelected(item);
        }
}