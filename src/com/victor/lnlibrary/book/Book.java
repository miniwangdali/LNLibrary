package com.victor.lnlibrary.book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Book{
	private String author = new String();
	private String bookLink = new String();
	private List<Dossier> dossiers = new ArrayList<Dossier>();
	private String illustrator = new String();
	private String imagepath = new String();
	private String introduction = new String();
	private boolean isSaved = false;
	private String newest = new String();
	private String publisher = new String();
	private String title = new String();
	private String updatetime = new String();

	public String getAuthor(){
		return this.author;
	}

	public String getBookLink(){
		return this.bookLink;
	}

	public Dossier getDossier(String dossiername){
		Iterator<Dossier> iterator = dossiers.iterator();
		while (iterator.hasNext()) {
			Dossier dossier = (Dossier) iterator.next();
			if(dossiername.equals(dossier.getDossiertitle())){
				return dossier;
			}
		}
		return null;
	}

	public List<Dossier> getDossiers(){
		return this.dossiers;
	}

	public String getIllustrator(){
		return this.illustrator;
	}

	public String getImagepath(){
		return this.imagepath;
	}

 	public String getIntroduction(){
 		return this.introduction;
 	}

 	public String getNewest(){
 		return this.newest;
 	}

 	public String getPublisher(){
 		return this.publisher;
 	}

 	public String getTitle(){
 		return this.title;
 	}

 	public String getUpdatetime(){
 		return this.updatetime;
 	}

 	public boolean hasDossier(String dossiername){
 		Iterator<Dossier> iterator = dossiers.iterator();
		while (iterator.hasNext()) {
			Dossier dossier = (Dossier) iterator.next();
			if(dossiername.equals(dossier.getDossiertitle())){
				return true;
			}
		}
		return false;
 	}

 	public boolean isSaved(){
 		return this.isSaved;
 	}

 	public void setAuthor(String author){
 		this.author = author;
  }

 	public void setBookLink(String bookLink){
 		this.bookLink = bookLink;
 	}

 	public void setDossiers(List<Dossier> dossiers){
 		this.dossiers = dossiers;
 	}

 	public void setIllustrator(String illustrator){
 		this.illustrator = illustrator;
 	}

 	public void setImagepath(String imagepath){
 		this.imagepath = imagepath;
 	}

 	public void setIntroduction(String introduction){
 		this.introduction = introduction;
 	}

 	public void setNewest(String newest){
 		this.newest = newest;
 	}

 	public void setPublisher(String publisher){
 		this.publisher = publisher;
 	}

 	public void setSaved(boolean isSaved){
 		this.isSaved = isSaved;
 	}

 	public void setTitle(String bookname){
 		this.title = bookname;
 	}

 	public void setUpdatetime(String updatetime){
 		this.updatetime = updatetime;
 	}
}