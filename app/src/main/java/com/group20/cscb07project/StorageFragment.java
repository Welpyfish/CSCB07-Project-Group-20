package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StorageFragment extends Fragment {
    private EditText tester;
    private DatabaseReference dbRef;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_storage, container, false);

        dbRef = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com/").getReference();
        userRef=dbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        tester = view.findViewById(R.id.name);
        tester.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                userRef.child("name").setValue(tester.getText().toString().trim());
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tester.setText(snapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
