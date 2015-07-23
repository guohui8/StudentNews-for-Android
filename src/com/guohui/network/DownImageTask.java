package com.guohui.network;

import java.lang.ref.WeakReference;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.guohui.interfacer.DownImageInterface;
import com.guohui.util.ImageUtil;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownImageTask extends AsyncTask<String, Void, Bitmap> {

	private HttpClient httpClient;
	private HttpParams httpParams;
	private int idx;
	private WeakReference<ImageView> imageViewReference;
	private DownImageInterface obj;
	private int pos;
	private boolean refresh = true;

	public DownImageTask(int paramInt1, int paramInt2,
			DownImageInterface paramDownImageInterface) {
		this.obj = paramDownImageInterface;
		this.idx = paramInt1;
		this.pos = paramInt2;
	}

	public DownImageTask(String paramString, int paramInt1, int paramInt2,
			DownImageInterface paramDownImageInterface) {
		this.obj = paramDownImageInterface;
		this.idx = paramInt1;
		this.pos = paramInt2;
	}

	/*
	 * @Override protected Bitmap doInBackground(String... params) { // TODO
	 * Auto-generated method stub return null; }
	 */
	protected Bitmap doInBackground(String... paramArrayOfString) {
		return downloadBitmap(paramArrayOfString[0], paramArrayOfString[1],
				paramArrayOfString[2]);
	}

	Bitmap downloadBitmap(String paramString1, String paramString2,
			String paramString3) {
		Bitmap bitmap = null;
		if (paramString1 != null && paramString1.trim().length() >= 1) {
			bitmap =ImageUtil.createFromStream(paramString1);
			return bitmap;
		} else {
			return bitmap;
		}
	}

	public HttpClient getHttpClient() {
		this.httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(this.httpParams, 20000);
		HttpConnectionParams.setSoTimeout(this.httpParams, 20000);
		HttpConnectionParams.setSocketBufferSize(this.httpParams, 8192);
		HttpClientParams.setRedirecting(this.httpParams, true);
		HttpProtocolParams.setUserAgent(this.httpParams,
				"ATI10_B1/100007&128/000000&ADR&203016&Android&V2");
		this.httpClient = new DefaultHttpClient(this.httpParams);
		return this.httpClient;
	}

	protected void onPostExecute(Bitmap paramBitmap) {
		if (isCancelled())
			paramBitmap = null;
		if (this.imageViewReference != null) {
			ImageView localImageView = (ImageView) this.imageViewReference
					.get();
			if (localImageView != null)
				localImageView.setImageBitmap(paramBitmap);
		}		
		this.obj.notifyChangeImage(this.idx, this.pos, paramBitmap);
	}

}