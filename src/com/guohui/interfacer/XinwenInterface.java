package com.guohui.interfacer;

import java.util.HashMap;
import java.util.List;
import com.guohui.model.Xinwen;

import android.content.Context;
public  abstract interface XinwenInterface {
	public abstract Context getContext();

	public abstract void notifyChangeShare(int paramInt1, int paramInt2);

	public abstract void setNews(List<Xinwen> paramList1, HashMap<Object, Object> paramHashMap);

}
