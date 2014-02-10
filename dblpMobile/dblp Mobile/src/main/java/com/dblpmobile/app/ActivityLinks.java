package com.dblpmobile.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ActivityLinks extends ActionBarActivity
{
    Context context = this;

    boolean linksUpdated = false;
    ArrayList<Author> links = new ArrayList<Author>();

    String urlToSearch;

    boolean isFAQ = false;
    boolean answerReceived = false;
    String faqAnswer;
    String question;

    ListView linksList;

    Integer numberOfEntries = 0;
    ProgressBar loadingProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_links);

        TextView articleText = (TextView)findViewById(R.id.articleTextView);
        articleText.setText("Computer Science Links");

        Bundle pageWanted= getIntent().getExtras();
        if (getIntent().hasExtra("urlSearched"))
        {
            urlToSearch = (pageWanted.getString("urlSearched"));
        }
        if (getIntent().hasExtra("searchType"))
        {
            articleText.setText("Frequently Asked Questions");
            isFAQ = true;
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

        linksList = (ListView)findViewById(R.id.articleListView);
        linksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(isFAQ)
                {
                    question = links.get(i).getName();
                    questionPopUp(links.get(i).getUrlpt());

                    while(true)
                    {
                        if (answerReceived)
                        {
                            callAnswerDialog(faqAnswer, question);
                            faqAnswer = null;
                            answerReceived = false;
                            break;
                        }
                    }
                }
                else
                {
                    Intent x = new Intent(ActivityLinks.this, ActivityWebView.class);
                    Bundle extras = new Bundle();
                    extras.putString("URL", links.get(i).getUrlpt());
                    x.putExtras(extras);
                    startActivity(x);
                }
            }
        });

        new AsyncLinksLoader().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_links, menu);
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


    public void questionPopUp (final String urlOfAnswer)
    {
        Thread t = new Thread()
        {
            public void run()
            {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                //Sending a message to the server
                try
                {
                    // Connect to the web site


                    StringBuffer answer = new StringBuffer();
                    Document document = Jsoup.connect(urlOfAnswer).get();
                    Elements linksInLinks = document.select("p");
                    for (Element link : linksInLinks)
                    {
                        {
                            if (!link.hasAttr("href"))
                            {
                                answer.append(link.text());
                                answer.append("\n\n");
                            }
                        }
                    }
                    faqAnswer = answer.toString();
                    answerReceived = true;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "in catch block", Toast.LENGTH_LONG).show();
                }
                Looper.loop(); //Loop in the message queue
            }
        };
        t.start();
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

    private class AsyncLinksLoader extends AsyncTask<Void, Void, Void>
    {
        Dialog loadingDialog = new Dialog(ActivityLinks.this);

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
                links.clear();
                // Connect to the web site

                boolean foundStart = false;
                boolean foundEnd = false;

                Document document = Jsoup.connect(urlToSearch).get();
                Elements linksInLinks = document.getAllElements();
                loadingProgress.setMax(linksInLinks.size());
                loadingProgress.setSecondaryProgress(linksInLinks.size());
                for (Element link : linksInLinks)
                {
                    loadingProgress.incrementProgressBy(1);
                    {
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

                                Author newLink = new Author(linkText, linkHref);
                                links.add(newLink);
                                numberOfEntries++;
                            }
                        }
                        if (link.hasAttr("id"))
                        {
                            if(link.attr("id").equals("headline"))
                            {
                                foundStart = true;
                            }
                        }
                    }
                }
                linksUpdated = true;
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

            if(linksUpdated)
            {
                AuthorArrayAdapter adapter = new AuthorArrayAdapter(context, links);
                linksList.setAdapter(adapter);
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
