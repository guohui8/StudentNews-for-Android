package com.guohui.interfacer;

import java.util.List;
import android.content.Context;
public abstract interface ArticleInterface {

	public abstract Context getContext();

	public abstract void notifyChangeShare(int paramInt1, int paramInt2);

	public abstract void setArticle(List paramList1, List paramList2,
			String paramString);	
}
