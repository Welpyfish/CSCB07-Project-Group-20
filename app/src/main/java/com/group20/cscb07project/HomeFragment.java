package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {

    private MaterialButton startQuestionnaireButton;
    private MaterialButton updateAnswersButton;
    private MaterialButton viewPlanButton;
    private TextView titleText;
    private TextView subtitleText;
    FloatingExitButton exitButton;

    // TODO: Make update lead to non empty questionnaire

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        titleText = view.findViewById(R.id.home_title);
        subtitleText = view.findViewById(R.id.home_subtitle);
        startQuestionnaireButton = view.findViewById(R.id.start_questionnaire_button);
        updateAnswersButton = view.findViewById(R.id.update_answers_button);
        viewPlanButton = view.findViewById(R.id.view_plan_button);

        // Set text
        titleText.setText(R.string.home_title);
        subtitleText.setText(R.string.home_subtitle);
        startQuestionnaireButton.setText(R.string.start_questionnaire);
        updateAnswersButton.setText(R.string.update_answers);
        viewPlanButton.setText(R.string.view_plan);

        // Setup click listeners
        startQuestionnaireButton.setOnClickListener(v -> loadFragment(new QuestionnaireFragment()));
        updateAnswersButton.setOnClickListener(v -> loadFragment(new QuestionnaireFragment()));
        viewPlanButton.setOnClickListener(v -> loadFragment(new SafetyPlanFragment()));

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
