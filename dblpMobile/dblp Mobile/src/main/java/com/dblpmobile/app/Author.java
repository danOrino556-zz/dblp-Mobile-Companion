package com.dblpmobile.app;

/**
 * Created by danielrobertson on 1/24/14.
 */
public class Author
{
    private String name;
    private String urlpt = "dummy field";
    public Author()
    {
    }

    public Author (String incomingName, String incomingURL)
    {
        setName(incomingName);
        setUrlpt(incomingURL);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlpt() {
        return urlpt;
    }

    public void setUrlpt(String urlpt) {
        this.urlpt = urlpt;
    }

    public String toString()
    {
        return "Name : " + getName() + " Key : " + getUrlpt() + "\n";
    }
}
