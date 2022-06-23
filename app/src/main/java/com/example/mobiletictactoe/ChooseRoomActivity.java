package com.example.mobiletictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseRoomActivity extends AppCompatActivity {

    ListView listView;
    Button button;
    TextView textViewRoomName;

    List<String> roomsList;

    String roomName = "";
    boolean opponent = false;

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);

        database = FirebaseDatabase.getInstance();

        listView = findViewById(R.id.listViewRooms);
        button = findViewById(R.id.btnCreateRoom);
        textViewRoomName = findViewById(R.id.tvNameOfYourRoom);

        roomsList = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Creating Room");
                button.setEnabled(false);
                roomName = generateName(5);
                roomRef = database.getReference("rooms/" + roomName + "/player1");
                textViewRoomName.setText(roomName);
                addRoomEventListener();
                roomRef.setValue(roomName);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomName = roomsList.get(position);
                roomRef = database.getReference("rooms/" + roomName + "/player2");
                addRoomEventListener();
                roomRef.setValue("Opponent");
                opponent = true;
            }
        });

        addRoomsEventListener();
    }

    private String generateName(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void addRoomEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                button.setText("Create Room");
                button.setEnabled(true);

                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                i.putExtra("mode", 2);
                i.putExtra("roomName", roomName);
                i.putExtra("opponent", opponent);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                button.setText("Create Room");
                button.setEnabled(true);
                Toast.makeText(ChooseRoomActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventListener() {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : rooms) {
                    if(snapshot.getChildrenCount() < 2) {
                        roomsList.add(snapshot.getKey());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        ChooseRoomActivity.this, android.R.layout.simple_list_item_1,
                        roomsList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}