package com.victor.lnlibrary.htmlparser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Update {
	
	private Document doc = null;
	private String versionName = null;
	private String newVersionInfo = null;
	
	public Update(){
		try {
			setVersionName(new String());
			setNewVersionInfo(new String());
			doc = Jsoup.connect("http://miniwangdali.github.io/index").timeout(0).get();
			
			parser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parser(){
		if(doc != null){
			Elements items = doc.select("span");
			setVersionName(items.get(0).text());
			items = doc.select("p");
			setNewVersionInfo(items.get(0).text());
		}
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getNewVersionInfo() {
		return newVersionInfo;
	}

	public void setNewVersionInfo(String newVersionInfo) {
		this.newVersionInfo = newVersionInfo;
	}

}
