package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BookBrief{
	private String author = null;
	private String booklink = null;
	private Document doc = null;
	private String illustrator = null;
	private String imageLink = null;
	private String newest = null;
	private String publisher = null;
	private ArrayList<String> tempList = null;
	private String title = null;
	private String updatetime = null;

	public BookBrief(String link, String name){
		try{
			this.title = new String();
			this.title = name;
			this.booklink = new String();
			this.booklink = link;
			this.imageLink = new String();
			this.author = new String();
			this.illustrator = new String();
			this.publisher = new String();
			this.updatetime = new String();
			this.newest = new String();
			this.tempList = new ArrayList<String>();
			this.doc = Jsoup.connect(link).userAgent("lnlibrary").timeout(0).get();
			parser();

		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void parser(){
		if(doc != null){
			//找到信息
			Elements details = doc.select("div.linovel-detail-title");
			imageLink = details.select("div.linovel-cover").select("img").get(0).attr("abs:data-cover");
			
			for(Element detail : details){
//				Elements tds = detail.select("span");
//				for(Element td : tds){
//					tempList.add(td.text());
//				}
//				Elements ns = detail.select("td[colspan=3]");
//
//				//录入信息
//				author = tempList.get(0);
//				illustrator = tempList.get(1);
//				publisher = tempList.get(2);
//				updatetime = tempList.get(3);
//				newest = ns.get(0).text();
				author = detail.select("span:contains(作者)").get(0).select("a").get(0).text();
				illustrator = detail.select("span:contains(插画)").get(0).select("label").get(1).text();
				publisher = detail.select("span:contains(文库)").get(0).select("label").get(1).text();
				updatetime = detail.select("p:contains(最新更新)").get(0).select("span").get(0).text();
				newest = "Null";
			}
			
		}
	}

	public String getAuthor() {
		return author;
	}

	public String getBooklink() {
		return booklink;
	}

	public String getIllustrator() {
		return illustrator;
	}

	public String getImageLink() {
		return imageLink;
	}

	public String getNewest() {
		return newest;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getTitle() {
		return title;
	}

	public String getUpdatetime() {
		return updatetime;
	}
	

}