package com.guohui.service;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.guohui.student.MainActivity;
import com.guohui.student.R;
import com.guohui.util.ConstUtils;
import com.guohui.util.FileUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Download extends Service {

	private static final int ID_NOTIFICATION = 1000;
	private NotificationManager notificationManager;
	private Notification notification;
	private RemoteViews remoteViews;
	private MyHandler myHandler;
	private long downloadSize = 0;
	private long totalSize;
	private int download_precent = 0;
	private MyThread myThread;
	private String download_url;
	private File tempFile = null;//临时文件
	private boolean cancleDownload = false;
	
	private PendingIntent pIntent;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		CreateNotification(intent.getStringExtra("title"));
		this.myHandler = new MyHandler(Looper.myLooper(), this);
		Message message = this.myHandler.obtainMessage(3);
		this.myHandler.sendMessage(message);
		this.download_url = intent.getStringExtra("url");
		this.myThread = new MyThread();
		this.myThread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void CreateNotification(String title) {
		this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.notification = new Notification();
		this.notification.icon = R.drawable.biz_offline_icon_download;
		this.notification.tickerText = title;
		this.notification.when = System.currentTimeMillis();
		this.notification.defaults = Notification.DEFAULT_LIGHTS;
		this.remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
		this.notification.contentView = this.remoteViews;
		
		Intent localIntent = new Intent(this, MainActivity.class);
        localIntent.putExtra("pintent", "pintent");
		this.pIntent = PendingIntent.getActivity(this, 0, localIntent, 0);
		//this.notification.setLatestEventInfo(this, "", "", this.pIntent);
        this.notification.contentIntent = this.pIntent;
		this.notificationManager.notify(ID_NOTIFICATION, notification);
	}
	
	@SuppressWarnings("unused")
	private void setNotification(int paramInt1, int paramInt2, String paramString, int paramInt3) {
		this.remoteViews = new RemoteViews(getPackageName(), paramInt1);
	    this.notification.icon = paramInt2;
	    this.notification.contentView = this.remoteViews;
	    this.notification.contentIntent = this.pIntent;
	    this.notification.flags = paramInt3;
	}

	class MyHandler extends Handler {
		
		private Context context;
		
		public MyHandler(Looper looper, Context context) {
			super(looper);
			this.context = context;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg != null) {
				switch(msg.what) {
					case 0:
					case 1:	
						ConstUtils.showToast(context, msg.obj.toString(), Toast.LENGTH_SHORT);
						break;
					case 2:
						//下载完成
						download_precent = 0;
						notification.icon = R.drawable.biz_offline_icon_success;
						notification.tickerText = "成功下载更新程序";
						notification.flags = Notification.FLAG_ONGOING_EVENT;//显示在正在运行下面
						remoteViews = new RemoteViews(getPackageName(), R.layout.notification_result);
						remoteViews.setTextViewText(R.id.download_notify_result, "成功下载更新程序");
						//remoteViews.setTextViewText(R.id.download_notify_info, "无网络信号时，您也可以阅读新闻了" + new SimpleDateFormat("HH:mm").format(new Date()));
						notification.contentView = remoteViews;
						notificationManager.notify(ID_NOTIFICATION, notification);
						notificationManager.cancel(ID_NOTIFICATION);
						Install((File) msg.obj, context);
						// 停止掉当前的服务
						stopSelf();
						break;
					case 3:
						//计算百分比
						StringBuffer sb = new StringBuffer();
						sb.append("已下载");
						sb.append(download_precent);
						sb.append("%(");
						sb.append(FileUtil.getSoftwareSize(downloadSize));
						sb.append("/");
						sb.append(FileUtil.getSoftwareSize(totalSize));
						sb.append(")");
						remoteViews.setTextViewText(R.update_id.tvProcess, sb.toString());
						remoteViews.setProgressBar(R.update_id.pbDownload, 100,
								download_precent, false);
						notification.contentView = remoteViews;
						notificationManager.notify(ID_NOTIFICATION, notification);
						break;
					case 4:
						notification.icon = R.drawable.biz_offline_icon_failture;
						notification.tickerText = "下载更新程序失败！";
						remoteViews.setTextViewText(R.update_id.tvProcess, "下载更新程序失败");
						remoteViews.setProgressBar(R.update_id.pbDownload, 100, download_precent, false);
						notification.contentView = remoteViews;
						notificationManager.notify(ID_NOTIFICATION, notification);
						notificationManager.cancel(ID_NOTIFICATION);
						break;
				}
			}
		}

		
	}
	
	class MyThread extends Thread {
		
		public void run() {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(download_url);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				totalSize = entity.getContentLength();//获取文件总长度
				InputStream is = entity.getContent();
				if (is != null) {
					File rootFile = new File(FileUtil.DOWNLOADPATH);
					if (!rootFile.exists() && !rootFile.isDirectory())
						rootFile.mkdirs();
					tempFile = new File(FileUtil.DOWNLOADPATH + File.separator 
							
							+ download_url.substring(download_url.lastIndexOf(File.separator) + 1));
					if (tempFile.exists())
						tempFile.delete();
					tempFile.createNewFile();
					// 已读出流作为参数创建一个带有缓冲的输出流
					BufferedInputStream bis = new BufferedInputStream(is);
					// 创建一个新的写入流，讲读取到的图像数据写入到文件中
					FileOutputStream fos = new FileOutputStream(tempFile);
					// 已写入流作为参数创建一个带有缓冲的写入流
					BufferedOutputStream bos = new BufferedOutputStream(fos);

					int read;
					int precent = 0;
					byte[] buffer = new byte[1024];
					while ((read = bis.read(buffer)) != -1 && !cancleDownload) {
						bos.write(buffer, 0, read);
						downloadSize += read;
						precent = (int) (((double) downloadSize / totalSize) * 100);
						// 每下载完成3%就通知任务栏进行修改下载进度
						if (precent - download_precent >= 5) {
							download_precent = precent;
							Message message = myHandler.obtainMessage(3,
									precent);
							myHandler.sendMessage(message);
						}
					}
					bos.flush();
					bos.close();
					fos.flush();
					fos.close();
					is.close();
					bis.close();
				}

				if (!cancleDownload) {
					Message message = myHandler.obtainMessage(2, tempFile);
					myHandler.sendMessage(message);
				} else {
					tempFile.delete();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Message message = myHandler.obtainMessage(4, "下载更新文件失败");
				myHandler.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
				Message message = myHandler.obtainMessage(4, "下载更新文件失败");
				myHandler.sendMessage(message);
			} catch (Exception e) {
				Message message = myHandler.obtainMessage(4, "下载更新文件失败");
				myHandler.sendMessage(message);
			}
			
		}
	}
	
	private void Install(File file, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}	
}