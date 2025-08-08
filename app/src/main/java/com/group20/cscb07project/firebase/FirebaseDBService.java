package com.group20.cscb07project.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDBService {
    private static FirebaseDBService instance;
    private final FirebaseDatabase db;
    private String path;

    public static FirebaseDBService getInstance() {
        if (instance == null) {
            instance = new FirebaseDBService();
        }
        return instance;
    }

    private FirebaseDBService(){
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
                callback.onFailure(task.getException());
            }
        });

    }
}
