package com.guohui.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;

public class FileUtil {

	public static final String ROOTPATH;
	public static final String CACHEPATH;
	public static final String CACHEPATH2;
	public static final String DOWNLOADPATH;
	static {
		ROOTPATH = Environment.getExternalStorageDirectory() + File.separator
				+ "sjb2014";
		CACHEPATH = ROOTPATH;
		CACHEPATH2 = ROOTPATH + File.separator + "photo";
		DOWNLOADPATH = ROOTPATH + File.separator +"download";
	}

	public FileUtil() {
		File tempFile = new File(DOWNLOADPATH);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
	}

	public static String getSoftwareSize(long paramLong) {

		String s;
		if (paramLong >= 1048576L) {
			s = (new StringBuilder()).append(paramLong % 1048576L).toString();
			if (s.length() > 2)
				s = s.substring(0, 2);
			s = (new StringBuilder()).append(paramLong / 1048576L).append(".")
					.append(s).append("M").toString();
		} else {
			s = (new StringBuilder()).append(paramLong / 1024L).append("K")
					.toString();
		}
		return s;
	}

	public static boolean isSDCardMounted() {
		boolean flag;
		if (!Environment.getExternalStorageState().equals("mounted"))
			flag = false;
		else
			flag = true;
		return flag;
	}

	public static boolean isSDCardMountedReadOnly() {
		boolean flag;
		if (!Environment.getExternalStorageState().equals("mounted_ro"))
			flag = false;
		else
			flag = true;
		return flag;
	}

	public static boolean deleteFile(File file) {
		boolean flag = true;
		if (isSDCardMounted()) {
			if (file.exists()) {
				int i = 0;
				if (file.isFile()) {
					file.delete();
				}
				if (file.isDirectory()) {
					File[] f = file.listFiles();
					do {
						if (i >= f.length) {
							file.delete();
							break;
						}
						try {
							deleteFile(f[i]);
						} catch (Exception e) {
							e.printStackTrace();
							flag = false;
						}
						i++;
					} while (true);
				}
				flag = false;
			}
		}
		return flag;
	}

	public static long getFileSize(File paramFile) throws Exception {
		long l1 = 0L;
		if (paramFile.exists()) {
			try {
				File[] arrayOfFile = paramFile.listFiles();
				for (int i = 0; i < arrayOfFile.length; i++) {
					if (!arrayOfFile[i].isDirectory()) {
						long l3 = arrayOfFile[i].length();
						l1 += l3;
					} else {
						l1 += getFileSize(arrayOfFile[i]);
					}
				}
			} catch (Exception localException) {
				localException.printStackTrace();
			} catch (OutOfMemoryError localOutOfMemoryError) {
				localOutOfMemoryError.printStackTrace();
			}
		}
		return l1;
	}

	// ��ȡͼƬ·������
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}
}