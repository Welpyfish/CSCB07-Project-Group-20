package com.example.b07demosummer2024;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView signInWithPin = findViewById(R.id.signInTextView);
        SpannableString content = new SpannableString("Sign in with PIN");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        signInWithPin.setText(content);
        signInWithPin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SetPinActivity.class);
            startActivity(intent);
        });
    }
} 