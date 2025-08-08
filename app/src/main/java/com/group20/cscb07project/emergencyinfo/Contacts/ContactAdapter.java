package com.group20.cscb07project.emergencyinfo.Contacts;

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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactItem> contacts;
    private final OnContactClickListener listener;

    public interface OnContactClickListener {
        void onEditContact(ContactItem contact);
        void onDeleteContact(ContactItem contact);
    }

    public ContactAdapter(List<ContactItem> contacts, OnContactClickListener listener) {
        this.contacts = contacts != null ? contacts : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_card, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactItem contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updateContacts(List<ContactItem> newContacts) {
        this.contacts = newContacts != null ? newContacts : new ArrayList<>();
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactName;
        private final TextView contactRelationship;
        private final TextView contactPhone;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactRelationship = itemView.findViewById(R.id.contact_relationship);
            contactPhone = itemView.findViewById(R.id.contact_phone);
            editButton = itemView.findViewById(R.id.edit_contact_button);
            deleteButton = itemView.findViewById(R.id.delete_contact_button);
        }

        public void bind(ContactItem contact) {
            contactName.setText(contact.getName());
            contactRelationship.setText(contact.getRelationship());
            contactPhone.setText(contact.getPhone());

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditContact(contact);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteContact(contact);
                }
            });
        }
    }
}
