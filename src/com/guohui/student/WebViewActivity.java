package com.guohui.student;


import java.net.URLDecoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends ZYWebActivity{
	
	private boolean openexternal;
	private String title;
	private String url;	
	public WebViewActivity() {
		
	}
	
	protected WebViewClient createWebViewClient() {
		return new MyWebViewClient();
	}
	
	public void onCreate(Bundle bundle) {		
		super.onCreate(bundle);
		boolean flag = false;
		if (getIntent().getData() == null) {
			url = getIntent().getStringExtra("url");
			title = getIntent().getStringExtra("title");
			openexternal = getIntent().getBooleanExtra("openExternal", false);
		} else {
			url = getIntent().getData().getQueryParameter("url");
			title = getIntent().getData().getQueryParameter("title");
			String s = getIntent().getData().getQueryParameter("openexternal");
			if ("1".equals(s) || "true".equals(s))
				flag = true;
			openexternal = flag;
			if (url != null)
				url = URLDecoder.decode(url);
		}
		if (url == null)
			finish();
		webView.loadUrl(url);		
		setTitle(title);
	}
	
	public boolean onKeyDown(int i, KeyEvent keyevent) {
		boolean flag = true;
		if (i != KeyEvent.KEYCODE_BACK || keyevent.getAction() != KeyEvent.ACTION_UP || !webView.canGoBack())
			flag = super.onKeyDown(i, keyevent);
		else
			webView.goBack();
		return flag;
	}
	
	public class MyWebViewClient extends ZYWebActivity.ZYWebViewClient {
		public MyWebViewClient() {
			
		}
		
		public boolean shouldOverrideUrlLoading(WebView webview, String s) {
			boolean flag;
			if (!openexternal)
				flag = super.shouldOverrideUrlLoading(webview, s);
			else if (!s.equals(url)) {
				openExternalUrl(s);
				flag = true;
			} else {
				flag = false;
			}
			return flag;
		}
	}		
}