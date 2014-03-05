package com.dblpmobile.app;

import java.util.ArrayList;

/**
 * Created by danielrobertson on 1/24/14.
 */
public class Article
{
    private String key;
    private ArrayList<String> authors= new ArrayList<String>();
    private ArrayList<String> editors= new ArrayList<String>();
    private String type;
    private String title;
    private String bookTitle;
    private String journal;
    private String volume;
    private String number;
    private String month;
    private String year;
    private String pages;
    private String address;
    private String url;
    private String ee;
    private String cdRom;
    private String cite;
    private String publisher;
    private String note;
    private String crossRef;
    private String isbn;
    private String series;
    private String school;
    private String chapter;
    private String mdate;


    public String toString()
    {
        return getTitle();
    }

    //Not all articles will fill all fields.....but thats OK
    public String listAttributes()
    {
        StringBuffer attributesString = new StringBuffer();

        if (getKey() != null)
            attributesString.append("Key : " + getKey() + "\n");
        if (getMdate() != null)
            attributesString.append("Date : " + getMdate() + "\n");

        for (String author : authors)
        {
            attributesString.append("Author : " + author + "\n");
        }
        for (String editor : editors)
        {
            attributesString.append("Editor : " + editor + "\n");
        }

        if (getType() != null)
            attributesString.append("Type : " + getType() + "\n");
        if (getTitle() != null)
            attributesString.append("Title : " + getTitle() + "\n");
        if (getBookTitle() != null)
            attributesString.append("Book Title : " + getBookTitle()+ "\n");
        if (getJournal() != null)
            attributesString.append("Journal : " + getJournal() + "\n");
        if (getVolume() != null)
            attributesString.append("Volume : " + getVolume() + "\n");
        if (getNumber() != null)
            attributesString.append("Number : " + getNumber() + "\n");
        if (getMonth() != null)
            attributesString.append("Month : " + getMonth() + "\n");
        if (getYear() != null)
            attributesString.append("Year : " + getYear() + "\n");
        if (getPages() != null)
            attributesString.append("Pages : " + getPages() + "\n");
        if (getAddress() != null)
            attributesString.append("Address : " + getAddress() + "\n");
        if (getUrl() != null)
            attributesString.append("URL : " + getUrl() + "\n");
        if (getEe() != null)
            attributesString.append("Electronic Edition : " + getEe() + "\n");
        if (getCdRom() != null)
            attributesString.append("CdRom : " + getCdRom() + "\n");
        if (getCite() != null)
            attributesString.append("Cite : " + getCite() + "\n");
        if (getPublisher() != null)
            attributesString.append("Publisher : " + getPublisher() + "\n");
        if (getNote() != null)
            attributesString.append("Note : " + getNote() + "\n");
        if (getCrossRef() != null)
            attributesString.append("Cross Reference : " + getCrossRef() + "\n");
        if (getIsbn() != null)
            attributesString.append("Isbn : " + getIsbn() + "\n");
        if (getSeries() != null)
            attributesString.append("Series : " + getSeries() + "\n");
        if (getSchool() != null)
            attributesString.append("School : " + getSchool() + "\n");

        return attributesString.toString();
    }

    //Setters for Collections

    public void addAuthor(String newAuthor)
    {
        authors.add(newAuthor);
    }

    public void addEditor(String newEditor)
    {
        editors.add(newEditor);
    }

    //Getters
    public String getKey() {
        return key;
    }
    public ArrayList<String> getAuthors() {
        return authors;
    }
    public ArrayList<String> getEditors()
    {
        return editors;
    }
    public String getTitle() {
        return title;
    }
    public String getJournal() {
        return journal;
    }
    public String getVolume() {
        return volume;
    }
    public String getYear() {
        return year;
    }
    public String getPages() {
        return pages;
    }
    public String getUrl() {
        return url;
    }
    public String getEe() {
        return ee;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public String getMdate() {
        return mdate;
    }
    public String getNumber() {
        return number;
    }
    public String getMonth() {
        return month;
    }
    public String getAddress() {
        return address;
    }
    public String getCdRom() {
        return cdRom;
    }
    public String getCite() {
        return cite;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getNote() {
        return note;
    }
    public String getCrossRef() {
        return crossRef;
    }
    public String getIsbn() {
        return isbn;
    }
    public String getSeries() {
        return series;
    }
    public String getSchool() {
        return school;
    }
    public String getType() {
        return type;
    }
    public String getChapter() {
        return chapter;
    }

    //Setters
    public void setKey(String key) {
        this.key = key;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setJournal(String journal) {
        this.journal = journal;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setPages(String pages) {
        this.pages = pages;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setEe(String ee) {
        this.ee = ee;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCdRom(String cdRom) {
        this.cdRom = cdRom;
    }
    public void setCite(String cite) {
        this.cite = cite;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void setCrossRef(String crossRef) {
        this.crossRef = crossRef;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setSeries(String series) {
        this.series = series;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setChapter(String chapter) {this.chapter = chapter;}
    public void setMdate(String mdate) {
        this.mdate = mdate;
    }
}
