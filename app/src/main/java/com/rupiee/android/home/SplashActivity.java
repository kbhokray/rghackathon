package com.rupiee.android.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rupiee.android.R;

/**
 * Created by ketan on 9/3/17.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
