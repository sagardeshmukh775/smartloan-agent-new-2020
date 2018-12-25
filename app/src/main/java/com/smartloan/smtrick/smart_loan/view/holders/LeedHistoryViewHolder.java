package com.smartloan.smtrick.smart_loan.view.holders;

import android.support.v7.widget.RecyclerView;

import com.smartloan.smtrick.smart_loan.databinding.HistoryAdapterLayoutBinding;

public class LeedHistoryViewHolder extends RecyclerView.ViewHolder {
    public HistoryAdapterLayoutBinding historyAdapterLayoutBinding;

    public LeedHistoryViewHolder(HistoryAdapterLayoutBinding historyAdapterLayoutBinding) {
        super(historyAdapterLayoutBinding.getRoot());
        this.historyAdapterLayoutBinding = historyAdapterLayoutBinding;
    }
}
