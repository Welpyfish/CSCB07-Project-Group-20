package com.group20.cscb07project;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.group20.cscb07project.question.CheckboxQuestion;
import com.group20.cscb07project.question.DropdownQuestion;
import com.group20.cscb07project.question.QuestionView;
import com.group20.cscb07project.question.RadioGroupQuestion;
import com.group20.cscb07project.question.TextQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuestionnaireFragment extends Fragment {

    private JSONObject questionnaireData;
    private Map<String, Object> userResponses = new HashMap<>();
    private LinearLayout mainContainer;
    private String currentBranch = null;
    private LinearLayout branchContainer;
    private List<QuestionView> allQuestionViews = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        scrollView.setBackgroundColor(getResources().getColor(R.color.bg));
        scrollView.setFillViewport(true);

        mainContainer = new LinearLayout(getContext());
        mainContainer.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(96, 96, 96, 96);

        scrollView.addView(mainContainer);

        FirebaseDB.getInstance().setPath("/users/"+ Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()+"/");

        loadQuestionnaireData();
        buildQuestionnaireFromJSON();

        return scrollView;
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

    private void buildQuestionnaireFromJSON() {
        try {
            JSONObject questionnaire = questionnaireData.getJSONObject("questionnaire");
            JSONArray sections = questionnaire.getJSONArray("sections");
            addTitle(questionnaire.getString("title"));
            addProgressIndicator();

            for (int i = 0; i < sections.length(); i++) {
                JSONObject section = sections.getJSONObject(i);
                String sectionId = section.getString("id");
                
                if (sectionId.equals("warm_up")) {
                    buildWarmUpSection(section);
                } else if (sectionId.equals("branch_specific")) {
                    buildBranchSpecificSection(section);
                } else if (sectionId.equals("follow_up")) {
                    buildFollowUpSection(section);
                }
            }

            addSubmitButton();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addTitle(String title) {
        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setTextSize(24);
        titleView.setTextColor(getResources().getColor(R.color.black));
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setGravity(Gravity.CENTER);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        titleView.setPadding(0, 0, 0, 96);
        mainContainer.addView(titleView);
    }

    private void addProgressIndicator() {
        LinearProgressIndicator progressIndicator = new LinearProgressIndicator(getContext());
        progressIndicator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        progressIndicator.setPadding(0, 0, 0, 96);
        mainContainer.addView(progressIndicator);
    } // I give up im not implementing this :((

    private void buildWarmUpSection(JSONObject section) throws JSONException {
        MaterialCardView card = createSectionCard();
        LinearLayout cardContent = createCardContent();
        addSectionTitle(section.getString("title"), cardContent);
        
        JSONArray questions = section.getJSONArray("questions");
        for (int i = 0; i < questions.length(); i++) {
            JSONObject question = questions.getJSONObject(i);
            buildQuestion(question, cardContent);
        }
        card.addView(cardContent);
        mainContainer.addView(card);
        addSpacing();
    }

    private void buildBranchSpecificSection(JSONObject section) throws JSONException {
        MaterialCardView card = createSectionCard();
        LinearLayout cardContent = createCardContent();
        addSectionTitle(section.getString("title"), cardContent);
        
        branchContainer = new LinearLayout(getContext());
        branchContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        branchContainer.setOrientation(LinearLayout.VERTICAL);
        branchContainer.setTag("branch_container");
        cardContent.addView(branchContainer);
        card.addView(cardContent);
        mainContainer.addView(card);
        addSpacing();
    }

    private void buildFollowUpSection(JSONObject section) throws JSONException {
        MaterialCardView card = createSectionCard();
        LinearLayout cardContent = createCardContent();
        addSectionTitle(section.getString("title"), cardContent);
        
        JSONArray questions = section.getJSONArray("questions");
        for (int i = 0; i < questions.length(); i++) {
            JSONObject question = questions.getJSONObject(i);
            buildQuestion(question, cardContent);
        }
        card.addView(cardContent);
        mainContainer.addView(card);
        addSpacing();
    }

    private MaterialCardView createSectionCard() {
        MaterialCardView card = new MaterialCardView(getContext());
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        card.setCardBackgroundColor(getResources().getColor(R.color.white));
        card.setRadius(48);
        card.setCardElevation(16);
        card.setPadding(0, 0, 0, 64);
        return card;
    }

    private LinearLayout createCardContent() {
        LinearLayout cardContent = new LinearLayout(getContext());
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        cardContent.setPadding(80, 80, 80, 80);
        return cardContent;
    }

    private void addSectionTitle(String title, LinearLayout container) {
        TextView sectionTitle = new TextView(getContext());
        sectionTitle.setText(title);
        sectionTitle.setTextSize(18);
        sectionTitle.setTextColor(getResources().getColor(R.color.darkGreen));
        sectionTitle.setTypeface(null, Typeface.BOLD);
        sectionTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        sectionTitle.setPadding(0, 0, 0, 64);
        container.addView(sectionTitle);
    }

    private void buildQuestion(JSONObject question, LinearLayout container) throws JSONException {
        String questionId = question.getString("id");
        String questionText = question.getString("question");
        String questionType = question.getString("type");
        boolean isRequired = question.optBoolean("required", true);

        TextView questionTextView = new TextView(getContext());
        questionTextView.setText(questionText + (isRequired ? " *" : ""));
        questionTextView.setTextSize(16);
        questionTextView.setTextColor(getResources().getColor(R.color.black));
        questionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        questionTextView.setPadding(0, 0, 0, 32);
        container.addView(questionTextView);

        QuestionView questionView = null;

        switch (questionType) {
            case "radio" -> {
                RadioGroupQuestion radioGroupQuestion = new RadioGroupQuestion(getContext(), question);
                questionView = radioGroupQuestion;
                radioGroupQuestion.setCallback((branch, isChecked) -> {
                    if(isChecked) {
                        currentBranch = branch;
                        updateBranchSpecificQuestions(branch);
                    } else {
                    }
                });
                container.addView(radioGroupQuestion.getView());
            }
            case "radio_with_conditional" -> {
                RadioGroupQuestion radioGroupQuestion = new RadioGroupQuestion(getContext(), question);
                questionView = radioGroupQuestion;
                JSONArray options = question.getJSONArray("options");
                for (int i = 0; i < options.length(); i++) {
                    JSONObject option = options.getJSONObject(i);
                    if(option.has("conditional_field")){
                        JSONObject conditionalField = option.getJSONObject("conditional_field");
                        TextQuestion conditionalQuestion = new TextQuestion(getContext(), conditionalField);
                        conditionalQuestion.getView().setVisibility(View.GONE);
                        boolean isConditionalRequired = conditionalField.optBoolean("required", true);
                        radioGroupQuestion.setCallback((ignore, isChecked) -> {
                            if(isChecked){
                                conditionalQuestion.getView().setVisibility(View.VISIBLE);
                                if (isConditionalRequired) {
                                    allQuestionViews.add(conditionalQuestion);
                                }
                            } else {
                                conditionalQuestion.getView().setVisibility(View.GONE);
                                if (isConditionalRequired) {
                                    allQuestionViews.remove(conditionalQuestion);
                                }
                            }
                        });
                        container.addView(conditionalQuestion.getView());
                    }
                }
                container.addView(radioGroupQuestion.getView());
            }
            case "dropdown" -> {
                DropdownQuestion dropdownQuestion = new DropdownQuestion(getContext(), question);
                questionView = dropdownQuestion;
                container.addView(dropdownQuestion.getView());
            }
            case "text" -> {
                TextQuestion textQuestion = new TextQuestion(getContext(), question);
                questionView = textQuestion;
                container.addView(textQuestion.getView());
            }
            case "checkbox" -> buildCheckboxGroup(question, container);
            case "date" -> {
                TextQuestion textQuestion = new TextQuestion(getContext(), question);
                questionView = textQuestion;
                EditText dateEditText = ((TextInputLayout)textQuestion.getView()).getEditText();
                dateEditText.setInputType(InputType.TYPE_CLASS_DATETIME);
                dateEditText.setTextDirection(View.TEXT_DIRECTION_LTR);
                container.addView(textQuestion.getView());
            }
        }

        if (questionView != null && isRequired) {
            allQuestionViews.add(questionView);
        }

        addQuestionSpacing(container);
    }
    private void buildCheckboxGroup(JSONObject question, LinearLayout container) throws JSONException {
        String questionId = question.getString("id");
        boolean isRequired = question.optBoolean("required", true);

        final LinearLayout checkboxContainer = new LinearLayout(getContext());
        checkboxContainer.setOrientation(LinearLayout.VERTICAL);

        JSONArray options = question.getJSONArray("options");
        final List<CheckBox> checkboxes = new ArrayList<>();
        for (int i = 0; i < options.length(); i++) {
            JSONObject option = options.getJSONObject(i);
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(option.getString("text"));
            checkBox.setTag(option.getString("value"));
            checkboxContainer.addView(checkBox);
            checkboxes.add(checkBox);
        }

        container.addView(checkboxContainer);

        QuestionView checkboxGroupView = new QuestionView(getContext(), question) {

            @Override
            protected View createView(Context context) {
                return checkboxContainer;
            }
            @Override
            protected void addListener() {
                for (CheckBox checkBox : checkboxes) {
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        setValue(getCurrentValue());
                    });
                }
            }

            @Override
            public String getCurrentValue() {
                List<String> checkedValues = new ArrayList<>();
                for (CheckBox checkBox : checkboxes) {
                    if (checkBox.isChecked()) {
                        checkedValues.add(checkBox.getTag().toString());
                    }
                }
                if (checkedValues.isEmpty()) {
                    return null;
                }
                return String.join(",", checkedValues);
            }

            @Override
            public void updateUI(String value) {
                if (value != null) {
                    String[] selected = value.split(",");
                    for (CheckBox checkBox : checkboxes) {
                        checkBox.setChecked(false);
                        for (String s : selected) {
                            if (checkBox.getTag().equals(s)) {
                                checkBox.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            }
        };

        if (isRequired) {
            allQuestionViews.add(checkboxGroupView);
        }
    }


    private void updateBranchSpecificQuestions(String branch) {
        try {
            if (branchContainer != null) {
                branchContainer.removeAllViews();

                List<QuestionView> questionsToRemove = new ArrayList<>();
                for (QuestionView questionView : allQuestionViews) {
                    if (questionView.getView().getParent() == branchContainer) {
                        questionsToRemove.add(questionView);
                    }
                }
                allQuestionViews.removeAll(questionsToRemove);

                JSONObject branchSpecificSection = questionnaireData.getJSONObject("questionnaire")
                        .getJSONArray("sections").getJSONObject(1);
                JSONObject branches = branchSpecificSection.getJSONObject("branches");
                JSONObject branchQuestions = branches.getJSONObject(branch);

                JSONArray questions = branchQuestions.getJSONArray("questions");
                for (int i = 0; i < questions.length(); i++) {
                    JSONObject question = questions.getJSONObject(i);
                    buildQuestion(question, branchContainer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addSpacing() {
        View spacing = new View(getContext());
        spacing.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                64
        ));
        mainContainer.addView(spacing);
    }

    private void addQuestionSpacing(LinearLayout container) {
        View spacing = new View(getContext());
        spacing.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                64
        ));
        container.addView(spacing);
    }

    private void addSubmitButton() {
        MaterialButton submitButton = new MaterialButton(getContext());
        submitButton.setText("SUBMIT");
        submitButton.setTextSize(18);
        submitButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        submitButton.setBackgroundTintList(ColorStateList.valueOf(
                getResources().getColor(R.color.darkGreen)));
        submitButton.setPadding(32, 16, 32, 16);

        submitButton.setOnClickListener(v -> {
            collectAllResponses();
            
            boolean allAnswered = true;
            for (QuestionView questionView : allQuestionViews) {
                String value = questionView.getCurrentValue();
                if (value == null || value.trim().isEmpty()) {
                    // For conditional fields, only validate if they're visible
                    if (questionView.getView().getVisibility() == View.VISIBLE) {
                        allAnswered = false;
                        break;
                    }
                }
            }
            
            if (allAnswered) {
                Toast.makeText(getContext(), "Responses recorded", Toast.LENGTH_SHORT).show();
                
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SafetyPlanFragment())
                        .addToBackStack(null)
                        .commit();
                }
            }
        });
        mainContainer.addView(submitButton);
        View bottomSpacing = new View(getContext());
        bottomSpacing.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                180
        ));
        mainContainer.addView(bottomSpacing);
    }

    private void collectAllResponses() {
        List<String> missingQuestions = new ArrayList<>();

        for (QuestionView questionView : allQuestionViews) {
            // Only validate if the question's view is currently VISIBLE
            if (questionView.getView().getVisibility() == View.VISIBLE) {
                String value = questionView.getCurrentValue();
                if (value == null || value.trim().isEmpty()) {
                    missingQuestions.add(questionView.getQuestionId());
                }
            }
        }

        if (!missingQuestions.isEmpty()) {
            Toast.makeText(getContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
            return;
        }

        for (QuestionView questionView : allQuestionViews) {
            String questionId = questionView.getQuestionId();
            String value = questionView.getCurrentValue();

            // Only save if the question has a value AND its view is visible
            if (value != null && !value.trim().isEmpty() && questionView.getView().getVisibility() == View.VISIBLE) {
                FirebaseDB.getInstance().setValue(questionId, value, new FirebaseResultCallback() {
                    @Override
                    public void onSuccess() {
                        // Success
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), "Failed to save response for " + questionId, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        // Save the current branch separately
        if (currentBranch != null) {
            FirebaseDB.getInstance().setValue("branch", currentBranch, new FirebaseResultCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure() {
                }
            });
        }
    }
} 
