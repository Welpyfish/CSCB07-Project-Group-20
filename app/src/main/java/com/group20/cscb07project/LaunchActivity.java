package com.group20.cscb07project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group20.cscb07project.auth.LoginActivity;

public class LaunchActivity extends AppCompatActivity {
    FloatingExitButton exitButton;
    Button getStartedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launch);

        getStartedButton = findViewById(R.id.getStartedButton);
        exitButton = findViewById(R.id.exitButton);
        CheckBox disclaimerCheckbox = findViewById(R.id.disclaimerCheckbox);
        TextView disclaimerTextView = findViewById(R.id.disclaimerTextView);

        // Initially disable the button
        getStartedButton.setEnabled(false);
        getStartedButton.setAlpha(0.5f);

        disclaimerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                getStartedButton.setEnabled(true);
                getStartedButton.setAlpha(1.0f);
                disclaimerTextView.setText(getString(R.string.disclaimer));
                disclaimerTextView.setTextColor(getResources().getColor(R.color.grey));
            } else {
                getStartedButton.setEnabled(false);
                getStartedButton.setAlpha(0.5f);
                disclaimerTextView.setText("Please accept the terms and conditions to proceed");
                disclaimerTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disclaimerCheckbox.isChecked()) {
                    Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(LaunchActivity.this);
                exitButton.exitApp();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        exitButton.setActivity(LaunchActivity.this);
        exitButton.exitApp();
    }
}