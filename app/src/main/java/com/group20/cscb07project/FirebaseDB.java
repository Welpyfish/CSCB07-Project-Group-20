package com.group20.cscb07project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDB {
        private DatabaseReference dbRef;

    public FirebaseDB(){
        dbRef = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com/").getReference();
    }

    public void queryDB(String username){
        DatabaseReference query = dbRef.child("users").child(username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //???
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
