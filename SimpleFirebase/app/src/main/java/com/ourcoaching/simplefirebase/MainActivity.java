package com.ourcoaching.simplefirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private FirebaseAnalytics mFirebaseAnalytics;
    // Downloaded from https://github.com/KuKapoor02
    // Visit https://www.ourcoaching.com/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickMeNow();
            }
        });

    }

    private void ClickMeNow() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "button");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "R.id.button");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "click me button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
