package com.example.mobiletictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSinglePlayer = findViewById(R.id.btnSinglePlayer);
        Button buttonTwoPlayers = findViewById(R.id.btnTwoPlayers);

        buttonSinglePlayer.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(i);
        });
    }
}