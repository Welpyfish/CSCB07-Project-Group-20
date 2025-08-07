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

            for (String questionId : userResponses.keySet()) {
                String response = userResponses.get(questionId);

                // Only process tips for relevant questions
                if (!relevantQuestions.contains(questionId)) {
                    continue;
                }

                if (tipsData.has(questionId)) {
                    Object tipObject = tipsData.get(questionId);
                    String tipContent = null;

                    if (tipObject instanceof String) {
                        tipContent = (String) tipObject;
                    } else if (tipObject instanceof JSONObject) {
                        JSONObject conditionalTip = (JSONObject) tipObject;
                        if (conditionalTip.has(response)) {
                            tipContent = conditionalTip.getString(response);
                        }
                    }

                    if (tipContent != null) {
                        tipContent = replaceAllPlaceholders(tipContent);
                        newTips.add(new TipAdapter.Tip(tipContent, questionId));
                    }
                }
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    tipAdapter.updateTips(newTips);
                    if (newTips.size() > 0) {
                        subtitleText.setText("Safety plans are personal and are not guaranteed to prevent harm");
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String replaceAllPlaceholders(String tipContent) {
        String result = tipContent;
        if (userResponses == null) {
            return result;
        }

        for (Map.Entry<String, String> entry : userResponses.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.trim().isEmpty()) {
                result = result.replace("{" + key + "}", value);
            }
        }
        return result;
    }

}