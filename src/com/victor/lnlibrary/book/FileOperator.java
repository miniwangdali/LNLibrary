package com.victor.lnlibrary.book;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperator{
	private static String FILEPATH = "/LNLibrary/";
	private String SDPATH;
	private boolean hasSD = false;
	
	public FileOperator(){
		//得到当前外部存储设备的目录      /SDCARD/...
		hasSD = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	private File createSDDir(String dirName){
		File file = new File(this.SDPATH + FILEPATH + dirName);
		file.mkdirs();
		return file;
	}

	private File createSDFile(String fileName) throws IOException{
		File file = new File(this.SDPATH + FILEPATH + fileName);
		file.createNewFile();
		return file;
	}

	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + FILEPATH + fileName);
		return file.exists();
	}

	public boolean writeFile(String foldername, String bookname, String content){
		File file = null;
		FileOutputStream fos = null;
		try{
			createSDDir(foldername);
			file = createSDFile(foldername + "/" + bookname + ".txt");
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
			
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean writeFile(String foldername, String filename, Bitmap image){
		File file = null;
		FileOutputStream fos = null;
		try{
			createSDDir(foldername);
			file = createSDFile(foldername + "/" + filename + ".txt");
			fos = new FileOutputStream(file);
			image.compress(CompressFormat.PNG, 100, fos);
			
			fos.flush();
			fos.close();
			
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	public File[] readFiles(String folderPath){
		try{
			File file = new File(SDPATH + FILEPATH + folderPath);
			return file.listFiles();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public boolean isHasSD() {
		return hasSD;
	}

}