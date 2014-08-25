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
			searchString = ("http://lknovel.lightnovel.cn/main/booklist/" + URLEncoder.encode(keyword, "utf8") + "/");
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
			Elements results = doc.select("li.inline");
			for(Element result : results){
				Elements books = result.select(".lk-ellipsis");
				for(Element book : books){
					if(!bookList.contains(book.text())){
						linkList.add(book.attr("abs:href"));
						bookList.add(book.text());
					}
					
				}
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