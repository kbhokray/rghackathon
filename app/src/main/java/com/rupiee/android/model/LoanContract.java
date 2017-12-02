package com.rupiee.android.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.3.1.
 */
public final class LoanContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b604051602080610a2d8339810160405280805160008054600160a060020a03338116600160a060020a031992831681178316179092556007805492909316911617905550506109ca806100636000396000f3006060604052361561008b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416635628d03e81146100905780636faea806146100ba578063704eeb5d146100ba578063897ab6ee1461011f5780638da5cb5b14610147578063c988179114610176578063e0ab311a1461021b578063f2fde38b14610244575b600080fd5b341561009b57600080fd5b6100a3610263565b60405161ffff909116815260200160405180910390f35b34156100c557600080fd5b61010b60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061029295505050505050565b604051901515815260200160405180910390f35b341561012a57600080fd5b61014561ffff60043516600160a060020a0360243516610298565b005b341561015257600080fd5b61015a610374565b604051600160a060020a03909116815260200160405180910390f35b341561018157600080fd5b61010b6004803561ffff16906024803560ff16919060649060443590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284375094965061038395505050505050565b341561022657600080fd5b61014561ffff6004351663ffffffff6024358116906044351661073f565b341561024f57600080fd5b610145600160a060020a03600435166107f6565b6000805433600160a060020a0390811691161461027f57600080fd5b5060015460a060020a900461ffff165b90565b50600190565b61ffff80831660009081526006602052604090208054909160a060020a9091041615156102c457600080fd5b805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03838116919091178083556007547f1a26249163ed5273c3e172b8e49c0824978c71cd661e5786a23dce5221494587928592911690760100000000000000000000000000000000000000000000900463ffffffff16604051600160a060020a03938416815291909216602082015263ffffffff9091166040808301919091526060909101905180910390a1505050565b600054600160a060020a031681565b600080600260ff8616111561039757600080fd5b6103a084610292565b506103aa83610292565b5060ff851615156103bd575060016103e1565b60ff8516600114156103d1575060026103e1565b60ff8516600214156103e1575060035b60e06040519081016040908152600080835261ffff8916602084015263ffffffff84169183019190915260608201819052608082015260a0810185905260c0810184905260018151815473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03919091161781556020820151815461ffff9190911660a060020a0275ffff0000000000000000000000000000000000000000199091161781556040820151815463ffffffff919091167601000000000000000000000000000000000000000000000279ffffffff00000000000000000000000000000000000000000000199091161781556060820151815463ffffffff9190911660d060020a027fffff00000000ffffffffffffffffffffffffffffffffffffffffffffffffffff909116178155608082015160028201805460ff19166001838181111561052757fe5b021790555060a082015181600301908051610546929160200190610891565b5060c082015181600401908051610561929160200190610891565b50505061ffff8681166000908152600660205260409020600180548254600160a060020a0390911673ffffffffffffffffffffffffffffffffffffffff1990911617808355815475ffff00000000000000000000000000000000000000001990911660a060020a918290049094160292909217808255825479ffffffff00000000000000000000000000000000000000000000199091167601000000000000000000000000000000000000000000009182900463ffffffff9081169092021780835583547fffff00000000ffffffffffffffffffffffffffffffffffffffffffffffffffff90911660d060020a918290049092160217815560035460028201805460ff9092169160ff191684838181111561067857fe5b0217905550600382018160030190805460018160011615610100020316600290046106a492919061090f565b50600482018160040190805460018160011615610100020316600290046106cc92919061090f565b50506007547f3366c511f3434019713550cc49460c1e8972a13cf9bb4063d5ddc60fc1e400ae91508790600160a060020a03168760405161ffff9093168352600160a060020a03909116602083015260ff166040808301919091526060909101905180910390a150600195945050505050565b6000805433600160a060020a0390811691161461075b57600080fd5b5061ffff831660009081526006602052604090206001600282015460ff16600181111561078457fe5b141561078f57600080fd5b80547fffff00000000ffffffffffffffffffffffffffffffffffffffffffffffffffff811660d060020a9182900463ffffffff908116850181169092021782559283166000908152600190910160205260409020805463ffffffff19169190921617905550565b60005433600160a060020a0390811691161461081157600080fd5b600160a060020a038116151561082657600080fd5b600054600160a060020a0380831691167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a36000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106108d257805160ff19168380011785556108ff565b828001600101855582156108ff579182015b828111156108ff5782518255916020019190600101906108e4565b5061090b929150610984565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061094857805485556108ff565b828001600101855582156108ff57600052602060002091601f016020900482015b828111156108ff578254825591600101919060010190610969565b61028f91905b8082111561090b576000815560010161098a5600a165627a7a72305820af87c3d9b88c7bace10b0edad91765aee92f08eed4a14ee616b9671813c348d50029";

    private LoanContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private LoanContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<LoanCreationEventResponse> getLoanCreationEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanCreation", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}, new TypeReference<Address>() {}, new TypeReference<Uint8>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanCreationEventResponse> responses = new ArrayList<LoanCreationEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanCreationEventResponse typedResponse = new LoanCreationEventResponse();
            typedResponse.loanId = (Uint16) eventValues.getNonIndexedValues().get(0);
            typedResponse.memberAddr = (Address) eventValues.getNonIndexedValues().get(1);
            typedResponse.loanProduct = (Uint8) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanCreationEventResponse> loanCreationEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanCreation", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}, new TypeReference<Address>() {}, new TypeReference<Uint8>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanCreationEventResponse>() {
            @Override
            public LoanCreationEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanCreationEventResponse typedResponse = new LoanCreationEventResponse();
                typedResponse.loanId = (Uint16) eventValues.getNonIndexedValues().get(0);
                typedResponse.memberAddr = (Address) eventValues.getNonIndexedValues().get(1);
                typedResponse.loanProduct = (Uint8) eventValues.getNonIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public List<LoanDisbusementEventResponse> getLoanDisbusementEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanDisbusement", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint32>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanDisbusementEventResponse> responses = new ArrayList<LoanDisbusementEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanDisbusementEventResponse typedResponse = new LoanDisbusementEventResponse();
            typedResponse.from = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.to = (Address) eventValues.getNonIndexedValues().get(1);
            typedResponse.amount = (Uint32) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanDisbusementEventResponse> loanDisbusementEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanDisbusement", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint32>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanDisbusementEventResponse>() {
            @Override
            public LoanDisbusementEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanDisbusementEventResponse typedResponse = new LoanDisbusementEventResponse();
                typedResponse.from = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse.to = (Address) eventValues.getNonIndexedValues().get(1);
                typedResponse.amount = (Uint32) eventValues.getNonIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnershipTransferred", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnershipTransferred", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Future<Uint16> getCurrentLoanId() {
        Function function = new Function("getCurrentLoanId", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Bool> verifyBplAttestation(Utf8String bplAttestation) {
        Function function = new Function("verifyBplAttestation", 
                Arrays.<Type>asList(bplAttestation), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Bool> verifyCsAttestation(Utf8String csAttestation) {
        Function function = new Function("verifyCsAttestation", 
                Arrays.<Type>asList(csAttestation), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> addLoanDisbursed(Uint16 loanId, Address from) {
        Function function = new Function("addLoanDisbursed", Arrays.<Type>asList(loanId, from), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Address> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> createLoan(Uint16 loanId, Uint8 loanProduct, Utf8String csAttestation, Utf8String bplAttestation) {
        Function function = new Function("createLoan", Arrays.<Type>asList(loanId, loanProduct, csAttestation, bplAttestation), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> repayInstallment(Uint16 loanId, Uint32 ts, Uint32 amount) {
        Function function = new Function("repayInstallment", Arrays.<Type>asList(loanId, ts, amount), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> transferOwnership(Address newOwner) {
        Function function = new Function("transferOwnership", Arrays.<Type>asList(newOwner), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<LoanContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Address _memberAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_memberAddr));
        return deployAsync(LoanContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static Future<LoanContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Address _memberAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_memberAddr));
        return deployAsync(LoanContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static LoanContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LoanContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static LoanContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LoanContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class LoanCreationEventResponse {
        public Uint16 loanId;

        public Address memberAddr;

        public Uint8 loanProduct;
    }

    public static class LoanDisbusementEventResponse {
        public Address from;

        public Address to;

        public Uint32 amount;
    }

    public static class OwnershipTransferredEventResponse {
        public Address previousOwner;

        public Address newOwner;
    }
}
