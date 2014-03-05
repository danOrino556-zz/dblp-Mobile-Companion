package com.dblpmobile.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by danielrobertson on 2/25/14.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "articlesdb";

    // Articles table name
    private static final String TABLE_ARTICLES = "articles";
    private static final String TABLE_AUTHORS = "authors";
    private static final String TABLE_EDITORS = "editors";
    private static final String TABLE_FOLDERS = "folders";

    // Articles Table Columns names
    private static final String KEY_ID = "id";
    private static final String DBLP_KEY = "key";
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String BOOK_TITLE = "book_title";
    private static final String JOURNAL = "journal";
    private static final String VOLUME = "volume";
    private static final String NUMBER = "number";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private static final String PAGES = "pages";
    private static final String ADDRESS = "address";
    private static final String URL = "url";
    private static final String EE = "ee";
    private static final String CDROM = "cdRom";
    private static final String CITE = "cite";
    private static final String PUBLISHER = "publisher";
    private static final String NOTE = "note";
    private static final String CROSSREF = "crossRef";
    private static final String ISBN = "isbn";
    private static final String SERIES = "series";
    private static final String SCHOOL = "school";
    private static final String CHAPTER = "chapter";
    private static final String MDATE ="mdate";

    //Author Table Column Names
    private static final String AUTHOR_NAME = "author_name";

    //Editor Table Column Names
    private static final String EDITOR_NAME = "editor_name";

    //Folder Table Column Names
    private static final String FOLDER_NAME = "folder_name";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ARTICLES_TABLE = "CREATE TABLE " + TABLE_ARTICLES +
                "(" + DBLP_KEY + " TEXT PRIMARY KEY," +
                TYPE + " TEXT," +
                TITLE + " TEXT," +
                BOOK_TITLE + " TEXT," +
                JOURNAL + " TEXT," +
                VOLUME + " TEXT," +
                NUMBER + " TEXT," +
                MONTH + " TEXT," +
                YEAR + " TEXT," +
                PAGES + " TEXT," +
                ADDRESS + " TEXT," +
                URL + " TEXT," +
                EE + " TEXT," +
                CDROM + " TEXT," +
                CITE + " TEXT," +
                PUBLISHER + " TEXT," +
                NOTE + " TEXT," +
                CROSSREF + " TEXT," +
                ISBN + " TEXT," +
                SERIES + " TEXT," +
                SCHOOL + " TEXT," +
                CHAPTER + " TEXT," +
                MDATE + " TEXT" + ")";
        db.execSQL(CREATE_ARTICLES_TABLE);

        String CREATE_AUTHORS_TABLE = "CREATE TABLE " + TABLE_AUTHORS + "("
                + DBLP_KEY + " TEXT PRIMARY KEY," +
                AUTHOR_NAME + " TEXT," + ")";
        db.execSQL(CREATE_AUTHORS_TABLE);

        String CREATE_EDITORS_TABLE = "CREATE TABLE " + TABLE_EDITORS + "("
                + DBLP_KEY + " TEXT PRIMARY KEY," +
                EDITOR_NAME + " TEXT," + ")";
        db.execSQL(CREATE_EDITORS_TABLE);

        String CREATE_FOLDERS_TABLE = "CREATE TABLE " + TABLE_FOLDERS + "("
                + DBLP_KEY + " TEXT PRIMARY KEY," +
                FOLDER_NAME + " TEXT," + ")";
        db.execSQL(CREATE_FOLDERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDITORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDERS);

        // Create tables again
        onCreate(db);
    }

    // Adding new article
    void addArticle(Article article)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBLP_KEY, article.getKey());
        values.put(TYPE, article.getType());
        values.put(TITLE, article.getTitle());
        values.put(BOOK_TITLE, article.getBookTitle());
        values.put(JOURNAL, article.getJournal());
        values.put(VOLUME, article.getVolume());
        values.put(NUMBER, article.getNumber());
        values.put(MONTH, article.getMonth());
        values.put(YEAR, article.getYear());
        values.put(PAGES, article.getPages());
        values.put(ADDRESS, article.getAddress());
        values.put(URL, article.getUrl());
        values.put(EE, article.getEe());
        values.put(CDROM, article.getCdRom());
        values.put(CITE, article.getCite());
        values.put(PUBLISHER, article.getPublisher());
        values.put(NOTE, article.getNote());
        values.put(CROSSREF, article.getCrossRef());
        values.put(ISBN, article.getIsbn());
        values.put(SERIES, article.getSeries());
        values.put(SCHOOL, article.getSchool());
        values.put(CHAPTER, article.getChapter());
        values.put(MDATE, article.getMdate());

        // Inserting Row
        db.insert(TABLE_ARTICLES, null, values);

        ContentValues authors = new ContentValues();
        for (String author : article.getAuthors())
        {
            authors.put(AUTHOR_NAME, author);
        }
        db.insert(TABLE_AUTHORS, null, authors);

        ContentValues editors = new ContentValues();
        for (String editor : article.getEditors())
        {
            authors.put(EDITOR_NAME, editor);
        }
        db.insert(TABLE_EDITORS, null, editors);

        db.close(); // Closing database connection
    }
}
