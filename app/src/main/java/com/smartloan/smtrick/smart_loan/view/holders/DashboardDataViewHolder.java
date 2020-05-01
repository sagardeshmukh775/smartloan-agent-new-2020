package com.smartloan.smtrick.smart_loan.view.holders;

import android.support.v7.widget.RecyclerView;

import com.smartloan.smtrick.smart_loan.databinding.DashboardDataAdapterLayoutBinding;

public class DashboardDataViewHolder extends RecyclerView.ViewHolder {
    public DashboardDataAdapterLayoutBinding dashboardDataAdapterLayoutBinding;

    public DashboardDataViewHolder(DashboardDataAdapterLayoutBinding dashboardDataAdapterLayoutBinding) {
        super(dashboardDataAdapterLayoutBinding.getRoot());
        this.dashboardDataAdapterLayoutBinding = dashboardDataAdapterLayoutBinding;
    }
}