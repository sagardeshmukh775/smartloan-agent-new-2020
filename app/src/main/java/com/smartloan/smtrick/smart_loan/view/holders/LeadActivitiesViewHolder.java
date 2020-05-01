package com.smartloan.smtrick.smart_loan.view.holders;

import android.support.v7.widget.RecyclerView;

import com.smartloan.smtrick.smart_loan.databinding.LeadActivitieAdapterBinding;

public class LeadActivitiesViewHolder extends RecyclerView.ViewHolder {
    public LeadActivitieAdapterBinding leadActivitieAdapterBinding;

    public LeadActivitiesViewHolder(LeadActivitieAdapterBinding leadActivitieAdapterBinding) {
        super(leadActivitieAdapterBinding.getRoot());
        this.leadActivitieAdapterBinding = leadActivitieAdapterBinding;
    }
}
