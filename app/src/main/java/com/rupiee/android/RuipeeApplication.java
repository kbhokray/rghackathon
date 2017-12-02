package com.rupiee.android;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.crashlytics.android.Crashlytics;
import com.rupiee.android.model.MemberContract;
import com.rupiee.android.model.RupieeToken;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ketan on 8/3/17.
 */

public class RuipeeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        ActiveAndroid.initialize(this);
//        initializeIon();
    }

    public RuipeeApplication() {
        super();
    }

    private static final String URL_INFURA_ROPSTEN = "https://ropsten.infura.io/tWKJEj5eN0zj6FzToTJe";
    private static final String ADDRESS_TOKEN_RUPIEE = "0x5d0ff00c01d2f706b44a148715a718747353dd4a";// "0xb902bc9e472a1db6c3fc29ad2d2ec0898e0d1ad9"; //"0xdef83f938b89568c923fe915d095f5700caceb1b"; <-- old
    private static final String ADDRESS_CONTRACT_MEMBER = "0x5575c0d14b3a5ad3db56c05fddc13f6b2413f566";
    private static final String GAS_PRICE_WEI = "100000000000";
    private static final String GAS_LIMIT = "3712388";

    private Web3j mWeb3j;
    private RupieeToken mRupieeToken;
    private MemberContract mMemberContract;

    private void setup() throws IOException {
        mWeb3j = Web3jFactory.build(new InfuraHttpService(URL_INFURA_ROPSTEN));
        Credentials credentials;

        File path = getApplicationContext().getExternalFilesDir(null);
        File walletFile = new File(path, "rupiee.wallet");
        // walletFile.getParentFile().mkdirs();
        try (InputStream in = getResources().openRawResource(R.raw.investor);
            FileOutputStream out = new FileOutputStream(walletFile)) {
            int length;
            byte[] bytes = new byte[1024];
            while ((length = in.read(bytes)) != -1) {
                out.write(bytes, 0, length);
            }
            credentials = WalletUtils.loadCredentials("Password@123", walletFile);
        } catch (IOException | CipherException e) {
            throw new IOException();
        }
        mRupieeToken = RupieeToken.load(ADDRESS_TOKEN_RUPIEE, mWeb3j, credentials, new BigInteger(GAS_PRICE_WEI), new BigInteger(GAS_LIMIT));
        mMemberContract = MemberContract.load(ADDRESS_CONTRACT_MEMBER, mWeb3j, credentials, new BigInteger(GAS_PRICE_WEI), new BigInteger(GAS_LIMIT));
    }

    public Web3j getWeb3j() throws IOException {
        if(mWeb3j == null) {
            setup();
        }
        return mWeb3j;
    }

    public RupieeToken getRupieeToken() throws IOException {
        if(mRupieeToken == null) {
           setup();
        }
        return mRupieeToken;
    }

    public MemberContract getMemberContract() throws IOException {
        if(mMemberContract == null) {
            setup();
        }
        return mMemberContract;
    }
}
