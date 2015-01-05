package com.victor.lnlibrary.htmlparser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Update {
	
	private Document doc = null;
	private String versionName = null;
	private String newVersionInfo = null;
	private String downloadLink = null;
	
	public Update(){
		try {
			versionName = new String();
			newVersionInfo = new String();
			downloadLink = new String();
			doc = Jsoup.connect("http://miniwangdali.github.io/index").timeout(0).get();
			
			parser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			versionName = null;
			newVersionInfo = null;
			downloadLink = null;
			e.printStackTrace();
		}
	}
	
	private void parser(){
		if(doc != null){
			Elements items = doc.select("span.versioncode");
			setVersionName(items.get(0).text());
			items = doc.select("ul.versioninfo").select("li");
			setNewVersionInfo("更新内容：");
			for(Element element : items){
				setNewVersionInfo(newVersionInfo + "\n" + element.text());
			}
			items = doc.select("a.download");
			setDownloadLink(items.get(0).select("a").attr("abs:href"));
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

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

}
