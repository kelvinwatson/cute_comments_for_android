package com.watsonlogic.cutecomment;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//creates the database
public class CommentsSQLiteHelper extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "comments";

    //columns
    public static final String COLUMN_NAMED_INSERT_ID = "_id";
    public static final String COLUMN_NAMED_POSITION = "position";
    public static final String COLUMN_NAMED_COLOR = "color";
    public static final String COLUMN_NAMED_COMMENT = "comment";
    public static final String COLUMN_NAMED_DATE = "date";
    public static final String COLUMN_NAMED_SUCCESS = "success";


    private static final String DATABASE_NAME = "commments.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + " (" +
            COLUMN_NAMED_INSERT_ID + " integer primary key autoincrement, " +
            COLUMN_NAMED_POSITION + " integer, " +
            COLUMN_NAMED_COLOR + " text, " +
            COLUMN_NAMED_COMMENT + " text, " +
            COLUMN_NAMED_DATE + " date, " +
            COLUMN_NAMED_SUCCESS + " tinyint)";

    public CommentsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public CommentsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public CommentsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    //SQLiteDatabase db is the Java representation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //called if database version is increased in your app
        Log.w(CommentsSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //getReadableDatabase() read mode
    //getWritableDatabase() write mode
}
