package com.guohui.student;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import com.newqm.sdkoffer.QuMiConnect;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.guohui.db.DBUtils;
import com.guohui.db.HistoryRecords;
import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import com.guohui.util.Constant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PicShowActivity extends ZYActivity implements OnTouchListener,
		OnGestureListener {
	private ProgressDialog mpDialog;
	Intent intent = new Intent();
	Button btn_fav;
	private ViewFlipper flipper;
	private GestureDetector detector;
	public static photoinfo[] photolist;
	public static int showing;
	public static boolean loadingimg;
	public static int albumid;
	public static int previd;
	private GetImage getimg;
	TextView txt_title, txt_pages;
	public String title;
	private boolean showOther = true;
	private String sdpath = Environment.getExternalStorageDirectory().getPath();
	int tonext;
	boolean changing;
	private RelativeLayout header;
	private float minscale;
	private float maxscale = 3;
	private int oimgw;
	private int oimgh;
	private int imgw;
	private int imgh;
	boolean onleft;
	boolean onright;
	private String aid;
	int scw;
	int sch;
	HashMap<Integer, Matrix> matrixmap = new HashMap();
	Matrix savedMatrix = new Matrix();
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	DBUtils db;
	String fpath;
	Integer his_result, fav_result;
	Bitmap bmp = null;
	Handler Handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String errmsg = msg.getData().getString("msg");
			if (errmsg == null) {

			} else if (errmsg == "") {
				addphoto();
			} else {
				Toast.makeText(PicShowActivity.this, errmsg,
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	Handler Handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int id = 0;
			// ImageView imgi = (ImageView)
			// NewsShowActivity.this.findViewById(id);
			// imgi.setImageBitmap(null);
			// imgi.setImageResource(R.drawable.loading);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(1);
		setContentView(R.layout.pichow);
		QuMiConnect.getQumiConnectInstance(this).initPopAd(this); // 初始化插屏广告

		db = new DBUtils(this);
		// 检查网络状态
		if (!this.isConnectNetWork()) {
			ConstUtils.showToast(PicShowActivity.this, R.string.Net_Failure);
		} else {
			// 获取传过来的参数
			Bundle bundle = this.getIntent().getExtras();
			aid = bundle.getString("articleid");
			title = bundle.getString("title");
			// Log.e("aid", aid);
			detector = new GestureDetector(this);
			header = ((RelativeLayout) findViewById(R.id.header));
			txt_title = ((TextView) findViewById(R.id.txt_title));
			txt_pages = ((TextView) findViewById(R.id.txt_pages));
			btn_fav = ((Button) findViewById(R.id.btn_fav));
			btn_fav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 如果收藏记录为0，则收藏
					fav_result = db.isValidHistoryNews(aid, "2");
					if (fav_result < 1) {
						// 插入收藏表
						initTable("2");
						// Log.e("添加收藏","add");
						ConstUtils.showToast(PicShowActivity.this,
								R.string.add_fav);
						btn_fav.setBackgroundResource(R.drawable.star_icon);
					} else {
						// 取消收藏
						// Log.e("取消收藏","del");
						db.delFav(aid, "2");
						ConstUtils.showToast(PicShowActivity.this,
								R.string.cancel_fav);
						btn_fav.setBackgroundResource(R.drawable.unstar_icon);
					}

				}
			});
			flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);

			if (albumid != 0 || photolist == null || photolist.length == 0) {
				getphotolist getplist = new getphotolist();
				getplist.id = aid;
				getplist.txtitle = title;
				albumid = 0;
				txt_title.setText(getplist.txtitle);
				getplist.start();
				// Log.e("num",photolist.length+"");
				// System.out.println("getdate");
			} else {
				tonext = 3;
				addphoto();
				// System.out.println("addphoto");
			}
			// 获取返回的数量
			his_result = db.isValidHistoryNews(aid, "1");
			if (his_result < 1) {
				// 插入历史记录表
				initTable("1");
			}
			fav_result = db.isValidHistoryNews(aid, "2");
			if (fav_result < 1) {
				btn_fav.setBackgroundResource(R.drawable.unstar_icon);
			} else {
				btn_fav.setBackgroundResource(R.drawable.star_icon);
			}
		}
	}

	// 初始化数据库表,插入对应数据
	private void initTable(String type) {
		db = new DBUtils(this);
		ArrayList<HistoryRecords> list = new ArrayList<HistoryRecords>();
		list.add(new HistoryRecords(aid, title, type));
		// Log.e("insert","插入了");
		// 将信息添加到数据库中
		db.add(list);
	}

	private void addphoto() {
		for (int i = 0; i < photolist.length; i++) {
			flipper.addView(addTextView(photolist[i].id));
			/*int count=photolist.length-1;
			if(i==count){
				QuMiConnect.getQumiConnectInstance(PicShowActivity.this)
				.showPopUpAd(PicShowActivity.this);
			}*/
		}
		setimg(showing, 0);
	}

	private View addTextView(int photoid) {
		ImageView iv = new ImageView(this);
		iv.setScaleType(ImageView.ScaleType.MATRIX);
		iv.setOnTouchListener(this);
		iv.setId(photoid);
		return iv;
	}

	private void setVisibale(int i) {
		header.setVisibility(i);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// System.out.println("X " + event.getX());
		if (loadingimg) {
			return true;
		}
		ImageView myImageView = (ImageView) v;
		float[] mv = new float[9];
		Matrix matrix = new Matrix();
		if (matrixmap.containsKey(showing)) {
			matrix = matrixmap.get(showing);
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:// 设置拖拉模式
			matrix.set(myImageView.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			// Log.e("mode", "drag");
			break;
		case MotionEvent.ACTION_UP:
			changing = false;
			setVisibale(4);
			// Log.e("mode", "up");
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			// Log.e("mode", "none");
			PicShowActivity picarticleactivity2 = PicShowActivity.this;
			boolean flag1,
			flag;
			if (showOther) {
				flag1 = false;
			} else {
				flag1 = true;
			}
			picarticleactivity2.showOther = flag1;
			if (showOther) {
				setVisibale(0);
			} else {
				setVisibale(8);
			}
			flag = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:// 设置多点触摸模式
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {// 若为DRAG模式，则点击移动图片
				float tx = (event.getX() - start.x);
				float ty = (event.getY() - start.y);
				if (!changing && onleft && tx > scw / 4) {
					showi(false);
					tonext = 2;
					setimg(showing, 0);
					changing = true;
					return true;
				} else if (!changing && onright && tx < -scw / 4) {
					showi(true);
					tonext = 1;
					setimg(showing, 0);
					changing = true;
					return true;
				}
				matrix.set(savedMatrix);
				matrix.getValues(mv);
				if (scw >= imgw) {
					if (mv[2] + tx >= (scw - imgw) / 2) {
						tx = (scw - imgw) / 2 - mv[2];
						onleft = true;
					} else if (mv[2] + tx <= (scw - imgw) / 2) {
						tx = (scw - imgw) / 2 - mv[2];
						onright = true;
					} else {
						onleft = false;
						onright = false;
					}
				} else {
					if (mv[2] + tx >= 0) {
						tx = -mv[2];
						onleft = true;
					} else if (mv[2] + tx <= scw - imgw) {
						tx = scw - imgw - mv[2];
						onright = true;
					} else {
						onleft = false;
						onright = false;
					}
				}
				if (sch >= imgh) {
					if (mv[5] + ty >= (sch - imgh) / 2) {
						ty = (sch - imgh) / 2 - mv[5];
					} else if (mv[5] + ty <= (sch - imgh) / 2) {
						ty = (sch - imgh) / 2 - mv[5];
					}
				} else {
					if (mv[5] + ty >= 0) {
						ty = -mv[5];
					} else if (mv[5] + ty <= sch - imgh) {
						ty = sch - imgh - mv[5];
					}
				}
				matrix.postTranslate(tx, ty);
			} else if (mode == ZOOM) {// 若为ZOOM模式，则点击触摸缩放
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = (newDist / oldDist);
					matrix.getValues(mv);
					if (mv[0] * scale < minscale) {
						scale = minscale / mv[0];
					} else if (mv[0] * scale > maxscale) {
						scale = maxscale / mv[0];
					}
					matrix.postScale(scale, scale, mid.x, mid.y);
					matrix.getValues(mv);
					imgw = (int) (oimgw * mv[0]);
					imgh = (int) (oimgh * mv[0]);
					float tx = 0;
					float ty = 0;
					if (scw >= imgw) {
						if (mv[2] >= (scw - imgw) / 2) {
							onleft = true;
							tx = (scw - imgw) / 2 - mv[2];
						} else if (mv[2] <= (scw - imgw) / 2) {
							tx = (scw - imgw) / 2 - mv[2];
							onright = true;
						} else {
							onright = false;
							onleft = false;
						}
					} else {
						if (mv[2] >= 0) {
							tx = -mv[2];
							onleft = true;
						} else if (mv[2] <= scw - imgw) {
							tx = scw - imgw - mv[2];
							onright = true;
						} else {
							onleft = false;
							onright = false;
						}
					}
					if (sch >= imgh) {
						if (mv[5] > (sch - imgh) / 2) {
							ty = (sch - imgh) / 2 - mv[5];
						} else if (mv[5] < (sch - imgh) / 2) {
							ty = (sch - imgh) / 2 - mv[5];
						}
					} else {
						if (mv[5] > 0) {
							ty = -mv[5];
						} else if (mv[5] < sch - imgh) {
							ty = sch - imgh - mv[5];
						}
					}
					matrix.postTranslate(tx, ty);
				}
			}
			break;
		}
		myImageView.setImageMatrix(matrix);
		matrixmap.put(showing, matrix);
		return true;
	}

	// 计算移动距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 计算中点位置
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private void initmatrix() {
		Matrix matrix = new Matrix();
		if (matrixmap.containsKey(showing)) {
			matrix = matrixmap.get(showing);
		}
		ImageView imgi = (ImageView) PicShowActivity.this
				.findViewById(photolist[showing].id);
		matrix.set(imgi.getImageMatrix());

		// 获取分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		scw = dm.widthPixels; // 当前分辨率 宽度
		sch = dm.heightPixels; // 当前分辨率高度
		float wscale = scw / (float) oimgw;
		float hscale = sch / (float) oimgh;
		float scale;
		if (wscale < hscale) {
			scale = wscale;
		} else {
			scale = hscale;
		}
		matrix.setScale(scale, scale);
		minscale = scale;
		imgw = (int) ((int) oimgw * scale);
		imgh = (int) ((int) oimgh * scale);
		int dx = (scw - imgw) / 2;
		int dy = (sch - imgh) / 2;
		matrix.postTranslate(dx, dy);
		onleft = onright = true;
		changing = false;
		imgi.setImageMatrix(matrix);
		matrixmap.put(showing, matrix);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	private void showi(boolean isnext) {
		previd = photolist[showing].id;
		if (isnext) {
			showing++;
			if (showing == photolist.length) {
				showing = 0;
				QuMiConnect.getQumiConnectInstance(PicShowActivity.this)
				.showPopUpAd(PicShowActivity.this);
			}
		} else {
			if (showing == 0) {
				showing = photolist.length - 1;
			} else {
				showing--;
			}
		}
	}

	private void setimg(int i, int time) {
		if (!this.isConnectNetWork()) {
			ConstUtils.showToast(PicShowActivity.this, R.string.Net_Failure);
		} else {
			loadingimg = true;
			flippershow(tonext);
			tonext = 0;
			int ids = photolist[i].id;

			ImageView imgi = (ImageView) PicShowActivity.this
					.findViewById(ids);
			fpath = sdpath + "/sport/photo/" + aid + "/photo" + ids
					+ ".jpg";
			// Log.e("fpath", fpath);
			File f = new File(fpath);
			// 压缩图片
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = true;
			// 获取这个图片的宽和高
			options.inJustDecodeBounds = false;
			// 计算缩放比
			int be = 1;
			options.inSampleSize = be;
			// 重新读入图片，注意这次要把options.inJustDecodeBounds设为false哦
			bmp = BitmapFactory.decodeFile(fpath, options);
			
			//Bitmap bmp = BitmapFactory.decodeFile(fpath);
			if (imgi != null && f.exists() && bmp != null) {
				imgi.setImageBitmap(bmp);
				oimgw = bmp.getWidth();
				oimgh = bmp.getHeight();
				initmatrix();
				if (previd != 0) {
					setimgnull setnull = new setimgnull();
					setnull.id = previd;
					setnull.start();
				}
				loadingimg = false;
			} else if (imgi != null && time < 3) {
				getimg = new GetImage();
				getimg.i = i;
				getimg.time = time;
				getimg.execute();
			}
			int k = i + 1;
			txt_pages.setText(k + "/" + photolist.length);
		}

	}

	private void flippershow(int isnext) {
		if (isnext == 1) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			this.flipper.showNext();
		} else if (isnext == 2) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			this.flipper.showPrevious();
		} else if (isnext == 3) {
			this.flipper.setDisplayedChild(showing);
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (loadingimg) {
			return true;
		}
		if (e1.getX() - e2.getX() > 120) {
			showi(true);
			tonext = 1;
			setimg(showing, 0);
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			showi(false);
			tonext = 2;
			setimg(showing, 0);
			return true;
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public static void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void saveMyBitmap(Bitmap mBitmap, String path, String bitName)
			throws IOException {
		PicShowActivity.createPath(path);
		File f = new File(path + bitName);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			ConstUtils.showToast(this, R.string.Net_Failure);
		}
		try {
			fOut.flush();
		} catch (IOException e) {
			// e.printStackTrace();
			ConstUtils.showToast(this, R.string.Net_Failure);
		}
		try {
			fOut.close();
		} catch (IOException e) {
			// e.printStackTrace();
			ConstUtils.showToast(this, R.string.Net_Failure);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		boolean mayInterruptIfRunning = getimg.isCancelled();
		getimg.cancel(mayInterruptIfRunning);
	}

	public void recycle() {
		if (bmp != null && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
			//Log.e("内存回收", "内存回收");
		}
		System.gc();
		// Log.e("内存回收11","内存回收11");
	}

	@Override
	protected void onDestroy() {
		if (getimg != null) {
			getimg.cancel(true);
		}
		recycle();
		super.onDestroy();
	}

	@Override
	public void finish() {
		if (getimg != null) {
			getimg.cancel(true);
		}
		//Log.d("photolist", "photolist is null");
		photolist = null;
		albumid = 0;
		recycle();
		super.finish();
	}

	private class setimgnull extends Thread {
		public int id;

		@Override
		public void run() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// e.printStackTrace();
				Log.e("stop", "stop");
			}
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt("id", this.id);
			// Log.e("id",this.id+"");
			message.setData(bundle);
			Handler2.sendMessage(message);
		}
	}

	private class GetImage extends AsyncTask {
		public int i;
		public int id2;
		public int time;

		public GetImage() {
			super();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			/** 此方法在主线程执行，任务执行的结果作为此方法的参数返回 */
			setimg(i, ++time);
			mpDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			/** 执行预处理，它运行于UI线程，可以为后台任务做一些准备工作，比如绘制一个进度条控件 */
			// probar.show();
			showDialog();
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... params) {
			/**
			 * 此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。
			 * 在执行过程中可以调用publicProgress(Progress…)来更新任务的进度。
			 **/
			this.id2 = photolist[i].id;
			try {
				//Bitmap bitmap;
				HttpClient client = new DefaultHttpClient();
				String imgurl = photolist[this.i].url;
				URI uri = URI.create(imgurl);
				HttpGet get = new HttpGet(uri);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				int length = (int) entity.getContentLength();
				InputStream in = entity.getContent();
				if (in != null) {
					String dir = sdpath + "/sport/photo/" + aid + "/";
					createPath(dir);
					String fpath = dir + "photo" + this.id2 + ".jpg";
					FileOutputStream os = new FileOutputStream(fpath);
					byte[] buf = new byte[50];
					byte[] imgData = new byte[length];
					int ch = -1;
					int count = 0;
					while ((ch = in.read(buf)) != -1) {
						os.write(buf, 0, ch);
						count += ch;
						if (length > 0) {
							publishProgress((int) ((count / (float) length) * 100));
						}
					}
					/*BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					options.inPurgeable = true;
					options.inJustDecodeBounds = true;
					options.inInputShareable = true;
					int be = (int) (options.outHeight / (float) 550);
					if (be <= 0) {
						be = 2;
					}
					options.inSampleSize = be;
					bitmap = BitmapFactory.decodeByteArray(imgData, 0,
							imgData.length, options);
					// bitmap = BitmapFactory.decodeStream(in);*/

					in.close();
					os.close();
				}
			} catch (Exception e) {
				ConstUtils.showToast(PicShowActivity.this,
						R.string.Net_Failure);
				// e.printStackTrace();
			}
			return null;
		}
	}

	// 声明加载提示框
	public void showDialog() {
		mpDialog = new ProgressDialog(this);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		mpDialog.setIcon(R.drawable.spinner_black_48);// 设置图标
		mpDialog.setMessage("图片正在加载中...");
		mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
		mpDialog.show();
	}

	class getphotolist extends Thread {
		public String err = "";
		public String id;
		public String txtitle;

		@Override
		public void run() {
			getdata();
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("msg", this.err);
			message.setData(bundle);
			Handler1.sendMessage(message);
		}

		private boolean getdata() {
			String strResult = "{\"err\":true,\"msg\":\"读取失败，请检查网络！\"}";
			HttpGet httpRequest = new HttpGet(Constant.WEB_DIR
					+ Constant.ImageShowUrl + this.id);
			// Log.e("url", Constant.WEB_DIR + Constant.NewsImageShow +
			// this.id);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String sr = EntityUtils.toString(httpResponse.getEntity());
					if (!sr.equals("")) {
						strResult = sr;
					}
				}
			} catch (IOException e) {
				// e.printStackTrace();
				ConstUtils.showToast(PicShowActivity.this,
						R.string.Net_Failure);
			}
			Gson gson = new Gson();
			Json json = gson.fromJson(strResult, Json.class);
			if (json.ok) {
				if (json.photolist.length == 0) {
					this.err = "此相册没有图片。";
				} else {
					photolist = json.photolist;
					txtitle = json.title;
					// Log.e("title", txtitle);
				}
				return true;
			} else {
				this.err = json.msg;
			}
			return false;
		}
	}

	public boolean onPrepareOptionsMenu(Menu paramMenu) {
		paramMenu.clear();
		// 收藏
		fav_result = db.isValidHistoryNews(aid, "2");
		if (fav_result < 1) {
			paramMenu.add(0, ConstUtils.FAV_ID, 0, R.string.favroite)
					.setShortcut('4', 'd').setIcon(R.drawable.unstar_icon);

		} else {
			paramMenu.add(0, ConstUtils.FAV_ID, 0, R.string.cancel_fav)
					.setShortcut('4', 'd').setIcon(R.drawable.star_icon);
		}
		// 设为壁纸
		paramMenu.add(0, ConstUtils.BZ_ID, 0, R.string.setbizhi)
				.setShortcut('4', 'd').setIcon(R.drawable.ic_menu_download);

		return super.onPrepareOptionsMenu(paramMenu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ConstUtils.FAV_ID:
			// 如果收藏记录为0，则收藏
			fav_result = db.isValidHistoryNews(aid, "2");
			if (fav_result < 1) {
				// 插入收藏表
				initTable("2");
				// Log.e("添加收藏","add");
				ConstUtils.showToast(PicShowActivity.this, R.string.add_fav);
				btn_fav.setBackgroundResource(R.drawable.star_icon);
			} else {
				// 取消收藏
				// Log.e("取消收藏","del");
				db.delFav(aid, "2");
				ConstUtils
						.showToast(PicShowActivity.this, R.string.cancel_fav);
				btn_fav.setBackgroundResource(R.drawable.unstar_icon);
			}
			break;
		case ConstUtils.BZ_ID:
			setBizhi();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setBizhi() {
		new AlertDialog.Builder(this)
				.setTitle("设置壁纸")
				.setMessage("是否设置为壁纸？")
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int which) {
						paramDialogInterface.dismiss();
					}
				})
				.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							// Log.e("pic",Constant.DownloadDir+ "/" +
							// bitmap.toString() + ".jpg");
							setWallpaper(BitmapFactory.decodeFile(fpath));
							Toast toast = Toast.makeText(
									getApplicationContext(), "壁纸设置成功",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}).show();
	}

	// 屏蔽返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			// photolist.clone();
			photolist = null;
			showing = 0;
			// Log.e("photolist","清空");
			// intent.setClass(NewsShowActivity.this, IndexActivity.class);
			// startActivity(intent);
			PicShowActivity.this.finish();
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