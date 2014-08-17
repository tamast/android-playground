package com.taktam.android.lambdatest;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;


public class LambdaTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lambda_test);

        TextView testTextView = (TextView) findViewById(R.id.myTextView);

        this.findViewById(R.id.testButton).setOnClickListener((view) -> {
            testTextView.setText("Retrolambda works, oh yeah!");

            Animation animation = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(100);
            animation.setFillAfter(true);
            view.setAnimation(animation);

            Toast.makeText(getApplication(), "Lambda works!", Toast.LENGTH_LONG).show();
        });
    }
}
