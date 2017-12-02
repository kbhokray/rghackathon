package com.rupiee.android.mfi.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rupiee.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ketan on 7/3/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {


    public static class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_name) TextView mNameTextView;
        @BindView(R.id.user_type) TextView mTypeTextView;
        @BindView(R.id.user_stat) ImageView mStatusTextView;
        @BindView(R.id.user_textdrawable) ImageView mLetterImageView;
        @BindView(R.id.user_created) TextView mCreatedDateTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View gView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        UserViewHolder recipientViewHolder = new UserViewHolder(gView);
        return recipientViewHolder;
    }

    private List<User> mUserList = new ArrayList<>();

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.mNameTextView.setText(user.getName());
        String userType = "";
        switch(user.getType()) {
            case 0:
                userType = "Investor";
                break;
            case 1:
                userType = "Borrower";
                break;
        }
        holder.mTypeTextView.setText(userType);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        String firstLetter = user.getName().substring(0,1).toUpperCase();
        int color = generator.getColor(firstLetter);
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
        holder.mLetterImageView.setImageDrawable(drawable);

        String dateString = new SimpleDateFormat("dd MMM yyyy").format(user.getCreated()).toUpperCase();
        holder.mCreatedDateTextView.setText(dateString);

        if(user.getTxStatus() != null) {
            switch (user.getTxStatus()) {
                case UPLOADED:
                    holder.mStatusTextView.setImageResource(R.drawable.ic_success);
                    break;
                case CREATED:
                    holder.mStatusTextView.setImageResource(R.drawable.ic_waiting);
                    break;
                case ERRORED:
                    holder.mStatusTextView.setImageResource(R.drawable.ic_error);
                    break;
            }
        }

        return;
    }

    public void clearView() {
        int size = mUserList.size();
        for(int i = 0; i < size; i++) {
            deleteItem(0);
        }
    }

    public void addItem(User user, int index) {
        mUserList.add(user);
        notifyItemInserted(index);
    }

    public User getItem(int index) {
        return mUserList.get(index);
    }

    public void deleteItem(int index) {
        mUserList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }
}