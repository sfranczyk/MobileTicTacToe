package com.example.mobiletictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobiletictactoe.interfaces.IGameAi;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private GameState gameState = new GameState();
    private CellAdapter adapter;
    private IGameAi ai;
    private GameStatistics gameStatistics = new GameStatistics();

    private TextView textViewScorePlayerO;
    private TextView textViewScorePlayerX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        GridView board = findViewById(R.id.boardGame);
        textViewScorePlayerO = findViewById(R.id.textViewScorePlayerO);
        textViewScorePlayerX = findViewById(R.id.textViewScorePlayerX);

        ai = new GameAi(gameState);

        board.setNumColumns(10);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screen_width = dm.widthPixels;
        int screen_height = dm.heightPixels;
        int orientation = getResources().getConfiguration().orientation;

        Log.d("scr", String.valueOf(screen_height));
        Log.d("scr", String.valueOf(screen_width));

        int marginsWidth = 10;
        int boardDimensions = (orientation == Configuration.ORIENTATION_LANDSCAPE ?
                screen_height : screen_width) - marginsWidth * 2;

        ViewGroup.LayoutParams params = board.getLayoutParams();
        params.height = params.width = boardDimensions;
        board.setLayoutParams(params);

        int cellDimension = boardDimensions * 10 / 109;
        int gridWidth = boardDimensions * 2 / 105;

        board.setVerticalSpacing(gridWidth);
        board.setHorizontalSpacing(gridWidth + 2);

        adapter = new CellAdapter(this, gameState, cellDimension);

        board.setAdapter(adapter);

        board.setOnItemClickListener((adapterView, view, position, l) -> {
            gameState.playerMove(position);
            if (gameState.checkWin()) {
                gameStatistics.incrementScore(gameState.getWinner());
                textViewScorePlayerO.setText(Integer.toString(gameStatistics.getScorePlayerO()));
                textViewScorePlayerX.setText(Integer.toString(gameStatistics.getScorePlayerX()));
            }
            adapter.notifyDataSetChanged();

            gameState.playerMove(ai.getNextMove());
            gameState.checkWin();
            adapter.notifyDataSetChanged();
        });
    }
}