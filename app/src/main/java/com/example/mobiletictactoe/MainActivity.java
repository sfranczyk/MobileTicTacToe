package com.example.mobiletictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

//    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSinglePlayer = findViewById(R.id.btnSinglePlayer);
        Button buttonTwoPlayers = findViewById(R.id.btnTwoPlayersHotseat);
        Button buttonTwoPlayersOnline = findViewById(R.id.btnTwoPlayersOnline);
        Button buttonStatistics = findViewById(R.id.btnStatistics);
        Button buttonExit = findViewById(R.id.btnExit);

        buttonTwoPlayers.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            i.putExtra("mode", GameMode.PvPOffline);
            startActivity(i);
        });

        buttonTwoPlayersOnline.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), ChooseRoomActivity.class);
            startActivity(i);
        });

        buttonSinglePlayer.setOnClickListener((l) -> {
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            i.putExtra("mode", GameMode.SinglePlayer);
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


//        databaseReference = FirebaseDatabase.getInstance().getReference("path");
//
//        databaseReference.setValue("here there").addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getApplicationContext(), "x", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

