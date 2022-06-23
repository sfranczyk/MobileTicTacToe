package com.example.mobiletictactoe.scoreDb;

import android.provider.BaseColumns;

public final class ScoresForSave {
    private ScoresForSave() {
    }

    public static class PlayerScoreColumns implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String O_SCORE = "o_score";
        public static final String X_SCORE = "x_score";
        public static final String TIME = "time";
        public static final String OPPONENT = "opponent";
    }
}
