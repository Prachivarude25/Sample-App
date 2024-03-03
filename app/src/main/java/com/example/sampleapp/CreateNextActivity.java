package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateNextActivity extends AppCompatActivity {
    EditText tittleEditText,contentEditText;
    ImageButton saveNotebtn;
    FirebaseFirestore db;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_next);

        tittleEditText=findViewById(R.id.notes_tittle_text);
        contentEditText=findViewById(R.id.notes_content_text);
        saveNotebtn=findViewById(R.id.save_note_btn);

        saveNotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });


    }

    private void saveNote() {
        String noteTittle=tittleEditText.getText().toString();
        String noteContent=contentEditText.getText().toString();

        if(noteTittle==null || noteTittle.isEmpty())
        {
            tittleEditText.setError("Tittle is required");
            return ;
        }
        Note note=new Note();
        note.setTittle(noteTittle);
        note.setContent(noteContent);
        saveNoteToFirebase(note);
    }


   void saveNoteToFirebase(Note note) {
       FirebaseAuth auth = FirebaseAuth.getInstance();
       FirebaseUser currentUser = auth.getCurrentUser();

       if (currentUser != null) {
           String userId = currentUser.getEmail();//getUid();

           FirebaseFirestore db = FirebaseFirestore.getInstance();
           CollectionReference userNotesCollection = db.collection(userId);//here notes are saved for the current logged in user
           userNotesCollection.add(note)
                   .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentReference> task) {
                           if (task.isSuccessful()) {
                               // Note is added successfully
                               Utility.showToast(CreateNextActivity.this, "Note added successfully");
                               finish();
                           } else {
                               // Failed while adding note
                               Utility.showToast(CreateNextActivity.this, "Failed while adding note: " + task.getException().getMessage());
                           }
                       }
                   });
       } else {
           // User not authenticated, handle accordingly
           Utility.showToast(CreateNextActivity.this, "User not authenticated");
       }
   }




}