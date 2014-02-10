package com.dblpmobile.app;

/**
 * Created by danielrobertson on 1/31/14.
 */
public class ReferenceCreator
{
    public static String apaReference(Article incomingArticle)
    {
        if (incomingArticle == null)
        {
            return "Empty Bibliography";
        }

        StringBuffer returnReference = new StringBuffer();
        for (int i = 0; i < incomingArticle.getAuthors().size(); i ++)
        {
            returnReference.append(incomingArticle.getAuthors().get(i));
            if(i == incomingArticle.getAuthors().size()-1)
            {
                returnReference.append(". ");
            }
            else
            {
                returnReference.append(", ");
            }
        }

        if (incomingArticle.getAuthors().isEmpty() && !incomingArticle.getEditors().isEmpty())
        {
            for (int i = 0; i < incomingArticle.getEditors().size(); i ++)
            {
                returnReference.append(incomingArticle.getEditors().get(i));
                if(i == incomingArticle.getEditors().size()-1)
                {
                    returnReference.append(". ");
                }
                else
                {
                    returnReference.append(", ");
                }
            }

        }
        returnReference.append("(");
        returnReference.append(incomingArticle.getYear());
        returnReference.append("). ");
        returnReference.append(incomingArticle.getTitle());
        returnReference.append(". ");
        if (incomingArticle.getJournal() != null)
        {
            returnReference.append(incomingArticle.getJournal());
            returnReference.append(", ");
        }
        if (incomingArticle.getVolume() != null)
        {
            returnReference.append(incomingArticle.getVolume());
            returnReference.append(", ");
        }
        returnReference.append(incomingArticle.getPages());
        returnReference.append(".");

        return returnReference.toString();
    }


    public static String mlaReference(Article incomingArticle)
    {
        if (incomingArticle == null)
        {
            return "Empty Bibliography";
        }

        StringBuffer returnReference = new StringBuffer();
        for (int i = 0; i < incomingArticle.getAuthors().size(); i ++)
        {
            returnReference.append(incomingArticle.getAuthors().get(i));
            if(i == incomingArticle.getAuthors().size()-1)
            {
                returnReference.append(". ");
            }
            else
            {
                returnReference.append(", ");
            }
        }

        if (incomingArticle.getAuthors().isEmpty() && !incomingArticle.getEditors().isEmpty())
        {
            for (int i = 0; i < incomingArticle.getEditors().size(); i ++)
            {
                returnReference.append(incomingArticle.getEditors().get(i));
                if(i == incomingArticle.getEditors().size()-1)
                {
                    returnReference.append(". ");
                }
                else
                {
                    returnReference.append(", ");
                }
            }

        }
        returnReference.append("\" ");
        returnReference.append(incomingArticle.getTitle());
        returnReference.append(".\" ");

        if (incomingArticle.getJournal() != null)
        {
            returnReference.append(incomingArticle.getJournal());
            returnReference.append(" ");
        }

        if (incomingArticle.getVolume() != null)
        {
            returnReference.append(incomingArticle.getVolume());
            returnReference.append(" ");
        }

        returnReference.append("(");
        returnReference.append(incomingArticle.getYear());
        returnReference.append("): ");


        if (incomingArticle.getPages() != null)
        {
            returnReference.append(incomingArticle.getPages());
        }
        returnReference.append(". Web. ");

        returnReference.append(incomingArticle.getMdate());
        returnReference.append(".");

        return returnReference.toString();
    }

    public static String xmlReference(Article incomingArticle)
    {
        StringBuffer xmlString = new StringBuffer();

        if (incomingArticle.getType() != null)
            xmlString.append("<" + incomingArticle.getType()+
                    " " +"key=\"" +incomingArticle.getKey() +"\" " +
                    "mdate=\"" +incomingArticle.getMdate() + "\"" +">\n");

        if (!incomingArticle.getAuthors().isEmpty())
        {
            for(String author: incomingArticle.getAuthors())
            {
                xmlString.append("     <author>"+ author + "<author>\n");
            }
        }
        if (!incomingArticle.getEditors().isEmpty())
        {
            for(String editor: incomingArticle.getEditors())
            {
                xmlString.append("     <editor>"+ editor + "<editor>\n");
            }
        }
        if(incomingArticle.getTitle() != null)
        {
            xmlString.append("     <title>"+ incomingArticle.getTitle() + "<title>\n");
        }
        if(incomingArticle.getBookTitle() != null)
        {
            xmlString.append("     <booktitle>"+ incomingArticle.getBookTitle() + "<booktitle>\n");
        }
        if(incomingArticle.getJournal() != null)
        {
            xmlString.append("     <journal>"+ incomingArticle.getJournal() + "<journal>\n");
        }
        if(incomingArticle.getVolume() != null)
        {
            xmlString.append("     <volume>"+ incomingArticle.getVolume() + "<volume>\n");
        }
        if(incomingArticle.getNumber() != null)
        {
            xmlString.append("     <number>"+ incomingArticle.getNumber() + "<number>\n");
        }
        if(incomingArticle.getMonth() != null)
        {
            xmlString.append("     <month>"+ incomingArticle.getMonth() + "<month>\n");
        }
        if(incomingArticle.getYear() != null)
        {
            xmlString.append("     <year>"+ incomingArticle.getYear() + "<year>\n");
        }
        if(incomingArticle.getPages() != null)
        {
            xmlString.append("     <pages>"+ incomingArticle.getPages() + "<pages>\n");
        }
        if(incomingArticle.getAddress() != null)
        {
            xmlString.append("     <address>"+ incomingArticle.getAddress() + "<url>\n");
        }
        if(incomingArticle.getUrl() != null)
        {
            xmlString.append("     <url>"+ incomingArticle.getUrl() + "<url>\n");
        }
        if(incomingArticle.getEe() != null)
        {
            xmlString.append("     <ee>"+ incomingArticle.getEe() + "<ee>\n");
        }
        if(incomingArticle.getCdRom() != null)
        {
            xmlString.append("     <cdrom>"+ incomingArticle.getCdRom() + "<cdrom>\n");
        }
        if(incomingArticle.getCite() != null)
        {
            xmlString.append("     <cite>"+ incomingArticle.getCite() + "<cite>\n");
        }
        if(incomingArticle.getPublisher() != null)
        {
            xmlString.append("     <publisher>"+ incomingArticle.getPublisher() + "<publisher>\n");
        }
        if(incomingArticle.getNote() != null)
        {
            xmlString.append("     <note>"+ incomingArticle.getNote() + "<note>\n");
        }
        if(incomingArticle.getCrossRef() != null)
        {
            xmlString.append("     <crossref>"+ incomingArticle.getCrossRef() + "<crossref>\n");
        }
        if(incomingArticle.getIsbn() != null)
        {
            xmlString.append("     <isbn>"+ incomingArticle.getIsbn() + "<isbn>\n");
        }
        if(incomingArticle.getSeries() != null)
        {
            xmlString.append("     <series>"+ incomingArticle.getSeries() + "<series>\n");
        }
        if(incomingArticle.getSchool() != null)
        {
            xmlString.append("     <school>"+ incomingArticle.getSchool() + "<school>\n");
        }
        if(incomingArticle.getChapter() != null)
        {
            xmlString.append("     <chapter>"+ incomingArticle.getChapter() + "<chapter>\n");
        }
        xmlString.append("</" + incomingArticle.getType()+">\n");
        return xmlString.toString();
    }
}
