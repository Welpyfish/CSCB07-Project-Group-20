package com.group20.cscb07project.question;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.group20.cscb07project.FirebaseDB;
import com.group20.cscb07project.FirebaseResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class QuestionView {
    View view;
    private String questionId;
    private String value;

    JSONObject question;
    BiConsumer<String, Boolean> callback;

    public QuestionView(Context context, JSONObject question){
        this.question = question;
        questionId = getQuestionData("id");
        view = createView(context);
        FirebaseDB.getInstance().addListener(questionId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateUI(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addListener();
    }

    String getQuestionData(String key){
        try {
            return question.getString(key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    JSONArray getQuestionOptions(String key){
        try {
            return question.getJSONArray(key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    abstract View createView(Context context);
    abstract void addListener();

    private void updateDB(){
        FirebaseDB.getInstance().setValue(questionId, value, new FirebaseResultCallback() {
            @Override
            public void onSuccess() {
                // Success - no toast to avoid spamming
            }

            @Override
            public void onFailure() {
                Toast.makeText(view.getContext(), "Failed to save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View getView() {
        return view;
    }

    public void setValue(String value){
        if(value!= null && !value.equals(this.value)){
            this.value = value;
            updateDB();
        }
    }

    public abstract void updateUI(String value);

    public void setCallback(BiConsumer<String, Boolean> callback){
        this.callback = callback;
    }
}
