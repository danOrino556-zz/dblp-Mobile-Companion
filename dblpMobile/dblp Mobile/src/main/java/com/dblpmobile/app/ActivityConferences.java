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

public class ActivityConferences extends ActionBarActivity
{
    Context context = this;

    boolean conferencesUpdated = false;
    String conferenceLetterSearched = null;

    ArrayList <Author> conferences = new ArrayList<Author>();
    ListView conferenceList;

    Integer numberOfEntries = 0;
    ProgressBar loadingProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_conference);

        Bundle conferenceWanted= getIntent().getExtras();
        if (getIntent().hasExtra("letterSearched"))
        {
            conferenceLetterSearched = (conferenceWanted.getString("letterSearched"));
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

        TextView conferenceTitle = (TextView)findViewById(R.id.conferenceTextView);
        conferenceTitle.setText("Conferences starting with : " + conferenceLetterSearched);

        conferenceList = (ListView)findViewById(R.id.conferenceListView);
        conferenceList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent x = new Intent(ActivityConferences.this, ActivityArticleList.class);
                Bundle extras = new Bundle();
                extras.putString("urlSearched", conferences.get(i).getUrlpt());
                extras.putString("authorSearched", conferences.get(i).getName());
                extras.putString("isBibliography", "true");
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        new AsyncConferenceLoader().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_conferences, menu);
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

    private class AsyncConferenceLoader extends AsyncTask<Void, Void, Void>
    {
        Dialog loadingDialog = new Dialog(ActivityConferences.this);

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
                conferences.clear();
                // Connect to the web site

                String conferenceLetterSearchedlower = conferenceLetterSearched.toLowerCase();
                String url = "http://www.informatik.uni-trier.de/~ley/db/conf/index-"+conferenceLetterSearchedlower+".html";

                boolean foundStart = false;
                boolean foundEnd = false;

                Document document = Jsoup.connect(url).get();
                Elements links = document.getAllElements();
                loadingProgress.setMax(links.size());
                loadingProgress.setSecondaryProgress(links.size());
                for (Element link : links)
                {
                    loadingProgress.incrementProgressBy(1);
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

                            Author nextConference = new Author(linkText, linkHref);
                            conferences.add(nextConference);
                            numberOfEntries++;
                        }
                    }
                    if (link.hasAttr("href"))
                    {
                        if(link.text().equals("3"))
                        {
                            foundStart = true;
                        }
                    }
                }
                conferencesUpdated = true;
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

            if (conferencesUpdated)
            {
                AuthorArrayAdapter adapter = new AuthorArrayAdapter(context, conferences);
                conferenceList.setAdapter(adapter);
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
