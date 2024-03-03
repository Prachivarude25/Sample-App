package com.example.sampleapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CreateNoteActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;
    Button logoutbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recyclerView);
        menuBtn = findViewById(R.id.menu_btn);
        logoutbtn=findViewById(R.id.button3);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreateNoteActivity.this, LoginActivity.class));
                finish(); // Close the current activity
            }
        });

        addNoteBtn.setOnClickListener((v) -> startActivity(new Intent(CreateNoteActivity.this, CreateNextActivity.class)));
        menuBtn.setOnClickListener((v) -> showMenu());
        fetchNotesFromFirestore();
    }

    void showMenu() {
        // Display Menu
    }

    void fetchNotesFromFirestore() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference userNotesCollection = db.collection(userEmail);
            userNotesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Note> notes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Note note = document.toObject(Note.class);
                            notes.add(note);
                        }

                        // Update the RecyclerView with the fetched notes
                        updateRecyclerView(notes);
                    } else {
                        // Handle the error
                        Toast.makeText(CreateNoteActivity.this, "Failed to fetch notes: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // User not authenticated, handle accordingly
            Toast.makeText(CreateNoteActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }

    }

    void updateRecyclerView(List<Note> notes) {
        // Update the RecyclerView with the fetched notes
        recyclerView.setLayoutManager(new LinearLayoutManager(CreateNoteActivity.this));
        noteAdapter = new NoteAdapter(notes);
        recyclerView.setAdapter(noteAdapter);
    }
}