package com.smartloan.smtrick.smart_loan.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.HistoryAdapterLayoutBinding;
import com.smartloan.smtrick.smart_loan.models.History;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.holders.LeedHistoryViewHolder;

import java.util.ArrayList;

public class LeedHistoryAdapter extends RecyclerView.Adapter<LeedHistoryViewHolder> {

    private ArrayList<History> historyArrayList;
    private Context context;

    public LeedHistoryAdapter(Context context, ArrayList<History> data) {
        this.historyArrayList = data;
        this.context = context;
    }

    @Override
    public LeedHistoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        HistoryAdapterLayoutBinding historyAdapterLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.history_adapter_layout, parent, false);
        return new LeedHistoryViewHolder(historyAdapterLayoutBinding);
    }

    private History getModel(int position) {
        return (historyArrayList.get(position));
    }

    @Override
    public void onBindViewHolder(final LeedHistoryViewHolder holder, final int listPosition) {
        try {
            History invoice = getModel(listPosition);
            if (!Utility.isEmptyOrNull(invoice.getUpdatedbyId()))
                holder.historyAdapterLayoutBinding.txtIdValue.setText(invoice.getUpdatedbyId());
            else
                holder.historyAdapterLayoutBinding.txtIdValue.setText(getMessage(R.string.na));
            if (!Utility.isEmptyOrNull(invoice.getUpdatedByName()))
                holder.historyAdapterLayoutBinding.txtNameValue.setText(invoice.getUpdatedByName());
            else
                holder.historyAdapterLayoutBinding.txtNameValue.setText(getMessage(R.string.na));
            if (!Utility.isEmptyOrNull(invoice.getStatus()))
                holder.historyAdapterLayoutBinding.txtStatusValue.setText(invoice.getStatus());
            else
                holder.historyAdapterLayoutBinding.txtStatusValue.setText(getMessage(R.string.na));
            if (invoice.getUpdatedDateTimeLong() != null && invoice.getUpdatedDateTimeLong() > 0)
                holder.historyAdapterLayoutBinding.txtDateTimeValue.setText(Utility.convertMilliSecondsToFormatedDate(invoice.getUpdatedDateTimeLong(), Constant.GLOBAL_DATE_FORMATE));
            else
                holder.historyAdapterLayoutBinding.txtDateTimeValue.setText(getMessage(R.string.na));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    private String getMessage(int id) {
        return context.getString(id);
    }

    public void reload(ArrayList<History> arrayList) {
        historyArrayList.clear();
        historyArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }
}