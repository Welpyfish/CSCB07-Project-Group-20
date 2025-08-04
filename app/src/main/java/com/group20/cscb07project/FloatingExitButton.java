package com.group20.cscb07project;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FloatingExitButton extends FloatingActionButton {

    /* This class implements an action button that when clicked terminates the app
        to call this function first set caller activity as button activity. As follows:
            exitButton.setActivity(LaunchActivity.this);

        Then on click listener to call exitApp() method.


        Override onNewIntent function in root class as follow:
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            exitButton.setActivity(RootActivity.this);
            exitButton.exitApp();
        }
     */


    private Uri url;                            //Uri for redirection
    private Activity activity;                  //Calling activity

    public FloatingExitButton(@NonNull Context context) {
        super(context);
        setUrl("https://www.amazon.ca/");
    }

    public FloatingExitButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUrl("https://www.amazon.ca/");
    }

    public FloatingExitButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUrl("https://www.amazon.ca/");
    }

    public void setUrl(String link) {                                 //Set url as link argument
        this.url = Uri.parse(link);

    }

    public void setActivity(Activity activity) {
        this.activity = activity;                                     //Set argument activity as calling activity
    }

    public void exitApp (){
        Intent redirection = new Intent(Intent.ACTION_VIEW, url);       //Create intent for redirection and flag for new task
        redirection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Context context = getContext();
        if (activity instanceof LaunchActivity){
            this.getContext().startActivity(redirection);               //Redirects and finishes task if caller is root activity
            activity.finishAndRemoveTask();
        }
        else{                                                           //Redirects to root activity
            Intent intent = new Intent(activity, LaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
    }
}
