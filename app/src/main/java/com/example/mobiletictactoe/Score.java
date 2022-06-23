package com.example.mobiletictactoe;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Score {
    public int oScore;
    public int xScore;
    public Date date;
    public String opponent;

    @NonNull
    @SuppressLint("DefaultLocale")
    public String toString() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        return String.format("O: %d, X: %d | %s | date: %s", oScore, xScore,
                opponent, strDate);
    }
}
