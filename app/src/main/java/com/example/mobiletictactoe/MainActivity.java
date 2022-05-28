package com.example.mobiletictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSinglePlayer = findViewById(R.id.btnSinglePlayer);
        Button buttonTwoPlayers = findViewById(R.id.btnTwoPlayersHotseat);
        Button buttonStatistics = findViewById(R.id.btnStatistics);
        Button buttonExit = findViewById(R.id.btnExit);

        buttonTwoPlayers.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(i);
        });

        buttonSinglePlayer.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            i.putExtra("isAi", true);
            startActivity(i);
        });

        buttonStatistics.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
            startActivity(i);
        });

        buttonExit.setOnClickListener((l) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        });
    }
}