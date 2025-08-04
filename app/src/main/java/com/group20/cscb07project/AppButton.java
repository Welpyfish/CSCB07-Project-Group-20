package com.group20.cscb07project;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppButton extends Button {

    public AppButton(@NonNull Context context) {
        super(context);
        init();
    }

    public AppButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Set default styling based on the style specification
        setBackgroundColor(getResources().getColor(R.color.bg)); // Background color
        setTextColor(getResources().getColor(R.color.white)); // White text
        setTextSize(20);
        setTypeface(getTypeface(), android.graphics.Typeface.BOLD); // Bold text style
        setGravity(android.view.Gravity.CENTER); // Center gravity
        setPadding(24, 12, 24, 12); // Padding: 24dp start/end, 12dp top/bottom
        setElevation(6f);
        
        // Set corner radius (3dp)
        setClipToOutline(true);
        
        // Set default layout parameters
        setLayoutParams(new android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Ensure the button has proper styling when attached to window
        setBackgroundColor(getResources().getColor(R.color.bg));
        setTextColor(getResources().getColor(R.color.white));
    }
} 