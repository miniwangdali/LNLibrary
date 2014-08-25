package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Content{
	private Document doc = null;
	private List<String> linkList = null;
	private String text = null;

	public Content(String paramString){
		try{
			text = new String();
			linkList = new ArrayList<String>();
			doc = Jsoup.connect(paramString).userAgent("lnlibrary").timeout(0).get();
			parser();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void parser(){
		Elements mainDivs = doc.select("div#J_view");
		for(Element mainDiv : mainDivs){
			Elements textDivs = mainDiv.select("div > div[id]");
			for(Element textDiv : textDivs){
				Elements imgDivs = textDiv.select("div.lk-view-img");
				if(imgDivs.isEmpty()){
					//文本内容
					text = text + textDiv.text() + "\n";
				}else{
					//图片链接
					text = text + "@img@" + "\n";
					linkList.add(imgDivs.get(0).select("img").get(0).attr("abs:data-cover"));
				}
			}
		}
	}

	public List<String> getLinkList() {
		return linkList;
	}

	public String getText() {
		return text;
	}

	
}