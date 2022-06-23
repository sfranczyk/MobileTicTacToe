package com.example.mobiletictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletictactoe.scoreDb.ScoresRepository;
import com.example.mobiletictactoe.interfaces.IGameAi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {

    private GameStateParcelable gameState;
    private CellAdapter adapter;
    private IGameAi ai;
    private GameStatistics gameStatistics;

    private TextView textViewScorePlayerO;
    private TextView textViewScorePlayerX;
    private Button btnNextGame;
    private Button btnBackToMenu;

    String roomName = "";
    String role = "";
    String message = "";
    String move;

    FirebaseDatabase database;
    DatabaseReference messageRef;
    GameMode mode;
    boolean escaped = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hideTopBars();

        mode = GameMode.values()[getIntent().getIntExtra("mode", 0)];

        TextView tvNameRoom = findViewById(R.id.gameTitle);
        GridView board = findViewById(R.id.boardGame);
        textViewScorePlayerO = findViewById(R.id.textViewScorePlayerO);
        textViewScorePlayerX = findViewById(R.id.textViewScorePlayerX);
        btnBackToMenu = findViewById(R.id.buttonSaveAndExit);
        btnNextGame = findViewById(R.id.buttonNextGame);

        setGameObjects(savedInstanceState);
        btnNextGameDisabled();

        if (mode == GameMode.SinglePlayer) {
            ai = new GameAi(gameState);
        } else if (mode == GameMode.PvPOnline) {
            database =FirebaseDatabase.getInstance();
            roomName = getIntent().getStringExtra("roomName");

            if (getIntent().getBooleanExtra("opponent", false)) {
                role = "quest";
            } else {
                role = "host";
            }
            move = "host";

            tvNameRoom.setText(role + ": " + roomName);

            messageRef = database.getReference("rooms/" + roomName + "/message");
            addMessageEventListener();
        }

        adapter = new CellAdapter(this, gameState, getCellDimension(board));
        board.setAdapter(adapter);

        board.setOnItemClickListener((adapterView, view, position, l) -> {
            switch (mode) {
                case SinglePlayer:
                    playerMove(position);
                    if (!gameState.isEnd()) {
                        updateDisplayCurrentTurn();
                    }
                    playerMove(ai.getNextMove());
                    break;
                case PvPOffline:
                    playerMove(position);
                    if (!gameState.isEnd()) {
                        updateDisplayCurrentTurn();
                    }
                    break;
                case PvPOnline:
                    if (role.equals("host")) {
                        if (move.equals("guest"))
                            return;
                        playerMove(position);
                        if (!gameState.isEnd()) {
                            updateDisplayCurrentTurn();
                            message = "host:move:" + position;
                        } else {
                            message = "host:move:" + position + ":win";
                        }
                        messageRef.setValue(message);
                        move = "guest";

                    } else {
                        if (move.equals("host"))
                            return;
                        playerMove(position);
                        if (!gameState.isEnd()) {
                            updateDisplayCurrentTurn();
                            message = "guest:move:" + position;
                        } else {
                            message = "guest:move:" + position + ":win";
                        }
                        messageRef.setValue(message);
                        move = "host";
                    }
                    break;
            }
        });

        btnNextGame.setOnClickListener((l) -> {
            gameState.reset(CellValue.O);
            btnNextGameDisabled();
            adapter.notifyDataSetChanged();
            updateDisplayCurrentTurn();

            if (mode == GameMode.PvPOnline) {
                message = "host:nextgame";
                messageRef.setValue(message);
                move = "host";
            }
        });

        btnBackToMenu.setOnClickListener((l) -> {
            if (gameStatistics.getScorePlayerO() > 0 || gameStatistics.getScorePlayerX() > 0) {
                ScoresRepository sr = new ScoresRepository(this);
                sr.insertNewScore(gameStatistics.getScorePlayerO(),
                        gameStatistics.getScorePlayerX(), mode);

                if (mode == GameMode.PvPOnline) {
                    if (!escaped) {
                        message = role + ":isout";
                        messageRef.setValue(message);
                    } else {
                        database.getReference("rooms/" + roomName).removeValue();
                    }
                }
            }

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });
    }

    private void addMessageEventListener() {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //message received
                String newMessage = dataSnapshot.getValue(String.class);
                Log.d("gg", "onDataChange: " + newMessage);
                if (newMessage == null)
                    return;
                if (role.equals("host")) {
                    if (newMessage.contains("guest:")) {
                        if (newMessage.contains("move")) {
                            String[] content = newMessage.split(":");
                            int position = Integer.parseInt(content[2]);
                            playerMove(position);
                            if (!gameState.isEnd()) {
                                updateDisplayCurrentTurn();
                            }
                            move = role;
                        } else if (newMessage.contains("isout")) {
                            btnNextGameDisabled();
                            btnNextGame.setText("host escaped");
                            escaped = true;
                        }
                    }
                } else {
                    if (newMessage.contains("host:")) {
                        if (newMessage.contains("move")) {
                            String[] content = newMessage.split(":");
                            int position = Integer.parseInt(content[2]);
                            playerMove(position);
                            if (!gameState.isEnd()) {
                                updateDisplayCurrentTurn();
                            }
                            move = role;
                        } else if (newMessage.contains("nextgame")) {
                            gameState.reset(CellValue.O);
                            btnNextGameDisabled();
                            adapter.notifyDataSetChanged();
                            updateDisplayCurrentTurn();
                            move = "host";
                        } else if (newMessage.contains("isout")) {
                            btnNextGameDisabled();
                            btnNextGame.setText("guest escaped");
                            escaped = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error
                messageRef.setValue(message);
            }
        });
    }

    private void afterWin() {
        gameStatistics.incrementScore(gameState.getWinner());
        updateGameStatisticsTextView();
        if (mode != GameMode.PvPOnline) {
            btnNextGameEnabled();
        } else {
            if (role.equals("host")) {
                btnNextGameEnabled();
            } else {
                btnNextGame.setText("Waiting for host");
            }
        }
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

        boardDimensions = boardDimensions - boardDimensions % 259;

        int gridWidth = boardDimensions / 259;
        int cellDimension = gridWidth * 25;

        ViewGroup.LayoutParams params = board.getLayoutParams();
        params.height = params.width = boardDimensions - 3;
        board.setLayoutParams(params);

        board.setHorizontalSpacing(gridWidth * 5);
        board.setVerticalSpacing(gridWidth * 3 + 3);

        return cellDimension;
    }

    @SuppressLint("SetTextI18n")
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
            btnBackToMenu.setText("Back to menu");
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