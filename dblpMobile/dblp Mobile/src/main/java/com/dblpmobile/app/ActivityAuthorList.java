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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ActivityAuthorList extends ActionBarActivity
{
    Context context = this;

    //Author Search
    String authorNameSearched = null;
    ArrayList<Author> authors = new ArrayList<Author>();

    Integer numberOfEntries = 0;

    boolean authorsUpdated = false;

    ListView authorList;
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_author_list);

        //Received via voice/dialog from the previous ativity
        Bundle authorWanted= getIntent().getExtras();
        if (getIntent().hasExtra("authorSearched"))
        {
            authorNameSearched = (authorWanted.getString("authorSearched"));
        }

        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonDirectory);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
//
        authorList = (ListView)findViewById(R.id.authorListView);
        authorList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //Transition
                String urlSelected = authors.get(i).getUrlpt();
                Intent x = new Intent(ActivityAuthorList.this, ActivityArticleList.class);
                Bundle extras = new Bundle();
                extras.putString("urlSearched", urlSelected);
                extras.putString("authorSearched", authors.get(i).getName());
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

            }
        });

        TextView authorTitle = (TextView)findViewById(R.id.authorTextView);
        authorTitle.setText("Results for : " + authorNameSearched);

        new AsyncAuthorLoader().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_author_list, menu);
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


    private class AsyncAuthorLoader extends AsyncTask<Void, Void, Void>
    {
        Dialog loadingDialog = new Dialog(ActivityAuthorList.this);

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

            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you dont want to pass argument and u can access the
            //parent class' variable url over here
            try
            {
                String url = "http://www.informatik.uni-trier.de/~ley/pers/hs?q=" + authorNameSearched;
                authors.clear();
                // Connect to the web site

                Document document = Jsoup.connect(url).get();

                boolean foundStart = false;
                boolean foundEnd = false;

                //The following title select is checked just in case we dont need an author
                //list since the initial search is specific enough for a transition directly to
                //articles

                Elements titles = document.select("title");
                for(Element title : titles)
                {
                    //if the title contains author, it will be for the author list
                    if(!title.text().contains("author"))
                    {

                        String [] namesSearches = authorNameSearched.split(" ");

                        //The individual author pages must be all lowercase with the first letter
                        //capitalized
                        for (int j = 0; j< namesSearches.length; j++)
                        {
                            namesSearches[j] = Character.toUpperCase(namesSearches[j].charAt(0)) +
                                               namesSearches[j].substring(1).toLowerCase();
                        }


                        //final URL must be in this type of format :
                        //http://dblp.uni-trier.de/pers/hd/r/Robertson:A=_Gerry.html

                        StringBuilder moddedURL = new StringBuilder();
                        moddedURL.append("http://dblp.uni-trier.de/pers/hd/");
                        String lastName = namesSearches[namesSearches.length - 1].toLowerCase();
                        moddedURL.append(lastName.charAt(0) + "/");

                        for(int i = (namesSearches.length -1); i > 0; i--)
                        {
                            moddedURL.append(namesSearches[i] + ":");
                        }

                        moddedURL.append(namesSearches[0]);
                        moddedURL.append(".html");

                        //Now that we have a properly formatted URL we can transition directly to
                        //the activity
                        Intent x = new Intent(ActivityAuthorList.this, ActivityArticleList.class);
                        Bundle extras = new Bundle();
                        extras.putString("urlSearched", moddedURL.toString());
                        extras.putString("authorSearched", authorNameSearched);
                        x.putExtras(extras);
                        startActivity(x);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        finish();
                    }
                }


                Elements links = document.getAllElements();
                loadingProgress.setMax(links.size());
                loadingProgress.setSecondaryProgress(links.size());
                for (Element link : links)
                {
                    loadingProgress.incrementProgressBy(1);

                    if (link.hasAttr("id"))
                    {
                        if(link.attr("id").equals("headline"))
                        {
                            foundStart = true;
                        }
                    }
                    if (link.hasAttr("id"))
                    {
                        if(link.attr("id").equals("clear-both"))
                        {
                            foundEnd = true;
                        }
                    }
                    if( foundStart && !foundEnd)
                    {
                        if(link.hasAttr("href"))
                        {
                            String linkHref = link.attr("href");
                            String linkText = link.text();

                            Author nextAuthor = new Author(linkText, linkHref);
                            authors.add(nextAuthor);
                            numberOfEntries++;
                        }
                    }
                }
                authorsUpdated = true;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (authorsUpdated)
            {
                AuthorArrayAdapter adapter = new AuthorArrayAdapter(context, authors);
                authorList.setAdapter(adapter);
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
            loadingDialog.dismiss();
        }
    }
}
