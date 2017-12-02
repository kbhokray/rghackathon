package com.rupiee.android.investor;

import android.os.Bundle;

import com.rupiee.android.R;

/**
 * Created by ketan on 9/3/17.
 */

public class TxActivity extends TxDrawerActivity {

    private static final String TAG = TxActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tx);
        setupDrawer();
        TxFragment txFragment = TxFragment.newInstance();
        addFragment(R.id.ta_fragcontainer, txFragment);
    }
}
