package com.rupiee.android.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupiee.android.R;
import com.rupiee.android.model.HomeEntryVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ketan on 7/4/17.
 */

public class HomeEntryAdapter extends RecyclerView.Adapter<HomeEntryAdapter.HomeEntryViewHolder> {

    private HomeFragment mHomeFragment;

    public HomeEntryAdapter(HomeFragment homeFragment) {
        mHomeFragment = homeFragment;
    }

    public static class HomeEntryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.homeentry_image) ImageView mHomeEntryImageView;
        @BindView(R.id.homeentry_category) TextView mCategoryTextView;
        @BindView(R.id.homeentry_expln) TextView mExplanationTextView;

        public HomeEntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public HomeEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home, parent, false);
        view.setOnClickListener(mHomeFragment);
        HomeEntryViewHolder heViewHolder = new HomeEntryViewHolder(view);
        return heViewHolder;
    }

    List<HomeEntryVo> mHomeEntryList = new ArrayList<>();
    @Override
    public void onBindViewHolder(HomeEntryViewHolder holder, int position) {
        HomeEntryVo homeEntry = mHomeEntryList.get(position);

        int image = homeEntry.getImage();
        String category = homeEntry.getCategory();
        String expln = homeEntry.getExpln();

        holder.mHomeEntryImageView.setImageResource(image);
        holder.mCategoryTextView.setText(category);
        holder.mExplanationTextView.setText(expln);

        return;
    }

    public void clearView() {
        int size = mHomeEntryList.size();
        for(int i = 0; i < size; i++) {
            deleteItem(0);
        }
    }

    public void addItem(HomeEntryVo homeEntry, int index) {
        mHomeEntryList.add(homeEntry);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mHomeEntryList.remove(index);
        notifyItemRemoved(index);
    }

    public HomeEntryVo getItem(int index) {
        return mHomeEntryList.get(index);
    }

    @Override
    public int getItemCount() {
        return mHomeEntryList == null ? 0 : mHomeEntryList.size();
    }

}
