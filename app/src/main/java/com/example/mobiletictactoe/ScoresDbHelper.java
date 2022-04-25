package com.example.mobiletictactoe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoresDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ScoresForSave.PlayerScoreColumns.TABLE_NAME + " (" +
                    ScoresForSave.PlayerScoreColumns._ID + " INTEGER PRIMARY KEY," +
                    ScoresForSave.PlayerScoreColumns.O_SCORE + " INTEGER," +
                    ScoresForSave.PlayerScoreColumns.X_SCORE + " INTEGER," +
                    ScoresForSave.PlayerScoreColumns.TIME + " TEXT," +
                    ScoresForSave.PlayerScoreColumns.COMPUTER + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ScoresForSave.PlayerScoreColumns.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Scores.db";

    public ScoresDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
