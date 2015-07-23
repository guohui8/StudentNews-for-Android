package com.guohui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;

public class HttpUtil {
	/**
	 * 通过url获取HttpGet对象
	 * 
	 * @param url
	 * @return
	 */
	public static HttpGet getHttpGet(String url) {
		HttpGet request = new HttpGet(url);
		return request;
	}

	/**
	 * 通过url获取HttpPost对象
	 * 
	 * @param url
	 * @return
	 */
	public static HttpPost getHttpPost(String url) {
		HttpPost request = new HttpPost(url);
		return request;
	}

	/**
	 * 通过HttpGet获取HttpResponse对象
	 * 
	 * @param request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse getHttpResponse(HttpGet request)
			throws ClientProtocolException, IOException {
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}

	/**
	 * 通过HttpPost获取HttpResponse对象
	 * 
	 * @param request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse getHttpResponse(HttpPost request)
			throws ClientProtocolException, IOException {
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}

	/**
	 * 通过url发送post请求返回请求结果(实质还是get方法later修改)
	 * 
	 * @param url
	 * @return
	 */
	public static String queryStringForPost(String url) {
		HttpPost request = HttpUtil.getHttpPost(url);
		String result = null;
		try {

			HttpResponse response = HttpUtil.getHttpResponse(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				//result = EntityUtils.toString(response.getEntity());
				//return new String (result.getBytes("ISO-8859-1"),"GB2312");
				return result;
			}
		} catch (ClientProtocolException e) {
			Log.w("HttpUtil", Constant.NERTWORKERR + ":" + e.toString());
			return Constant.NERTWORKERR;
		} catch (IOException e) {
			Log.w("HttpUtil", Constant.NERTWORKERR + ":" + e.toString());
			return Constant.NERTWORKERR;
		}
		return null;
	}

	/**
	 * 通过HttpPost发送post请求，返回请求结果
	 * 
	 * @param request
	 * @return
	 */
	public static String queryStringForPost(HttpPost request) {
		String result = null;
		try {
			HttpResponse response = HttpUtil.getHttpResponse(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
				//return new String (result.getBytes("ISO-8859-1"),"GB2312");
				return result;
			}
		} catch (ClientProtocolException e) {
			Log.w("HttpUtil", Constant.NERTWORKERR + ":" + e.toString());
			return Constant.NERTWORKERR;
		} catch (IOException e) {
			Log.w("HttpUtil", Constant.NERTWORKERR + ":" + e.toString());
			return Constant.NERTWORKERR;
		}
		return null;
	}

	/**
	 * 通过url发送get请求，返回请求结果
	 * 
	 * @param url
	 * @return
	 */
	public static String queryStringForGet(String url) {
		HttpGet request = HttpUtil.getHttpGet(url);
		String result = null;
		try {
			HttpResponse response = HttpUtil.getHttpResponse(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
				//return new String (result.getBytes("ISO-8859-1"),"GB2312");
				return result;
			}
		} catch (ClientProtocolException e) {
			Log.w("HttpUtil", Constant.NERTWORKERR + ":" + e.toString());
			return Constant.NERTWORKERR;
		} catch (IOException e) {
			Log.w("HttpUtil", Constant.NERTWORKERR + ":" + e.toString());
			return Constant.NERTWORKERR;
		}
		return null;
	}

	//
	public static HttpURLConnection getConn(String path)
			throws ProtocolException {
		// 建立一个URL对象
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 得到打开的链接对象
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		// 设置请求超时与请求方式
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		return conn;
	}

	public static boolean checkNetworkConnection(Context context) {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable() || mobile.isAvailable())
			return true;
		else
			return false;
	}

	public static boolean checkNet(Context context) {

		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {

					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * 检测网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean CheckNetwork(Context context) {
		boolean bool = false;
		ConnectivityManager localConnectivityManager = (ConnectivityManager) context
				.getSystemService("connectivity");
		if (localConnectivityManager.getActiveNetworkInfo() != null)
			bool = localConnectivityManager.getActiveNetworkInfo()
					.isAvailable();
		Log.w("Tools", "CheckNetwork:" + bool);
		return bool;
	}

	
	public static boolean checkNet22(OnItemClickListener onItemClickListener) {

		try {
			ConnectivityManager connectivity = (ConnectivityManager) ((Context) onItemClickListener)
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {

					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}	
	
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>60) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
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
				//BitmapFactory.Options options = new BitmapFactory.Options();
				//options.inJustDecodeBounds = true;
				//bitmap = BitmapFactory.decodeByteArray(imgData, 0,
					//	imgData.length, options);
				//options.inJustDecodeBounds = false;

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565; 
				options.inPurgeable = true;
				options.inInputShareable = true;
				int be = (int) (options.outHeight / (float) 450);
				if (be <= 0) {
					be = 1;
				} 
				options.inSampleSize = be;
				bitmap = BitmapFactory.decodeByteArray(imgData, 0,
						imgData.length, options);

			}
			return bitmap;
		} catch (MalformedURLException e) {
			//Log.e("error","error");
			return null;
		} catch (IOException e) {
			//Log.e("error2","error2");
			return null;
		}
	}
}