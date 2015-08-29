package com.victor.lnlibrary.book;

import java.io.InputStream;

public interface XmlParser{
	public Book parse(InputStream is) throws Exception;

	public String serialize(Book book) throws Exception;
}