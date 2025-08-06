package com.group20.cscb07project.EmergencyInfo;

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
import com.group20.cscb07project.R;

public class LocationsListFragment extends Fragment {

    private RecyclerView locationsRecyclerView;
    private FloatingActionButton addLocationFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations_list, container, false);

        locationsRecyclerView = view.findViewById(R.id.locations_recycler_view);
        addLocationFab = view.findViewById(R.id.add_location);

        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addLocationFab.setOnClickListener(v -> {
        });

        return view;
    }
} 