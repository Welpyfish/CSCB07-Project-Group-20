package com.group20.cscb07project.EmergencyInfo.Locations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.cscb07project.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<LocationItem> locations;
    private OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onEditLocation(LocationItem location);
        void onDeleteLocation(LocationItem location);
    }

    public LocationAdapter(List<LocationItem> locations, OnLocationClickListener listener) {
        this.locations = locations != null ? locations : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_card, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationItem location = locations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void updateLocations(List<LocationItem> newLocations) {
        this.locations = newLocations != null ? newLocations : new ArrayList<>();
        notifyDataSetChanged();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView locationAddress;
        private TextView locationNote;
        private ImageButton editButton;
        private ImageButton deleteButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationAddress = itemView.findViewById(R.id.location_address);
            locationNote = itemView.findViewById(R.id.location_note);
            editButton = itemView.findViewById(R.id.edit_location_button);
            deleteButton = itemView.findViewById(R.id.delete_location_button);
        }

        public void bind(LocationItem location) {
            locationAddress.setText(location.getAddress());
            locationNote.setText(location.getNote());

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditLocation(location);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteLocation(location);
                }
            });
        }
    }
}
