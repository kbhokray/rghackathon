package com.rupiee.android.mfi.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.rupiee.android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rupiee.android.utils.Constants.INTENT_WEB3_BALANCE;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_TXUPLOAD;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_USERREGISTER;

/**
 * Created by ketan on 13/10/17.
 */

public class UserListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static UserListFragment newInstance() {

        Bundle args = new Bundle();

        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected UserAdapter mUserAdapter;
    private BroadcastReceiver mUserRegisteredReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // onRefresh();
                }
            });
        }
    };


    @BindView(R.id.list_user) RecyclerView mUserRecyclerView;
    LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlist, null, false);
        ButterKnife.bind(this, view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mUserRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        populateUsers();

        return view;
    }

    private void populateUsers() {
        mUserAdapter = new UserAdapter();
        mUserRecyclerView.setAdapter(mUserAdapter);

        List<User> users = new Select().all().from(User.class).execute();
        for (User user : users) {
            mUserAdapter.addItem(user, mUserAdapter.getItemCount());
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mUserAdapter = new UserAdapter();
        mUserRecyclerView.setAdapter(mUserAdapter);
        mUserAdapter.clearView();
        populateUsers();
    }
    @Override
    public void onStart() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUserRegisteredReceiver, new IntentFilter(INTENT_WEB3_USERREGISTER));
        super.onStart();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUserRegisteredReceiver);
        super.onStop();
    }

}
