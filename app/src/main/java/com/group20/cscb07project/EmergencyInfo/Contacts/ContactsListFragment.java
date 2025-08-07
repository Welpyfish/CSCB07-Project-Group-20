package com.group20.cscb07project.EmergencyInfo.Contacts;

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

public class ContactsListFragment extends Fragment implements ContactAdapter.OnContactClickListener {

    private RecyclerView contactsRecyclerView;
    private FloatingActionButton addContactFab;
    private ContactAdapter contactAdapter;
    private ContactManager contactManager;
    private List<ContactItem> contactsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        addContactFab = view.findViewById(R.id.add_contact);

        contactManager = ContactManager.getInstance();
        contactsList = new ArrayList<>();

        contactAdapter = new ContactAdapter(contactsList, this);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsRecyclerView.setAdapter(contactAdapter);

        loadContacts();

        addContactFab.setOnClickListener(v -> {
            AddEditContactFragment fragment = new AddEditContactFragment();
            loadFragment(fragment);
        });

        return view;
    }

    private void loadContacts() {
        contactManager.getContacts(new ContactManager.ContactListCallback() {
            @Override
            public void onSuccess(List<ContactItem> contacts) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            contactsList.clear();
                            contactsList.addAll(contacts);
                            contactAdapter.updateContacts(contactsList);
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
                            Toast.makeText(getContext(), "Error loading contacts", Toast.LENGTH_SHORT).show();
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
            if (isAdded() && getParentFragmentManager() != null) {
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
    public void onEditContact(ContactItem contact) {
        try {
            AddEditContactFragment fragment = new AddEditContactFragment();
            Bundle args = new Bundle();
            args.putString("contact_id", contact.getId());
            args.putString("contact_name", contact.getName());
            args.putString("contact_relationship", contact.getRelationship());
            args.putString("contact_phone", contact.getPhone());
            fragment.setArguments(args);
            loadFragment(fragment);
        } catch (Exception e) {
            // silent
        }
    }

    @Override
    public void onDeleteContact(ContactItem contact) {
        contactManager.deleteContact(contact.getId(), new ContactManager.ContactCallback() {
            @Override
            public void onSuccess() {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            Toast.makeText(getContext(), "Contact deleted", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(), "Error deleting contact", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }
        });
    }
} 