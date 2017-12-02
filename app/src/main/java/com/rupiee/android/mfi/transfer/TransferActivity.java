package com.rupiee.android.mfi.transfer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.rupiee.android.R;

/**
 * Created by ketan on 13/10/17.
 */

public class TransferActivity extends AppCompatActivity {

    private static final String TAG = TransferActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transfer);
        TransferFragment transferFragment = TransferFragment.newInstance();
        addFragment(R.id.ta_fragcontainer, transferFragment);
    }

    Fragment mCurrentFragment;
    protected void addFragment(int viewId, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(viewId, fragment)
                .commit();
        mCurrentFragment = fragment;
    }
}
