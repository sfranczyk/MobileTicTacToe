package com.example.mobiletictactoe.scoreDb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.example.mobiletictactoe.GameMode;
import com.example.mobiletictactoe.Score;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class ScoresRepository {
    ScoresDbHelper dbHelper;

    public ScoresRepository(Context context) {
        dbHelper = new ScoresDbHelper(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertNewScore(int oScore, int xScore, GameMode mode) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String opponent;
        switch (mode) {
            case SinglePlayer:
                opponent = "COMPUTER";
                break;
            case PvPOnline:
                opponent = "GAMER ONLINE";
                break;
            case PvPOffline:
                opponent = "GAMER OFFLINE";
                break;
            default:
                opponent = "";
        }

        ContentValues values = new ContentValues();
        values.put(ScoresForSave.PlayerScoreColumns.O_SCORE, oScore);
        values.put(ScoresForSave.PlayerScoreColumns.X_SCORE, xScore);
        values.put(ScoresForSave.PlayerScoreColumns.TIME, Instant.now().toString());
        values.put(ScoresForSave.PlayerScoreColumns.OPPONENT, opponent);

        db.insert(ScoresForSave.PlayerScoreColumns.TABLE_NAME, null, values);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Score> getScoresFromDb() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                ScoresForSave.PlayerScoreColumns.O_SCORE,
                ScoresForSave.PlayerScoreColumns.X_SCORE,
                ScoresForSave.PlayerScoreColumns.TIME,
                ScoresForSave.PlayerScoreColumns.OPPONENT
        };

        String sortOrder = ScoresForSave.PlayerScoreColumns.TIME + " DESC";

        @SuppressLint("Recycle") Cursor cursor = db.query(
                ScoresForSave.PlayerScoreColumns.TABLE_NAME, projection,
                null,null,null,null, sortOrder
        );

        ArrayList<Score> scores = new ArrayList<>();
        while(cursor.moveToNext()) {
            Score s = new Score();
            s.oScore = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ScoresForSave.PlayerScoreColumns.O_SCORE));
            s.xScore = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ScoresForSave.PlayerScoreColumns.X_SCORE));
            s.date = Date.from( Instant.parse( cursor.getString(
                    cursor.getColumnIndexOrThrow(ScoresForSave.PlayerScoreColumns.TIME))));
            s.opponent = cursor.getString(
                    cursor.getColumnIndexOrThrow(ScoresForSave.PlayerScoreColumns.OPPONENT));

            scores.add(s);
        }
        cursor.close();
        return scores;
    }
}
