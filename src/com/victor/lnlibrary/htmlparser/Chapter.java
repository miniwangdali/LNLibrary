package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Chapter{
	private String briefintroduction = null;
	private List<String> chapterList = null;
	private Document doc = null;
	private List<String> linkList = null;

	public Chapter(String link){
		try{
			chapterList = new ArrayList<String>();
			linkList = new ArrayList<String>();
			briefintroduction = new String();
			doc = Jsoup.connect(link).userAgent("lnlibrary").timeout(0).get();
			parser();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void parser(){
		if(doc != null){
			Elements divs = doc.select("div.span10");
			for(Element div : divs){
				Elements introductions = div.select("p[style]");
				briefintroduction = introductions.get(0).text();
			}
			Elements div2s = doc.select("div.span9");
			for(Element div : div2s){
				Elements lis = div.select("li");
				for(Element li : lis){
					chapterList.add(li.text());
					linkList.add(li.select("a[href]").attr("abs:href"));
				}
			}
		}
	}

	public String getBriefintroduction() {
		return briefintroduction;
	}

	public List<String> getChapterList() {
		return chapterList;
	}

	public List<String> getLinkList() {
		return linkList;
	}
	
	
}