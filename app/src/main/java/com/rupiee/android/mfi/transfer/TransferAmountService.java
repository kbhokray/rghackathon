package com.rupiee.android.mfi.transfer;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.rupiee.android.RuipeeApplication;
import com.rupiee.android.model.RupieeToken;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.rupiee.android.utils.Constants.EXTRA_DISBURSE_AMOUNT;
import static com.rupiee.android.utils.Constants.EXTRA_DISBURSE_FROM;
import static com.rupiee.android.utils.Constants.EXTRA_DISBURSE_TO;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_AMOUNTTRANSFERED;

/**
 * Created by ketan on 10/10/17.
 */

public class TransferAmountService extends IntentService {
    private static final String TAG = TransferAmountService.class.getSimpleName();

    LocalBroadcastManager mUserRegisteredBroadcaster;

    public TransferAmountService() {
        super("Loan Disbursal Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String toAddress = intent.getStringExtra(EXTRA_DISBURSE_TO);
        String fromAddress = intent.getStringExtra(EXTRA_DISBURSE_FROM);
        int amount = intent.getIntExtra(EXTRA_DISBURSE_AMOUNT, 0);

        try {
            RupieeToken rupieeToken = ((RuipeeApplication) getApplication()).getRupieeToken();
            TransactionReceipt transactionReceipt = rupieeToken.transferFrom(new Address(fromAddress), new Address(toAddress), new Uint256(amount)).get();

            String txHash = transactionReceipt.getTransactionHash();
            List<Log> logs = transactionReceipt.getLogs();

            if(logs == null || logs.isEmpty()) {
            } else {
            }

            Intent txUploadNotifyIntent = new Intent(INTENT_WEB3_AMOUNTTRANSFERED);
            mUserRegisteredBroadcaster.sendBroadcast(txUploadNotifyIntent);
        } catch (IOException e) {
            e.printStackTrace();
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
