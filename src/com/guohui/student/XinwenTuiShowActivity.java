package com.guohui.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.guohui.db.DBUtils;
import com.guohui.db.HistoryRecords;
import com.guohui.interfacer.DownImageInterface;
import com.guohui.interfacer.XinwenInterface;
import com.guohui.student.R;
import com.guohui.model.Image;
import com.guohui.model.Xinwen;
import com.guohui.network.DownImageTask;
import com.guohui.network.DownXinwenShowTask;
import com.guohui.util.ConstUtils;
import com.guohui.util.MyApplication;
import com.guohui.util.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class XinwenTuiShowActivity extends ZYActivity implements DownImageInterface,
		XinwenInterface, GestureDetector.OnGestureListener {
	Intent intent = new Intent();
	private int current;
	private TextView mTitle;
	private TextView mDesc,catid,loadingtxt;
	private String title ;
	DBUtils db ;
	String newsid, s_txtsize, s_screen;
	Integer his_result,fav_result;
	private RelativeLayout headerView, bottomView;
	Button btn_fav;
	private LinearLayout imgLoading,bglayout,bgtitle;
	private List<Bitmap> bitmapList;
	private ImageView imgView[];
	private Bitmap btNone;
	GestureDetector gestureScanner;
	private String imgUrl;
	private int screenHeight;
	private int screenWidth;
	float movex = 0.0F;
	float movey = 0.0F;
	private String newsId,newsTitle;
	private String channelId;
	private List<Image> imgList;
	private List<Xinwen> pageList;
	private String mType,mContent;
	ViewStub view_stub;
	String isshow,picurl,picurl2,weburl,url;
	TextView textview;
	RelativeLayout mAdContainer;
	ScrollView sv;
	private boolean isFullScreen = false;
	Button button1, button2, button3, button4;
	int chooseItem = -1;
	String arg0[] = { "17", "19", "21", "23", "25", "27", "29" };
	ArrayList Allarg0 = new ArrayList();
	int int0[] = new int[10];
	boolean choosebln[] = new boolean[arg0.length];
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.xinwenshow);
		MyApplication.getInstance().addActivity(this);
		db = new DBUtils(this);
		// 检查网络状态
		if (!Tools.CheckNetwork(this)) {
			finish();
		} else {
			Intent intent = getIntent();
			Bundle bundle = null;
			if (intent != null) {
				bundle = intent.getExtras();
				if (bundle != null) {
					try {
						this.newsId = bundle.getString("newsId");						
						this.newsTitle = bundle.getString("newsTitle");
					} catch (Exception e) {
						finish();
					}
				}
			}
			DisplayMetrics localDisplayMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(
					localDisplayMetrics);
			this.screenWidth = localDisplayMetrics.widthPixels;
			this.screenHeight = localDisplayMetrics.heightPixels;
			this.btNone = BitmapFactory.decodeResource(getResources(),
					R.drawable.iconbg);
			initLayout();			
			getArticle(newsId, "22");	
			fav_result = db.isValidHistoryNews(newsId,"4");
			if (fav_result < 1) {
				btn_fav.setBackgroundResource(R.drawable.unstar_icon);				
			}else{
				btn_fav.setBackgroundResource(R.drawable.star_icon);				
			}
			this.gestureScanner = new GestureDetector(this);
			
		}		
	}

	// 初始化数据库表,插入对应数据
	private void initTable(String type) {
		db = new DBUtils(this);
		ArrayList<HistoryRecords> list = new ArrayList<HistoryRecords>();
		list.add(new HistoryRecords(newsId, newsTitle,type));
		//Log.e("insert","插入了");
		// 将信息添加到数据库中
		db.add(list);
	}
	private void initLayout() {
		sv= (ScrollView) findViewById(R.id.ScrollView);
		this.headerView = (RelativeLayout) findViewById(R.id.header);
		this.bottomView = (RelativeLayout) findViewById(R.id.LinearBottom);
		this.bglayout = (LinearLayout) findViewById(R.id.bglayout);
		this.bgtitle= (LinearLayout) findViewById(R.id.bgtitle);
		this.mTitle = ((TextView) findViewById(R.id.newsTitle));
		this.catid = ((TextView) findViewById(R.id.catid));
		this.loadingtxt = ((TextView) findViewById(R.id.textView1));
		this.loadingtxt.setTextColor(Color.WHITE);		
		this.mDesc = ((TextView) findViewById(R.id.newsAddTime));
		this.imgLoading = ((LinearLayout) findViewById(R.id.imgloading));
		this.imgLoading.setBackgroundResource(R.color.color_black);
		initButton();
		//收藏按钮
		btn_fav	= ((Button) findViewById(R.id.btn_fav));		
		btn_fav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//如果收藏记录为0，则收藏
				fav_result = db.isValidHistoryNews(newsId,"4");
				if (fav_result < 1) {
					// 插入收藏表
					initTable("4");
					//Log.e("添加收藏","add");
					ConstUtils.showToast(XinwenTuiShowActivity.this,
							R.string.add_fav);
					btn_fav.setBackgroundResource(R.drawable.star_icon);
				}else{
					//取消收藏
					//Log.e("取消收藏","del");
					db.delFav(newsId,"4");
					ConstUtils.showToast(XinwenTuiShowActivity.this,
							R.string.cancel_fav);
					btn_fav.setBackgroundResource(R.drawable.unstar_icon);
				}
			}
		});	
	}
	
	private void setVisibale(int i) {
		this.headerView.setVisibility(i);
		this.bottomView.setVisibility(i);
	}


	private void getArticle(String paramString1, String paramString2) {

		if ((paramString2 != null) && (!"".equals(paramString2.trim()))) {
			this.imgLoading.setVisibility(0);
			this.current = 0;
			if (this.pageList != null)
				this.pageList.clear();
			if (this.imgList != null)
				this.imgList.clear();
			DownXinwenShowTask localDownArticleTask = new DownXinwenShowTask(this);
			String[] arrayOfString = new String[2];
			arrayOfString[0] = paramString1;
			arrayOfString[1] = paramString2;
			localDownArticleTask.execute(arrayOfString);
			// 获取返回的数量
			his_result = db.isValidHistoryNews(newsId,"3");
			if (his_result < 1) {
				// 插入历史记录表
				//Log.e("插入历史记录表","插入历史记录表");
				initTable("3");
			}	
		}
	}

	@SuppressWarnings("null")
	private void loadImage(int paramInt) {
		if ((this.imgList != null) && (this.imgList.get(paramInt) != null)) {
			String str = ((Image) this.imgList.get(paramInt)).getImageURL();
			if ((str != null) || (!str.trim().equals(""))) {
				DownImageTask localDownImageTask = new DownImageTask(paramInt,
						1, this);
				String[] arrayOfString = new String[3];
				arrayOfString[0] = str;
				arrayOfString[1] = "12";
				arrayOfString[2] = "34";
				localDownImageTask.execute(arrayOfString);
				
			}
		}
	}

	private void drawArticle() {
		LinearLayout linearlayout = (LinearLayout) findViewById(R.id.articleBody);
		linearlayout.removeAllViews();
		int i = 0;
		if ((this.imgList != null) && (this.imgList.size() > 0)) {
			gestureScanner
					.setOnDoubleTapListener(new android.view.GestureDetector.OnDoubleTapListener() {
						public boolean onDoubleTap(MotionEvent motionevent) {
							// Log.v("test", "onDoubleTap");
							return false;
						}

						public boolean onDoubleTapEvent(MotionEvent motionevent) {
							// Log.v("test", "onDoubleTapEvent");
							return false;
						}

						public boolean onSingleTapConfirmed(
								MotionEvent motionevent) {
							// Log.v("test", "onSingleTapConfirmed");
							if (imgUrl != null && !imgUrl.trim().equals("")) {
								Intent intent = new Intent(XinwenTuiShowActivity.this,
										BigImageActivity.class);

								intent.putExtra("IMGURL", imgUrl);
								startActivity(intent);
							}
							return false;
						}
					});
		}
		if (this.pageList != null && this.pageList.size() > 0) {
			for (int j = 0; j < this.pageList.size(); j++) {
				Object localObj = this.pageList.get(j);
				if (localObj instanceof Xinwen) {
					Xinwen news = (Xinwen) localObj;
					this.mType = news.getType();
					this.channelId = news.getChannel();
					this.mContent = news.getContent();
					if (this.mType.equalsIgnoreCase("img")) {
						imgView[i] = new ImageView(this);
						if (this.mContent != null
								&& this.mContent.trim().length() > 1) {
							imgView[i].setImageBitmap((Bitmap) bitmapList
									.get(i));
							imgView[i].setPadding(0, 5, 0, 5);
							linearlayout.addView(imgView[i]);
							final int sel = i;
							imgView[i]
									.setOnTouchListener(new android.view.View.OnTouchListener() {

										public boolean onTouch(View view,
												MotionEvent motionevent) {
											boolean flag;
											if (imgList.size() == 0) {
												flag = false;
											} else {
												current = sel;
												if (current < 0
														|| current > imgList
																.size())
													current = 0;
												imgUrl = ((Image) imgList
														.get(sel))
														.getImageURL();
												flag = gestureScanner
														.onTouchEvent(motionevent);
											}
											return flag;
										}
									});
						}
						i++;
					}
					// 如果是文字
					if (this.mType.equalsIgnoreCase("txt")) {
						textview = new TextView(this);
						textview.setTextColor(getResources().getColor(
								R.color.color_title));
						textview.setLineSpacing(0.0F, 1.2F);
						textview.setPadding(6, 0,6, 10);
						textview.setTextSize(Float.parseFloat(this.preferences().getString("CustomTextSize", "17")));
						textview.setText(((Xinwen) pageList.get(j)).getContent());
						//textview.setTextSize(17);
						getScreen();							
						linearlayout.addView(textview);
						textview.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (isFullScreen) {
									setVisibale(8);
									XinwenTuiShowActivity.this.isFullScreen = false;
								} else {
									setVisibale(0);
									XinwenTuiShowActivity.this.isFullScreen = true;
								}
							}
						});
											
					}
				}
			}
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		movex = e.getRawX();
		movey = e.getRawY();
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}

	@Override
	public void notifyChangeShare(int paramInt1, int paramInt2) {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setNews(List<Xinwen> paramList1,
			HashMap<Object, Object> paramHashMap) {
		this.imgLoading.setVisibility(4);
		this.title = (String) paramHashMap.get("title");
		this.channelId = (String) paramHashMap.get("catid");
		String paramString = (new StringBuilder(
				(String) paramHashMap.get("inputtime"))).append("  来源：")
				.append((String) paramHashMap.get("copyfrom")).toString();
		findViewById(R.id.list_divider_line).setBackgroundResource(
				R.drawable.list_divider_line);
		this.mTitle.setText(title);
		this.catid.setText(channelId);
		this.mDesc.setText(paramString);		
		

		if ((paramList1 != null) && (paramList1.size() > 0)) {
			this.pageList = paramList1;
			if (((List<Image>) paramHashMap.get("imgList")).size() > 0) {
				this.imgList = ((List<Image>) paramHashMap.get("imgList"));
			}
			if ((this.imgList != null) && (this.imgList.size() > 0)) {
				this.imgView = new ImageView[this.imgList.size()];
				this.bitmapList = new ArrayList(this.imgList.size());
				for (int j = 0; j < this.imgList.size(); j++) {
					this.bitmapList.add(this.btNone);
				}
				if (imgView != null && imgList != null && imgList.size() > 0) {
					loadImage(current);
				}
			}
			drawArticle();
		} else {
			finish();
		}	
	}

	@Override
	public Context getContext() {
		return getBaseContext();
	}

	@Override
	public void notifyChangeImage(int paramInt1, int paramInt2,
			Bitmap paramBitmap) {
		if (paramBitmap == null) {
			paramBitmap = this.btNone;
		} else {
			bitmapList.add(paramInt1, paramBitmap);
			int k = paramBitmap.getWidth();
			int l = paramBitmap.getHeight();
			float f = (float) screenWidth / (float) k;
			float f1 = (float) screenHeight / (float) l;
			float f2;
			int i1;
			if (f < f1)
				f2 = f;
			else
				f2 = f1;
			if (k > screenWidth) {
				imgView[paramInt1]
						.setScaleType(android.widget.ImageView.ScaleType.FIT_START);
				imgView[paramInt1]
						.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
								(int) (f * (float) k),
								(int) (f2 * (float) paramBitmap.getHeight())));
			}
			imgView[paramInt1].setImageBitmap(paramBitmap);
			i1 = paramInt1 + 1;
			if (i1 < this.imgList.size())
				loadImage(i1);

		}
	}
	public boolean onPrepareOptionsMenu(Menu paramMenu) {
		paramMenu.clear();
		// 收藏
		fav_result = db.isValidHistoryNews(newsId,"4");
		if (fav_result < 1) {
			paramMenu.add(0, ConstUtils.FAV_ID, 0, R.string.favroite)
			.setShortcut('4', 'd').setIcon(R.drawable.unstar_icon);
	
		}else{
			paramMenu.add(0, ConstUtils.FAV_ID, 0, R.string.cancel_fav)
			.setShortcut('4', 'd').setIcon(R.drawable.star_icon);				
		}
		// 我看过的
		paramMenu.add(0, ConstUtils.SEE_ID, 0, R.string.mysee)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_copy);
		//返回
		paramMenu.add(0, ConstUtils.BACK_INDEX, 0, R.string.str_back)
		.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_default);

		return super.onPrepareOptionsMenu(paramMenu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {		
		case ConstUtils.FAV_ID:
			//如果收藏记录为0，则收藏
			fav_result = db.isValidHistoryNews(newsId,"4");
			if (fav_result < 1) {
				// 插入收藏表
				initTable("4");
				//Log.e("添加收藏","add");
				ConstUtils.showToast(XinwenTuiShowActivity.this,
						R.string.add_fav);
				btn_fav.setBackgroundResource(R.drawable.star_icon);
			}else{
				//取消收藏
				//Log.e("取消收藏","del");
				db.delFav(newsId,"4");
				ConstUtils.showToast(XinwenTuiShowActivity.this,
						R.string.cancel_fav);
				btn_fav.setBackgroundResource(R.drawable.unstar_icon);
			}		
			break;
		case ConstUtils.SEE_ID:
			intent.setClass(XinwenTuiShowActivity.this, MyseeActivity.class);
			startActivity(intent);
			XinwenTuiShowActivity.this.finish();
			break;
		case ConstUtils.BACK_INDEX:
			finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	private void initButton() {
		button1 = (Button) findViewById(R.id.fontBtn);
		button2 = (Button) findViewById(R.id.menuBtn);
		button3 = (Button) findViewById(R.id.sunBtn);
		button4 = (Button) findViewById(R.id.shareBtn);
		// 字体大小设置
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_font();
			}
		});
		// 返回
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(XinwenTuiShowActivity.this,
						MainActivity.class));
			}
		});
		// 夜间模式
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				s_screen = preferences().getString("CustomScreen", "sun");
				if (s_screen.equals("sun")) {
					preferences().edit().putString("CustomScreen", "moon")
							.commit();
					// Log.e("场景：","白天");
					changeTxtColorMoon();
				} else {
					preferences().edit().putString("CustomScreen", "sun")
							.commit();
					changeTxtColorSun();
					// Log.e("场景：","晚上");
				}
			}
		});
		// 分享
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.setType("text/plain");
				intent.putExtra("android.intent.extra.SUBJECT", "分享文章");
				startActivity(Intent.createChooser(intent,
						"告诉朋友" + mTitle.getText()));
			}

		});
	}
	private void dialog_font() {
		// 参数是默认被选中的选项位置，使用“-1”来表示默认情况下不选中任何选项。
		new AlertDialog.Builder(this)
				.setTitle("选择字体大小")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(arg0, -1,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								chooseItem = which;
							}
						})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						int txtSize = 16;
						switch (chooseItem) {
						case 0:
							txtSize = 17;
							// Toast.makeText(NewslistActivity.this,txtSize,Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							chooseItem = -1;
							break;
						case 1:
							txtSize = 19;
							// Toast.makeText(NewslistActivity.this,txtSize,Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							chooseItem = -1;
							break;
						case 2:
							txtSize = 21;
							// Toast.makeText(NewslistActivity.this,txtSize,Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							chooseItem = -1;
							break;
						case 3:
							txtSize = 23;
							// Toast.makeText(NewslistActivity.this,txtSize,Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							chooseItem = -1;
							break;
						case 4:
							txtSize = 25;
							// Toast.makeText(NewslistActivity.this,txtSize,Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							chooseItem = -1;
							break;
						case 5:
							txtSize = 27;
							// Toast.makeText(NewslistActivity.this,txtSize,Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							chooseItem = -1;
							break;
						case 6:
							txtSize = 29;
							// Toast.makeText(NewslistActivity.this,txtSize,Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							chooseItem = -1;
							break;
						default:
							txtSize = 17;
							dialog.dismiss();
							chooseItem = -1;
							break;
						}
						// 改变字体大小
						changeTxtSize(txtSize);
					}
				}).setNegativeButton("取消", null).show();
	}

	public void changeTxtSize(int txtSize) {
		mTitle.setTextSize(txtSize);
		mDesc.setTextSize(txtSize);
		textview.setTextSize(txtSize);
		this.preferences().edit().putString("CustomTextSize", txtSize + "")
				.commit();
		// Log.e("CustomTextSize",txtSize+"");
	}

	// 判断场景模式
	public void getScreen() {
		s_screen = preferences().getString("CustomScreen", "sun");
		// Log.e("screent",s_screen);
		if (s_screen.equals("sun")) {
			changeTxtColorSun();
		} else {
			changeTxtColorMoon();
		}
	}

	public void changeTxtColorMoon() {
		// 改版字体颜色
		mTitle.setTextColor(Color.WHITE);
		mDesc.setTextColor(Color.WHITE);
		textview.setTextColor(Color.WHITE);
		// 改变字体背景色
		bgtitle.setBackgroundResource(R.color.color_black);
		bglayout.setBackgroundResource(R.color.color_black);
		textview.setBackgroundResource(R.color.color_black);
		button3.setBackgroundResource(R.drawable.menu_sun);
	}

	public void changeTxtColorSun() {
		// 改版字体颜色
		mTitle.setTextColor(Color.BLACK);
		mDesc.setTextColor(Color.GRAY);		
		textview.setTextColor(Color.BLACK);
		// 改变字体背景色
		bgtitle.setBackgroundResource(R.color.white);
		bglayout.setBackgroundResource(R.color.white);
		textview.setBackgroundResource(R.color.white);
		button3.setBackgroundResource(R.drawable.menu_moon);
	}

	public void getTxtSize() {
		s_txtsize = preferences().getString("CustomTextSize", "17");
		textview.setTextSize(Integer.parseInt(s_txtsize));
		int title_size = Integer.parseInt(s_txtsize) + 2;
		mTitle.setTextSize(title_size);
		mDesc.setTextSize(Integer.parseInt(s_txtsize));
	}
	// 屏蔽返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			intent.setClass(XinwenTuiShowActivity.this, MainActivity.class);
			startActivity(intent);
			XinwenTuiShowActivity.this.finish();
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