package com.rupiee.android.investor;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.activeandroid.query.Select;
import com.rupiee.android.RuipeeApplication;
import com.rupiee.android.model.RupieeToken;
import com.rupiee.android.utils.PreferenceManager;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_AMOUNT;
import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_DIGEST64;
import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_SIGNBYTES;
import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_UPIID;
import static com.rupiee.android.utils.Constants.EXTRA_UPI_RESP;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_TXUPLOAD;

/**
 * Created by ketan on 10/10/17.
 */

public class UploadTxService extends IntentService {
    private static final String TAG = UploadTxService.class.getSimpleName();

    LocalBroadcastManager mTxUploadedBroadcaster;
    PreferenceManager mPreferenceManager;

    public UploadTxService() {
        super("Web3 Upload Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String address = "0xe4f3fa7723e95237d4837ff9b47735502bd07d5a";

        String upiResp = intent.getStringExtra(EXTRA_UPI_RESP);
        String digest64 = intent.getStringExtra(EXTRA_UPIRESP_DIGEST64);
        byte[] sign = intent.getByteArrayExtra(EXTRA_UPIRESP_SIGNBYTES);
        String upiId = intent.getStringExtra(EXTRA_UPIRESP_UPIID);
        int amount = intent.getIntExtra(EXTRA_UPIRESP_AMOUNT, 0);

        try {
            RupieeToken rupieeToken = ((RuipeeApplication) getApplication()).getRupieeToken();

            TransactionReceipt transactionReceipt = rupieeToken.mint(new Address(address), new Utf8String(upiResp), new Utf8String(digest64), new DynamicBytes(sign), new Utf8String(upiId), new Uint256(new BigInteger(String.valueOf(amount)))).get();

            String txHash = transactionReceipt.getTransactionHash();
            List<org.web3j.protocol.core.methods.response.Log> logs = transactionReceipt.getLogs();

            TxEntry txEntry = new Select()
                    .from(TxEntry.class)
                    .where("UPI_ID = ?", upiId)
                    .executeSingle();
            txEntry.setBcId(txHash);
            if(logs == null || logs.isEmpty()) {
                txEntry.setStatus(TxEntry.TxStatus.ERRORED);
            } else {
                txEntry.setStatus(TxEntry.TxStatus.UPLOADED);
            }
            txEntry.save();

            Intent txUploadNotifyIntent = new Intent(INTENT_WEB3_TXUPLOAD);
            mTxUploadedBroadcaster.sendBroadcast(txUploadNotifyIntent);

        } catch (IOException e) {
            Log.e(TAG, "run: ", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTxUploadedBroadcaster = LocalBroadcastManager.getInstance(this);
        mPreferenceManager = PreferenceManager.getInstance(getApplicationContext());
    }
}
