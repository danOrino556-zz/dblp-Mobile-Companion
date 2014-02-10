package com.dblpmobile.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by danielrobertson on 1/31/14.
 */
public class ArticleArrayAdapterJournals extends ArrayAdapter<Article>
{
    private final Context context;
    private final ArrayList<Article> values;

    public ArticleArrayAdapterJournals(Context context, ArrayList<Article> values)
    {
        super(context, R.layout.author_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.author_row, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.authorRowTextView);
        textView.setText(values.get(position).getTitle());

        return rowView;

    }
}
