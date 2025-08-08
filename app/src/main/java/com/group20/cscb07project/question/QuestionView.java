package com.group20.cscb07project.question;

import android.content.Context;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiConsumer;

public abstract class QuestionView {
    final View view;
    private final String questionId;
    private String value;

    final JSONObject question;
    BiConsumer<String, Boolean> callback;

    public QuestionView(Context context, JSONObject question){
        this.question = question;
        questionId = getQuestionData("id");
        view = createView(context);
        addListener();
    }

    String getQuestionData(String key){
        try {
            return question.getString(key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    JSONArray getQuestionOptions(){
        try {
            return question.getJSONArray("options");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract View createView(Context context);
    protected abstract void addListener();

    public View getView() {
        return view;
    }

    public void setValue(String value){
        if(value!= null && !value.equals(this.value)){
            this.value = value;
            // Don't save to Firebase immediately - wait for submit
        }
    }

    public void setCallback(BiConsumer<String, Boolean> callback){
        this.callback = callback;
    }

    public String getCurrentValue() {
        return value;
    }

    public String getQuestionId() {
        return questionId;
    }
}
