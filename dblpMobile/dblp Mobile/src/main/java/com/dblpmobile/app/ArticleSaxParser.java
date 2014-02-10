package com.dblpmobile.app;

import android.util.Log;
import android.widget.ProgressBar;

import org.xml.sax.XMLReader;

import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by danielrobertson on 1/30/14.
 */
public class ArticleSaxParser
{
    public static ArrayList<Article> parse(ArrayList<String>urls, ProgressBar loadingProgress)
    {
        ArrayList<Article> menu=null;
        try
        {
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SAXXMLHandler
            ArticleSaxHandler saxHandler = new ArticleSaxHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts

            loadingProgress.setMax(urls.size());
            loadingProgress.setSecondaryProgress(urls.size());

            for(int i = 0; i<urls.size(); i++)
            {
                xmlReader.parse(urls.get(i));
                loadingProgress.incrementProgressBy(1);
            }

            // get the `Employee list`
            menu = saxHandler.getmenu();

        } catch (Exception ex) {
            Log.d("XML", "SAXXMLParser: parse() failed");
        }

        // return Employee list
        return menu;
    }
}
