package com.example.mobiletictactoe;

import android.util.Pair;

import com.example.mobiletictactoe.interfaces.IGameAi;

import java.util.ArrayList;
import java.util.Random;

public class GameAi implements IGameAi {
    GameState gameState;

    public GameAi(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public int getNextMove() {
        ArrayList<Integer> freeCells = gameState.getFreeCellIndexes();
        Random random = new Random();
        return freeCells.get(random.nextInt(freeCells.size()));
    }
}
