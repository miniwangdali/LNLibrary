package com.victor.lnlibrary.book;

import android.util.Xml;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class BookParser implements XmlParser{
	
	@Override
	public Book parse(InputStream is) throws Exception{
    
		List<Dossier> dossiers = null;
		List<ChapterContent> chapters = null;
		List<String> contents = null;
		List<String> imageList = null;
    	Book book = null;
    	Dossier dossier = null;
    	ChapterContent chapter = null;
    	
    	XmlPullParser parser = Xml.newPullParser();
    	parser.setInput(is, "utf-8");
    	int eventType = parser.getEventType();
    	while(eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
			case XmlPullParser.START_DOCUMENT:
				eventType = parser.next();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("book")){
					book = new Book();
					dossiers = new ArrayList<Dossier>();
					book.setTitle(parser.getAttributeValue("", "name"));
					book.setAuthor(parser.getAttributeValue("", "author"));
					book.setIllustrator(parser.getAttributeValue("", "illustrator"));
					book.setPublisher(parser.getAttributeValue("", "publisher"));
					book.setNewest(parser.getAttributeValue("", "newest"));
					book.setUpdatetime(parser.getAttributeValue("", "updatetime"));
					book.setImagepath(parser.getAttributeValue("", "image"));
					book.setBookLink(parser.getAttributeValue("", "booklink"));
					book.setIntroduction(parser.getAttributeValue("", "intro"));
	              	book.setSaved(Boolean.parseBoolean(parser.getAttributeValue("", "saved")));
	              	eventType = parser.next();
	            
				}else if (parser.getName().equals("dossier")) {
					dossier = new Dossier();
					chapters = new ArrayList<ChapterContent>();
					dossier.setDossiertitle(parser.getAttributeValue("", "title"));
					dossier.setImagepath(parser.getAttributeValue("", "image"));
					dossier.setDossierLink(parser.getAttributeValue("", "dossierlink"));
					dossier.setDownloaded(Boolean.parseBoolean(parser.getAttributeValue("", "downloaded")));
					dossier.setLastRead(Integer.parseInt(parser.getAttributeValue("", "lastread")));
		              
		            eventType = parser.next();
				}else if (parser.getName().equals("chapter")){
					chapter = new ChapterContent();
		            chapter.setChaptertitle(parser.getAttributeValue("", "title"));
		            dossier.getChapters().add(parser.getAttributeValue("", "title"));
		            chapter.setChapterlink(parser.getAttributeValue("", "chapterlink"));
		            chapter.setProgress(Double.parseDouble(parser.getAttributeValue("","progress")));
		            contents = new ArrayList<String>();
		            imageList = new ArrayList<String>();
		            eventType = parser.next();
				}else if (parser.getName().equals("content")) {
					eventType = parser.next();
					if (parser.getText() != null){
						contents.add(parser.getText());
					}else{
						contents.add("");
					}
					eventType = parser.next();
				}else if(parser.getName().equals("img")){
					eventType = parser.next();
					imageList.add(parser.getText());
					eventType = parser.next();
				}
				break;
			case XmlPullParser.END_TAG:
				if(parser.getName().equals("book")){
					book.setDossiers(dossiers);
				    dossiers = null;
				    
				}else if (parser.getName().equals("dossier")) {
					dossier.setChapterContents(chapters);
			        dossiers.add(dossier);
			        chapters = null;
			        dossier = null;
				}else if (parser.getName().equals("chapter")) {
					chapter.setContents(contents);
			        chapter.setImageList(imageList);
			        chapters.add(chapter);
			        chapter = null;
			        contents = null;
			        imageList = null;
				}
				eventType = parser.next();
				break;
			}
			
		}
    	
    	return book;
	}
    	
    	
  
	@Override
	public String serialize(Book book) throws Exception{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("utf-8", true);
		serializer.startTag("", "book");
		serializer.attribute("", "name", book.getTitle());
		serializer.attribute("", "author", book.getAuthor());
		serializer.attribute("", "illustrator", book.getIllustrator());
		serializer.attribute("", "publisher", book.getPublisher());
		serializer.attribute("", "newest", book.getNewest());
		serializer.attribute("", "updatetime", book.getUpdatetime());
		serializer.attribute("", "image", book.getImagepath());
		serializer.attribute("", "intro", book.getIntroduction());
		serializer.attribute("", "booklink", book.getBookLink());
		serializer.attribute("", "saved", String.valueOf(book.isSaved()));
		for(Dossier dossier : book.getDossiers()){
			serializer.startTag("", "dossier");
		    serializer.attribute("", "title", dossier.getDossiertitle());
		    serializer.attribute("", "image", dossier.getImagepath());
		    
		    serializer.attribute("", "dossierlink", dossier.getDossierLink());
		    serializer.attribute("", "downloaded", String.valueOf(dossier.isDownloaded()));
		    serializer.attribute("", "lastread", String.valueOf(dossier.getLastRead()));
		    
		    for(ChapterContent chapterContent : dossier.getChapterContents()){
		    	serializer.startTag("", "chapter");
		        serializer.attribute("", "title", chapterContent.getChaptertitle());
		        serializer.attribute("", "chapterlink", chapterContent.getChapterlink());
		        serializer.attribute("", "progress", String.valueOf(chapterContent.getProgress()));
		        
		        for(String content : chapterContent.getContents()){
		        	serializer.startTag("", "content");
		            serializer.text(content);
		            serializer.endTag("", "content");
		        }
		        for(String image : chapterContent.getImageList()){
		        	serializer.startTag("", "img");
		            serializer.text(image);
		            serializer.endTag("", "img");
		        }
		        serializer.endTag("", "chapter");
		    }
		    serializer.endTag("", "dossier");
		}
		
		serializer.endTag("", "book");
        serializer.endDocument();
		
        return writer.toString();
	}
		
}