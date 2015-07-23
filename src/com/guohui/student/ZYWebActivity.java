package com.guohui.student;


import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ZYWebActivity extends ZYActivity {
	
	protected FrameLayout mask;
	private TextView progressText;
	private ProgressBar progressView;
	private TextView titleView;
	protected WebView webView;

	public ZYWebActivity() {
		
	}

	protected WebChromeClient createWebChromeClient() {
		return new ZYWebChromeClient();
	}

	protected WebViewClient createWebViewClient() {
		return new ZYWebViewClient();
	}

	protected int customTitleType() {
		return 2;
	}

	protected void hideProgress() {
		if (progressView != null)
			progressView.setVisibility(View.GONE);
		if (progressText != null)
			progressText.setVisibility(View.GONE);
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.global_webview);
		webView = (WebView) findViewById(R.id.webview);
		mask = (FrameLayout) findViewById(R.id.mask);
		titleView = (TextView) findViewById(android.R.id.title);
		progressView = (ProgressBar) findViewById(R.id.progress);
		progressText = (TextView) findViewById(R.id.progress_text);
		setupWebSettings(webView.getSettings());
		webView.setScrollBarStyle(0);
		Object obj = createWebChromeClient();
		if (obj != null)
			webView.setWebChromeClient(((WebChromeClient) (obj)));
		obj = createWebViewClient();
		if (obj != null)
			webView.setWebViewClient(((WebViewClient) (obj)));
	}

	protected void onDestroy() {
		super.onDestroy();
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
	}

	protected void openExternalUrl(String s) {
		try {
			startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
		} catch (Exception e) {			
			ConstUtils.showToast(this, (new StringBuilder("�޷���t��\n")).append(s).toString(), Toast.LENGTH_SHORT);
		}
	}

	public void setTitle(CharSequence charsequence) {
		super.setTitle(charsequence);
		if (titleView != null)
			titleView.setText(charsequence);
	}

	protected void setupWebSettings(WebSettings websettings) {
		websettings.setBuiltInZoomControls(false);
		websettings.setSaveFormData(false);
		websettings.setSavePassword(false);
		websettings.setJavaScriptEnabled(true);
		websettings.setSupportZoom(false);
		websettings.setUseWideViewPort(true);
	}

	protected void showProgress(int i) {
		byte byte0 = 0;
		if (progressView != null)
			progressView.setVisibility(0);
		if (progressText != null) {
			TextView textview = progressText;
			if (i < 0)
				byte0 = 8;
			textview.setVisibility(byte0);
			textview = progressText;
			String s;
			if (i >= 0)
				s = (new StringBuilder(String.valueOf(i))).append("%")
						.toString();
			else
				s = null;
			textview.setText(s);
		}
	}

	public class ZYWebChromeClient extends WebChromeClient {

		public ZYWebChromeClient() {
			
		}

		public void onProgressChanged(WebView webview, int i) {
			super.onProgressChanged(webview, i);
			if (i < 100)
				showProgress(i);
			setTitle(webview.getTitle());
		}

	}

	public class ZYWebViewClient extends WebViewClient {

		private long errorMillis = 0L;
		private long startMillis = 0L;

		public ZYWebViewClient() {
			
		}

		public void onPageFinished(WebView webview, String s) {
			super.onPageFinished(webview, s);
			long l = SystemClock.uptimeMillis() - errorMillis;
			if (l <= 0L || l >= 200L) {
				setTitle(webView.getTitle());
				hideProgress();
				if (mask != null) {
					mask.setVisibility(8);
					mask.removeAllViews();
				}
				webView.setVisibility(0);
				long l1 = SystemClock.uptimeMillis() - startMillis;
			}
		}

		public void onPageStarted(WebView webview, String s, Bitmap bitmap) {
			super.onPageStarted(webview, s, bitmap);
			showProgress(-1);
			startMillis = SystemClock.uptimeMillis();
		}

		public void onReceivedError(WebView webview, int i, String s, String s1) {
			super.onReceivedError(webview, i, s, s1);
			errorMillis = SystemClock.uptimeMillis();
			hideProgress();
			webView.setVisibility(4);
			mask.removeAllViews();
			getLayoutInflater().inflate(R.layout.error_item, mask, true);
			mask.findViewById(R.id.btn_retry).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View view) {
							mask.setVisibility(View.GONE);
							webView.reload();
						}

					});
			String s2 = "������ʱ�����ã����Ժ�����";
			try {
				NetworkInfo networkinfo = ((ConnectivityManager) getSystemService("connectivity"))
						.getActiveNetworkInfo();
				if (networkinfo == null || !networkinfo.isConnected())
					s2 = "�޷�l�ӵ�������������l���Ƿ����";
			} catch (Exception e) {
			}
			((TextView) mask.findViewById(android.R.id.text1)).setText(s2);
			mask.setVisibility(View.VISIBLE);
		}

		public boolean shouldOverrideUrlLoading(WebView webview, String s) {
			boolean flag = true;

			if (s.startsWith("http://") || s.startsWith("https://")) {
				if (!s.startsWith("http://maps.google.com/")
						&& !s.startsWith("http://www.youtube.com/")
						&& !s.startsWith("http://market.android.com/")) {
					if (!s.contains("&tag=external")
							&& !s.contains("?tag=external"))
						flag = super.shouldOverrideUrlLoading(webview, s);
					else
						openExternalUrl(s);
				} else {
					openExternalUrl(s);
				}
			} else {
				openExternalUrl(s);
			}
			return flag;
		}

	}
}