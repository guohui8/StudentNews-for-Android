package com.guohui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class Tools {

	public static int getScreenMetrics(Context context) {
		int j = 0;
		DisplayMetrics displayMetrics = context.getApplicationContext()
				.getResources().getDisplayMetrics();
		int i = displayMetrics.widthPixels;
		int k = displayMetrics.heightPixels;

		if (i == 240 && k == 320)
			j = 1;
		if (i == 320 && k == 480)
			j = 2;
		if (i == 480 && k == 800)
			j = 3;
		if (i == 640 && k == 960)
			j = 4;
		if (i == 720)
			j = 5;

		return j;
	}

	/**
	 * ��ȡ��Ļ���
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		return context.getApplicationContext().getResources()
				.getDisplayMetrics().widthPixels;
	}

	/**
	 * ��ȡ��Ļ��͸�
	 * 
	 * @param paramContext
	 * @return HashMap[ w : ��? h : ��]
	 */
	public static HashMap<String, Integer> getWidth_Height(Context paramContext) {
		new DisplayMetrics();
		Object obj = paramContext.getApplicationContext().getResources()
				.getDisplayMetrics();
		int i = ((DisplayMetrics) obj).widthPixels;
		int j = ((DisplayMetrics) obj).heightPixels;
		HashMap<String, Integer> obj1 = new HashMap<String, Integer>();
		obj1.put("w", Integer.valueOf(i));
		obj1.put("h", Integer.valueOf(j));
		return obj1;
	}
	
	public static int getCutHeadPicHeight(int i) {
		int j;
		if (i > 0) {
			if (i > 240)
				j = (i * 255) / 480;
			else
				j = 90;
		} else {
			j = 0;
		}
		return j;
	}

	/**
	 * �������
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

	public static boolean isCMWAPMobileNet(Context context) {
		boolean flag = false;
		if (context != null && !isWifi(context)) {
			Object obj = (ConnectivityManager) context
					.getSystemService("connectivity");
			if (obj != null) {
				obj = ((ConnectivityManager) (obj)).getNetworkInfo(0);
				if (obj != null) {
					obj = ((NetworkInfo) (obj)).getExtraInfo();
					if (obj != null
							&& ((String) (obj)).toLowerCase().contains("wap"))
						flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * �Ƿ���wifi
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		boolean i = false;
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if ((networkInfo != null)
				&& (networkInfo.getType() == ConnectivityManager.TYPE_WIFI))
			i = true;
		Log.i("Tools", "isWifi:" + i);
		return i;
	}

	public static String JsonToString(JSONObject jsonobject) {
		String s = "";
		if (jsonobject != null)
			s = jsonobject.toString();
		return s;
	}

	public static void clearCookies(Context context) {
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	/**
	 * Display a simple alert dialog with the given text and title.
	 * 
	 * @param context
	 *            Android context in which the dialog should be displayed
	 * @param title
	 *            Alert dialog title
	 * @param text
	 *            Alert dialog message
	 */
	public static void showAlert(Context context, String title, String text) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(text);
		alertBuilder.create().show();
	}

	/**
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static String htmlDecoder(String s) throws Exception {
		if (s != null && !s.equals("")) {
			s = s.replaceAll("&amp;", "&");
			s = s.replaceAll("&lt;", "<");
			s = s.replaceAll("&rt;", ">");
			s = s.replaceAll("&quot;", "\"");
			s = s.replaceAll("&039;", "'");
			s = s.replaceAll("&nbsp;", " ");
			s = s.replaceAll("&nbsp", " ");
			s = s.replaceAll("<br>", "\n");
			s = s.replaceAll("\r\n", "\n");
			s = s.replaceAll("&#8826;", "?");
			s = s.replaceAll("&#8226;", "?");
			s = s.replaceAll("&#9642;", "?");
			s = s.replaceAll("&amp;", "&");
		}
		return s;
	}

	/**
	 * ʱ���ת����ʱ�䣨yyyy-MM-dd HH:mm:ss��
	 * 
	 * @param lng
	 * @return
	 */
	public static String long2time(long lng) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date(lng * 1000L));
		return date;
	}

	/**
	 * ʱ��ת��ʱ���
	 * 
	 * @param local
	 * @return
	 */
	public static long time2long(String local) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		try {
			date1 = format.parse(local);
			return date1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}