package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView titleText = view.findViewById(R.id.home_title);
        TextView subtitleText = view.findViewById(R.id.home_subtitle);
        MaterialButton startQuestionnaireButton = view.findViewById(R.id.start_questionnaire_button);
        MaterialButton updateAnswersButton = view.findViewById(R.id.update_answers_button);
        MaterialButton viewPlanButton = view.findViewById(R.id.view_plan_button);

        titleText.setText(R.string.home_title);
        subtitleText.setText(R.string.home_subtitle);
        startQuestionnaireButton.setText(R.string.start_questionnaire);
        updateAnswersButton.setText(R.string.update_answers);
        viewPlanButton.setText(R.string.view_plan);

        startQuestionnaireButton.setOnClickListener(v -> {
            loadFragment(new QuestionnaireFragment());
        });
        updateAnswersButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Future Feature: Shows current responses", Toast.LENGTH_LONG).show();
            loadFragment(new QuestionnaireFragment());
        });
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
