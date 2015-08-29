package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BookDetail{
	private Document doc = null;
	private ArrayList<String> dossierList = null;
	private ArrayList<String> imageLinkList = null;
	private String introduction = null;
	private ArrayList<String> linkList = null;

	public BookDetail(String link, String name){
		try{
			introduction = new String();
			dossierList = new ArrayList<String>();
			linkList = new ArrayList<String>();
			imageLinkList = new ArrayList<String>();
			doc = Jsoup.connect(link).userAgent("lnlibrary").timeout(0).get();
			parser();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	private void parser(){
		if(doc != null){
			//找到信息
			Elements details = doc.select("div.linovel-detail-result");
			introduction = doc.select("p.linovel-info-desc").get(0).text();
			//找到各卷
			Elements contents = doc.select("li.linovel-book-item");
			for(Element content : contents){
				imageLinkList.add(content.select("img").get(0).attr("abs:src"));
				Elements dossiers = content.select("h3");
				for(Element dossier : dossiers){
					dossierList.add(dossier.text());
					Elements aElements = dossier.select("a");
					for(Element aElement : aElements){
						linkList.add(aElement.attr("abs:href"));
					}
				}
			}
		}
	}

	public ArrayList<String> getDossierList() {
		return dossierList;
	}

	public ArrayList<String> getImageLinkList() {
		return imageLinkList;
	}

	public String getIntroduction() {
		return introduction;
	}

	public ArrayList<String> getLinkList() {
		return linkList;
	}
	
	
}