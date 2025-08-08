package com.group20.cscb07project.emergencyinfo.Locations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.cscb07project.R;

import java.util.ArrayList;
import java.util.List;

public class LocationsListFragment extends Fragment implements LocationAdapter.OnLocationClickListener {

    private LocationAdapter locationAdapter;
    private LocationManager locationManager;
    private List<LocationItem> locationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations_list, container, false);

        RecyclerView locationsRecyclerView = view.findViewById(R.id.locations_recycler_view);
        FloatingActionButton addLocationFab = view.findViewById(R.id.add_location);

        locationManager = LocationManager.getInstance();
        locationsList = new ArrayList<>();

        locationAdapter = new LocationAdapter(locationsList, this);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationsRecyclerView.setAdapter(locationAdapter);

        loadLocations();

        addLocationFab.setOnClickListener(v -> {
            AddEditLocationFragment fragment = new AddEditLocationFragment();
            loadFragment(fragment);
        });

        return view;
    }

    private void loadLocations() {
        locationManager.getLocations(new LocationManager.LocationListCallback() {
            @Override
            public void onSuccess(List<LocationItem> locations) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            locationsList.clear();
                            locationsList.addAll(locations);
                            locationAdapter.updateLocations(locationsList);
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            Toast.makeText(getContext(), "Error loading locations", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        try {
            if (isAdded()) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } catch (Exception e) {
            // silent
        }
    }

    @Override
    public void onEditLocation(LocationItem location) {
        try {
            AddEditLocationFragment fragment = new AddEditLocationFragment();
            Bundle args = new Bundle();
            args.putString("location_id", location.getId());
            args.putString("location_address", location.getAddress());
            args.putString("location_note", location.getNote());
            fragment.setArguments(args);
            loadFragment(fragment);
        } catch (Exception e) {
            // silent
        }
    }

    @Override
    public void onDeleteLocation(LocationItem location) {
        locationManager.deleteLocation(location.getId(), new LocationManager.LocationCallback() {
            @Override
            public void onSuccess() {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            Toast.makeText(getContext(), "Location deleted", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            Toast.makeText(getContext(), "Error deleting location", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }
        });
    }
}