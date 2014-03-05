package com.dblpmobile.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ActivityArticleList extends ActionBarActivity
{
    Context context = this;

    //The entry activity used determines which of these is actually used
    ArrayList <Article> articles = new ArrayList<Article>();
    ArrayList <Author> authors = new ArrayList<Author>();

    //Each of these variables are passed in from the previous activity
    String urlSearched;
    String authorSearched;

    //Every publication parsed will increment this variable by 1
    Integer numberOfEntries = 0;

    //These booleans act as a series of switches which dictate which
    //async parser process is called
    boolean isBibliography = false;
    boolean articlesUpdated = false;
    boolean isJournal = false;
    boolean isFirstJournalSearch = false;
    boolean isSeries = false;
    boolean isSecondSeries = false;
    boolean coAuthorsShown = false;

    //Main two lists. Author list only shown when there are possible coauthors
    ListView articleList;
    ListView authorList;
    ArticleArrayAdapter adapter;

    //Accessed by the async tasks while loading the activity
    ProgressBar loadingProgress;

    //The following UI items are turned on/off depending on search type
    String[] spinnerOptions = {"Sort By Year - Descending", "Sort By Year - Ascending",
                               "Sort By Type Z to A", "Sort By Type A to Z",
                               "Sort By Title Z to A", "Sort By Title A to Z"};

    //The coauthors button is only shown during author search
    Button toggleCoAuthorsButton;

    //Six different sorts / defined at the end of this activity
    Spinner articleSortSpinner;

    //These two buttons perform the actions on the entire displayed list
    ImageButton exportAllArticlesButton;
    ImageButton shareAllArticlesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_article_list);

        //Now we will go through and set the booleans that are defined at the start of this activity
        //These booleans will define which async loader we use
        Bundle articleWanted= getIntent().getExtras();
        if (getIntent().hasExtra("urlSearched"))
        {
            urlSearched = (articleWanted.getString("urlSearched"));
            authorSearched = (articleWanted.getString("authorSearched"));
        }
        if (getIntent().hasExtra("isBibliography"))
        {
            isBibliography = true;
        }
        if (getIntent().hasExtra("isJournal"))
        {
            isJournal = true;
        }

        if (getIntent().hasExtra("isSeries"))
        {
            isSeries = true;
        }
        if (getIntent().hasExtra("isSecondSeries"))
        {
            isSecondSeries = true;
        }
        if (getIntent().hasExtra("isFirst"))
        {
            isFirstJournalSearch = true;
        }

        TextView articleText = (TextView)findViewById(R.id.articleTextView);
        articleText.setText("Entries for : " + authorSearched);

        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonDirectory);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        exportAllArticlesButton = (ImageButton)findViewById(R.id.exportArticlesButton);
        exportAllArticlesButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Export all displayed articles to MLA, APA, or XML", "Help");
                return false;
            }
        });

        exportAllArticlesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog exportDocumentDialog = new Dialog(context);

                exportDocumentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = exportDocumentDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                exportDocumentDialog.setContentView(R.layout.dialog_export_document);

                final EditText emailAddress = (EditText)exportDocumentDialog.findViewById(R.id.exportDocumentEditText);

                final Spinner exportFormatSpinner = (Spinner)exportDocumentDialog.findViewById(R.id.exportOptionsSpinner);
                ArrayAdapter adapter = new ArrayAdapter(context,R.layout.spinner_text, CitationCreator.getCitationTypes());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                exportFormatSpinner.setAdapter(adapter);
                exportFormatSpinner.setMinimumHeight(30);



                Button sendEmailButton = (Button)exportDocumentDialog.findViewById(R.id.exportDocumentSendButton);
                sendEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        StringBuffer Citation = new StringBuffer();
                        StringBuffer Reference = new StringBuffer();

                        if(exportFormatSpinner.getSelectedItemPosition() == 0)
                        {
                            for(Article article:articles)
                            {
                                Citation.append("----------------------------------\n");
                                Citation.append(article.getTitle());
                                Citation.append("\n\n");
                                Citation.append("In Text Citatation : " + CitationCreator.apaCitation(article));
                                Citation.append("\n\n");
                                Citation.append(ReferenceCreator.apaReference(article));
                                Citation.append("\n\n");
                            }
                        }
                        else if(exportFormatSpinner.getSelectedItemPosition() == 1)
                        {
                            for(Article article:articles)
                            {
                                Citation.append("----------------------------------\n");
                                Citation.append(article.getTitle());
                                Citation.append("\n\n");
                                Citation.append("XML");
                                Citation.append("\n\n");
                                Citation.append(ReferenceCreator.xmlReference(article));
                                Citation.append("\n\n");
                            }
                        }
                        else
                        {
                            for(Article article:articles)
                            {
                                Citation.append("----------------------------------\n");
                                Citation.append(article.getTitle());
                                Citation.append("\n\n");
                                Citation.append("In Text Citatation : " + CitationCreator.mlaCitation(article));
                                Citation.append("\n\n");
                                Citation.append(ReferenceCreator.mlaReference(article));
                                Citation.append("\n\n");
                            }
                        }

                        String recAddress = emailAddress.getText().toString();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{recAddress});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Citations for : " + authorSearched);
                        i.putExtra(Intent.EXTRA_TEXT   , "Citations : \n\n" + Citation.toString() +
                                "\n\n");
                        try
                        {
                            context.startActivity(Intent.createChooser(i, "Send mail..."));
                        }
                        catch (android.content.ActivityNotFoundException ex)
                        {
                            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                        exportDocumentDialog.dismiss();
                    }
                });

                exportDocumentDialog.show();

            }
        });

        shareAllArticlesButton = (ImageButton)findViewById(R.id.shareArticlesButton);
        shareAllArticlesButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Share details for displayed articles via Google Drive or email", "Help");
                return false;
            }
        });

        shareAllArticlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                final Dialog emailDocumentDialog = new Dialog(context);
                emailDocumentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window dialogWindow = emailDocumentDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                emailDocumentDialog.setContentView(R.layout.dialog_share_document);

                final EditText emailAddress = (EditText)emailDocumentDialog.findViewById(R.id.shareDocumentEditText);

                Button sendEmailButton = (Button)emailDocumentDialog.findViewById(R.id.shareDocumentSendButton);
                sendEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        //Get text for all the displayed articles
                        StringBuffer articlesShareText = new StringBuffer();
                        for(Article article : articles)
                        {
                            articlesShareText.append("----------------------------------\n");
                            articlesShareText.append(article.getTitle() + "\n\n");
                            articlesShareText.append(article.listAttributes());
                            articlesShareText.append("\n\n");
                        }

                        String recAddress = emailAddress.getText().toString();

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{recAddress});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Publications for : " + authorSearched);
                        i.putExtra(Intent.EXTRA_TEXT   , articlesShareText.toString());
                        try
                        {
                            context.startActivity(Intent.createChooser(i, "Send mail..."));
                        }
                        catch (android.content.ActivityNotFoundException ex)
                        {
                            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                        emailDocumentDialog.dismiss();
                    }
                });

                emailDocumentDialog.show();
            }
        });

        //Journals usually display a journal page and then a volume page. If it is the first journal search
        //we need to display volumes instead of individual entries
        if (isFirstJournalSearch)
        {
            new AsyncJournalLoader().execute();
        }

        //Series are parsed in HTML differently than the other bibliography types
        //So if it is a series, we will have to use a special async loader
        else if(isSecondSeries)
        {
            new AsyncSeriesLoader().execute();
        }

        //All feeder activities that are ready to display individual entries
        //uses this loader
        else
        {
            new AsyncArticleLoader().execute();
        }

        articleList = (ListView)findViewById(R.id.articleListView);
        articleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(isJournal)
                {
                    Intent x = new Intent(ActivityArticleList.this, ActivityArticleList.class);
                    Bundle extras = new Bundle();
                    extras.putString("urlSearched", articles.get(i).getUrl());
                    extras.putString("authorSearched", articles.get(i).getTitle());
                    extras.putString("isBibliography", "true");
                    x.putExtras(extras);
                    startActivity(x);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
                if(isSeries)
                {
                    Intent x = new Intent(ActivityArticleList.this, ActivityArticleList.class);
                    Bundle extras = new Bundle();
                    extras.putString("urlSearched", articles.get(i).getUrl());
                    extras.putString("authorSearched", articles.get(i).getTitle());
                    extras.putString("isBibliography", "true");
                    extras.putString("isSecondSeries", "true");
                    x.putExtras(extras);
                    startActivity(x);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }
        });

        //This coauthor list will only be shown when coming from author search
        authorList = (ListView)findViewById(R.id.authorListView);
        authorList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String urlSelected = authors.get(i).getUrlpt();
                Intent x = new Intent(ActivityArticleList.this, ActivityArticleList.class);
                Bundle extras = new Bundle();
                extras.putString("urlSearched", urlSelected);
                extras.putString("authorSearched", authors.get(i).getName());
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

            }
        });
        authorList.setVisibility(View.GONE);

        //Sort the articles 6 different ways
        articleSortSpinner = (Spinner)findViewById(R.id.sortArticlespinner);
        ArrayAdapter adapter = new ArrayAdapter(context,R.layout.sorting_spinner_text, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        articleSortSpinner.setAdapter(adapter);
        articleSortSpinner.setMinimumHeight(30);

        //This is only shown if coming from author search
        toggleCoAuthorsButton = (Button)findViewById(R.id.toggleCoAuthorsButton);
        toggleCoAuthorsButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Toggles the list view between articles and coauthors", "Help");
                return false;
            }
        });

        toggleCoAuthorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!coAuthorsShown)
                {
                    articleList.setVisibility(View.GONE);
                    authorList.setVisibility(View.VISIBLE);
                    articleSortSpinner.setVisibility(View.GONE);
                    exportAllArticlesButton.setVisibility(View.GONE);
                    shareAllArticlesButton.setVisibility(View.GONE);
                    toggleCoAuthorsButton.setText("Toggle Bibliographies");
                    coAuthorsShown = true;
                }
                else
                {
                    articleList.setVisibility(View.VISIBLE);
                    authorList.setVisibility(View.GONE);
                    articleSortSpinner.setVisibility(View.VISIBLE);
                    exportAllArticlesButton.setVisibility(View.VISIBLE);
                    shareAllArticlesButton.setVisibility(View.VISIBLE);
                    toggleCoAuthorsButton.setText("Toggle Coauthors");
                    coAuthorsShown = false;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_article_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    private class AsyncArticleLoader extends AsyncTask<Void, Void, Void>
    {
        Dialog loadingDialog = new Dialog(ActivityArticleList.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window dialogWindow = loadingDialog.getWindow();
            dialogWindow.setBackgroundDrawableResource(R.drawable.white_rounded_corners);
            loadingDialog.setContentView(R.layout.daialog_loading_screen);
            loadingProgress = (ProgressBar) loadingDialog.findViewById(R.id.progressBar);
            loadingDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                articles.clear();
                numberOfEntries = 0;

                // Connect to the dblp site and parse by HTML tag

                Document document = Jsoup.connect(urlSearched).get();
                ArrayList <String> keys = new ArrayList<String>();

                Elements dbKeys = document.body().select("ul.bullets li > small");
                for (Element dbkey : dbKeys)
                {
                    StringBuffer updatedURL = new StringBuffer();
                    updatedURL.append("http://dblp.uni-trier.de/rec/bibtex/");
                    updatedURL.append(dbkey.text());
                    updatedURL.append(".xml");
                    keys.add(updatedURL.toString());
                    numberOfEntries++;
                }

                //CoAuthorIndex only if coming from author search. The following logic wont
                //parse any tags if it is not coming from author
                Elements coAuthorKeys = document.select("div.person > a");
                for (Element authorKey : coAuthorKeys)
                {
                    StringBuilder moddedURL = new StringBuilder();
                    moddedURL.append("http://dblp.uni-trier.de/pers/hd");


                    String oldURL = authorKey.attr("href");
                    String newURL = oldURL.substring(2);

                    moddedURL.append(newURL);
                    moddedURL.append(".html");

                    Author newAuthor = new Author(authorKey.text(), moddedURL.toString());
                    authors.add(newAuthor);
                }

                articles = ArticleSaxParser.parse(keys, loadingProgress);
                articlesUpdated = true;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //At this point, the loading dialog is still showing and all data has been parsed
            //from the site. Now we set a lot of variables
            if (articlesUpdated && isFirstJournalSearch)
            {
                ArticleArrayAdapterJournals adapter = new ArticleArrayAdapterJournals(context, articles);
                articleList.setAdapter(adapter);
            }

            else if(articlesUpdated && isSeries )
            {
                adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }

            else if(articlesUpdated && !isBibliography)
            {
                adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }
            else if (articlesUpdated && isBibliography)
            {
                adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }

            //This simply displays "match" or "matches" depending on plural or singular results
            if (numberOfEntries == 1)
            {
                TextView matchesFoundTextView = (TextView)findViewById(R.id.matchesFoundTextView);
                matchesFoundTextView.setText(numberOfEntries + " Match Found");
            }
            else
            {
                TextView matchesFoundTextView = (TextView)findViewById(R.id.matchesFoundTextView);
                matchesFoundTextView.setText(numberOfEntries + " Matches Found");
            }


            //The following sets are independent of data parsed
            AuthorArrayAdapter adapter = new AuthorArrayAdapter(context, authors);
            authorList.setAdapter(adapter);
            articleSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    Integer itemSelected = articleSortSpinner.getSelectedItemPosition();
                    if(itemSelected == 0)
                    {
                        sortArticlesByYearDescending();
                    }
                    else if(itemSelected == 1)
                    {
                        sortArticlesByYearAscending();
                    }
                    else if(itemSelected == 2 )
                    {
                        sortArticlesByTypeDescending();
                    }
                    else if(itemSelected == 3)
                    {
                        sortArticlesByTypeAscending();
                    }
                    else if(itemSelected == 4)
                    {
                        sortArticlesByTitleDescending();
                    }
                    else if(itemSelected == 5 )
                    {
                        sortArticlesByTitleAscending();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {
                     sortArticlesByYearDescending();
                }
            });



            if(authors.isEmpty())
            {
                toggleCoAuthorsButton.setVisibility(View.GONE);
            }

            //this method will be running on UI thread
            loadingDialog.dismiss();
        }
    }

    private class AsyncJournalLoader extends AsyncTask<Void, Void, Void>
    {
        Dialog loadingDialog = new Dialog(ActivityArticleList.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            Window dialogWindow = loadingDialog.getWindow();
            dialogWindow.setBackgroundDrawableResource(R.drawable.white_rounded_corners);
            loadingDialog.setContentView(R.layout.daialog_loading_screen);
            loadingProgress = (ProgressBar) loadingDialog.findViewById(R.id.progressBar);
            loadingDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            try
            {
                articles.clear();
                numberOfEntries = 0;

                boolean foundStart = false;
                boolean foundEnd = false;

                // Connect to the web site
                Document document = Jsoup.connect(urlSearched).get();
                Elements links = document.getAllElements();
                loadingProgress.setMax(links.size());
                loadingProgress.setSecondaryProgress(links.size());

                for (Element link : links)
                {
                    loadingProgress.incrementProgressBy(1);

                    if (link.hasAttr("id") && link.attr("id").equals("headline"))
                    {
                        foundStart = true;
                        foundEnd = false;
                    }

                    if( foundStart && !foundEnd && link.hasAttr("href"))
                    {
                        Article nextArticle = new Article();
                        nextArticle.setTitle(link.text());
                        nextArticle.setUrl(link.attr("href"));
                        articles.add(nextArticle);
                        numberOfEntries++;
                    }

                    if(link.hasAttr("id") && link.attr("id").equals("clear-both") )
                    {
                        foundEnd = true;
                        foundStart = false;
                    }
                }

                articlesUpdated = true;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //At this point, the loading dialog is still showing and all data has been parsed
            //from the site. Now we set a lot of variables
            if (articlesUpdated && isFirstJournalSearch)
            {
                ArticleArrayAdapterJournals adapter = new ArticleArrayAdapterJournals(context, articles);
                articleList.setAdapter(adapter);
            }

            else if(articlesUpdated && !isBibliography)
            {
                ArticleArrayAdapter adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }
            else if (articlesUpdated && isBibliography)
            {
                ArticleArrayAdapter adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }

            else if(articlesUpdated && isSeries)
            {
                ArticleArrayAdapter adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }

            if (numberOfEntries == 1)
            {
                TextView matchesFoundTextView = (TextView)findViewById(R.id.matchesFoundTextView);
                matchesFoundTextView.setText(numberOfEntries + " Match Found");
            }
            else
            {
                TextView matchesFoundTextView = (TextView)findViewById(R.id.matchesFoundTextView);
                matchesFoundTextView.setText(numberOfEntries + " Matches Found");
            }

            if(authors.isEmpty())
            {
                toggleCoAuthorsButton.setVisibility(View.GONE);
            }

            articleSortSpinner.setVisibility(View.GONE);
            exportAllArticlesButton.setVisibility(View.GONE);
            shareAllArticlesButton.setVisibility(View.GONE);

            //this method will be running on UI thread
            loadingDialog.dismiss();
        }
    }



    private class AsyncSeriesLoader extends AsyncTask<Void, Void, Void>
    {
        Dialog loadingDialog = new Dialog(ActivityArticleList.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            Window dialogWindow = loadingDialog.getWindow();
            dialogWindow.setBackgroundDrawableResource(R.drawable.white_rounded_corners);
            loadingDialog.setContentView(R.layout.daialog_loading_screen);
            loadingProgress = (ProgressBar) loadingDialog.findViewById(R.id.progressBar);
            loadingDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            try
            {
                articles.clear();
                numberOfEntries = 0;

                // Connect to the web site
                Document document = Jsoup.connect(urlSearched).get();

                ArrayList <String> keys = new ArrayList<String>();

                Elements dbKeys = document.body().select("ul.bullets > li.entry-editor");
                for (Element dbkey : dbKeys)
                {
                    StringBuffer updatedURL = new StringBuffer();
                    updatedURL.append("http://dblp.uni-trier.de/rec/bibtex/");
                    updatedURL.append(dbkey.id());
                    updatedURL.append(".xml");
                    keys.add(updatedURL.toString());

                    numberOfEntries++;
                }

                articles = ArticleSaxParser.parse(keys, loadingProgress);
                articlesUpdated = true;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //At this point, the loading dialog is still showing and all data has been parsed
            //from the site. Now we set a lot of variables
            if (articlesUpdated && isFirstJournalSearch)
            {
                ArticleArrayAdapterJournals adapter = new ArticleArrayAdapterJournals(context, articles);
                articleList.setAdapter(adapter);
            }

            else if(articlesUpdated && !isBibliography)
            {
                ArticleArrayAdapter adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }
            else if (articlesUpdated && isBibliography)
            {
                ArticleArrayAdapter adapter = new ArticleArrayAdapter(context, articles);
                articleList.setAdapter(adapter);
            }

            else if(articlesUpdated && isSeries)
            {
                ArticleArrayAdapterJournals adapter = new ArticleArrayAdapterJournals(context, articles);
                articleList.setAdapter(adapter);
            }

            if (numberOfEntries == 1)
            {
                TextView matchesFoundTextView = (TextView)findViewById(R.id.matchesFoundTextView);
                matchesFoundTextView.setText(numberOfEntries + " Match Found");
            }
            else
            {
                TextView matchesFoundTextView = (TextView)findViewById(R.id.matchesFoundTextView);
                matchesFoundTextView.setText(numberOfEntries + " Matches Found");
            }

            AuthorArrayAdapter adapter = new AuthorArrayAdapter(context, authors);
            authorList.setAdapter(adapter);

            articleSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    Integer itemSelected = articleSortSpinner.getSelectedItemPosition();
                    if(itemSelected == 0)
                    {
                        sortArticlesByYearDescending();
                    }
                    else if(itemSelected == 1)
                    {
                        sortArticlesByYearAscending();
                    }
                    else if(itemSelected == 2)
                    {
                        sortArticlesByTypeDescending();
                    }
                    else if(itemSelected == 3)
                    {
                        sortArticlesByTypeAscending();
                    }
                    else if(itemSelected == 4)
                    {
                        sortArticlesByTitleDescending();
                    }
                    else if(itemSelected == 5)
                    {
                        sortArticlesByTitleAscending();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {
                    sortArticlesByYearDescending();
                }
            });



            if(authors.isEmpty())
            {
                toggleCoAuthorsButton.setVisibility(View.GONE);
            }

            if(authors.isEmpty())
            {
                toggleCoAuthorsButton.setVisibility(View.GONE);
            }

            //this method will be running on UI thread
            loadingDialog.dismiss();
        }
    }

    public void sortArticlesByYearAscending()
    {
        for(int i = 0; i<articles.size(); i++)
        {
            for(int j =articles.size() -1 ; j > 0; j--)
            {
                if (Integer.parseInt(articles.get(j).getYear()) < Integer.parseInt(articles.get(j - 1).getYear()))
                {
                    Article temp = articles.get(j);
                    articles.set(j, articles.get(j-1));
                    articles.set(j-1, temp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void sortArticlesByYearDescending()
    {
        for(int i = 0; i<articles.size(); i++)
        {
            for(int j =articles.size() -1 ; j > 0; j--)
            {
                if (Integer.parseInt(articles.get(j).getYear()) > Integer.parseInt(articles.get(j-1).getYear()))
                {
                    Article temp = articles.get(j);
                    articles.set(j, articles.get(j-1));
                    articles.set(j-1, temp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void sortArticlesByTypeAscending()
    {
        for(int i = 0; i<articles.size(); i++)
        {
            for(int j =articles.size() -1 ; j > 0; j--)
            {
                int stringCompareResult = articles.get(j).getType().compareTo(articles.get(j-1).getType());
                if (stringCompareResult < 0)
                {
                    Article temp = articles.get(j);
                    articles.set(j, articles.get(j-1));
                    articles.set(j-1, temp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void sortArticlesByTypeDescending()
    {
        for(int i = 0; i<articles.size(); i++)
        {
            for(int j =articles.size() -1 ; j > 0; j--)
            {
                int stringCompareResult = articles.get(j).getType().compareTo(articles.get(j-1).getType());
                if (stringCompareResult > 0)
                {
                    Article temp = articles.get(j);
                    articles.set(j, articles.get(j-1));
                    articles.set(j-1, temp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void sortArticlesByTitleAscending()
    {
        for(int i = 0; i<articles.size(); i++)
        {
            for(int j =articles.size() -1 ; j > 0; j--)
            {
                Integer stringCompareResult = articles.get(j).getTitle().compareTo(articles.get(j-1).getTitle());
                if (stringCompareResult < 0)
                {
                    Article temp = articles.get(j);
                    articles.set(j, articles.get(j-1));
                    articles.set(j-1, temp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void sortArticlesByTitleDescending()
    {
        for(int i = 0; i<articles.size(); i++)
        {
            for(int j = articles.size() -1 ; j > 0; j--)
            {
                Integer stringCompareResult = articles.get(j).getTitle().compareTo(articles.get(j - 1).getTitle());
                if (stringCompareResult > 0)
                {
                    Article temp = articles.get(j);
                    articles.set( j, articles.get(j-1));
                    articles.set(j-1, temp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void callAnswerDialog (final String answer, final String question)
    {
        final Dialog answerSearchDialog = new Dialog(this);

        answerSearchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window dialogWindow = answerSearchDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);

        answerSearchDialog.setContentView(R.layout.dialog_faq_answer);
        answerSearchDialog.setTitle(question);

        final TextView questionTitle = (TextView)answerSearchDialog.findViewById(R.id.dialogQuestionTextView);
        questionTitle.setText(question);

        final TextView answerSearchTextField = (TextView)answerSearchDialog.findViewById(R.id.dialogAnswerTextView);
        answerSearchTextField.setText(answer.toString());

        final Button answerCloseButton = (Button)answerSearchDialog.findViewById(R.id.dialogAnswerButton);
        answerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                answerSearchDialog.dismiss();
            }
        });
        answerSearchDialog.show();
    }
}


