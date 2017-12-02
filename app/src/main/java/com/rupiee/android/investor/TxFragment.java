package com.rupiee.android.investor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.rupiee.android.R;
import com.rupiee.android.utils.PreferenceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rupiee.android.utils.Constants.INTENT_WEB3_BALANCE;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_TXUPLOAD;
import static com.rupiee.android.utils.Constants.PREF_WEB3_BALANCE;

/**
 * Created by ketan on 26/4/17.
 */
public class TxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static TxFragment newInstance() {

        Bundle args = new Bundle();

        TxFragment fragment = new TxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected TxAdapter mTxAdapter;
    private BroadcastReceiver mWeb3BalNotifier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateBalance();
                }
            });
        }
    };
    private BroadcastReceiver mTxUploadStatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRefresh();
                }
            });
        }
    };
    PreferenceManager mPreferenceManager;

    @BindView(R.id.tx_refresh) SwipeRefreshLayout mTxRefreshLayout;
    @BindView(R.id.list_txentries) RecyclerView mTxStatusRecyclerView;
    @BindView(R.id.tx_fab) FloatingActionButton mAddTxFab;
    @BindView(R.id.tx_balance) TextView mBalanceTextView;

    BottomSheetDialog mAddTxButtonDialog;
    Button mAddTxButton;
    LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tx, null, false);
        ButterKnife.bind(this, view);

        Intent getBalanceIntent = new Intent(getActivity(), BalanceService.class);
        getActivity().startService(getBalanceIntent);

        mPreferenceManager = PreferenceManager.getInstance(getContext());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTxStatusRecyclerView.setLayoutManager(mLayoutManager);
        mTxRefreshLayout.setOnRefreshListener(this);

        mAddTxFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddTxButtonDialog.show();
            }
        });

        mAddTxButtonDialog = new BottomSheetDialog(getActivity());
        View addTxButtonView = getActivity().getLayoutInflater().inflate(R.layout.dialog_addtxbutton, null);
        mAddTxButtonDialog.setContentView(addTxButtonView);
        mAddTxButton = (Button) addTxButtonView.findViewById(R.id.addTxButton);
        mAddTxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment payFragment = PayFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.ta_fragcontainer, payFragment)
                        .addToBackStack(null)
                        .commit();
                mAddTxButtonDialog.dismiss();

            }
        });

        updateBalance();
        populateTx();
        return view;
    }

    private void updateBalance() {
        String balance = mPreferenceManager.getString(PREF_WEB3_BALANCE) != null
                ? mPreferenceManager.getString(PREF_WEB3_BALANCE) : "0";
        mBalanceTextView.setText(balance);
    }

    private void populateTx() {
        List<TxEntry> txEntries = new Select().all().from(TxEntry.class).execute();

        mTxStatusRecyclerView.setVisibility(View.VISIBLE);
        mTxAdapter = new TxAdapter();
        mTxStatusRecyclerView.setAdapter(mTxAdapter);
        for (TxEntry txEntry : txEntries) {
            mTxAdapter.addItem(txEntry, mTxAdapter.getItemCount());
        }
        mTxAdapter.notifyDataSetChanged();
        mAddTxButton.setEnabled(true);
        mTxRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mTxAdapter = new TxAdapter();
        mTxStatusRecyclerView.setAdapter(mTxAdapter);
        mTxAdapter.clearView();
        populateTx();
    }
    @Override
    public void onStart() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mWeb3BalNotifier, new IntentFilter(INTENT_WEB3_BALANCE));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTxUploadStatReceiver, new IntentFilter(INTENT_WEB3_TXUPLOAD));
        super.onStart();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mWeb3BalNotifier);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTxUploadStatReceiver);
        super.onStop();
    }
}