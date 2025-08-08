package com.group20.cscb07project.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;
import android.widget.Toast;

import com.group20.cscb07project.FloatingExitButton;
import com.group20.cscb07project.MainActivity;
import com.group20.cscb07project.R;


public class SetPinActivity extends AppCompatActivity {
    private static final int PIN_LENGTH = 4;
    private final List<Integer> pin = new ArrayList<>();
    private ImageView[] pinDots;
    private int tries;
    private StringBuilder p;
    FloatingExitButton exitButton;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_set_pin);
        exitButton = findViewById(R.id.exitButton);
        setupEmergencyExit();

        p = new StringBuilder();

        // Set the title based on if PIN exists
        TextView titleTextView = findViewById(R.id.setPinTextView);
        if (PinManager.doesPinExist(this)) {
            titleTextView.setText(R.string.enter_pin);
        } else {
            titleTextView.setText(R.string.set_pin);
        }

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

                p.setLength(0);
                for (int num : pin) {
                    p.append(num);
                }
                String enteredPin = p.toString();

                if (PinManager.doesPinExist(this)) {
                    if (PinManager.verifyPin(this, enteredPin)) {
                        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        tries++;
                        Toast.makeText(this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
                        clearPin();
                        if (tries >= 3) {
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        }
                    }
                } else {
                    boolean stored = false;
                    int i = 0;
                    while ((!stored) && (i < 10)) {
                        stored = PinManager.savePin(this, enteredPin);
                        i++;
                    }
                    if (i >= 10) {
                        Toast.makeText(this, "Error saving pin, try again", Toast.LENGTH_SHORT).show();
                        clearPin();
                    } else {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                }
            }
        }
    }
    private void clearPin(){
        pin.clear();
        p.setLength(0);
        updatePinDots();
    }
    private void onBackspacePressed() {
        if (!pin.isEmpty()) {
            pin.remove(pin.size() - 1);
            updatePinDots();
        }
    }

    private void setupEmergencyExit() {
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(SetPinActivity.this);
                exitButton.exitApp();
            }
        });
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