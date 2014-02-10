package com.dblpmobile.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityLetterList extends ActionBarActivity
{
    String [] letters = {"A", "B", "C", "D", "E", "F", "G", "H","I","J","K","L","M","N","O",
        "P","Q","R","S","T","U","V","W","X","Y","Z"};
    boolean wantsJournal = false;
    boolean wantsConference = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_letter_list);

        Bundle articleWanted= getIntent().getExtras();
        if (getIntent().hasExtra("searchType"))
        {
            String searchWanted = (articleWanted.getString("searchType"));
            if (searchWanted.equals("journal"))
            {
                wantsJournal = true;
            }
            else if (searchWanted.equals("conference"))
            {
                wantsConference = true;
            }

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

        TextView letterListTitle = (TextView)findViewById(R.id.letterTextView);
        if(wantsJournal)
        {
            letterListTitle.setText("Search Journals");
        }
        else if (wantsConference)
        {
            letterListTitle.setText("Search Conferences");
        }

        ListView lettersList = (ListView)findViewById(R.id.letterListView);
        lettersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String letterSelected = letters[i];

                if (wantsJournal)
                {
                    Intent x = new Intent(ActivityLetterList.this, ActivityJournals.class);
                    Bundle extras = new Bundle();
                    extras.putString("letterSearched", letterSelected);

                    x.putExtras(extras);
                    startActivity(x);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }

                else if (wantsConference)
                {
                    Intent x = new Intent(ActivityLetterList.this, ActivityConferences.class);
                    Bundle extras = new Bundle();
                    extras.putString("letterSearched", letterSelected);

                    x.putExtras(extras);
                    startActivity(x);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }
        });


        final LetterArrayAdapter adapter = new LetterArrayAdapter(this, letters);
        lettersList.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_letter_list, menu);
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



}
