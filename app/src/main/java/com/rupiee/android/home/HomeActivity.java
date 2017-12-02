package com.rupiee.android.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.rupiee.android.R;
import com.rupiee.android.utils.PreferenceManager;

import static com.rupiee.android.utils.Constants.PREF_APPLICATION_MODE;
import static com.rupiee.android.utils.Constants.PREF_APPMODE_ADMIN;
import static com.rupiee.android.utils.Constants.PREF_APPMODE_INVESTOR;

/**
 * Created by ketan on 7/4/17.
 */

public class HomeActivity extends AppCompatActivity {

    Fragment mCurrentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PreferenceManager pm = PreferenceManager.getInstance(this);

        pm.putString(PREF_APPLICATION_MODE, PREF_APPMODE_ADMIN);

        if(PREF_APPMODE_ADMIN.equals(pm.getString(PREF_APPLICATION_MODE))) {

        }

        HomeFragment homeFragment = HomeFragment.newInstance();
        addFragment(homeFragment);
    }

    private void addFragment(Fragment newFragment) {
        mCurrentFragment = newFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ha_fragcontainer, mCurrentFragment)
                .commit();
    }

    public void changeActivity(Class<Activity> activtyClass) {
        Intent intent = new Intent(this, activtyClass);
        startActivity(intent);
    }
}
