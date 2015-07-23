package com.guohui.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohui.interfacer.XinwenInterface;
import com.guohui.model.Image;
import com.guohui.model.Xinwen;
import com.guohui.util.Constant;
import com.guohui.util.HttpUtil;

import android.os.AsyncTask;
import android.util.Log;
public class DownXinwenShowTask  extends AsyncTask<String, Void, Void>{
	
	XinwenInterface context;
	private List<Image> imgList;
	private List<Xinwen> newsList;
	HashMap<Object, Object> localHashMap = new HashMap<Object, Object>();
	
	public DownXinwenShowTask(XinwenInterface paramArticleInterface) {
		this.context = paramArticleInterface;
	}

	@Override
	protected Void doInBackground(String... paramArrayOfString) {
		try {
			getNewsContent(paramArrayOfString[0], paramArrayOfString[1]);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
	
	public void getNewsContent(String paramString1, String paramString2) {
		String comm_url = Constant.WEB_DIR+Constant.XinwenShowUrl+paramString1;
		
		//Log.e("URL:", comm_url);
		String result = HttpUtil.queryStringForPost(comm_url);
		//Log.e("result:", result);
		try {
			JSONArray picsArray = null;
			JSONObject obj = new JSONObject(result);
			JSONObject dataObj = obj.getJSONObject("data");
			if(!dataObj.isNull("pics")) {
				picsArray = dataObj.getJSONArray("pics");
			}
			JSONArray newsArray = dataObj.getJSONArray("info");
			List<Xinwen> articleList2 = new ArrayList<Xinwen>();
			if (newsArray != null && newsArray.length() > 0) {
				
				for (int i = 0; i < newsArray.length(); i++) {
					JSONObject article_info = newsArray.getJSONObject(i);
					Xinwen article = new Xinwen(
						article_info.getString("type"),
						article_info.getString("message")
					);
					articleList2.add(article);
				}
				this.newsList = articleList2;
			} else {
				this.newsList = articleList2;
			}
			List<Image> imgList2 = new ArrayList<Image>();
			if (picsArray != null && picsArray.length() > 0) {
				for (int i = 0; i < picsArray.length(); i++) {
					Image img = new Image();
					img.setImageURL(picsArray.optString(i));
					imgList2.add(img);
				}
				this.imgList = imgList2;
			} else {
				this.imgList = imgList2;
			}
			localHashMap.put("imgList", this.imgList);
			localHashMap.put("title", dataObj.getString("title"));
			localHashMap.put("catid", dataObj.getString("catid"));
			localHashMap.put("copyfrom", dataObj.getString("copyfrom"));
			localHashMap.put("inputtime", dataObj.getString("inputtime"));
			//Log.e("HashMap", localHashMap.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
	protected void onPostExecute(Void paramVoid) {
		this.context.setNews(this.newsList, this.localHashMap);
	}
}