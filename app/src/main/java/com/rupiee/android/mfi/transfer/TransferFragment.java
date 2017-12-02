package com.rupiee.android.mfi.transfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.rupiee.android.R;
import com.rupiee.android.mfi.user.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.rupiee.android.utils.Constants.EXTRA_DISBURSE_AMOUNT;
import static com.rupiee.android.utils.Constants.EXTRA_DISBURSE_FROM;
import static com.rupiee.android.utils.Constants.EXTRA_DISBURSE_TO;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_ALLBALANCEUPDATE;
import static com.rupiee.android.utils.Utils.animateView;

/**
 * Created by ketan on 2/12/17.
 */

public class TransferFragment extends Fragment {

    private BroadcastReceiver mWeb3AllBalUpdateNotifier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mProgressOverlay != null) {
                        animateView(mProgressOverlay, GONE, 0, 200);
                    }
                    if(mXferAmountEditText != null) {
                        mXferAmountEditText.setEnabled(true);
                    }
                    if(mTransferButton != null) {
                        mTransferButton.setEnabled(true);
                    }
                    populateSpinners();
                }
            });
        }
    };

    public static TransferFragment newInstance() {

        Bundle args = new Bundle();

        TransferFragment fragment = new TransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.overlay_progress) View mProgressOverlay;

    @BindView(R.id.transfer_layout) CoordinatorLayout mMainCoordinatorLayout;
    @BindView(R.id.transfer_investors) Spinner mXferInvestorsSpinner;
    @BindView(R.id.transfer_borrowers) Spinner mXferBorrowersSpinner;
    @BindView(R.id.transfer_amount) EditText mXferAmountEditText;
    @BindView(R.id.transfer_submit) Button mTransferButton;

    @BindView(R.id.transfer_from_addr) TextView mFromAddrTextView;
    @BindView(R.id.transfer_from_balance) TextView mFromBalanceTextView;

    @BindView(R.id.transfer_to_addr) TextView mToAddrTextView;
    @BindView(R.id.transfer_to_balance) TextView mToBalanceTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tranfer, null, false);
        ButterKnife.bind(this, view);

        animateView(mProgressOverlay, View.VISIBLE, 0.4f, 200);

        final Intent getAllBalancesIntent = new Intent(getActivity(), GetAllBalancesService.class);
        getActivity().startService(getAllBalancesIntent);

        mXferInvestorsSpinner.setEnabled(false);
        mXferInvestorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserData invInfo = (UserData) mXferInvestorsSpinner.getSelectedItem();
                String addr = invInfo.getAddress();
                long balance = invInfo.getBalance();
                mFromAddrTextView.setText(addr);
                mFromBalanceTextView.setText(String.valueOf(balance));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mXferBorrowersSpinner.setEnabled(false);
        mXferBorrowersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserData invInfo = (UserData) mXferBorrowersSpinner.getSelectedItem();
                String addr = invInfo.getAddress();
                long balance = invInfo.getBalance();
                mToAddrTextView.setText(addr);
                mToBalanceTextView.setText(String.valueOf(balance));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mXferAmountEditText.setEnabled(false);

        mTransferButton.setEnabled(false);
        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData investor = (UserData) mXferInvestorsSpinner.getSelectedItem();
                String fromAddress = investor.getAddress();

                UserData borrower = (UserData) mXferBorrowersSpinner.getSelectedItem();
                String toAddress = borrower.getAddress();

                int amount = Integer.parseInt(mXferAmountEditText.getText().toString());

                Intent transferAmountIntent = new Intent(getActivity(), TransferAmountService.class);
                transferAmountIntent.putExtra(EXTRA_DISBURSE_TO, toAddress);
                transferAmountIntent.putExtra(EXTRA_DISBURSE_FROM, fromAddress);
                transferAmountIntent.putExtra(EXTRA_DISBURSE_AMOUNT, amount);
                getActivity().startService(transferAmountIntent);

                cleanup();
            }
        });

        return view;
    }

    private void cleanup() {
        mXferInvestorsSpinner.setSelection(0);
        mXferBorrowersSpinner.setSelection(0);
        Snackbar.make(mMainCoordinatorLayout, "Transfer request submitted", Snackbar.LENGTH_LONG).show();
    }

    public void populateSpinners() {
        List<User> users = new Select().from(User.class).execute();
        List<UserData> invBalances = new ArrayList<>();
        List<UserData> brwBalances = new ArrayList<>();
        for (User user : users) {
            if(user.getType() == 0) {
                invBalances.add(new UserData(user.getName(), user.getAddress(), user.getBalance()));
            } else {
                brwBalances.add(new UserData(user.getName(), user.getAddress(), user.getBalance()));
            }
        }

        populateInvestorSpinner(invBalances);
        populateBorrowerSpinner(brwBalances);
    }

    private void populateInvestorSpinner(List<UserData> balances) {
        final List<UserData> bal = balances;

        ArrayAdapter<UserData> dataAdapter = new ArrayAdapter<UserData>(getActivity(),
                android.R.layout.simple_spinner_item, balances) {
            @Override
            public boolean isEnabled(int position) {
                return bal.get(position).getBalance() >= 0;
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mXferInvestorsSpinner.setAdapter(dataAdapter);
        mXferInvestorsSpinner.setEnabled(true);

        Toast.makeText(getContext(), "Investors Updated!", Toast.LENGTH_SHORT).show();
    }

    private void populateBorrowerSpinner(List<UserData> balances) {
        final List<UserData> bal = balances;

        ArrayAdapter<UserData> dataAdapter = new ArrayAdapter<UserData>(getActivity(),
                android.R.layout.simple_spinner_item, balances) {
            @Override
            public boolean isEnabled(int position) {
                return bal.get(position).getBalance() >= 0;
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mXferBorrowersSpinner.setAdapter(dataAdapter);
        mXferBorrowersSpinner.setEnabled(true);

        Toast.makeText(getContext(), "Borrowers Updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mWeb3AllBalUpdateNotifier, new IntentFilter(INTENT_WEB3_ALLBALANCEUPDATE));
        super.onStart();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mWeb3AllBalUpdateNotifier);
        super.onStop();
    }

    private class UserData {
        private String name;
        private String address;
        private long balance;

        public UserData(String name, String address, long balance) {
            this.name = name;
            this.address = address;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
