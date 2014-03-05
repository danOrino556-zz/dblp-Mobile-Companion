package com.dblpmobile.app;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by danielrobertson on 1/30/14.
 */
public class ArticleSaxHandler extends DefaultHandler
{
    private ArrayList<Article> menu;
    private String tempVal;
    private Article tempEmp;

    public ArticleSaxHandler() {
        menu = new ArrayList<Article>();
    }

    public ArrayList<Article> getmenu()
    {
        return menu;
    }

    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset
        tempVal = "";
        if (qName.equalsIgnoreCase("article") ||
            qName.equalsIgnoreCase("inproceedings") ||
            qName.equalsIgnoreCase("proceedings") ||
            qName.equalsIgnoreCase("book")  ||
            qName.equalsIgnoreCase("incollection") ||
            qName.equalsIgnoreCase("phdthesis") ||
            qName.equalsIgnoreCase("mastersthesis") ||
            qName.equalsIgnoreCase("www"))
        {
            tempEmp = new Article();
            tempEmp.setType(qName);
            tempEmp.setKey(attributes.getValue("key"));
            tempEmp.setMdate(attributes.getValue("mdate"));
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        tempVal = new String(ch, start, length);
    }


    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("article") ||
                qName.equalsIgnoreCase("inproceedings") ||
                qName.equalsIgnoreCase( "proceedings") ||
                qName.equalsIgnoreCase( "book")  ||
                qName.equalsIgnoreCase("incollection") ||
                qName.equalsIgnoreCase("phdthesis") ||
                qName.equalsIgnoreCase("mastersthesis") ||
                qName.equalsIgnoreCase("www"))
        {
            // add it to the list
            menu.add(tempEmp);
        }

        else if (qName.equalsIgnoreCase("author"))
        {
            tempEmp.addAuthor(tempVal);
        }

        else if (qName.equalsIgnoreCase("editor"))
        {
            tempEmp.addEditor(tempVal);
        }

        else if (qName.equalsIgnoreCase("title"))
        {
            tempEmp.setTitle(tempVal);
        }

        else if (qName.equalsIgnoreCase("booktitle"))
        {
            tempEmp.setBookTitle(tempVal);
        }

        else if (qName.equalsIgnoreCase("pages"))
        {
            tempEmp.setPages(tempVal);
        }

        else if (qName.equalsIgnoreCase("year"))
        {
            tempEmp.setYear(tempVal);
        }

        else if (qName.equalsIgnoreCase("address"))
        {
            tempEmp.setAddress(tempVal);
        }

        else if (qName.equalsIgnoreCase("journal"))
        {
            tempEmp.setJournal(tempVal);
        }

        else if (qName.equalsIgnoreCase("volume"))
        {
            tempEmp.setVolume(tempVal);
        }

        else if (qName.equalsIgnoreCase("number"))
        {
            tempEmp.setNumber(tempVal);
        }

        else if (qName.equalsIgnoreCase("month"))
        {
            tempEmp.setMonth(tempVal);
        }

        else if (qName.equalsIgnoreCase("url"))
        {
            tempEmp.setUrl(tempVal);
        }

        else if (qName.equalsIgnoreCase("cdrom"))
        {
            tempEmp.setCdRom(tempVal);
        }

        else if (qName.equalsIgnoreCase("cite"))
        {
            tempEmp.setCite(tempVal);
        }

        else if (qName.equalsIgnoreCase("publisher"))
        {
            tempEmp.setPublisher(tempVal);
        }

        else if (qName.equalsIgnoreCase("note"))
        {
            tempEmp.setNote(tempVal);
        }

        else if (qName.equalsIgnoreCase("crossref"))
        {
            tempEmp.setCrossRef(tempVal);
        }

        else if (qName.equalsIgnoreCase("isbn"))
        {
            tempEmp.setIsbn(tempVal);
        }

        else if (qName.equalsIgnoreCase("ee"))
        {
            tempEmp.setEe(tempVal);
        }

        else if (qName.equalsIgnoreCase("series"))
        {
            tempEmp.setSeries(tempVal);
        }

        else if (qName.equalsIgnoreCase("school"))
        {
            tempEmp.setSchool(tempVal);
        }

        else if (qName.equalsIgnoreCase("chapter"))
        {
            tempEmp.setChapter(tempVal);
        }
    }
}
