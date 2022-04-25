package com.example.mobiletictactoe;

public class GameStatistics {
    private int scorePlayerO;
    private int scorePlayerX;

    GameStatistics() {
        reset();
    }

    GameStatistics(int scorePlayerO, int scorePlayerX) {
        this.scorePlayerO = scorePlayerO;
        this.scorePlayerX = scorePlayerX;
    }

    public void reset() {
        scorePlayerO = 0;
        scorePlayerX = 0;
    }

    public void incrementScore(CellValue c) {
        if (c == CellValue.O) {
            ++scorePlayerO;
        } else if (c == CellValue.X) {
            ++scorePlayerX;
        }
    }

    public void incrementScorePlayerO(){
        ++scorePlayerO;
    }

    public void incrementScorePlayerX(){
        ++scorePlayerX;
    }

    public int getScorePlayerO() {
        return scorePlayerO;
    }

    public int getScorePlayerX() {
        return scorePlayerX;
    }
}
