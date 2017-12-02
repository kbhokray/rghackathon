package com.rupiee.android.mfi.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.dd.CircularProgressButton;
import com.rupiee.android.R;

import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rupiee.android.utils.Constants.EXTRA_USER_AADHAAR;
import static com.rupiee.android.utils.Constants.EXTRA_USER_ADDRESS;
import static com.rupiee.android.utils.Constants.EXTRA_USER_MOBILE;
import static com.rupiee.android.utils.Constants.EXTRA_USER_NAME;
import static com.rupiee.android.utils.Constants.EXTRA_USER_TYPE;
import static com.rupiee.android.utils.Constants.EXTRA_USER_VPA;
import static com.rupiee.android.utils.Constants.VALID_ETHEREUM_ADDRS;

/**
 * Created by ketan on 8/3/17.
 */

public class RegisterUserFragment extends Fragment {

    private static final String TAG = RegisterUserFragment.class.getSimpleName();

    public static RegisterUserFragment newInstance() {

        Bundle args = new Bundle();

        RegisterUserFragment fragment = new RegisterUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.regacc_layout) CoordinatorLayout mMainCoordinatorLayout;
    @BindView(R.id.regacc_name) EditText mUserNameTextView;
    @BindView(R.id.regacc_aadhar) EditText mUserAadhaarTextView;
    @BindView(R.id.regacc_vpa) EditText mUserVpaTextView;
    @BindView(R.id.regacc_mobile) EditText mUserMobileTextView;
    @BindView(R.id.regacc_type) RadioGroup mUserTypeRadioGroup;
    @BindView(R.id.button_checkin) CircularProgressButton mRegisterButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regaccount, null, false);
        ButterKnife.bind(this, view);

        //TestData
//        mUserNameTextView.setText("Burvesh");
//        mUserAadhaarTextView.setText("1234567812345678");
//        mUserVpaTextView.setText("burvesh@upi");
//        mUserMobileTextView.setText("8112940024");
        //TestData

        setupButton();
        return view;
    }

    private void setupButton() {
        mRegisterButton.setProgress(0);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateName() || !validateAadharId() || !validateVpa() || !validateMobile()) {
                    return;
                }
                mRegisterButton.setEnabled(false);
                String userName = String.valueOf(mUserNameTextView.getText());
                String userAadharId = String.valueOf(mUserAadhaarTextView.getText());
                String userVpa = String.valueOf(mUserVpaTextView.getText());
                long userMobile = Long.parseLong(String.valueOf(mUserMobileTextView.getText()));
                String userAddress = null;
                int userType = 0;
                switch(mUserTypeRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.regacc_type_inv:
                        userType = 0;
                        userAddress = "0xe4f3fa7723e95237d4837ff9b47735502bd07d5a";
                        break;
                    case R.id.regacc_type_brw:
                        userType = 1;
                        userAddress = "0x3f82a110bdc0092fe7ebe0d14f5592edccb047a8";//generateNewWallet();
                        break;
                }

                User user = new User();
                user.setName(userName);
                user.setAddress(userAddress);
                user.setAadhaar(userAadharId);
                user.setVpa(userVpa);
                user.setType(userType);
                user.setMobile(userMobile);
                user.setCreated(new Date().getTime());
                user.setTxStatus(User.UserTxStatus.CREATED);
                user.save();

                Intent registerUserIntent = new Intent(getActivity(), RegisterUserService.class);
                registerUserIntent.putExtra(EXTRA_USER_NAME, userName);
                registerUserIntent.putExtra(EXTRA_USER_ADDRESS, userAddress);
                registerUserIntent.putExtra(EXTRA_USER_AADHAAR, userAadharId);
                registerUserIntent.putExtra(EXTRA_USER_VPA, userVpa);
                registerUserIntent.putExtra(EXTRA_USER_TYPE, userType);
                registerUserIntent.putExtra(EXTRA_USER_MOBILE, userMobile);
                getActivity().startService(registerUserIntent);

                cleanup();
            }
        });
    }

    private void cleanup() {
        mUserNameTextView.getText().clear();
        mUserAadhaarTextView.getText().clear();
        mUserVpaTextView.getText().clear();
        mUserMobileTextView.getText().clear();
        mUserTypeRadioGroup.clearCheck();
        mRegisterButton.setEnabled(true);
        mUserTypeRadioGroup.check(R.id.regacc_type_inv);

        Snackbar.make(mMainCoordinatorLayout, "Registration request sent", Snackbar.LENGTH_LONG).show();
    }
    private boolean validateName() {
        if(mUserNameTextView.getText() == null || "".equals(mUserNameTextView.getText()) || String.valueOf(mUserNameTextView.getText()).length() < 4) {
            mUserNameTextView.setError("Name should be atleast 4 characters");
            return false;
        } else {
            return true;
        }
    }
    private boolean validateAadharId() {
        if(mUserAadhaarTextView.getText() == null || "".equals(mUserAadhaarTextView.getText()) || String.valueOf(mUserAadhaarTextView.getText()).length() < 16) {
            mUserAadhaarTextView.setError("Please enter a valid Aadhar number");
            return false;
        } else {
            return true;
        }
    }
    private boolean validateVpa() {
        if (mUserVpaTextView.getText() != null && !"".equals(String.valueOf(mUserVpaTextView.getText())) && !String.valueOf(mUserVpaTextView.getText()).contains("@")) {
            mUserVpaTextView.setError("Please enter correct VPA");
            return false;
        } else {
            return true;
        }
    }
    private boolean validateMobile() {
        if(mUserMobileTextView.getText() == null || "".equals(mUserMobileTextView.getText()) || String.valueOf(mUserMobileTextView.getText()).length() < 10) {
            mUserMobileTextView.setError("Please enter a valid mobile number");
            return false;
        } else {
            return true;
        }
    }

    private String generateNewWallet() {
        return VALID_ETHEREUM_ADDRS[new Random().nextInt(VALID_ETHEREUM_ADDRS.length)];
    }

}
