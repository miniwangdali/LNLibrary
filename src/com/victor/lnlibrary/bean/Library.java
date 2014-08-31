package com.victor.lnlibrary.bean;

import com.victor.lnlibrary.book.Book;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Library{
	private static List<Book> myLibrary = new ArrayList<Book>();
	private static Book tempBook = new Book();

	public static void addBook(Book book){
		myLibrary.add(book);
	}

	public static Book getBook(String bookname){
		Iterator<Book> iterator = myLibrary.iterator();
		while(iterator.hasNext()){
			Book book = iterator.next();
			if(bookname.equals(book.getTitle())){
				return book;
			}
		}
		return null;
	}

	public static List<Book> getMyLibrary(){
		return myLibrary;
	}

	public static Book getTempBook(){
		
		return tempBook;
	}

	public static boolean isInLibrary(String bookname){
		Iterator<Book> iterator = myLibrary.iterator();
		while(iterator.hasNext()){
			Book book = iterator.next();
			if(bookname.equals(book.getTitle())){
				return true;
			}
		}
		return false;
	}

	public static void setMyLibrary(List<Book> bookList){
		myLibrary = bookList;
	}

	public static void setTempBook(Book book){
		tempBook = book;
	}
}