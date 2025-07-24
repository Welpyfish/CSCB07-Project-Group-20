package com.group20.cscb07project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SetPinActivity extends AppCompatActivity {
    private static final int PIN_LENGTH = 4;
    private final List<Integer> pin = new ArrayList<>();
    private ImageView[] pinDots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        pinDots = new ImageView[] {
                findViewById(R.id.pinDot1),
                findViewById(R.id.pinDot2),
                findViewById(R.id.pinDot3),
                findViewById(R.id.pinDot4)
        };

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        for (int id : buttonIds) {
            Button btn = findViewById(id);
            btn.setOnClickListener(v -> onDigitPressed(Integer.parseInt(btn.getText().toString())));
        }

        ImageButton backspace = findViewById(R.id.btnBackspace);
        backspace.setOnClickListener(v -> onBackspacePressed());
        updatePinDots();
    }

    private void onDigitPressed(int digit) {
        if (pin.size() < PIN_LENGTH) {
            pin.add(digit);
            updatePinDots();
            if (pin.size() == PIN_LENGTH) {
                // TODO: Handle PIN complete (e.g., save or verify PIN)
                SharedPreferences preferences = getSharedPreferences("projectpreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                StringBuilder p = new StringBuilder();
                for(int i=0; i<pin.size(); i++){
                    p.append(pin.get(i));
                }
                editor.putString("PIN", String.valueOf(p));
                editor.apply();
                Intent intent = new Intent(SetPinActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void onBackspacePressed() {
        if (!pin.isEmpty()) {
            pin.remove(pin.size() - 1);
            updatePinDots();
        }
    }

    private void updatePinDots() {
        for (int i = 0; i < PIN_LENGTH; i++) {
            if (i < pin.size()) {
                pinDots[i].setImageResource(R.drawable.pin_dot_filled);
            } else {
                pinDots[i].setImageResource(R.drawable.pin_dot_unfilled);
            }
        }
    }
} 