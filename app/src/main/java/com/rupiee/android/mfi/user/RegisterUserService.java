package com.rupiee.android.mfi.user;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.activeandroid.query.Select;
import com.rupiee.android.RuipeeApplication;
import com.rupiee.android.model.RupieeToken;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint40;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.rupiee.android.utils.Constants.EXTRA_USER_AADHAAR;
import static com.rupiee.android.utils.Constants.EXTRA_USER_ADDRESS;
import static com.rupiee.android.utils.Constants.EXTRA_USER_MOBILE;
import static com.rupiee.android.utils.Constants.EXTRA_USER_NAME;
import static com.rupiee.android.utils.Constants.EXTRA_USER_TYPE;
import static com.rupiee.android.utils.Constants.EXTRA_USER_VPA;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_USERREGISTER;

/**
 * Created by ketan on 10/10/17.
 */

public class RegisterUserService extends IntentService {
    private static final String TAG = RegisterUserService.class.getSimpleName();

    LocalBroadcastManager mUserRegisteredBroadcaster;

    public RegisterUserService() {
        super("Register User Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String userName = intent.getStringExtra(EXTRA_USER_NAME);
        String userAddress = intent.getStringExtra(EXTRA_USER_ADDRESS);
        String userAadhaar = intent.getStringExtra(EXTRA_USER_AADHAAR);
        String userVpa = intent.getStringExtra(EXTRA_USER_VPA);
        int userType = intent.getIntExtra(EXTRA_USER_TYPE, 0);
        long userMobile = intent.getLongExtra(EXTRA_USER_MOBILE, 0);

        try {
            RupieeToken rupieeToken = ((RuipeeApplication) getApplication()).getRupieeToken();

            TransactionReceipt transactionReceipt = rupieeToken.registerAccount(new Address(userAddress), new Uint8(userType), new Utf8String(userAadhaar), new Utf8String(userName), new Utf8String(userVpa), new Uint40(userMobile)).get();
            String txHash = transactionReceipt.getTransactionHash();
            List<org.web3j.protocol.core.methods.response.Log> logs = transactionReceipt.getLogs();

            User user = new Select()
                    .from(User.class)
                    .where("ADDRESS = ?", userAddress)
                    .executeSingle();

            user.setTxId(txHash);
            if(logs == null || logs.isEmpty()) {
                user.setTxStatus(User.UserTxStatus.ERRORED);
            } else {
                user.setTxStatus(User.UserTxStatus.UPLOADED);
            }
            user.save();

            Intent txUploadNotifyIntent = new Intent(INTENT_WEB3_USERREGISTER);
            mUserRegisteredBroadcaster.sendBroadcast(txUploadNotifyIntent);

//            Web3j web3j = ((RuipeeApplication) getApplication()).getWeb3j();
//            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
//            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
//            Log.d(TAG, "clientVersion: " + clientVersion);
//            new DynamicBytes(new byte[]{});
//            TransactionReceipt transactionReceipt = rupieeToken.addAccount(new Address("0xe4f3fa7723e95237d4837ff9b47735502bd07d5a"), new Uint8(0), new Utf8String("aadhaar"), new Utf8String("name"), new Utf8String("kb@upi"), new Uint40(new BigInteger("9619588877"))).get();
//            String txHash = transactionReceipt.getTransactionHash();
//            Log.d(TAG, "txHash: " + txHash);
        } catch (IOException e) {
            Log.e(TAG, "run: ", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mUserRegisteredBroadcaster = LocalBroadcastManager.getInstance(this);
    }
}
