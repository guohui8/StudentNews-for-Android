package com.guohui.loader;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
public class AsyncBitmapLoader {
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;
	private ThreadPoolExecutor executor;
	
	public AsyncBitmapLoader() {
		this.imageCache = new HashMap<String, SoftReference<Bitmap>>();
		this.executor = new ThreadPoolExecutor(1, 50, 180, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public Bitmap loadBitMap(final ImageCacher.EnumImageType imgType, final ImageView imageView, final String imageURL, final ImageCallBack imageCallBack) {
		//Log.e("ImageUrl:", imageURL);
		final String folder = ImageCacher.GetImageFolder(imgType);
		File baseDir = new File(folder);
		if(!baseDir.exists()) {
			baseDir.mkdirs();
		}
		if (this.imageCache.containsKey(imageURL)) {
			SoftReference<Bitmap> reference = this.imageCache.get(imageURL);
			Bitmap bitMap = reference.get();
			if (bitMap != null) return bitMap;
		} else {
			String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
			String newBitmapName = bitmapName.replace(".", "_");
            File cacheDir = new File(folder);
            File[] cacheFiles = cacheDir.listFiles();
            if(cacheFiles == null){
            	return null;
            }
            int i = 0;
            for(; i<cacheFiles.length; i++) {
            	if(newBitmapName.equals(cacheFiles[i].getName()))  break;
            }
            if(i < cacheFiles.length) {
            	return BitmapFactory.decodeFile(folder + File.separator + newBitmapName);
            }
		}
		final Handler handler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				imageCallBack.imageLoad(imageView, (Bitmap)msg.obj);
			}
		};

		executor.execute(new Runnable() {
			@Override
			public void run() {
				InputStream localInputStream = null;
				try {
					URL url = new URL(imageURL);
					HttpURLConnection conn = ((HttpURLConnection) url.openConnection());
					conn.setConnectTimeout(6 * 1000);
					conn.setReadTimeout(6 * 1000);
					conn.setRequestMethod("GET");
					conn.setUseCaches(false);
					conn.setInstanceFollowRedirects(true);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						localInputStream = conn.getInputStream();
					} else {
						localInputStream = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Bitmap bitmap = BitmapFactory.decodeStream(localInputStream);
				imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
				Message msg = handler.obtainMessage(0, bitmap);
				handler.sendMessage(msg);
				String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
				String newBitmapName = bitmapName.replace(".", "_");
				File bitmapFile = new File(folder + File.separator + newBitmapName);
				if(!bitmapFile.exists()) {
					try {
                        bitmapFile.createNewFile();
                    } catch (IOException e) {  
                        //e.printStackTrace();
                        Log.e("log","已经存在");
                    }
				}
				FileOutputStream fos;
				try {  
                    fos = new FileOutputStream(bitmapFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                    fos.close();
                } catch (FileNotFoundException e) { 
                	Log.e("log","已经存在");
                   // e.printStackTrace();  
                } catch (IOException e) {  
                	Log.e("log","已经存在");
                   // e.printStackTrace();  
                } catch (Exception e) {
                	Log.e("log","已经存在");
                	//e.printStackTrace();
                }
			}
			
		});
		return null;
	}

	public interface ImageCallBack {
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}
}