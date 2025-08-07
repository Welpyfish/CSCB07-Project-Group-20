package com.group20.cscb07project.question;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.R;

import org.json.JSONObject;

public class TextQuestion extends QuestionView{
    private TextInputLayout textInputLayout;
    private TextInputEditText editText;

    public TextQuestion(Context context, JSONObject question) {
        super(context, question);
    }

    @Override
    View createView(Context context) {
        textInputLayout = new TextInputLayout(context);
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.setHint(getQuestionData("hint"));
        textInputLayout.setBoxStrokeColor(context.getResources().getColor(R.color.darkGreen));

        editText = new TextInputEditText(context);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.addView(editText);

        return textInputLayout;
    }

    @Override
    void addListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setValue(editable.toString());
            }
        });
    }

    @Override
    public void updateUI(String value) {
        editText.setText(value);
    }


}
