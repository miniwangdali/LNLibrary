package com.victor.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.htmlparser.Update;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateUtil {

	//检查更新的URL
	private static String CHECKURL = "http://...";
	//apk下载路径
	private static String downloadUrl = "";
	//下载包本地存储路径及名称
	private static String savePath = Environment.getExternalStorageDirectory() + "/LNLibrary/";
	private static String fileName = "LNLibrary.apk";
	
	
    private Dialog noticeDialog;
    private ProgressBar mProgress;
    private boolean interceptFlag = false;
    private Dialog downloadDialog;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    
	private String versionName;
	private String newVersionName;
	private String newVersionInfo;
	private Context mContext;
	
	public UpdateUtil(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		try {
			versionName = context.getPackageManager().getPackageInfo(
			        context.getPackageName(), 0).versionName;
			Update mUpdate = new Update();
			newVersionName = mUpdate.getVersionName();
			newVersionInfo = mUpdate.getNewVersionInfo();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			versionName = null;
		}
	}
	
	private boolean checkUpdate(){
		if(versionName != getNewVersion() && getNewVersion() != null){
			return true;
		}else{
			return false;
		}
	}

	private String getNewVersion(){
		return newVersionName;
	}
	/**
     * 版本更新入口
     */
	public boolean update(){
		try{
			if(checkUpdate()){
				showNoticeDialog();
			}
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(mContext, "更新失败：" + e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	/**
     * 更新UI的Handler
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWN_UPDATE:
                mProgress.setProgress(progress);
                break;
            case DOWN_OVER:
                if (downloadDialog != null)
                    downloadDialog.dismiss();
                installApk();
                break;
            default:
                break;
            }
        };
    };
    /**
     * 弹出更新提示对话框
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("新版本");
        builder.setMessage(newVersionInfo);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
        // 设置弹出对话框大小属性
        /*WindowManager.LayoutParams lp = noticeDialog.getWindow()
                .getAttributes();
        lp.width = 400;
        lp.height = 500;
        noticeDialog.getWindow().setAttributes(lp);*/
    }
    /**
     * 下载进度对话框
     */
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("正在下载，请稍后...");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }
    /**
     * 启动线程下载apk
     */
    private void downloadApk() {
        Thread downLoadThread = new Thread(downApkRunnable);
        downLoadThread.start();
    }
    /**
     * 安装包下载线程
     */
    private Runnable downApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                
                File apkFile = new File(savePath + fileName);
                FileOutputStream fos = new FileOutputStream(apkFile);
 
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    handler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        handler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
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
        mContext.startActivity(i);
    }
}
