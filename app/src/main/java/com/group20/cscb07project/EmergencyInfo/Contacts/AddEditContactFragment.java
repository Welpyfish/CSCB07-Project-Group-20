package com.group20.cscb07project.EmergencyInfo.Contacts;

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

public class AddEditContactFragment extends Fragment {

    private TextInputEditText contactNameEditText;
    private TextInputEditText relationshipEditText;
    private TextInputEditText phoneEditText;
    private MaterialButton saveContactButton;
    private MaterialButton cancelContactButton;
    private ContactManager contactManager;
    private boolean isEditMode = false;
    private String contactId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_contact, container, false);

        contactNameEditText = view.findViewById(R.id.contact_name_edit_text);
        relationshipEditText = view.findViewById(R.id.relationship_edit_text);
        phoneEditText = view.findViewById(R.id.phone_edit_text);
        saveContactButton = view.findViewById(R.id.save_contact_button);
        cancelContactButton = view.findViewById(R.id.cancel_contact_button);

        contactManager = ContactManager.getInstance();

        // Check if we're in edit mode
        Bundle args = getArguments();
        if (args != null) {
            contactId = args.getString("contact_id");
            if (contactId != null) {
                isEditMode = true;
                contactNameEditText.setText(args.getString("contact_name", ""));
                relationshipEditText.setText(args.getString("contact_relationship", ""));
                phoneEditText.setText(args.getString("contact_phone", ""));
            }
        }

        saveContactButton.setOnClickListener(v -> saveContact());
        cancelContactButton.setOnClickListener(v -> goBack());

        return view;
    }

    private void saveContact() {
        String name = contactNameEditText.getText().toString().trim();
        String relationship = relationshipEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            contactNameEditText.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(relationship)) {
            relationshipEditText.setError("Relationship is required");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone number is required");
            return;
        }

        ContactItem contact = new ContactItem();
        contact.setName(name);
        contact.setRelationship(relationship);
        contact.setPhone(phone);

        if (isEditMode) {
            contact.setId(contactId);
            contactManager.updateContact(contact, new ContactManager.ContactCallback() {
                @Override
                public void onSuccess() {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Contact updated successfully", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Error updating contact", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }
            });
        } else {
            contactManager.addContact(contact, new ContactManager.ContactCallback() {
                @Override
                public void onSuccess() {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Contact added successfully", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Error adding contact", Toast.LENGTH_SHORT).show();
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