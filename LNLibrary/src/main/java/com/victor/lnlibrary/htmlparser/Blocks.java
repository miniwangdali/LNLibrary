package com.victor.lnlibrary.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Blocks{
	private ArrayList<String> blockList = null;
	private Document doc = null;

	public Blocks(){
		try{
			blockList = new ArrayList<String>();
			doc = Jsoup.connect("http://www.linovel.com/").userAgent("lnlibrary").timeout(0).get();
			parser();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	private void parser(){
		if(doc != null){
			//找到左侧页
			Elements items = doc.select("div.linovel-index-box");
			
			for(Element item : items){
				//找到块标题
				Elements blocks = item.select("h1");
				for(Element block : blocks){
					blockList.add(block.text());
				}
			}
		}
	}
	
	public ArrayList<String> getBlockList(){
		return blockList;
	}
}