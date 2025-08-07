package com.group20.cscb07project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDB {
    private static FirebaseDB instance;
    private FirebaseDatabase db;
    private String path;

    public static FirebaseDB getInstance() {
        if (instance == null) {
            instance = new FirebaseDB();
        }
        return instance;
    }

    private FirebaseDB(){
        db = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com/");
        setPath("/");
    }

    //on login, set path to "/users/{uid}/"
    public void setPath(String path) {
        this.path = path;
    }


    public void setValue(String relativePath, String value, FirebaseResultCallback callback){
        db.getReference(path+relativePath).setValue(value).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                callback.onSuccess();
            }else{
                callback.onFailure();
            }
        });

    }

    public String getValue(String relativePath, FirebaseResultCallback callback) {
        return db.getReference(path+relativePath).get().toString();
    }
    public void addListener(String relativePath, ValueEventListener listener){
        db.getReference(path+relativePath).addValueEventListener(listener);
    }

//    public void queryDB(String username){
//        DatabaseReference query = dbRef.child("users").child(username);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //???
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//    }


}
