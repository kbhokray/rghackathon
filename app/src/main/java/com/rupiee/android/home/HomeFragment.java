package com.rupiee.android.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rupiee.android.R;
import com.rupiee.android.investor.TxActivity;
import com.rupiee.android.mfi.transfer.TransferActivity;
import com.rupiee.android.mfi.user.RegisterUserActivity;
import com.rupiee.android.model.HomeEntryVo;
import com.rupiee.android.utils.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rupiee.android.utils.Constants.PREF_APPLICATION_MODE;
import static com.rupiee.android.utils.Constants.PREF_APPMODE_ADMIN;
import static com.rupiee.android.utils.Constants.PREF_APPMODE_INVESTOR;

/**
 * Created by ketan on 7/4/17.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected HomeEntryAdapter mHomeEntryAdapter;

    @BindView(R.id.home_mainImage) ImageView mMainImageView;
    @BindView(R.id.list_homeentry) RecyclerView mHomeEntryRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private PreferenceManager mPreferenceManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null, false);
        ButterKnife.bind(this, view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHomeEntryRecyclerView.setLayoutManager(mLayoutManager);

        mPreferenceManager = PreferenceManager.getInstance(getActivity());

        populateHomeEntries();

        return view;
    }

    private void populateHomeEntries() {
        if(mHomeEntryAdapter == null) {
            mHomeEntryAdapter = new HomeEntryAdapter(this);
            mHomeEntryRecyclerView.setAdapter(mHomeEntryAdapter);
        }

        if(PREF_APPMODE_ADMIN.equals(mPreferenceManager.getString(PREF_APPLICATION_MODE))){
            mHomeEntryAdapter.addItem(new HomeEntryVo(R.drawable.ic_addaccount, "Accounts", "Register investor and borrower accounts", RegisterUserActivity.class), mHomeEntryAdapter.getItemCount());
            mHomeEntryAdapter.addItem(new HomeEntryVo(R.drawable.ic_transfer, "Transfer", "Transfer money from investors to borrowers", TransferActivity.class), mHomeEntryAdapter.getItemCount());
        }
        if(PREF_APPMODE_INVESTOR.equals(mPreferenceManager.getString(PREF_APPLICATION_MODE))) {
            mHomeEntryAdapter.addItem(new HomeEntryVo(R.drawable.ic_invest, "Investments", "Manage your investments", TxActivity.class), mHomeEntryAdapter.getItemCount());
        }
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mHomeEntryRecyclerView.indexOfChild(v);
        HomeEntryVo homeEntry = mHomeEntryAdapter.getItem(itemPosition);
        ((HomeActivity) getActivity()).changeActivity(homeEntry.getTargetActivity());
    }
}
