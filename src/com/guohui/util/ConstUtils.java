package com.guohui.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.format.DateFormat;
import android.view.Menu;
import android.widget.Toast;

public class ConstUtils {

	private static Toast toast;
	public static final String[] CHANNEL;
	
	//主菜单
	public static final int ABOUT_ID = Menu.FIRST;
	public static final int SETTING_ID = Menu.FIRST + 1;
	public static final int EXIT_ID = Menu.FIRST + 2;
	public static final int BACK_INDEX = Menu.FIRST + 3;
	public static final int FAV_ID = Menu.FIRST + 4;
	public static final int SEE_ID = Menu.FIRST + 5;
	public static final int BZ_ID = Menu.FIRST+6;
	static {
		String[] arrayOfString1 = new String[3];
	    arrayOfString1[0] = "news";
	    arrayOfString1[1] = "finance";
	    arrayOfString1[2] = "tech";
	    CHANNEL = arrayOfString1;
	}
	public static void cancleToast() {
		if (toast != null)
			toast.cancel();
	}
	/**
	 * 时间戳转换时间 （1970-01-01 00:00:00）
	 * @param paramLong 时间戳
	 * @return yyyy-MM-dd kk:mm:ss 时间字符串
	 */
	public static String createTime(long paramLong) {
		return DateFormat.format("yyyy-MM-dd kk:mm:ss", paramLong).toString();
	}
	
	public static void deleteIMGFromDB(Context context, String s, String s1) {
		
	}

	/**
	 * 已实例的 Toast 弹窗
	 * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
	 * @param paramInt 指定信息ID {@linkplain R.string}
	 */
	public static void showToast(Context context, int paramInt) {
		cancleToast();
		toast = Toast.makeText(context, paramInt, Toast.LENGTH_SHORT);
		toast.setGravity(17, 0, 0);
		toast.show();
	}
	
	public static int getWordCount(String s)
    {

        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }
	/**
     * 已实例Toast.LENGTH_SHORT 的 Toast 弹窗
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     */
	public static void showToast(Context context, String text) {
		cancleToast();
		toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(17, 0, 0);
		toast.show();
	}
	/**
     * 已实例的 Toast 弹窗
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     */
	public static void showToast(Context context, String text,
			int duration) {
		cancleToast();
		toast = Toast.makeText(context, text, duration);
		toast.setGravity(17, 0, 0);
		toast.show();
	}
	/**
	 * 检测是否有网络
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
		return bool;
	}
}