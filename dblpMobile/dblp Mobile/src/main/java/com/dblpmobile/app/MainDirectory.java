package com.dblpmobile.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainDirectory extends ActionBarActivity
{
    public static final int REQUEST_LAST = 1;

    String authorNameSearched = null;

    boolean searchCollapsed = false;
    boolean bibliographiesCollapsed = false;
    boolean linksCollapsed = false;
    boolean mydblpCollapsed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Context context = this;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_directory);

        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonDirectory);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                  onBackPressed();
            }
        });

        final RelativeLayout searchLayout = (RelativeLayout)findViewById(R.id.searchMainLayout);
        final ImageButton collapseSearchButton = (ImageButton)findViewById(R.id.collapseSearchButton);
        collapseSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!searchCollapsed)
                {
                    searchLayout.setVisibility(View.GONE);
                    collapseSearchButton.setImageResource(android.R.drawable.ic_menu_more);
                    searchCollapsed = true;
                }
                else if(searchCollapsed)
                {
                    searchLayout.setVisibility(View.VISIBLE);
                    collapseSearchButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    searchCollapsed = false;
                }

            }
        });
        collapseSearchButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Collapse all the author search buttons", "Help");
                return false;
            }
        });

        Button authorSearchButton = (Button)findViewById(R.id.authorButton);
        authorSearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog authorSearchDialog = new Dialog(MainDirectory.this);

                authorSearchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = authorSearchDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                authorSearchDialog.setContentView(R.layout.dialog_search);
                authorSearchDialog.setTitle("Type an author");

                final EditText authorSearchTextField = (EditText)authorSearchDialog.findViewById(R.id.authorNameText);

                final Button authorSearch = (Button)authorSearchDialog.findViewById(R.id.dialogAuthorButton);
                authorSearch.setBackgroundResource(R.drawable.dark_grey_rounded_corners);
                authorSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {


                        authorNameSearched = authorSearchTextField.getText().toString();

                        Intent i = new Intent(MainDirectory.this, ActivityAuthorList.class);
                        Bundle extras = new Bundle();
                        extras.putString("authorSearched", authorNameSearched);

                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

                        authorSearchDialog.dismiss();
                    }
                });

                authorSearchDialog.show();
            }
        });

        ImageButton micSearchButton = (ImageButton)findViewById(R.id.micSearchButton);
        micSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try
                {
                    startActivityForResult(i, REQUEST_LAST);
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });
        micSearchButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Conduct a standard author search via voice", "Help");
                return false;
            }
        });


        final Button facetedSearchButton = (Button)findViewById(R.id.facetedSearchButton);
        facetedSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                facetedSearchButton.setText("Faceted Search is Currently not Available");
            }
        });

        final Button freeSearchButton = (Button)findViewById(R.id.freeSearchButton);
        freeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                freeSearchButton.setText("Free Search is Currently not Implemented");

            }
        });

        final Button completeSearchButton = (Button)findViewById(R.id.completeSearchButton);
        completeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                completeSearchButton.setText("Complete Search is Currently not Implemented");
            }
        });

        final RelativeLayout bibliopgraphiesLayout = (RelativeLayout)findViewById(R.id.bibliographiesMainLayout);
        final ImageButton collapseBiblographiesButton = (ImageButton)findViewById(R.id.collapseBibliographyButton);
        collapseBiblographiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!bibliographiesCollapsed)
                {
                    bibliopgraphiesLayout.setVisibility(View.GONE);
                    collapseBiblographiesButton.setImageResource(android.R.drawable.ic_menu_more);
                    bibliographiesCollapsed = true;
                }
                else if(bibliographiesCollapsed)
                {
                    bibliopgraphiesLayout.setVisibility(View.VISIBLE);
                    collapseBiblographiesButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    bibliographiesCollapsed = false;
                }

            }
        });
        collapseBiblographiesButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Collapse all the bibliography search buttons", "Help");
                return false;
            }
        });

        final Button conferencesSearchButton = (Button)findViewById(R.id.conferencesButton);
        conferencesSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(MainDirectory.this, ActivityLetterList.class);
                Bundle extras = new Bundle();
                extras.putString("searchType", "conference");
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        final Button journalSearchButton = (Button)findViewById(R.id.journalButton);
        journalSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(MainDirectory.this, ActivityLetterList.class);
                Bundle extras = new Bundle();
                extras.putString("searchType", "journal");
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        final Button seriesSearchButton = (Button) findViewById(R.id.seriesButton);
        seriesSearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                Intent x = new Intent(MainDirectory.this, ActivityJournals.class);
                Bundle extras = new Bundle();
                extras.putString("letterSearched", "series");
                extras.putString("isSeries", "true");
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        Button booksReferenceButton = (Button)findViewById(R.id.booksReferenceButton);
        booksReferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent x = new Intent(MainDirectory.this, ActivityArticleList.class);
                Bundle extras = new Bundle();
                extras.putString("urlSearched", "http://www.informatik.uni-trier.de/~ley/db/reference/index.html");
                extras.putString("authorSearched", "Encyclopedias, Handbooks, etc.");
                extras.putString("isBibliography", "true");
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

            }
        });

        Button collectionsButton = (Button)findViewById(R.id.booksCollectionsButton);
        collectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent x = new Intent(MainDirectory.this, ActivityArticleList.class);
                Bundle extras = new Bundle();
                extras.putString("urlSearched", "http://www.informatik.uni-trier.de/~ley/db/books/collections/index.html");
                extras.putString("authorSearched", "Collections");
                extras.putString("isBibliography", "true");
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        final RelativeLayout mydblpLayout = (RelativeLayout)findViewById(R.id.myDPLPMainLayout);
        final ImageButton collapsemydblpButton = (ImageButton)findViewById(R.id.collapsemyDBLPButton);
        collapsemydblpButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Collapse the mydblp log in button", "Help");
                return false;
            }
        });

        collapsemydblpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!mydblpCollapsed)
                {
                    mydblpLayout.setVisibility(View.GONE);
                    collapsemydblpButton.setImageResource(android.R.drawable.ic_menu_more);
                    mydblpCollapsed = true;
                }
                else if(mydblpCollapsed)
                {
                    mydblpLayout.setVisibility(View.VISIBLE);
                    collapsemydblpButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    mydblpCollapsed = false;
                }
            }
        });

        Button mydblpLogInButton = (Button)findViewById(R.id.myDBLPLogInButton);
        mydblpLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final Dialog userLogInDialog = new Dialog(MainDirectory.this);

                userLogInDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = userLogInDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                userLogInDialog.setContentView(R.layout.dialog_login);


                userLogInDialog.show();
            }
        });



        final RelativeLayout linksLayout = (RelativeLayout)findViewById(R.id.linksMainLayout);
        final ImageButton collapseLinksButton = (ImageButton)findViewById(R.id.collapseLinksButton);
        collapseLinksButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                callAnswerDialog("Collapse the FAQ and Links buttons", "Help");
                return false;
            }
        });
        collapseLinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!linksCollapsed)
                {
                    linksLayout.setVisibility(View.GONE);
                    collapseLinksButton.setImageResource(android.R.drawable.ic_menu_more);
                    linksCollapsed = true;
                }
                else if(linksCollapsed)
                {
                    linksLayout.setVisibility(View.VISIBLE);
                    collapseLinksButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    linksCollapsed = false;
                }
            }
        });

        final Button cscOrganizationsButton = (Button) findViewById(R.id.cscOrgButton);
        cscOrganizationsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                Intent x = new Intent(MainDirectory.this, ActivityLinks.class);
                Bundle extras = new Bundle();
                String urlSelected = "http://www.informatik.uni-trier.de/~ley/db/links.html";
                extras.putString("urlSearched", urlSelected);
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        final Button faqButton = (Button)findViewById(R.id.faqButton);
        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                Intent x = new Intent(MainDirectory.this, ActivityLinks.class);
                Bundle extras = new Bundle();
                String urlSelected = "http://www.informatik.uni-trier.de/~ley/faq/index.html";
                extras.putString("urlSearched", urlSelected);
                extras.putString("searchType", "faq");
                x.putExtras(extras);
                startActivity(x);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        ImageButton expandAllButton = (ImageButton)findViewById(R.id.expanAllButton);
        expandAllButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {

                return false;
            }
        });
        expandAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(searchCollapsed)
                {
                    searchLayout.setVisibility(View.VISIBLE);
                    collapseSearchButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    searchCollapsed = false;
                }

                if(bibliographiesCollapsed)
                {
                    bibliopgraphiesLayout.setVisibility(View.VISIBLE);
                    collapseBiblographiesButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    bibliographiesCollapsed = false;
                }

                if(mydblpCollapsed)
                {
                    mydblpLayout.setVisibility(View.VISIBLE);
                    collapsemydblpButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    mydblpCollapsed = false;
                }

                if(linksCollapsed)
                {
                    linksLayout.setVisibility(View.VISIBLE);
                    collapseLinksButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    linksCollapsed = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_directory, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_LAST  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            Intent i = new Intent(MainDirectory.this, ActivityAuthorList.class);
            Bundle extras = new Bundle();
            extras.putString("authorSearched", thingsYouSaid.get(0));

            i.putExtras(extras);
            startActivity(i);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
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
