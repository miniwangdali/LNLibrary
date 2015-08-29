package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchItems{
	private List<String> bookList = null;
	private Document doc = null;
	private List<String> linkList = null;
	private String searchString = null;

	public SearchItems(String keyword){
		try{
			bookList = new ArrayList<String>();
			linkList = new ArrayList<String>();
			searchString = new String();
			searchString = ("http://www.linovel.com/n/search/" + URLEncoder.encode(keyword, "utf8") + ".html");
			searchString = searchString.replace("+", " ");
			doc = Jsoup.connect(searchString).userAgent("lnlibrary").timeout(0).get();
			parser();
		}catch (IOException e){
			e.printStackTrace();
		}

	}

	private void parser(){
		if(doc != null){
			//找到搜索结果
			Elements results = doc.select("li.linovel-item");
			for(Element result : results){
				Element link = result.select("h2").get(0).select("a").get(0);
				linkList.add(link.attr("abs:href"));
				bookList.add(link.text());
			}
		}
	}

	public List<String> getBookList() {
		return bookList;
	}

	public List<String> getLinkList() {
		return linkList;
	}

	
}