package com.rupiee.android.mfi.user;

import android.os.Bundle;

import com.rupiee.android.R;

/**
 * Created by ketan on 13/10/17.
 */

public class RegisterUserActivity extends UserDrawerActivity {

    private static final String TAG = RegisterUserActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reguser);
        setupDrawer();
        RegisterUserFragment registerUserFragment = RegisterUserFragment.newInstance();
        addFragment(R.id.rua_fragcontainer, registerUserFragment);
    }
}
