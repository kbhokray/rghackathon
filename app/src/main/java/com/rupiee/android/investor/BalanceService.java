package com.rupiee.android.investor;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rupiee.android.RuipeeApplication;
import com.rupiee.android.model.RupieeToken;
import com.rupiee.android.utils.PreferenceManager;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.rupiee.android.utils.Constants.INTENT_WEB3_BALANCE;
import static com.rupiee.android.utils.Constants.PREF_WEB3_BALANCE;

/**
 * Created by ketan on 10/10/17.
 */

public class BalanceService extends IntentService {
    private static final String TAG = BalanceService.class.getSimpleName();

    LocalBroadcastManager mBalChngNotifyBroadcaster;
    PreferenceManager mPreferenceManager;

    public BalanceService() {
        super("Web3 Balance Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String address = "0xe4f3fa7723e95237d4837ff9b47735502bd07d5a";

        try {

            RupieeToken rupieeToken = ((RuipeeApplication) getApplication()).getRupieeToken();
            Uint256 balance = rupieeToken.balanceOf(new Address(address)).get();
            String balanceStr = balance.getValue().toString();

            mPreferenceManager.putString(PREF_WEB3_BALANCE, balanceStr);
            Intent balChngNotifyIntent = new Intent(INTENT_WEB3_BALANCE);
            mBalChngNotifyBroadcaster.sendBroadcast(balChngNotifyIntent);

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
        mBalChngNotifyBroadcaster = LocalBroadcastManager.getInstance(this);
        mPreferenceManager = PreferenceManager.getInstance(getApplicationContext());
    }
}
