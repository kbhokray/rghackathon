package com.rupiee.android.mfi.user;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ketan on 13/10/17.
 */

@Table(name = "USER")
public class User extends Model {

    @Column(name = "ADDRESS", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String address;
    @Column(name = "NAME")
    private String name;
    @Column(name = "AADHAAR")
    private String aadhaar;
    @Column(name = "VPA")
    private String vpa;
    @Column(name = "MOBILE")
    private long mobile;
    @Column(name = "TYPE")
    private int type;
    @Column(name = "BALANCE")
    private long balance;
    @Column(name = "TX_ID")
    private String txId;
    @Column(name = "TX_STATUS")
    private UserTxStatus txStatus;
    @Column(name = "CREATED")
    private long created;

    public enum UserTxStatus {
        CREATED,
        UPLOADED,
        ERRORED
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public UserTxStatus getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(UserTxStatus txStatus) {
        this.txStatus = txStatus;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
