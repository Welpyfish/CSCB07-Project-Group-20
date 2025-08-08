package com.group20.cscb07project.question;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DropdownQuestion extends QuestionView{
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    public DropdownQuestion(Context context, JSONObject question) {
        super(context, question);
    }

    @Override
    protected View createView(Context context) {
        spinner = new Spinner(context);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        JSONArray options = getQuestionOptions();
        String[] optionsArray = new String[options.length()];
        for (int i = 0; i < options.length(); i++) {
            try {
                optionsArray[i] = options.getString(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, optionsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    @Override
    protected void addListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setValue(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
