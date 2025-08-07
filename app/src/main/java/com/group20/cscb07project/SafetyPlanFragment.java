package com.group20.cscb07project;

import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

        tipsList = new ArrayList<>();
        tipAdapter = new TipAdapter(tipsList);
        tipsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tipsRecyclerView.setAdapter(tipAdapter);

        loadQuestionnaireData();
        loadUserResponses();

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
                    Log.w("SafetyPlan", "Failed to read user responses.", databaseError.toException());
                }
            });
        }
    }

    private void generateTips() {
        if (questionnaireData == null || userResponses == null) {
            return;
        }
        try {
            JSONObject tipsData = questionnaireData.getJSONObject("tips");
            List<TipAdapter.Tip> newTips = new ArrayList<>();
            String currentBranch = userResponses.get("relationship_status");

            Map<String, List<String>> branchQuestions = new HashMap<>();
            branchQuestions.put("still_in_relationship", Arrays.asList("abuse_types", "recording_incidents", "emergency_contact"));
            branchQuestions.put("planning_to_leave", Arrays.asList("leave_date", "go_bag", "emergency_money", "safe_place"));
            branchQuestions.put("post_separation", Arrays.asList("continued_contact", "protection_order", "safety_tools"));

            List<String> relevantQuestions = branchQuestions.get(currentBranch);
            if (relevantQuestions == null) {
                relevantQuestions = new ArrayList<>();
            }

            List<String> warmUpQuestions = Arrays.asList("relationship_status", "city", "safe_room", "live_with", "children");
            relevantQuestions.addAll(warmUpQuestions);

            Log.d("SafetyPlan", "Relevant questions for branch " + currentBranch + ": " + relevantQuestions);

            for (String questionId : userResponses.keySet()) {
                String response = userResponses.get(questionId);
                Log.d("SafetyPlan", "Processing question: " + questionId + " with response: " + response);

                // Only process tips for relevant questions
                if (!relevantQuestions.contains(questionId)) {
                    Log.d("SafetyPlan", "Skipping irrelevant question: " + questionId);
                    continue;
                }

                if (tipsData.has(questionId)) {
                    Object tipObject = tipsData.get(questionId);
                    String tipContent = null;

                    if (tipObject instanceof String) {
                        tipContent = (String) tipObject;
                        Log.d("SafetyPlan", "Found simple tip for " + questionId + ": " + tipContent);
                    } else if (tipObject instanceof JSONObject) {
                        JSONObject conditionalTip = (JSONObject) tipObject;
                        if (conditionalTip.has(response)) {
                            tipContent = conditionalTip.getString(response);
                            Log.d("SafetyPlan", "Found conditional tip for " + questionId + " with response " + response + ": " + tipContent);
                        } else {
                            Log.d("SafetyPlan", "No conditional tip found for " + questionId + " with response " + response);
                        }
                    }

                    if (tipContent != null) {
                        tipContent = replacePlaceholders(tipContent, questionId, response);
                        newTips.add(new TipAdapter.Tip(tipContent, questionId));
                        Log.d("SafetyPlan", "Added tip: " + tipContent);
                    } else {
                        Log.d("SafetyPlan", "No tip content found for " + questionId);
                    }
                } else {
                    Log.d("SafetyPlan", "No tip found for question: " + questionId);
                }
            }
            Log.d("SafetyPlan", "Total tips generated: " + newTips.size());

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    tipAdapter.updateTips(newTips);
                    if (newTips.size() > 0) {
                        subtitleText.setText("Safety plans are personal and not guaranteed to prevent harm");
                    }
                });
            }

        } catch (JSONException e) {
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
                    String codeWord = userResponses.get("code_word");
                    if (codeWord != null) {
                        result = result.replace("{code_word}", codeWord);
                    }
                }
                break;
            case "live_with":
                if (response.equals("partner")) {
                    String safeRoom = userResponses.get("safe_room");
                    if (safeRoom != null) {
                        result = result.replace("{safe_room}", safeRoom);
                    }
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