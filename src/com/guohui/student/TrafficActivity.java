package com.guohui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.guohui.util.MyApplication;

public class TrafficActivity extends ZYActivity{
	TextView top_main_text,btn_home;
	Intent intent = new Intent();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.traffic);
		MyApplication.getInstance().addActivity(this);
		findView();				
	}

	private void findView(){		
		top_main_text = (TextView) findViewById(R.id.top_main_text);
		top_main_text.setText("交通介绍");
		btn_home = (Button) findViewById(R.id.btn_home);		
		btn_home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TrafficActivity.this.finish();
			}
		});
	}
}
