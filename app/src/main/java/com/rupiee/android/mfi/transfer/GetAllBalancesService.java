package com.rupiee.android.mfi.transfer;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.activeandroid.query.Select;
import com.rupiee.android.RuipeeApplication;
import com.rupiee.android.mfi.user.User;
import com.rupiee.android.model.RupieeToken;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.rupiee.android.utils.Constants.INTENT_WEB3_ALLBALANCEUPDATE;
import static com.rupiee.android.utils.Constants.INTENT_WEB3_BALANCEUPDATE;

/**
 * Created by ketan on 10/10/17.
 */

public class GetAllBalancesService extends IntentService {
    private static final String TAG = GetAllBalancesService.class.getSimpleName();

    LocalBroadcastManager mWeb3Broadcaster;

    public GetAllBalancesService() {
        super("Get all balances");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            RupieeToken rupieeToken = ((RuipeeApplication) getApplication()).getRupieeToken();
            List<User> investors = new Select().from(User.class).execute();
            for (User investor : investors) {
                Uint256 balance = rupieeToken.balanceOf(new Address(investor.getAddress())).get();
                investor.setBalance(balance.getValue().longValue());
                investor.save();
            }

            Intent balUpdateNotifyIntent = new Intent(INTENT_WEB3_ALLBALANCEUPDATE);
            mWeb3Broadcaster.sendBroadcast(balUpdateNotifyIntent);
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
        mWeb3Broadcaster = LocalBroadcastManager.getInstance(this);
    }
}
