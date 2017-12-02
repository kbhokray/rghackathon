package com.rupiee.android.investor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.rupiee.android.R;
import com.rupiee.android.utils.Constants;
import com.rupiee.android.utils.PreferenceManager;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_AMOUNT;
import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_DIGEST64;
import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_SIGNBYTES;
import static com.rupiee.android.utils.Constants.EXTRA_UPIRESP_UPIID;
import static com.rupiee.android.utils.Constants.EXTRA_UPI_RESP;
import static com.rupiee.android.utils.Constants.INTENT_UPIRESULT;
import static com.rupiee.android.utils.Constants.UPIRESULT_PARAM_STATUS;
import static com.rupiee.android.utils.Constants.UPIRESULT_PARAM_TRTXNREF;
import static com.rupiee.android.utils.Constants.UPIRESULT_PARAM_TXNID;
import static com.rupiee.android.utils.Constants.UPIRESULT_PARAM_TXNREF;

/**
 * Created by ketan on 8/3/17.
 */

public class PayFragment extends Fragment {

    private static final String TAG = PayFragment.class.getSimpleName();

    public static PayFragment newInstance() {

        Bundle args = new Bundle();

        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.investor_pay) EditText mPayAmountTextView;
    @BindView(R.id.button_checkin) CircularProgressButton mCheckInButton;

    private PreferenceManager mPreferenceManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, null, false);
        ButterKnife.bind(this, view);
        mPreferenceManager = PreferenceManager.getInstance(getActivity());
        setupButton();
        return view;
    }

    private void setupButton() {
        mCheckInButton.setProgress(0);
        mCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateAmount()) {
                    return;
                }
                String amount = String.valueOf(mPayAmountTextView.getText());
                String upiUrl = generateUpiUrl(Integer.parseInt(amount));
                mCheckInButton.setEnabled(false);
                openUpiApp(upiUrl, 1);
            }
        });
    }

    private void openUpiApp(String upiUri, int index) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upiUri));
        Intent chooser = Intent.createChooser(intent, "Pay with...");
        startActivityForResult(chooser, index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data == null) {
            Toast.makeText(getContext(), "Transaction failed, please try again", Toast.LENGTH_SHORT).show();
            mCheckInButton.setEnabled(true);
            return;
        }
        Bundle resultData = data.getExtras();
        if(!resultData.containsKey(INTENT_UPIRESULT)) {
            Toast.makeText(getContext(), "Transaction failed, please try again", Toast.LENGTH_SHORT).show();
            mCheckInButton.setEnabled(true);
            return;
        }

        String uriResponseString = resultData.getString(INTENT_UPIRESULT);
        Map<String, String> resultPairs = splitQuery(uriResponseString);


        if((!resultPairs.containsKey(UPIRESULT_PARAM_TXNREF)
                || "".equals(resultPairs.get(UPIRESULT_PARAM_TXNREF))
                || "null".equals(resultPairs.get(UPIRESULT_PARAM_TXNREF))
                || "NA".equals(resultPairs.get(UPIRESULT_PARAM_TXNREF)))
                && (!resultPairs.containsKey(UPIRESULT_PARAM_TRTXNREF))) {
            Toast.makeText(getContext(), "Transaction Failed, please try again", Toast.LENGTH_SHORT).show();
            mCheckInButton.setEnabled(true);
            return;
        }

        String status = resultPairs.get(UPIRESULT_PARAM_STATUS);
        if(Constants.UPIRESULT_STATUS.FAILURE.name().equals(status)
                || Constants.UPIRESULT_STATUS.FAILED.name().equalsIgnoreCase(status)) {
            Toast.makeText(getContext(), "Transaction Failed, please try again", Toast.LENGTH_SHORT).show();
            mCheckInButton.setEnabled(true);
            return;
        }

        Toast.makeText(getContext(), "Paid!", Toast.LENGTH_SHORT).show();

        String upiId = resultPairs.get(UPIRESULT_PARAM_TXNID);
        String amountStr = String.valueOf(mPayAmountTextView.getText());
        int amount = Integer.parseInt(amountStr);

        TxEntry txEntry = new TxEntry();
        txEntry.setAmount(amount);
        txEntry.setUpiId(upiId);
        txEntry.setStatus(TxEntry.TxStatus.PAID);
        txEntry.setCreated(new Date().getTime());
        txEntry.save();

        String upiXml = null;
        try {
            upiXml = generateUpiXml(upiId, amountStr);
            Log.d(TAG, "UPI_RESP: " + upiXml);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(upiXml.getBytes(StandardCharsets.UTF_8));
            String digest64 = Base64.encodeToString(digest, Base64.DEFAULT);
            digest64 = digest64.replaceAll("\\n", "");

            Log.d(TAG, "DIGEST64: " + digest64);
            String signedInfo = generateSignedInfo(digest64);

            RSAPrivateKey privateKey = getPrivateKey();

            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privateKey);
            signer.update(signedInfo.getBytes());

            byte[] sign = signer.sign();
            String sign64 = Base64.encodeToString(sign, Base64.DEFAULT);
            Log.d(TAG, "SIGN: " + sign64);

            Intent uploadTxIntent = new Intent(getActivity(), UploadTxService.class);
            uploadTxIntent.putExtra(EXTRA_UPI_RESP, upiXml);
            uploadTxIntent.putExtra(EXTRA_UPIRESP_DIGEST64, digest64);
            uploadTxIntent.putExtra(EXTRA_UPIRESP_SIGNBYTES, sign);
            uploadTxIntent.putExtra(EXTRA_UPIRESP_UPIID, upiId);
            uploadTxIntent.putExtra(EXTRA_UPIRESP_AMOUNT, amount);
            getActivity().startService(uploadTxIntent);
        } catch (IOException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (SignatureException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidKeySpecException e) {
        }

        getFragmentManager().popBackStackImmediate();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Map<String, String> splitQuery(String result) {
        Map<String, String> queryPairs = new HashMap<>();
        String[] pairs = result.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryPairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
        return queryPairs;
    }

    private static String generateUpiXml(String txnId, String amount) throws IOException {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);
        xmlSerializer.startDocument("UTF-8", true);

        xmlSerializer.setPrefix("upi", "http://npci.org/upi/schema/");
        xmlSerializer.startTag("http://npci.org/upi/schema/", "RespPay");

        xmlSerializer.startTag("", "Head");
        xmlSerializer.attribute("", "msgId", "2");
        xmlSerializer.attribute("", "orgId", "npci");
        xmlSerializer.attribute("", "ts", "2015-01-16T14:15:47+05:30");
        xmlSerializer.attribute("", "ver", "1.0");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "Head");

        xmlSerializer.startTag("", "Txn");
        xmlSerializer.attribute("", "id", txnId);
        xmlSerializer.attribute("", "note", "Sending money for your use");
        xmlSerializer.attribute("", "ts", "2015-01-16T14:15:42+05:30");
        xmlSerializer.attribute("", "type", "PAY");

        xmlSerializer.startTag("", "RiskScores");

        xmlSerializer.startTag("", "Score");
        xmlSerializer.attribute("", "provider", "sp");
        xmlSerializer.attribute("", "type", "TXNRISK");
        xmlSerializer.attribute("" , "value", "");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "Score");

        xmlSerializer.startTag("", "Score");
        xmlSerializer.attribute("", "provider", "npci");
        xmlSerializer.attribute("", "type", "TXNRISK");
        xmlSerializer.attribute("" , "value", "");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "Score");

        xmlSerializer.endTag("", "RiskScores");
        xmlSerializer.endTag("", "Txn");

        xmlSerializer.startTag("", "Resp");

        xmlSerializer.attribute("", "approvalNum", "3MKBVB");
        xmlSerializer.attribute("", "reqMsgId", "1");
        xmlSerializer.attribute("", "result", "SUCCESS");

        xmlSerializer.startTag("", "Ref");
        xmlSerializer.attribute("", "addr", "kbhokray@upi");
        xmlSerializer.attribute("", "approvalNum", "AWHWU9");
        xmlSerializer.attribute("", "seqNum", "1");
        xmlSerializer.attribute("", "settAmount", amount);
        xmlSerializer.attribute("", "type", "PAYER");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "Ref");

        xmlSerializer.startTag("", "Ref");
        xmlSerializer.attribute("", "addr", "manager@upi");
        xmlSerializer.attribute("", "approvalNum", "ESOP61");
        xmlSerializer.attribute("", "seqNum", "2");
        xmlSerializer.attribute("", "settAmount", amount);
        xmlSerializer.attribute("", "type", "PAYEE");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "Ref");

        xmlSerializer.endTag("", "Resp");
        xmlSerializer.endTag("http://npci.org/upi/schema/", "RespPay");

        xmlSerializer.endDocument();

        return writer.toString().replaceAll("<\\?.*?\\?>", "");
    }

    private static String generateSignedInfo(String hash64) throws IOException {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);
        xmlSerializer.startDocument("UTF-8", true);

        xmlSerializer.startTag("", "SignedInfo");
        xmlSerializer.attribute("", "xmlns", "http://www.w3.org/2000/09/xmldsig#");

        xmlSerializer.startTag("", "CanonicalizationMethod");
        xmlSerializer.attribute("", "Algorithm", "http://www.w3.org/2001/10/xml-exc-c14n#");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "CanonicalizationMethod");

        xmlSerializer.startTag("", "SignatureMethod");
        xmlSerializer.attribute("", "Algorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "SignatureMethod");

        xmlSerializer.startTag("", "Reference");
        xmlSerializer.attribute("", "URI", "");

        xmlSerializer.startTag("", "Transforms");

        xmlSerializer.startTag("", "Transform");
        xmlSerializer.attribute("", "Algorithm", "http://www.w3.org/2000/09/xmldsig#enveloped-signature");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "Transform");

        xmlSerializer.startTag("", "Transform");
        xmlSerializer.attribute("", "Algorithm", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "Transform");

        xmlSerializer.endTag("", "Transforms");

        xmlSerializer.startTag("", "DigestMethod");
        xmlSerializer.attribute("", "Algorithm", "http://www.w3.org/2001/04/xmlenc#sha256");
        xmlSerializer.text("");
        xmlSerializer.endTag("", "DigestMethod");

        xmlSerializer.startTag("", "DigestValue");
        xmlSerializer.text(hash64);
        xmlSerializer.endTag("", "DigestValue");

        xmlSerializer.endTag("", "Reference");
        xmlSerializer.endTag("", "SignedInfo");

        xmlSerializer.endDocument();

        return writer.toString().replaceAll("<\\?.*?\\?>", "");
    }

    private RSAPrivateKey getPrivateKey() throws
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        InputStream in = getResources().openRawResource(R.raw.staging_privkey);
        int bytesRead = in.read(b);
        while (bytesRead != -1) {
            bos.write(b, 0, bytesRead);
            bytesRead = in.read(b);
        }
        byte[] bytes = bos.toByteArray();

        KeyFactory kf = KeyFactory.getInstance("RSA");
        KeySpec ks = new PKCS8EncodedKeySpec(bytes);
        return (RSAPrivateKey) kf.generatePrivate(ks);
    }

    private String generateUpiUrl(int amount) {
        String upiTemplate = "upi://pay?pa=%s&pn=%s&tr=1&tn=Payment&am=%d&cu=INR";
        String vpa = "9619589938@upi";
        String name = "Ketan Bhokray";
        return String.format(upiTemplate, vpa, name, amount);
    }

    private boolean validateAmount() {
        if(mPayAmountTextView.getText() == null || "".equals(String.valueOf(mPayAmountTextView.getText()))) {
            mPayAmountTextView.setError("Please enter an valid amount");
            return false;
        } else if (String.valueOf(mPayAmountTextView.getText()).length() > 2) {
            mPayAmountTextView.setError("Please commit only small amounts");
            return false;
        } else {
            return true;
        }
    }
}
