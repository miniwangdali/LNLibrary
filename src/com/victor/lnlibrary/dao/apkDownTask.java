package com.victor.lnlibrary.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.lnlibrary.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class apkDownTask extends AsyncTask<String, Integer, String> {

	private Activity mActivity;
	private String url;
	private static String savePath = Environment.getExternalStorageDirectory() + "/LNLibrary/";
	private static String fileName = "LNLibrary.apk";
	private int progress;
	
	//通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;

	
	public apkDownTask(Activity activity, String link) {
		super();
		// TODO Auto-generated constructor stub
		mActivity = activity;
		url = link;
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try {
            URL mUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            //conn.setConnectTimeout(10000);
            //conn.setReadTimeout(20000);
            conn.connect();
            int length = conn.getContentLength();
            InputStream is = conn.getInputStream();
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdir();
            }
            
            File apkFile = new File(savePath + fileName);
            if(apkFile.exists()){
            	apkFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(apkFile);

            int count = 0;
            byte buf[] = new byte[1024];
            while(true){
                int numread = is.read(buf);
                count += numread;
                progress = (int) (((float) count / length) * 100);
                // 更新进度
                publishProgress(progress);
                //下载完成
                if(numread <= 0){
                	break;
                }
                fos.write(buf, 0, numread);
            }
            fos.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		updateNotificationManager = (NotificationManager)mActivity.getSystemService(mActivity.NOTIFICATION_SERVICE);
		updateNotification = new Notification();
		
		//设置通知栏显示内容
		updateNotification.icon = R.drawable.icon;
		updateNotification.tickerText = "正在下载…";
		updateNotification.setLatestEventInfo(mActivity, "轻小说文库", "0%", updatePendingIntent);
		
		updateNotificationManager.notify(0, updateNotification);
		
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		
		updateNotification.setLatestEventInfo(mActivity, "轻小说文库", progress + "%", updatePendingIntent);
		
		updateNotificationManager.notify(0, updateNotification);
		
		super.onProgressUpdate(values);
	}

	/**
     * 安装APK
     */
    private void installApk() {
        File apkFile = new File(savePath + fileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                "application/vnd.android.package-archive");
        //mActivity.startActivity(i);
    }
	
}
