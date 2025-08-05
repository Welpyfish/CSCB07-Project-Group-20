package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QuestionnaireFragment extends Fragment {

    private JSONObject questionnaireData;
    private Map<String, Object> userResponses = new HashMap<>();
    private LinearLayout mainContainer;
    private String currentBranch = null;
    private LinearLayout branchContainer;

    // TODO: Implement data collection so we can do safety plan,
    //  update answers, and data delete


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
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setGravity(android.view.Gravity.CENTER);
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
        sectionTitle.setTypeface(null, android.graphics.Typeface.BOLD);
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

        TextView questionTextView = new TextView(getContext());
        questionTextView.setText(questionText);
        questionTextView.setTextSize(16);
        questionTextView.setTextColor(getResources().getColor(R.color.black));
        questionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        questionTextView.setPadding(0, 0, 0, 32);
        container.addView(questionTextView);

        if (questionType.equals("radio")) {
            buildRadioGroup(question, container);
        } else if (questionType.equals("radio_with_conditional")) {
            buildRadioGroupWithConditional(question, container);
        } else if (questionType.equals("dropdown")) {
            buildDropdown(question, container);
        } else if (questionType.equals("text")) {
            buildTextInput(question, container);
        } else if (questionType.equals("checkbox")) {
            buildCheckboxGroup(question, container);
        } else if (questionType.equals("date")) {
            buildDateInput(question, container);
        }

        addQuestionSpacing(container);
    }

    private void buildRadioGroup(JSONObject question, LinearLayout container) throws JSONException {
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        JSONArray options = question.getJSONArray("options");
        for (int i = 0; i < options.length(); i++) {
            JSONObject option = options.getJSONObject(i);
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(option.getString("text"));
            radioButton.setId(View.generateViewId());
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            radioGroup.addView(radioButton);

            if (question.getString("id").equals("relationship_status")) {
                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        try {
                            String value = option.getString("value");
                            currentBranch = value;
                            updateBranchSpecificQuestions(value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        container.addView(radioGroup);
    }

    private void buildRadioGroupWithConditional(JSONObject question, LinearLayout container) throws JSONException {
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        JSONArray options = question.getJSONArray("options");
        for (int i = 0; i < options.length(); i++) {
            JSONObject option = options.getJSONObject(i);
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(option.getString("text"));
            radioButton.setId(View.generateViewId());
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            radioGroup.addView(radioButton);

            if (option.has("conditional_field")) {
                JSONObject conditionalField = option.getJSONObject("conditional_field");
                TextInputLayout conditionalLayout = buildConditionalInput(conditionalField);
                conditionalLayout.setVisibility(View.GONE);
                container.addView(conditionalLayout);

                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        conditionalLayout.setVisibility(View.VISIBLE);
                    } else {
                        conditionalLayout.setVisibility(View.GONE);
                    }
                });
            }
        }
        container.addView(radioGroup);
    }

    private TextInputLayout buildConditionalInput(JSONObject field) throws JSONException {
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.setHint(field.getString("hint"));
        textInputLayout.setBoxStrokeColor(getResources().getColor(R.color.darkGreen));

        TextInputEditText editText = new TextInputEditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.addView(editText);

        return textInputLayout;
    }

    private void buildDropdown(JSONObject question, LinearLayout container) throws JSONException {
        Spinner spinner = new Spinner(getContext());
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        JSONArray options = question.getJSONArray("options");
        String[] optionsArray = new String[options.length()];
        for (int i = 0; i < options.length(); i++) {
            optionsArray[i] = options.getString(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, optionsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        container.addView(spinner);
    }

    private void buildTextInput(JSONObject question, LinearLayout container) throws JSONException {
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.setHint(question.getString("hint"));
        textInputLayout.setBoxStrokeColor(getResources().getColor(R.color.darkGreen));

        TextInputEditText editText = new TextInputEditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.addView(editText);

        container.addView(textInputLayout);
    }

    private void buildCheckboxGroup(JSONObject question, LinearLayout container) throws JSONException {
        JSONArray options = question.getJSONArray("options");
        for (int i = 0; i < options.length(); i++) {
            JSONObject option = options.getJSONObject(i);
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(option.getString("text"));
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            container.addView(checkBox);
        }
    }

    private void buildDateInput(JSONObject question, LinearLayout container) throws JSONException {
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.setHint(question.getString("hint"));
        textInputLayout.setBoxStrokeColor(getResources().getColor(R.color.darkGreen));

        TextInputEditText editText = new TextInputEditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        editText.setInputType(android.text.InputType.TYPE_CLASS_DATETIME);
        textInputLayout.addView(editText);

        container.addView(textInputLayout);
    }

    private void updateBranchSpecificQuestions(String branch) {
        try {
            if (branchContainer != null) {
                branchContainer.removeAllViews();

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
        submitButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                getResources().getColor(R.color.darkGreen)));
        submitButton.setPadding(32, 16, 32, 16);

        submitButton.setOnClickListener(v -> {
            collectAllResponses();
            Toast.makeText(getContext(), "Responses recorded", Toast.LENGTH_SHORT).show();
            
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SafetyPlanFragment())
                    .addToBackStack(null)
                    .commit();
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
        userResponses.put("branch", currentBranch);
    }
} 