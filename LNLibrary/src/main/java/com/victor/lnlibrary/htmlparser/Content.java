package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
		Elements mainDivs = doc.select("div.content");
		String contentScript = mainDivs.get(0).select("script").get(1).toString();

		//analyze the script to find content
		contentScript = contentScript.substring(contentScript.indexOf("content") + 7); 	//save string after "var content"
		contentScript = contentScript.substring(contentScript.indexOf("content") + 9); 	//save string after "content:["
		contentScript = contentScript.substring(0, contentScript.indexOf("]\n};"));		//save string before the end - "]};"
		List<String> contents = new ArrayList<String>();
		String[] tempContents = contentScript.split("\\{");
		for(int i = 0; i < tempContents.length; i ++){
			contents.add(tempContents[i]);
		}
		contents.remove(0);
		for(String content : contents){
			//get content
			content = content.substring(content.indexOf("content") + 10);
			content = content.substring(0, content.indexOf("isSpace") - 3);
			//whether it is an image
			if(content.contains("[img]")){
				content = content.substring(content.indexOf("[img]") + 5);
				content = content.substring(0, content.indexOf("[/img]"));
				text = text + "@img@" + "\n";
				linkList.add("http://www.linovel.com" + content);
			}else{
				text = text + content;
			}
		}

//		for(Element mainDiv : mainDivs){
//			Elements textDivs = mainDiv.select("p");
//			for(Element textDiv : textDivs){
//				Elements imgDivs = textDiv.select("img");
//				if(imgDivs.isEmpty()){
//					//文本内容
//					text = text + textDiv.select("span").get(0).text() + "\n";
//				}else{
//					//图片链接
//					text = text + "@img@" + "\n";
//					linkList.add(imgDivs.get(0).attr("abs:src"));
//				}
//			}
//		}
	}

	public List<String> getLinkList() {
		return linkList;
	}

	public String getText() {
		return text;
	}

	
}