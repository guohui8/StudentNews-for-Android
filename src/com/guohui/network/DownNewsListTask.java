package com.guohui.network;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohui.db.DBUtils;
import com.guohui.student.NewsListActivity;
import com.guohui.model.News;
import com.guohui.util.Constant;
import com.guohui.util.HttpUtil;


import android.os.AsyncTask;
import android.util.Log;

public class DownNewsListTask extends AsyncTask<String, Void, List<News>> {
	NewsListActivity context;
	List<News> commt = new ArrayList<News>();

	public DownNewsListTask(NewsListActivity paramNewsListActivity) {
		this.context = paramNewsListActivity;
	}

	private List<News> getComment(String paramString1, String paramString2,
			int paramInt1, String paramString3, int paramInt2) {
		DBUtils db = new DBUtils(this.context);
		String comm_url =Constant.WEB_DIR+Constant.NewsListInfoUrl+ "&page="+paramInt2 +"&clsid=" + paramString1;
		String result = HttpUtil.queryStringForPost(comm_url);
		// Log.e("comm_url",comm_url);
		// Log.e("result",result);
		List<News> localList = null;
		if (result != null) {
			if (paramString2.equals("true")) {
				
				DBUtils.deleteFromeNewsByType(db.database, paramString1);
			}
			try {
				JSONArray array = new JSONArray(result);
				if (array != null && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jObj = array.getJSONObject(i);
						News comt = new News();
						comt.setId(jObj.getString("id"));
						comt.setTitle(jObj.getString("title"));
						comt.setDesc(jObj.getString("desc"));
						comt.setChannel(jObj.getString("catid"));
						comt.setPicurl(jObj.getString("avatar"));
						comt.setPosttime(jObj.getString("regdate"));
						comt.setHits(jObj.getString("hits"));
						commt.add(comt);
						
						String news_id = jObj.getString("id");
						if ((paramInt2 == 0) && (paramString2.equals("true"))) {
							DBUtils.insertDataToNewsTable(db.database,
									news_id, jObj.getString("title"), jObj.getString("catid"),
									jObj.getString("avatar"),
									jObj.getString("desc"), "1", jObj.getString("regdate"), jObj.getString("hits"));
						}
					}
					db.close();
					localList = commt;
				}
				return localList;
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				if (db != null)
					db.close();
			}
		}
		return null;
	}

	@Override
	protected List<News> doInBackground(String... paramArrayOfString) {
		List<News> localList1 = null;
		try {
			localList1 = getComment(paramArrayOfString[0],
					paramArrayOfString[1],
					Integer.parseInt(paramArrayOfString[2]),
					paramArrayOfString[3],
					Integer.parseInt(paramArrayOfString[4]));
			return localList1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localList1;
	}

	protected void onPostExecute(List<News> paramList) {
		this.context.setComment(paramList);
	}
}