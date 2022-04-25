package com.example.mobiletictactoe;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GameStatisticsTest {
    GameStatistics gameStatistics = new GameStatistics();
    @Test
    public void checkIncrement() {
        gameStatistics.incrementScore(CellValue.O);
        gameStatistics.incrementScore(CellValue.O);
        gameStatistics.incrementScore(CellValue.O);
        assertEquals(gameStatistics.getScorePlayerX(), 0);
        assertEquals(gameStatistics.getScorePlayerO(), 3);
    }
}
