package com.dblpmobile.app;

import java.util.ArrayList;

/**
 * Created by danielrobertson on 1/31/14.
 */
public class CitationCreator
{
    private static ArrayList<String> citationTypes = new ArrayList<String>();

    public static ArrayList<String> getCitationTypes()
    {
        citationTypes.clear();
        citationTypes.add("APA");
        citationTypes.add("XML");
        citationTypes.add("MLA");
        return citationTypes;
    }

    public static String apaCitation(Article incomingArticle)
    {
        StringBuffer returnCitation = new StringBuffer();
        returnCitation.append("(");
        if(!incomingArticle.getAuthors().isEmpty())
        {
            for (int i =0; i<incomingArticle.getAuthors().size(); i++)
            {
                returnCitation.append(incomingArticle.getAuthors().get(i));
                returnCitation.append(", ");
            }
        }
        if (incomingArticle.getAuthors().isEmpty() && !incomingArticle.getEditors().isEmpty())
        {
            for (int i =0; i<incomingArticle.getEditors().size(); i++)
            {
                returnCitation.append(incomingArticle.getEditors().get(i));
                returnCitation.append(", ");
            }
        }
        returnCitation.append(("("));
        returnCitation.append(incomingArticle.getYear());
        returnCitation.append(")");
        returnCitation.append(")");
        return  returnCitation.toString();
    }

    public static String mlaCitation(Article incomingArticle)
    {
        StringBuffer returnCitation = new StringBuffer();
        returnCitation.append("(");
        if(!incomingArticle.getAuthors().isEmpty())
        {
            for (int i =0; i<incomingArticle.getAuthors().size(); i++)
            {
                returnCitation.append(incomingArticle.getAuthors().get(i));
                returnCitation.append(", ");
            }
        }
        if (incomingArticle.getAuthors().isEmpty() && !incomingArticle.getEditors().isEmpty())
        {
            for (int i =0; i<incomingArticle.getEditors().size(); i++)
            {
                returnCitation.append(incomingArticle.getEditors().get(i));
                returnCitation.append(", ");
            }
        }

        if (incomingArticle.getPages() != null)
        {
            returnCitation.append( incomingArticle.getPages() );
        }
        returnCitation.append(")");
        return  returnCitation.toString();
    }
}
