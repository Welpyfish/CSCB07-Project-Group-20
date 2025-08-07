package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SafetyPlanFragment extends Fragment {

    private TextView titleText;
    private TextView subtitleText;
    private RecyclerView tipsRecyclerView;
    private TipAdapter tipAdapter;
    private List<TipAdapter.Tip> tipsList;
    private JSONObject questionnaireData;
    private Map<String, String> userResponses;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_safety_plan, container, false);

        titleText = view.findViewById(R.id.safety_plan_title);
        subtitleText = view.findViewById(R.id.safety_plan_subtitle);
        tipsRecyclerView = view.findViewById(R.id.tips_recycler_view);

        titleText.setText(R.string.safety_plan_title);
        subtitleText.setText(R.string.safety_plan_subtitle);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com").getReference();

        if (getContext() != null && tipsRecyclerView != null) {
            tipsList = new ArrayList<>();
            tipAdapter = new TipAdapter(tipsList);
            tipsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            tipsRecyclerView.setAdapter(tipAdapter);
            
            loadQuestionnaireData();
            loadUserResponses();
        }

        return view;
    }

    private void loadQuestionnaireData() {
        try {
            InputStream inputStream = getContext().getAssets().open("questionnaire_questions.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            
            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            questionnaireData = new JSONObject(jsonString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadUserResponses() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabaseRef.child("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userResponses = new HashMap<>();
                    
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        String value = snapshot.getValue(String.class);
                        if (key != null && value != null) {
                            userResponses.put(key, value);
                        }
                    }
                    generateTips();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void generateTips() {
        if (questionnaireData == null || userResponses == null || getActivity() == null) {
            return;
        }
        
        try {
            JSONObject tipsData = questionnaireData.getJSONObject("tips");
            List<TipAdapter.Tip> newTips = new ArrayList<>();
            String currentBranch = userResponses.get("branch");

            for (String questionId : userResponses.keySet()) {
                String response = userResponses.get(questionId);
                
                if (tipsData.has(questionId)) {
                    Object tipObject = tipsData.get(questionId);
                    String tipContent = null;

                    if (tipObject instanceof String) {
                        // Simple string tip - always show it
                        tipContent = (String) tipObject;
                    } else if (tipObject instanceof JSONObject) {
                        // Conditional tip based on response
                        JSONObject conditionalTip = (JSONObject) tipObject;
                        if (conditionalTip.has(response)) {
                            tipContent = conditionalTip.getString(response);
                        }
                    }
                    if (tipContent != null) {
                        tipContent = replacePlaceholders(tipContent, questionId, response);
                        newTips.add(new TipAdapter.Tip(tipContent, questionId));
                    }
                }
            }

            if (getActivity() != null && !getActivity().isFinishing()) {
                getActivity().runOnUiThread(() -> {
                    if (tipAdapter != null) {
                        tipAdapter.updateTips(newTips);
                        if (newTips.size() > 0 && subtitleText != null) {
                            subtitleText.setText("Safety plans are personal and not guaranteed to prevent harm");
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String replacePlaceholders(String tipContent, String questionId, String response) {
        String result = tipContent;
        
        result = result.replace("{answer}", response);
        
        switch (questionId) {
            case "city":
                result = result.replace("{city}", response);
                break;
            case "safe_room":
                result = result.replace("{safe_room}", response);
                break;
            case "emergency_contact":
                result = result.replace("{contact_name}", response);
                break;
            case "leave_date":
                result = result.replace("{leave_timing}", response);
                break;
            case "emergency_money":
                result = result.replace("{money_location}", response);
                break;
            case "support_type":
                result = result.replace("{support_choice}", response);
                break;
            case "children":
                if (response.equals("yes")) {
                    // Get code word from responses
                    String codeWord = userResponses.get("code_word");
                    if (codeWord != null) {
                        result = result.replace("{code_word}", codeWord);
                    }
                }
                break;
            case "live_with":
                if (response.equals("family") || response.equals("roommates")) {
                    result = result.replace("{family}", response);
                    result = result.replace("{roommates}", response);
                }
                break;
            case "abuse_types":
                result = result.replace("{abuse_type}", response);
                break;
            case "safe_place":
                if (response.equals("yes")) {
                    String tempShelter = userResponses.get("temp_shelter");
                    if (tempShelter != null) {
                        result = result.replace("{temp_shelter}", tempShelter);
                    }
                }
                break;
            case "protection_order":
                if (response.equals("yes")) {
                    String legalOrder = userResponses.get("legal_order");
                    if (legalOrder != null) {
                        result = result.replace("{legal_order}", legalOrder);
                    }
                }
                break;
            case "safety_tools":
                if (response.equals("yes")) {
                    String equipment = userResponses.get("equipment");
                    if (equipment != null) {
                        result = result.replace("{equipment}", equipment);
                    }
                }
                break;
        }
        return result;
    }


}
