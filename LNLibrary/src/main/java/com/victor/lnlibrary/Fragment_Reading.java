/*package com.victor.lnlibrary;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.Book;
import com.victor.lnlibrary.book.ChapterContent;
import com.victor.lnlibrary.book.Dossier;
import com.victor.lnlibrary.book.ImageOperator;
import java.util.List;

public class Fragment_Reading extends Fragment
{
  private String bookname;
  private String chaptertitle;
  private ChapterContent contents;
  private String dossiername;
  private LinearLayout readingContent;

  public String getBookname()
  {
    return this.bookname;
  }

  public String getChaptertitle()
  {
    return this.chaptertitle;
  }

  public String getDossiername()
  {
    return this.dossiername;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903049, paramViewGroup, false);
    ((TextView)localView.findViewById(2131165188)).setText(this.chaptertitle);
    ((TextView)localView.findViewById(2131165189));
    this.readingContent = ((LinearLayout)localView.findViewById(2131165191));
    this.contents = Library.getBook(this.bookname).getDossier(this.dossiername).getChapterContent(this.chaptertitle);
    List localList1 = this.contents.getContents();
    List localList2 = this.contents.getImageList();
    for (int i = 0; ; ++i)
    {
      if (i >= localList1.size())
        return localView;
      TextView localTextView = new TextView(getActivity());
      localTextView.setText((CharSequence)localList1.get(i));
      localTextView.setEms(2);
      this.readingContent.addView(localTextView);
      if (i >= localList2.size())
        continue;
      Bitmap localBitmap = new ImageOperator().loadImage((String)localList2.get(i));
      ImageView localImageView = new ImageView(getActivity());
      localImageView.setImageBitmap(localBitmap);
      localImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
      this.readingContent.addView(localImageView);
    }
  }

  public void setBookname(String paramString)
  {
    this.bookname = paramString;
  }

  public void setChaptertitle(String paramString)
  {
    this.chaptertitle = paramString;
  }

  public void setDossiername(String paramString)
  {
    this.dossiername = paramString;
  }
}*/