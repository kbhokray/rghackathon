package com.rupiee.android.investor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupiee.android.R;
import com.rupiee.android.utils.identicon.SymmetricIdenticon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ketan on 7/3/17.
 */

public class TxAdapter extends RecyclerView.Adapter<TxAdapter.TxViewHolder> {


    public static class TxViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tx_amount) TextView mTxAmount;
        @BindView(R.id.tx_id) TextView mUpiId;
        @BindView(R.id.tx_stat) ImageView mTxStatus;
        @BindView(R.id.tx_bcid) SymmetricIdenticon mBcIdImageView;

        public TxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public TxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_txstatus, parent, false);
        TxViewHolder recipientViewHolder = new TxViewHolder(view);
        return recipientViewHolder;
    }

    private List<TxEntry> mTxList = new ArrayList<>();

    @Override
    public void onBindViewHolder(TxViewHolder holder, int position) {
        TxEntry txEntry = mTxList.get(position);
        holder.mTxAmount.setText("₹" + txEntry.getAmount());
        holder.mUpiId.setText(txEntry.getUpiId());
        String bcId = txEntry.getBcId() != null ? txEntry.getBcId() : "-";
        holder.mBcIdImageView.show(bcId);
        if(txEntry.getStatus() != null) {
            switch (txEntry.getStatus()) {
                case UPLOADED:
                    holder.mTxStatus.setImageResource(R.drawable.ic_success);
                    break;
                case PAID:
                    holder.mTxStatus.setImageResource(R.drawable.ic_waiting);
                    break;
                case FAILED:
                case ERRORED:
                    holder.mTxStatus.setImageResource(R.drawable.ic_error);
                    break;
            }
        }
        return;
    }

    public void clearView() {
        int size = mTxList.size();
        for(int i = 0; i < size; i++) {
            deleteItem(0);
        }
    }

    public void addItem(TxEntry entry, int index) {
        mTxList.add(entry);
        notifyItemInserted(index);
    }

    public TxEntry getItem(int index) {
        return mTxList.get(index);
    }

    public void deleteItem(int index) {
        mTxList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mTxList == null ? 0 : mTxList.size();
    }
}