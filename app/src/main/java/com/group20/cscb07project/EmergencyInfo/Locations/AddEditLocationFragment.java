package com.group20.cscb07project.EmergencyInfo.Locations;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.group20.cscb07project.R;

public class AddEditLocationFragment extends Fragment {

    private TextInputEditText locationAddressEditText;
    private TextInputEditText locationNoteEditText;
    private MaterialButton saveLocationButton;
    private MaterialButton cancelLocationButton;
    private LocationManager locationManager;
    private boolean isEditMode = false;
    private String locationId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_location, container, false);

        locationAddressEditText = view.findViewById(R.id.address_edit_text);
        locationNoteEditText = view.findViewById(R.id.location_note_edit_text);
        saveLocationButton = view.findViewById(R.id.save_location_button);
        cancelLocationButton = view.findViewById(R.id.cancel_location_button);

        locationManager = LocationManager.getInstance();

        // Check if we're in edit mode
        Bundle args = getArguments();
        if (args != null) {
            locationId = args.getString("location_id");
            if (locationId != null) {
                isEditMode = true;
                locationAddressEditText.setText(args.getString("location_address", ""));
                locationNoteEditText.setText(args.getString("location_note", ""));
            }
        }

        saveLocationButton.setOnClickListener(v -> saveLocation());
        cancelLocationButton.setOnClickListener(v -> goBack());

        return view;
    }

    private void saveLocation() {
        String address = locationAddressEditText.getText().toString().trim();
        String note = locationNoteEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(address)) {
            locationAddressEditText.setError("Address is required");
            return;
        }
        if (TextUtils.isEmpty(note)) {
            locationNoteEditText.setError("Note is required");
            return;
        }

        LocationItem location = new LocationItem();
        location.setAddress(address);
        location.setNote(note);

        if (isEditMode) {
            location.setId(locationId);
            locationManager.updateLocation(location, new LocationManager.LocationCallback() {
                @Override
                public void onSuccess() {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Location updated successfully", Toast.LENGTH_SHORT).show();
                                goBack();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String error) {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Error updating Location", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }
            });
        } else {
            locationManager.addLocation(location, new LocationManager.LocationCallback() {
                @Override
                public void onSuccess() {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Location added successfully", Toast.LENGTH_SHORT).show();
                                goBack();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String error) {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Error adding Location", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }
            });
        }
    }

    private void goBack() {
        try {
            if (isAdded() && getParentFragmentManager() != null && getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        } catch (Exception e) {
            // If navigation fails, just ignore
        }
    }
} 