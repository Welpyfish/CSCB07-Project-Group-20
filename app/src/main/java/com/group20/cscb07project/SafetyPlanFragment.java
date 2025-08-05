package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SafetyPlanFragment extends Fragment {

    private TextView titleText;
    private TextView subtitleText;

    // TODO: Implement dynamic safety plan

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_safety_plan, container, false);

        titleText = view.findViewById(R.id.safety_plan_title);
        subtitleText = view.findViewById(R.id.safety_plan_subtitle);

        titleText.setText(R.string.safety_plan_title);
        subtitleText.setText(R.string.safety_plan_subtitle);

        return view;
    }
} 