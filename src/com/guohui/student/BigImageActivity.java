package com.guohui.student;


import com.guohui.interfacer.DownImageInterface;
import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import com.guohui.network.DownImageTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class BigImageActivity extends Activity implements DownImageInterface,
		View.OnTouchListener {

	private String imgUrl;
	private ImageView imgView;
	int startX;

	@Override
	public Context getContext() {
		return getBaseContext();
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.show_imgbig);
		String s;
		if (paramBundle != null)
			s = paramBundle.getString("IMGURL");
		else
			s = null;
		imgUrl = s;
		if (imgUrl == null) {
			Bundle bundle1 = getIntent().getExtras();
			String s1;
			if (bundle1 != null)
				s1 = bundle1.getString("IMGURL");
			else
				s1 = null;
			imgUrl = s1;
		}
		imgView = (ImageView) findViewById(R.id.bigImg);
		imgView.setVisibility(4);
		imgView.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				BigImageActivity.this.finish();
			}
		});
		notifyChangeImage(0, 0, null);
	}

	@Override
	public void notifyChangeImage(int paramInt1, int paramInt2,
			Bitmap paramBitmap) {
		if (paramBitmap != null) {
			findViewById(R.id.imgloading).setVisibility(8);
			this.imgView.setVisibility(0);
			this.imgView.setBackgroundColor(R.color.color_white);
			this.imgView.setImageBitmap(paramBitmap);
		} else {
			if (paramInt2 >= 0) {
				DownImageTask localDownImageTask = new DownImageTask(0, -1,
						this);
				String[] arrayOfString = new String[3];
				arrayOfString[0] = this.imgUrl;
				arrayOfString[1] = "";
				arrayOfString[2] = "";
				localDownImageTask.execute(arrayOfString);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		event.getAction();
		boolean flag;
		if (event.getAction() == 2) {
			flag = false;
		} else {
			finish();
			flag = true;
		}
		return flag;
	}

}