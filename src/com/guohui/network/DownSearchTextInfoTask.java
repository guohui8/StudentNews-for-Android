package com.guohui.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohui.student.SearchResultActivity;
import com.guohui.util.Constant;
import com.guohui.util.HttpUtil;

import android.os.AsyncTask;
import android.util.Log;

public class DownSearchTextInfoTask extends AsyncTask<String, Void, List<Map<String,String>>> {

	SearchResultActivity context;	
	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	
	public DownSearchTextInfoTask(SearchResultActivity paramActivity) {
		this.context = paramActivity;
	}
	
	public static  List<Map<String,String>> getSearchList(String paramString1){
        
        List<Map<String,String>> list = new ArrayList<Map<String,String>>(); 
        String comm_url = Constant.WEB_DIR+Constant.GetSearchTextUrl+"&searchText="+paramString1;	
		String result = HttpUtil.queryStringForPost(comm_url);
		//Log.i("requestUrl:", comm_url);
		//Log.i("RESULT:", result);
		if (result != null) {
			try {
				JSONArray array = new JSONArray(result);
				if (array != null && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {	
						JSONObject jObj = array.getJSONObject(i);
						Map<String,String> tempmap = new HashMap<String,String>();
						tempmap.put("title", jObj.getString("title")); 
						//Log.e("title",jObj.getString("title"));
			            list.add(tempmap);
					}
				}
				return list;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;          
    }
	
	
	@Override
	protected List<Map<String, String>> doInBackground(String... paramArrayOfString) {
		List<Map<String, String>> localList1 = null;
		try {
			localList1 = getSearchList(paramArrayOfString[0]);
			return localList1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localList1;
	}
	
	protected void onPostExecute(List<Map<String, String>> paramList) {
		this.context.setSearchList(paramList);
	}

}