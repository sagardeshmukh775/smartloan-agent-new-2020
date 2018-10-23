package com.smartloan.smtrick.smart_loan.view.holders;

import android.support.v7.widget.RecyclerView;

import com.smartloan.smtrick.smart_loan.databinding.InvoiceAdapterLayoutBinding;

public class InvoiceViewHolder extends RecyclerView.ViewHolder {
    public InvoiceAdapterLayoutBinding invoiceAdapterLayoutBinding;

    public InvoiceViewHolder(InvoiceAdapterLayoutBinding invoiceAdapterLayoutBinding) {
        super(invoiceAdapterLayoutBinding.getRoot());
        this.invoiceAdapterLayoutBinding = invoiceAdapterLayoutBinding;
    }
}
