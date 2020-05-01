package com.smartloan.smtrick.smart_loan.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.InvoiceAdapterLayoutBinding;
import com.smartloan.smtrick.smart_loan.models.Invoice;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.holders.InvoiceViewHolder;

import java.util.ArrayList;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceViewHolder> {

    private ArrayList<Invoice> invoiceArrayList;
    private Context context;

    public InvoiceAdapter(Context context, ArrayList<Invoice> data) {
        this.invoiceArrayList = data;
        this.context = context;
    }

    @Override
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        InvoiceAdapterLayoutBinding invoiceAdapterLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.invoice_adapter_layout, parent, false);
        return new InvoiceViewHolder(invoiceAdapterLayoutBinding);
    }

    public Invoice getModel(int position) {
        return (invoiceArrayList.get(invoiceArrayList.size() - 1 - position));
    }

    @Override
    public void onBindViewHolder(final InvoiceViewHolder holder, final int listPosition) {
        try {
            Invoice invoice = getModel(listPosition);
            holder.invoiceAdapterLayoutBinding.txtidvalue.setText(invoice.getInvoiceNumber());
            holder.invoiceAdapterLayoutBinding.txtcnamevalue.setText(invoice.getCustomerName());
            holder.invoiceAdapterLayoutBinding.txtbankvalue.setText(invoice.getBankName());
            holder.invoiceAdapterLayoutBinding.txtStatusValue.setText(invoice.getStatus());
            holder.invoiceAdapterLayoutBinding.txtcommisionvalue.setText(invoice.getPayoutPayableAfterTdsAmount());

            if (invoice.getStatus().equalsIgnoreCase(Constant.STATUS_APPROVED) && invoice.getInvoiceApprovedDateTimeLong() != null) {
                holder.invoiceAdapterLayoutBinding.txtDaysRemaining.setVisibility(View.VISIBLE);
                holder.invoiceAdapterLayoutBinding.txtDaysRemainingValue.setVisibility(View.VISIBLE);
                long daysRemaining = Utility.daysDifference(invoice.getInvoiceApprovedDateTimeLong());
                int colorId;
                if (daysRemaining >= 0)
                    if (daysRemaining == 0) {
                        colorId = R.color.yello;
                        holder.invoiceAdapterLayoutBinding.txtDaysRemainingValue.setText(getMessage(R.string.last_day));
                    } else {
                        colorId = R.color.green;
                        holder.invoiceAdapterLayoutBinding.txtDaysRemainingValue.setText(daysRemaining + getMessage(R.string.days));
                    }
                else {
                    colorId = R.color.red;
                    holder.invoiceAdapterLayoutBinding.txtDaysRemainingValue.setText(getMessage(R.string.overdue));
                }
                setViewColor(holder.invoiceAdapterLayoutBinding.txtDaysRemainingValue, colorId);
            } else {
                holder.invoiceAdapterLayoutBinding.txtDaysRemaining.setVisibility(View.GONE);
                holder.invoiceAdapterLayoutBinding.txtDaysRemainingValue.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMessage(int id) {
        return context.getString(id);
    }

    private void setViewColor(TextView inputTextView, int colorId) {
        //From API 23, getResources().getColor() is deprecated
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            inputTextView.setTextColor(ContextCompat.getColor(context, colorId));
        } else {
            inputTextView.setTextColor(context.getResources().getColor(colorId));
        }
    }


    @Override
    public int getItemCount() {
        return invoiceArrayList.size();
    }

    public void reload(ArrayList<Invoice> arrayList) {
        invoiceArrayList.clear();
        invoiceArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    private void startCountDown(final InvoiceViewHolder holder, final long timeLimit) {
        new CountDownTimer(timeLimit, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long remainingTime = timeLimit - millisUntilFinished;
                holder.invoiceAdapterLayoutBinding.txtDaysRemainingValue.setText(String.valueOf(remainingTime));
            }

            @Override
            public void onFinish() {
                //counttime.setText("Finished");
            }
        }.start();
    }
}