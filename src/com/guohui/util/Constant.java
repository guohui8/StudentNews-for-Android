package com.guohui.util;

import java.io.File;
import com.guohui.student.R;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 全局常量配置文件
 * 
 * @Module com.guohui.utils.Constant
 * @description
 * @author guohui
 * @version v1.1
 * @created 2012-9-25 上午11:12:36
 */
public class Constant {
	/**
	 * 切换项目测试与非测试环境
	 */
	public static boolean debug = true;
	public static String WEB_DIR = "http://www.guohui8.com/";
	// 获取远程链接地址
	//新闻地址
	public static String XinwenListInfoUrl = "getStudentJson.php?format=getXinwenListInfo";
	
	//新闻内容详情页
	public final static String XinwenShowUrl = "getStudentJson.php?format=xinwenshowlist&contentid=";
	
	//图片地址
	public static String NewsListInfoUrl = "getStudentJson.php?format=getPicListInfo";
	public static String ImageShowUrl = "getStudentJson.php?format=newsimagelist&pid=";	 
	public static String GetSoftXX = "getMobileSoft.php?format=getMobile_student&id=";
	public final static String GetSearchTextUrl = "getStudentJson.php?format=getSearchText";
	public final static String GetSearchPageUrl = "getStudentJson.php?format=getSearchPage";
	//开启广告地址
	public final static String NewsTextUrl="getStudentJson.php?format=getIsAds";
	
	//检查版本更新
	public static final String UPDATEURL	=	WEB_DIR + "getStudentVersion.php";
	/*
	 * 缓存文件夹
	 */
	public static String sportDir = Environment.getExternalStorageDirectory()
			+ File.separator + "sjb2014" + File.separator;
	public static String NewsimgDir = sportDir + "newsimg" + File.separator;
	public static String DownloadDir = sportDir + "download" + File.separator;
	public static String NERTWORKERR = "网络异常!";

	/**
	 * 获取应用程序名称
	 * 
	 * @param context
	 * @return 应用程序名称
	 * @deprecated
	 */
	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name)
				.toString();
		return verName;
	}
	public static int getVerCode(Context _context, String _package) {
		int verCode = -1;
		try {
			verCode = _context.getPackageManager().getPackageInfo(_package, 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}
	public static String getVerName(Context _context, String _package) {
		String verName = "";
		try {
			verName = _context.getPackageManager().getPackageInfo(_package, 0).versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;
	}
	public static String[] getMobilePhoneInformation(Context context) {		
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String[] paramString = new String[6];
		paramString[0] = telephonyManager.getDeviceId();
		paramString[1] = Build.MODEL.replace(" ", "");
		paramString[2] = Build.VERSION.SDK;
		paramString[3] = Build.VERSION.RELEASE;
		paramString[4] = telephonyManager.getSubscriberId();
		paramString[5] = Constant.getVerName(context,"com.guohui.sport");
		return paramString;
	}
	
}