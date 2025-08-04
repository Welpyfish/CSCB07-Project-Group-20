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

        setBackgroundColor(getResources().getColor(R.color.bg));
        setTextColor(getResources().getColor(R.color.white));
        setTextSize(20);
        setTypeface(getTypeface(), android.graphics.Typeface.BOLD);
        setGravity(android.view.Gravity.CENTER);
        setPadding(24, 12, 24, 12);
        setElevation(6f);
        setClipToOutline(true);
        setLayoutParams(new android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setBackgroundColor(getResources().getColor(R.color.bg));
        setTextColor(getResources().getColor(R.color.white));
    }
} 