package com.example.mobiletictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.mobiletictactoe.interfaces.IGameAi;

public class GameActivity extends AppCompatActivity {

    private GameStateParcelable gameState;
    private CellAdapter adapter;
    private IGameAi ai;
    private GameStatistics gameStatistics;

    private TextView textViewScorePlayerO;
    private TextView textViewScorePlayerX;
    private Button btnNextGame;
    private Button btnSaveAndExit;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hideTopBars();

        boolean isAi = getIntent().getBooleanExtra("isAi", false);

        GridView board = findViewById(R.id.boardGame);
        textViewScorePlayerO = findViewById(R.id.textViewScorePlayerO);
        textViewScorePlayerX = findViewById(R.id.textViewScorePlayerX);
        btnSaveAndExit = findViewById(R.id.buttonSaveAndExit);
        btnNextGame = findViewById(R.id.buttonNextGame);

        setGameObjects(savedInstanceState);
        btnNextGameDisabled();

        if (isAi) {
            ai = new GameAi(gameState);
        }
        adapter = new CellAdapter(this, gameState, getCellDimension(board));
        board.setAdapter(adapter);

        board.setOnItemClickListener((adapterView, view, position, l) -> {
            playerMove(position);

            if (!gameState.isEnd()) {
                updateDisplayCurrentTurn();
            }

            if (isAi) {
                playerMove(ai.getNextMove());
            }
        });

        btnNextGame.setOnClickListener((l) -> {
            gameState.reset(CellValue.O);
            btnNextGameDisabled();
            adapter.notifyDataSetChanged();
            updateDisplayCurrentTurn();
        });

        btnSaveAndExit.setOnClickListener((l) -> {
            if (gameStatistics.getScorePlayerO() > 0 || gameStatistics.getScorePlayerX() > 0) {
                ScoresRepository sr = new ScoresRepository(this);
                sr.insertNewScore(gameStatistics.getScorePlayerO(),
                        gameStatistics.getScorePlayerX(), isAi);
            }

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });
    }

    private void afterWin() {
        gameStatistics.incrementScore(gameState.getWinner());
        updateGameStatisticsTextView();
        btnNextGameEnabled();
    }

    private void playerMove(int position) {
        if (!gameState.playerMove(position)) {
            return;
        }
        if (gameState.checkWin()) {
            afterWin();
        }
        adapter.notifyDataSetChanged();
    }

    private void hideTopBars() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void setGameObjects(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            gameStatistics = new GameStatistics();
            gameState = new GameStateParcelable();
        } else {
            gameStatistics = new GameStatistics(
                savedInstanceState.getInt("ScoreO"), savedInstanceState.getInt("ScoreX")
            );
            gameState = savedInstanceState.getParcelable("gameState");
            updateGameStatisticsTextView();
            updateDisplayCurrentTurn();
        }
    }

    private int getCellDimension(GridView board) {
        board.setNumColumns(10);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screen_width = dm.widthPixels;
        int screen_height = dm.heightPixels;
        int orientation = getResources().getConfiguration().orientation;

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

        return cellDimension;
    }

    private void btnNextGameEnabled() {
//        btnNextGame.setBackgroundColor(Color.rgb(0,191,255));
        btnNextGame.setBackgroundColor(fetchPrimaryColor());
        btnNextGame.setText("Next Game");
        btnNextGame.setEnabled(true);
    }

    private void btnNextGameDisabled() {
        btnNextGame.setBackgroundColor(Color.rgb(255, 255, 255));
        btnNextGame.setEnabled(false);
    }

    private void updateGameStatisticsTextView() {
        textViewScorePlayerO.setText(Integer.toString(gameStatistics.getScorePlayerO()));
        textViewScorePlayerX.setText(Integer.toString(gameStatistics.getScorePlayerX()));
        if (gameStatistics.getScorePlayerO() > 0 || gameStatistics.getScorePlayerX() > 0) {
            btnSaveAndExit.setText("Save and exit");
        }
    }

    private void updateDisplayCurrentTurn() {
        btnNextGame.setText(String.format(
                "Current Player: %s", gameState.currentTurn == CellValue.O ? "O" : "X"));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("gameState", gameState);
        outState.putInt("ScoreO", gameStatistics.getScorePlayerO());
        outState.putInt("ScoreX", gameStatistics.getScorePlayerX());
    }

    private int fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = this.obtainStyledAttributes(
                typedValue.data, new int[] { androidx.appcompat.R.attr.colorPrimary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}