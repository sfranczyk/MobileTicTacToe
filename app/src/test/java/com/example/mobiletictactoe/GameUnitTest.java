package com.example.mobiletictactoe;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GameUnitTest {
    GameState gameState = new GameState();
    @Test
    public void checkHorizontalWin() {
        gameState.playerMove(1);
        gameState.playerMove(10);

        gameState.playerMove(2);
        gameState.playerMove(11);

        gameState.playerMove(3);
        gameState.playerMove(12);

        gameState.playerMove(4);
        gameState.playerMove(13);

        gameState.playerMove(5);

        gameState.checkWin();

        int[] actual = gameState.getWinLineIndex().stream().mapToInt(i->i).toArray();

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, actual);
    }

    @Test
    public void checkDiagonalWin() {
        gameState.playerMove(1);
        gameState.playerMove(10);

        gameState.playerMove(12);
        gameState.playerMove(11);

        gameState.playerMove(23);
        gameState.playerMove(15);

        gameState.checkWin();

        int[] actual = gameState.getWinLineIndex().stream().mapToInt(i->i).toArray();

        assertArrayEquals(new int[]{}, actual);

        gameState.playerMove(34);
        gameState.playerMove(13);

        gameState.playerMove(45);

        gameState.checkWin();

        actual = gameState.getWinLineIndex().stream().mapToInt(i->i).toArray();

        assertArrayEquals(new int[]{1, 12, 23, 34, 45}, actual);
    }

    @Test
    public void checkMoveAgain() {
        assertTrue(gameState.playerMove(32));
        assertFalse(gameState.playerMove(32));
    }

    @Test
    public void checkMoveAfterEndGame() {
        gameState.playerMove(1);
        gameState.playerMove(10);
        gameState.playerMove(12);
        gameState.playerMove(11);
        gameState.playerMove(23);
        gameState.playerMove(15);
        gameState.playerMove(34);
        gameState.playerMove(13);
        gameState.playerMove(45);

        gameState.checkWin();

        assertFalse(gameState.playerMove(32));
    }
}
