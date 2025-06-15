package com.example.gidanbeachresorts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private FirebaseFirestore db; // <-- UN-COMMENT or ADD BACK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance(); // <-- UN-COMMENT or ADD BACK
        recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(this, roomList);
        recyclerView.setAdapter(roomAdapter);
        roomAdapter.setOnRoomClickListener(this);

        // --- CALL fetchRooms() AGAIN ---
        fetchRooms();
    }

    @Override
    public void onRoomClick(Room room) {
        Intent intent = new Intent(HomeActivity.this, RoomDetailActivity.class);
        intent.putExtra("selected_room", room);
        startActivity(intent);
    }

    // --- ADD THIS METHOD BACK ---
    private void fetchRooms() {
        db.collection("Rooms")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        roomList.clear(); // Clear list before adding new data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Room room = document.toObject(Room.class);
                            roomList.add(room);
                        }
                        roomAdapter.notifyDataSetChanged(); // Refresh the list
                    } else {
                        Log.w("HomeActivity", "Error getting documents.", task.getException());
                        Toast.makeText(HomeActivity.this, "Error fetching rooms.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
    // We no longer need the local data method
    private void createLocalRoomData() {
        // ...
    }
    */
}
