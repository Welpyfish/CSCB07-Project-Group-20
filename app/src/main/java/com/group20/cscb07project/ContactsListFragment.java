package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactsListFragment extends Fragment {

    private RecyclerView contactsRecyclerView;
    private FloatingActionButton addContactFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        addContactFab = view.findViewById(R.id.add_contact);

        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: Set adapter when ContactAdapter is created

        addContactFab.setOnClickListener(v -> {
            // TODO: Navigate to add/edit contact fragment
        });

        return view;
    }
} 