package com.rupiee.android.utils;

/**
 * Created by ketan on 7/3/17.
 */

public class Constants {

    public static final String SERVER_HOST = "https://192.168.43.154:3000"; // "http://52.73.223.56";
    public static final String URLCONTEXT_REGISTRATION = "/registration";

    public static final String PREF_APPLICATION_MODE = "PREF_APPLICATION_MODE";
    public static final String PREF_APPMODE_INVESTOR = "PREF_APPMODE_INVESTOR";
    public static final String PREF_APPMODE_ADMIN = "PREF_APPMODE_ADMIN";

    public static final String INTENT_UPIRESULT = "response";
    public static final String UPIRESULT_PARAM_TXNID = "txnId";
    public static final String UPIRESULT_PARAM_STATUS = "Status";
    public static final String UPIRESULT_PARAM_TXNREF = "txnRef";
    public static final String UPIRESULT_PARAM_TRTXNREF = "TrtxnRef";

    public static final String INTENT_WEB3_BALANCE = "com.rupiee.android.investor.web3.BALANCE";

    public static final String INTENT_WEB3_LOANCREATED = "com.rupiee.android.investor.web3.LOANCREATED";
    public static final String INTENT_WEB3_BALANCEUPDATE = "com.rupiee.android.investor.web3.BALANCEUPDATE";
    public static final String INTENT_WEB3_TXUPLOAD = "com.rupiee.android.investor.web3.TXUPLOAD";
    public static final String INTENT_WEB3_USERREGISTER = "com.rupiee.android.investor.web3.INTENT_WEB3_USERREGISTER";
    public static final String INTENT_WEB3_LOANDISBURSED = "com.rupiee.android.investor.web3.INTENT_WEB3_LOANDISBURSED";

    public static final String INTENT_WEB3_AMOUNTTRANSFERED = "com.rupiee.android.investor.web3.AMOUNTTRANSFERED";

    public static final String INTENT_WEB3_ALLBALANCEUPDATE = "com.rupiee.android.investor.web3.ALLBALANCEUPDATE";

    public static final String PREF_WEB3_BALANCE = "PREF_WEB3_BALANCE";

    public static final String EXTRA_UPI_RESP = "EXTRA_UPI_RESP";
    public static final String EXTRA_UPIRESP_DIGEST64 = "EXTRA_UPIRESP_DIGEST64";
    public static final String EXTRA_UPIRESP_SIGNBYTES = "EXTRA_UPIRESP_SIGNBYTES";
    public static final String EXTRA_UPIRESP_UPIID = "EXTRA_UPIRESP_UPIID";
    public static final String EXTRA_UPIRESP_AMOUNT = "EXTRA_UPIRESP_AMOUNT";

    public static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    public static final String EXTRA_USER_ADDRESS = "EXTRA_USER_ADDRESS";
    public static final String EXTRA_USER_AADHAAR = "EXTRA_USER_AADHAAR";
    public static final String EXTRA_USER_VPA = "EXTRA_USER_VPA";
    public static final String EXTRA_USER_TYPE = "EXTRA_USER_TYPE";
    public static final String EXTRA_USER_MOBILE = "EXTRA_USER_MOBILE";

    public static final String TAG_DIALOG_LOANFORM = "TAG_DIALOG_LOANFORM";

    public static final String[] VALID_ETHEREUM_ADDRS = new String[] {
            "0x11a3c99a8d4fceda24725b6e98358a7240c8aa07",
            "0xf047a9bbf48d67935b6c99500c132fb8526fafee",
            "0x9051ab8ac3fc82575a77757e752b4d9fa86a3118",
            "0x1562e01011eeea4e89be762b94ba29a766f1ffca",
            "0x6285b282dfad09ae09450d4d89f32209f5227a5c",
            "0x8d206d88ee08a6c80a46c09129b5f35f83f1d6ed",
            "0xe71c92fce1c0b8192cae792067f71c320bb4764b",
            "0xd55f756510f2cd563fa8cbff027dc189bdcd555f",
            "0xbe08f1ea9bbabd38964ac1ef01cf67317941553b",
            "0x63d78b05820fb5aa4e2eca7b96da124cd68c1fd3",
            "0x2304d5323dc399c9a5c3bcde62b92c33edf0c2f0",
            "0x295bf2bda9af1c66be1da1f0a87b69f5018f25ed",
            "0x786476bce88591df4afa81b29b8017012195f097",
            "0xf9710b3695e43d0b9d4adfa93339dcf1479f5b62",
            "0xf148e9e21b479d652dd65a446f63a72a19baed59",
            "0x8ede705127f42b45b3a19fb632d7fa832faa7071",
            "0xf5762375c2eb6e64db8deb8621e62c1380bbf346",
            "0xd0fab2741cfad3da7fea34c671507619d64b8152",
            "0x0d98f3ad61abd77a591c65f800cda9598fd37c28",
            "0xa3b372c13bffbb0a4df9566904db6a5d6f5c1682",
            "0xee6e90587804dc15ce88599ff9961e35d6c2f63a",
            "0x067841c0df4f1fbcac53c4beec38701af3f1e173",
            "0xe09b3b299a799549bb22974ae029480b30d4751e",
            "0x1436a109a596824917fe420da3e5411d39818b77",
            "0x12cc810a60b0ea15e84229108c9996513c869450",
            "0xaeb6b51bb2b9f7b4551894688b2bbacc9cc0bd64",
            "0x4d160f44226f115d0cd966b2905c4f2bb8bd29eb",
            "0xde5efea117190b6c0fa2f77c7d5c121190e6440b",
            "0x91041aeec0a40bebcdcbda6988b6d0750de40ae3",
            "0xef159c01182aa0d7af703b6726e21e871b983829"
    };

    public static final String ARGUMENT_LOANDIALOG_BORROWERNAME = "ARGUMENT_LOANDIALOG_BORROWERNAME";
    public static final String ARGUMENT_LOANDIALOG_BORROWERADDR = "ARGUMENT_LOANDIALOG_BORROWERADDR";

    public static final String ARGUMENT_DISBURSAL_ADDRESS = "ARGUMENT_DISBURSAL_ADDRESS";
    public static final String ARGUMENT_DISBURSAL_NAME = "ARGUMENT_DISBURSAL_NAME";
    public static final String ARGUMENT_DISBURSAL_LOANID = "ARGUMENT_DISBURSAL_LOANID";
    public static final String ARGUMENT_DISBURSAL_AMOUNT = "ARGUMENT_DISBURSAL_AMOUNT";

    public static final String EXTRA_LOAN_BORROWERADDRESS = "EXTRA_LOAN_BORROWERADDRESS";
    public static final String EXTRA_LOAN_PRODUCT = "EXTRA_LOAN_PRODUCT";
    public static final String EXTRA_LOAN_CSATTESTATION = "EXTRA_LOAN_CSATTESTATION";
    public static final String EXTRA_LOAN_BPLATTESTATION = "EXTRA_LOAN_BPLATTESTATION";
    public static final String EXTRA_LOAN_LOANID = "EXTRA_LOAN_LOANID";

    public static final String EXTRA_DISBURSE_TO = "EXTRA_DISBURSE_TO";
    public static final String EXTRA_DISBURSE_FROM = "EXTRA_DISBURSE_FROM";
    public static final String EXTRA_DISBURSE_LOANID = "EXTRA_DISBURSE_LOANID";
    public static final String EXTRA_DISBURSE_AMOUNT = "EXTRA_DISBURSE_AMOUNT";

    public enum  UPIRESULT_STATUS {
        SUCCESS ("SUCCESS"),
        SUBMITTED ("SUBMITTED"),
        FAILURE ("FAILURE"),
        FAILED ("FAILED");

        private String status;

        UPIRESULT_STATUS(String status) {
            this.status = status;
        }
    }

    public enum BookingStatus {
        CHECKED_IN ("CHECKED_IN"),
        CHECKED_OUT ("CHECKED_OUT"),
        REJECTED ("REJECTED");

        private String status;

        BookingStatus(String status) {
            this.status = status;
        }
    }

    public enum CheckoutTxStatus {
        INIT ("INIT"),
        PULLED ("PULLED"),
        PAID ("PAID");

        private String status;

        CheckoutTxStatus(String status) {
            this.status = status;
        }
    }

}
