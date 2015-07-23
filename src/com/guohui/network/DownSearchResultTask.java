package com.guohui.network;



import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohui.student.SearchResultActivity;
import com.guohui.model.Xinwen;
import com.guohui.util.Constant;
import com.guohui.util.HttpUtil;

import android.os.AsyncTask;
import android.util.Log;
public class DownSearchResultTask extends AsyncTask<String, Void, List<Xinwen>>{
	SearchResultActivity context;
	List<Xinwen> commt = new ArrayList<Xinwen>();

	public DownSearchResultTask(SearchResultActivity paramNewsListActivity) {
		this.context = paramNewsListActivity;
	}

	private List<Xinwen> getComment(String paramString1, int paramInt1) {		
		String comm_url =Constant.WEB_DIR+Constant.GetSearchPageUrl+ "&page="+paramInt1 +"&searchText=" + paramString1;
		String result = HttpUtil.queryStringForPost(comm_url);
		//Log.e("comm_url",comm_url);
		//Log.e("result",result);
		List<Xinwen> localList = null;
		if (result != null) {			
			try {
				JSONArray array = new JSONArray(result);
				if (array != null && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jObj = array.getJSONObject(i);
						Xinwen comt = new Xinwen();
						comt.setId(jObj.getString("id"));
						comt.setTitle(jObj.getString("title"));
						comt.setChannel(jObj.getString("catid"));
						comt.setPosttime(jObj.getString("regdate"));
						commt.add(comt);
					}
					localList = commt;
				}
				return localList;
			} catch (JSONException e) {
				localList=null;
			}
		}		
		return null;
	}

	@Override
	protected List<Xinwen> doInBackground(String... paramArrayOfString) {
		List<Xinwen> localList1 = null;
		try {
			localList1 = getComment(paramArrayOfString[0],
					Integer.parseInt(paramArrayOfString[1]));
			return localList1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localList1;
	}

	protected void onPostExecute(List<Xinwen> paramList) {
		this.context.setComment(paramList);
	}
}