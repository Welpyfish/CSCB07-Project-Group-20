package com.group20.cscb07project.question;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.view.inputmethod.EditorInfo;

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
    protected View createView(Context context) {
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
        
        editText.setTextDirection(View.TEXT_DIRECTION_LTR);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | 
                             android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        
        textInputLayout.addView(editText);

        return textInputLayout;
    }

    @Override
    protected void addListener() {
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
        if (value != null && !value.equals(editText.getText().toString())) {
            editText.setText(value);
            // Set cursor to end of text
            editText.setSelection(value.length());
        }
    }

}
