package com.victor.lnlibrary.book;

import java.util.ArrayList;
import java.util.List;

public class ChapterContent{
	private String chapterlink = new String();
	private String chaptertitle = new String();
	private List<String> contents = new ArrayList<String>();
	private List<String> imageList = new ArrayList<String>();
	
	public String getChapterlink() {
		return chapterlink;
	}
	public void setChapterlink(String chapterlink) {
		this.chapterlink = chapterlink;
	}
	public String getChaptertitle() {
		return chaptertitle;
	}
	public void setChaptertitle(String chaptertitle) {
		this.chaptertitle = chaptertitle;
	}
	public List<String> getContents() {
		return contents;
	}
	public void setContents(List<String> contents) {
		this.contents = contents;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	
}