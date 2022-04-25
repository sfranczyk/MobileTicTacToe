package com.example.mobiletictactoe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StatisticsActivity extends AppCompatActivity {

    ListView listView;
    Button btnBack;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        listView = (ListView) findViewById(R.id.listView);
        btnBack = (Button) findViewById(R.id.btnBack);

        ScoresRepository sr = new ScoresRepository(this);
        ArrayList<String> scores = sr.getScoresFromDb()
                .stream().map(Score::toString).collect(Collectors.toCollection(ArrayList::new));

        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1, scores);

        listView.setAdapter(arrayAdapter);

        btnBack.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });
    }
}