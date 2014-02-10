package com.dblpmobile.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by danielrobertson on 1/27/14.
 */
public class ArticleArrayAdapter extends ArrayAdapter <Article>
{
    private final Context context;
    private final ArrayList<Article> values;

    public ArticleArrayAdapter(Context context, ArrayList<Article> values)
    {
        super(context, R.layout.author_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final int i = position;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.article_row, parent, false);

        TextView yearTag = (TextView)rowView.findViewById(R.id.articleYearTextView);
        yearTag.setText(values.get(position).getYear() + " : " + values.get(i).getType());

        TextView textView = (TextView) rowView.findViewById(R.id.articleRowTextView);
        textView.setText(values.get(position).getTitle());

        StringBuffer authorText = new StringBuffer();

        if (values.get(i).getAuthors().isEmpty() && !values.get(i).getEditors().isEmpty())
        {
            for (int j = 0; j < values.get(i).getEditors().size(); j++)
            {
                authorText.append(values.get(i).getEditors().get(j));
                if (j == values.get(i).getEditors().size()-1)
                {
                    authorText.append(".");
                }
                else
                {
                    authorText.append(", ");
                }
            }
        }
        else if (!values.get(i).getAuthors().isEmpty() && values.get(i).getEditors().isEmpty())
        {
            for (int j = 0; j < values.get(i).getAuthors().size(); j++)
            {
                authorText.append(values.get(i).getAuthors().get(j));
                if (j == values.get(i).getAuthors().size()-1)
                {
                    authorText.append(".");
                }
                else
                {
                    authorText.append(", ");
                }
            }
        }
        else if(values.get(i).getAuthors().isEmpty() && values.get(i).getEditors().isEmpty())
        {
            authorText.append("No Authors/Editors Listed");
        }

        TextView authorsText = (TextView)rowView.findViewById(R.id.articleAuthorTextView);
        authorsText.setText(authorText.toString());

        ImageButton viewDocumentButton = (ImageButton)rowView.findViewById(R.id.articleViewDocumentImageButton);
        viewDocumentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent x = new Intent(context, ActivityWebView.class);
                Bundle extras = new Bundle();
                extras.putString("URL", values.get(i).getEe());
                x.putExtras(extras);
                context.startActivity(x);
            }
        });

        ImageButton exportDocumentButton = (ImageButton)rowView.findViewById(R.id.articleExportDocumentImageButton2);
        exportDocumentButton.setOnClickListener(new View.OnClickListener()
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
                        String Citation;
                        String Reference;

                        if(exportFormatSpinner.getSelectedItemPosition() == 0)
                        {
                            Citation = CitationCreator.apaCitation(values.get(position));
                            Reference = ReferenceCreator.apaReference(values.get(position));
                        }
                        else if(exportFormatSpinner.getSelectedItemPosition() == 1)
                        {
                            Citation = "XML";
                            Reference = ReferenceCreator.xmlReference(values.get(position));
                        }
                        else
                        {
                            Citation = CitationCreator.mlaCitation(values.get(position));
                            Reference = ReferenceCreator.mlaReference(values.get(position));
                        }

                        String recAddress = emailAddress.getText().toString();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{recAddress});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Citation for : " + values.get(position).getTitle());
                        i.putExtra(Intent.EXTRA_TEXT   , "Citation : \n\n" + Citation +
                                "\n\nReference : \n\n" + Reference);
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

        ImageButton shareDocumentButton = (ImageButton)rowView.findViewById(R.id.articleShareDocumentImageButton3);
        shareDocumentButton.setOnClickListener(new View.OnClickListener()
        {
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
                        String recAddress = emailAddress.getText().toString();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{recAddress});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Publication : " + values.get(position).getTitle());
                        i.putExtra(Intent.EXTRA_TEXT   , values.get(position).listAttributes());
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

        ImageButton askOthersButton = (ImageButton)rowView.findViewById(R.id.articleAskOthersImageButton4);
        askOthersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent j = new Intent(context, ActivityWebView.class);
                Bundle extras = new Bundle();
                extras.putString("URL", "http://scholar.google.com/scholar?q=" + values.get(i).getTitle());
                j.putExtras(extras);
                context.startActivity(j);
            }
        });

        ImageButton articleInformationButton = (ImageButton)rowView.findViewById(R.id.articleInformationImageButton5);
        articleInformationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog answerSearchDialog = new Dialog(context);

                answerSearchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = answerSearchDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);

                answerSearchDialog.setContentView(R.layout.dialog_entry_information);
                answerSearchDialog.setTitle(values.get(i).getTitle());

                final TextView answerSearchTextField = (TextView)answerSearchDialog.findViewById(R.id.dialogAnswerTextView);
                answerSearchTextField.setText(values.get(i).listAttributes());

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
        });

        return rowView;
    }
}
