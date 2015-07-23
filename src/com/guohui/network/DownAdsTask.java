package com.guohui.network;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohui.student.XinwenShowActivity;
import com.guohui.util.Constant;
import com.guohui.util.HttpUtil;

import android.os.AsyncTask;
import android.util.Log;

public class DownAdsTask extends AsyncTask<String, Void, HashMap<Object, Object>> {
	XinwenShowActivity context;	
	public DownAdsTask(XinwenShowActivity paramContext) {
		this.context = paramContext;
	}
	
	protected HashMap<Object, Object> downloadAdsText(String paramString1) {
		HashMap<Object, Object> localHashMap;
		String comm_url=Constant.WEB_DIR+Constant.NewsTextUrl;
		//Log.e("URL:", comm_url);
		String result = HttpUtil.queryStringForPost(comm_url);
		//Log.e("RESULT:", result);
		try {
			localHashMap = new HashMap<Object, Object>();
			JSONObject obj = new JSONObject(result);			
			localHashMap.put("isshow", obj.optString("isshow"));
		} catch (JSONException e) {
			localHashMap = null;
			//e.printStackTrace();			
		}
		return localHashMap;
	}

	@Override
	protected HashMap<Object, Object> doInBackground(String... params) {
		return downloadAdsText(params[0]);
	}
	
	protected void onPostExecute(HashMap<Object, Object> paramHashMap) {
		//this.context.setAdsText(paramHashMap);
	}
}