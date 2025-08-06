package com.group20.cscb07project.question;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckboxQuestion extends QuestionView{
    private CheckBox checkBox;
    public CheckboxQuestion(Context context, JSONObject question) {
        super(context, question);
    }

    @Override
    View createView(Context context) {
        checkBox = new CheckBox(context);
        checkBox.setText(getQuestionData("text"));

        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        return checkBox;
    }

    @Override
    void addListener() {
        checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked){
                setValue("true");
            }else{
                setValue("false");
            }
        });
    }

    @Override
    public void updateUI(String value) {
        checkBox.setChecked("true".equals(value));
    }
}
