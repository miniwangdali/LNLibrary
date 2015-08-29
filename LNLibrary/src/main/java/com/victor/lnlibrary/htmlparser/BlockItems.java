package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BlockItems{
	private ArrayList<String> bookList = null;
	private ArrayList<String> booklinkList = null;
	private Document doc = null;

	public BlockItems(String blockname){
		try{
			booklinkList = new ArrayList<String>();
			bookList = new ArrayList<String>();
			doc = Jsoup.connect("http://www.linovel.com/").userAgent("lnlibrary").timeout(0).get();
			parser(blockname);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void parser(String title){
		if(doc != null){
			//找到左侧页
			Elements blocks = doc.select("div.linovel-index-box:contains(" + title + ")");
			
			for(Element block : blocks){
				//找到块书目
				Elements books = block.select("li.linovel-item");
				for(Element book : books){
					String bookname = book.select("h3").get(0).text();
					if(!bookList.contains(bookname)){
						bookList.add(bookname);
						booklinkList.add(book.select("a").attr("abs:href"));
					}
				}
			}
		}
	}


	public ArrayList<String> getBookList(){
		return this.bookList;
	}

	public ArrayList<String> getBooklinkList(){
		return this.booklinkList;
	}
}