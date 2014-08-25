package com.victor.lnlibrary.book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dossier{
	private List<ChapterContent> chapterContents = new ArrayList<ChapterContent>();
	private List<String> chapters = new ArrayList<String>();
	private String dossierLink = new String();
	private String dossiertitle = new String();
	private String imagepath = new String();
	private boolean isDownloaded = false;
	private int lastRead = -1;

	public ChapterContent getChapterContent(String chaptertitle){
		Iterator<ChapterContent> iterator = chapterContents.iterator();
		while (iterator.hasNext()) {
			ChapterContent chapterContent = (ChapterContent) iterator.next();
			if(chaptertitle.equals(chapterContent.getChaptertitle())){
				return chapterContent;
			}
		}
		return null;
	}

	public List<ChapterContent> getChapterContents(){
		return this.chapterContents;
	}

	public int getChapterId(String chaptertitle){
		for(int i = 0; i < chapterContents.size(); i ++){
			if(chaptertitle.equals(chapterContents.get(i).getChaptertitle())){
				return i;
			}
		}
		return 0;
	}

	public List<String> getChapters(){
		return this.chapters;
	}

	public String getDossierLink(){
		return this.dossierLink;
	}

	public String getDossiertitle(){
		return this.dossiertitle;
	}

	public String getImagepath(){
		return this.imagepath;
	}

	public int getLastRead(){
		return this.lastRead;
	}

	public boolean isDownloaded(){
		return this.isDownloaded;
	}

	public void setChapterContents(List<ChapterContent> chapterContents){
		this.chapterContents = chapterContents;
	}

	public void setChapters(List<String> chapters){
		this.chapters = chapters;
	}

 	public void setDossierLink(String dossierLink){
 		this.dossierLink = dossierLink;
 	}

 	public void setDossiertitle(String dossiertitle){
 		this.dossiertitle = dossiertitle;
 	}

 	public void setDownloaded(boolean downloaded){
 		this.isDownloaded = downloaded;
 	}

 	public void setImagepath(String imagepath){
 		this.imagepath = imagepath;
 	}

 	public void setLastRead(int lastRead){
 		this.lastRead = lastRead;
 	}
}