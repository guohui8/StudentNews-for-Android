package com.guohui.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtil {
	private static final int IO_BUFFER_SIZE = 4 * 1024;

	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		// Log.e("ImageUtil", path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public static Drawable getDrawableFromUrl(String url) throws Exception {
		return Drawable.createFromStream(getRequest(url), null);
	}

	public static Bitmap getBitmapFromUrl(String url) throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		return byteToBitmap(bytes);
	}

	public static Bitmap getRoundBitmapFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		Bitmap bitmap = byteToBitmap(bytes);
		return toRoundCorner(bitmap, pixels);
	}

	public static Drawable geRoundDrawableFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
		return toRoundCorner(bitmapDrawable, pixels);
	}

	public static Drawable geRoundDrawableFromUrl(String url, int width,
			int height, boolean cut) throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
		Bitmap bitmap = null;
		if (cut) {
			bitmap = resizeBitmap(bitmapDrawable.getBitmap(), width, height);
			// Log.i("ImageUtil", "geRoundDrawableFromUrl[width = " + width
			// + ",height=" + height + "]");
		} else {
			bitmap = drawableToBitmap(bitmapDrawable);
		}
		bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;
	}

	public static Drawable geRoundBitmapFromUrl(String url, int width,
			int height, boolean cut) throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
		Bitmap bitmap = null;
		if (cut) {
			bitmap = resizeBitmap(bitmapDrawable.getBitmap(), width, height);
			// Log.i("ImageUtil", "geRoundDrawableFromUrl[width = " + width
			// + ",height=" + height + "]");
		} else {
			bitmap = drawableToBitmap(bitmapDrawable);
		}
		bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;
	}

	public static byte[] getBytesFromUrl(String url) throws Exception {
		return readInputStream(getRequest(url));
	}

	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
		return Drawable.createFromStream(ins, null);
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable, int i, int j) {

		int k = drawable.getIntrinsicWidth();
		int i1 = drawable.getIntrinsicHeight();
		int l, j1 = 0;
		if (i < k)
			l = (k - i) / 2;
		else
			l = 0;
		if (j < i1)
			j1 = (i1 - j) / 2;
		if (l != 0)
			k = i;
		if (j1 != 0)
			i1 = j;

		Bitmap bitmap = Bitmap
				.createBitmap(
						i,
						j,
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(l, j1, k, i1);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/**
	 * 切割图片
	 * 
	 * @param bitmap
	 * @param i
	 * @param j
	 * @return
	 */
	public static Bitmap cutBitmap(Bitmap bitmap, int i, int j) {
		int j1 = 0;
		Bitmap bitmap1;
		if (bitmap != null) {
			int k = bitmap.getWidth();
			int i1 = bitmap.getHeight();
			int l;
			if (i < k)
				l = (k - i) / 2;
			else
				l = 0;
			if (j < i1)
				j1 = (i1 - j) / 2;
			if (l != 0)
				k = i;
			if (j1 != 0)
				i1 = j;
			bitmap1 = Bitmap.createBitmap(bitmap, l, j1, k, i1);
		} else {
			bitmap1 = null;
		}
		return bitmap1;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap) {
		Bitmap bitmap1 = null;
		if (bitmap != null) {
			int i = bitmap.getWidth();
			int j = bitmap.getHeight();
			if (i != 0 && j != 0) {
				float f1 = (float) 40 / (float) i;
				float f = (float) 40 / (float) j;
				Matrix matrix = new Matrix();
				matrix.postScale(f1, f);
				bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, true);
				if (bitmap != null && !bitmap.isRecycled())
					bitmap.recycle();
			}
		}
		return bitmap1;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, int i, int j) {
		Log.i("ImageUtil", "resizeBitmap 148");
		Bitmap bitmap1 = null;
		if (bitmap != null) {
			int l = bitmap.getWidth();
			int k = bitmap.getHeight();
			if (l != 0 && k != 0) {
				float f1 = (float) i / (float) l;
				float f = (float) j / (float) k;
				Matrix matrix = new Matrix();
				matrix.postScale(f1, f);
				if (l != i || k != j) {
					bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, l, k, matrix,
							true);
					if (bitmap != null && !bitmap.isRecycled())
						bitmap.recycle();
				} else {
					bitmap1 = bitmap;
				}
			}
		}
		return bitmap1;
	}

	// 获取图片路径函数
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap GetNetBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(url).openStream(),
					IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();

			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	protected Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		
		return newbm;

	}

	// 生成图片
	@SuppressLint("NewApi")
	public static Bitmap createFromStream(String imageURL) {
		InputStream is;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			Bitmap bitmap = null;
			int length = (int) conn.getContentLength();
			if (length != -1) {
				byte[] imgData = new byte[length];
				byte[] temp = new byte[20];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565; 
				options.inPurgeable = true;
				options.inInputShareable = true;
				int be = (int) (options.outHeight / (float) 400);
				if (be <= 0) {
					be = 1;
				} 
				options.inSampleSize = be;
				bitmap = BitmapFactory.decodeByteArray(imgData, 0,
						imgData.length, options);

			}
			return bitmap;
		} catch (MalformedURLException e) {
			Log.e("error","error");
			return null;
		} catch (IOException e) {
			Log.e("error2","error2");
			return null;
		}
	}

	// 生成小图片
	@SuppressLint("NewApi")
	public static Bitmap createFromStreamSmall(String imageURL) {
		InputStream is;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			Bitmap bitmap = null;
			// bitmap=BitmapFactory.decodeStream(is);
			int length = (int) conn.getContentLength();
			if (length != -1) {
				byte[] imgData = new byte[length];
				byte[] temp = new byte[150];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565; 
				options.inPurgeable = true;
				options.inInputShareable = true;
				int be = (int) (options.outHeight / (float) 120);
				if (be <= 0) {
					be = 1;
				} 
				options.inSampleSize = be;
				bitmap = BitmapFactory.decodeByteArray(imgData, 0,
						imgData.length, options);
			}
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}
}