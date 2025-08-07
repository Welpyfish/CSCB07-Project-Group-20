package com.group20.cscb07project.EmergencyInfo.Contacts;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ContactManager {
    private static ContactManager instance;
    private DatabaseReference databaseRef;
    private FirebaseAuth auth;
    private String userId;

    public interface ContactCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface ContactListCallback {
        void onSuccess(List<ContactItem> contacts);
        void onFailure(String error);
    }

    private ContactManager() {
        databaseRef = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com").getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }
    }

    public static ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
        }
        return instance;
    }

    public void addContact(ContactItem contact, ContactCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);

        Map<String, Object> contactData = new HashMap<>();
        contactData.put("name", contact.getName());
        contactData.put("relationship", contact.getRelationship());
        contactData.put("phone", contact.getPhone());

        databaseRef.child("users").child(userId).child("emergency_contacts").child(contactId)
                .setValue(contactData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateContact(ContactItem contact, ContactCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        Map<String, Object> contactData = new HashMap<>();
        contactData.put("name", contact.getName());
        contactData.put("relationship", contact.getRelationship());
        contactData.put("phone", contact.getPhone());

        databaseRef.child("users").child(userId).child("emergency_contacts").child(contact.getId())
                .setValue(contactData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteContact(String contactId, ContactCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        databaseRef.child("users").child(userId).child("emergency_contacts").child(contactId)
                .removeValue()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getContacts(ContactListCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        databaseRef.child("users").child(userId).child("emergency_contacts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<ContactItem> contacts = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ContactItem contact = snapshot.getValue(ContactItem.class);
                            if (contact != null) {
                                contact.setId(snapshot.getKey());
                                contacts.add(contact);
                            }
                        }
                        callback.onSuccess(contacts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailure(databaseError.getMessage());
                    }
                });
    }

    public void getContactById(String contactId, ContactListCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        databaseRef.child("users").child(userId).child("emergency_contacts").child(contactId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<ContactItem> contacts = new ArrayList<>();
                        ContactItem contact = dataSnapshot.getValue(ContactItem.class);
                        if (contact != null) {
                            contact.setId(dataSnapshot.getKey());
                            contacts.add(contact);
                        }
                        callback.onSuccess(contacts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailure(databaseError.getMessage());
                    }
                });
    }
} 