package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BookDetail{
	private Document doc = null;
	private List<String> dossierList = null;
	private List<String> imageLinkList = null;
	private String introduction = null;
	private List<String> linkList = null;

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
			Elements details = doc.select(".span12");
			introduction = details.select("p.ft-12").get(0).text();
			//找到各卷
			Elements contents = doc.select("dd");
			for(Element content : contents){
				imageLinkList.add(content.select("img").get(0).attr("abs:src"));
				Elements dossiers = content.select("h2");
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

	public List<String> getDossierList() {
		return dossierList;
	}

	public List<String> getImageLinkList() {
		return imageLinkList;
	}

	public String getIntroduction() {
		return introduction;
	}

	public List<String> getLinkList() {
		return linkList;
	}
	
	
}