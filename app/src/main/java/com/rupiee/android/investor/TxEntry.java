package com.rupiee.android.investor;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ketan on 10/10/17.
 */

@Table(name = "TX_ENTRY")
public class TxEntry extends Model{

    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "UPI_ID", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String upiId;
    @Column(name = "BC_ID")
    private String bcId;
    @Column(name = "STATUS")
    private TxStatus status;
    @Column(name = "CREATED")
    private long created;


    public enum TxStatus {
        PAID,
        FAILED,
        UPLOADED,
        ERRORED
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getBcId() {
        return bcId;
    }

    public void setBcId(String bcId) {
        this.bcId = bcId;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(TxStatus status) {
        this.status = status;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
