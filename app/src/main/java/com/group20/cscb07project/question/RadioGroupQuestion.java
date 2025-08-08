package com.group20.cscb07project.question;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RadioGroupQuestion extends QuestionView{
    private RadioGroup radioGroup;
    private ArrayList<RadioButton> radioButtons;
    public RadioGroupQuestion(Context context, JSONObject question) {
        super(context, question);
    }

    @Override
    protected View createView(Context context) {
        radioGroup = new RadioGroup(context);
        radioButtons = new ArrayList<>();
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        JSONArray options = getQuestionOptions();
        for (int i = 0; i < options.length(); i++) {
            JSONObject option;
            RadioButton radioButton = new RadioButton(context);
            try {
                option = options.getJSONObject(i);
                radioButton.setText(option.getString("text"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            radioButton.setId(View.generateViewId());
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            radioButtons.add(radioButton);
            radioGroup.addView(radioButton);

            if (getQuestionData("id").equals("relationship_status") || option.has("conditional_field")) {
                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (callback!=null) {
                        if (option.has("value")) {
                            try {
                                callback.accept(option.getString("value"), isChecked);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else{
                            callback.accept(null, isChecked);
                        }
                    }
                });
            }
        }
        return radioGroup;
    }

    @Override
    protected void addListener() {
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton radioButton = radioGroup.findViewById(i);
            setValue(radioButton.getText().toString());
        });
    }


}
