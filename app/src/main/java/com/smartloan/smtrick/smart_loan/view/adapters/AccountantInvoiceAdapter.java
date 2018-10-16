package com.smartloan.smtrick.smart_loan.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.models.Invoice;
import com.smartloan.smtrick.smart_loan.view.holders.InvoiceViewHolder;

import java.util.ArrayList;

public class AccountantInvoiceAdapter extends RecyclerView.Adapter<InvoiceViewHolder> {

    private ArrayList<Invoice> invoiceArrayList;

    public AccountantInvoiceAdapter(ArrayList<Invoice> data) {
        this.invoiceArrayList = data;
    }

    @Override
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.accountant_invoice_adapter_layout, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InvoiceViewHolder holder, final int listPosition) {
        try {
            Invoice invoice = invoiceArrayList.get(listPosition);
            holder.txtInvoiceId.setText(invoice.getInvoiceId());
            holder.txtCustomerName.setText(invoice.getCustomerName());
            holder.txtPhone.setText(invoice.getPhone());
            holder.txtStatus.setText(invoice.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return invoiceArrayList.size();
    }
}