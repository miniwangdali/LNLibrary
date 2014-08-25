package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BlockItems{
	private List<String> bookList = null;
	private List<String> booklinkList = null;
	private Document doc = null;

	public BlockItems(String blockname){
		try{
			booklinkList = new ArrayList<String>();
			bookList = new ArrayList<String>();
			doc = Jsoup.connect("http://lknovel.lightnovel.cn/").userAgent("lnlibrary").timeout(0).get();
			parser(blockname);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void parser(String title){
		if(doc != null){
			//找到左侧页
			Elements items = doc.select("div.span8");
			
			for(Element item : items){
				//找到块书目    index为板块序号
				Elements blocks = item.select("h3:contains(" + title + ")" + " + ul");
				for(Element block : blocks){
					Elements books = block.select(".lk-block");
					for(Element book : books){
						if(!bookList.contains(book.select("p").get(0).text())){
							bookList.add(book.select("p").get(0).text());
							booklinkList.add(book.select("p").get(0).select("a").attr("abs:href"));
						}
						
					}
				}
			}
		}
	}


	public List<String> getBookList(){
		return this.bookList;
	}

	public List<String> getBooklinkList(){
		return this.booklinkList;
	}
}