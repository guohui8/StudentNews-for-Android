package com.guohui.network;


import com.guohui.util.Constant;
import com.guohui.util.HttpUtil;
import android.os.AsyncTask;

public class DownMobileTask extends AsyncTask<String, Void, String> {

	private String subMobile(String paramString1, String paramString2,
			String paramString3, String paramString4, String paramString5, String paramString6) {
		String comm_url = Constant.WEB_DIR+Constant.GetSoftXX + paramString1
				+ "&model=" + paramString2 + "&sdk=" + paramString3
				+ "&release=" + paramString4 + "&imsi=" + paramString5+"&versionname="+paramString6;
		HttpUtil.queryStringForPost(comm_url);
		//Log.e("comm_url",comm_url);
		return null;
	}

	@Override
	protected String doInBackground(String... paramsString) {

		return subMobile(paramsString[0], paramsString[1], paramsString[2],
				paramsString[3], paramsString[4], paramsString[5]);
	}
}
