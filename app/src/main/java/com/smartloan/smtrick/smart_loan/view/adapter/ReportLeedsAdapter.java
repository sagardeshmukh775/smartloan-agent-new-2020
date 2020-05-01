package com.smartloan.smtrick.smart_loan.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.holders.LeedsViewHolder;

import java.util.ArrayList;

import static com.smartloan.smtrick.smart_loan.constants.Constant.GLOBAL_DATE_FORMATE;

public class ReportLeedsAdapter extends RecyclerView.Adapter<LeedsViewHolder> {

    private ArrayList<LeedsModel> leedModelArrayList;
    private Context context;

    public ReportLeedsAdapter(Context context,ArrayList<LeedsModel> data) {
        this.leedModelArrayList = data;
        this.context = context;
    }

    @Override
    public LeedsViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leeds_report_adapter_layout, parent, false);
        return new LeedsViewHolder(view);
    }

    private LeedsModel getModel(int position) {
        return leedModelArrayList.get(getItemCount() - 1 - position);
    }

    @Override
    public void onBindViewHolder(final LeedsViewHolder holder, final int listPosition) {
        try {
            LeedsModel leedModel = getModel(listPosition);
            if (!Utility.isEmptyOrNull(leedModel.getLeedNumber()))
                holder.txtLeedId.setText(leedModel.getLeedNumber());
            else
                holder.txtLeedId.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedModel.getCustomerName()))
                holder.txtCustomerName.setText(leedModel.getCustomerName());
            else
                holder.txtCustomerName.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedModel.getBankName()))
                holder.txtBank.setText(leedModel.getBankName());
            else
                holder.txtBank.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedModel.getExpectedLoanAmount()))
                holder.txtTotalAmount.setText(leedModel.getExpectedLoanAmount());
            else
                holder.txtTotalAmount.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedModel.getAgentId()))
                holder.txtAgentID.setText(leedModel.getAgentId());
            else
                holder.txtAgentID.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedModel.getStatus()))
                holder.txtStatus.setText(leedModel.getStatus());
            else
                holder.txtStatus.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedModel.getLoanType()))
                holder.txtLoanType.setText(leedModel.getLoanType());
            else
                holder.txtLoanType.setText(getString(R.string.na));
            if (leedModel.getCreatedDateTimeLong() > 0)
                holder.txtDate.setText(Utility.convertMilliSecondsToFormatedDate(leedModel.getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));
            else
                holder.txtDate.setText(getString(R.string.na));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getString(int id) {
        return context.getString(id);
    }

    @Override
    public int getItemCount() {
        return leedModelArrayList.size();
    }

    public void reload(ArrayList<LeedsModel> leedsModelArrayList) {
        leedModelArrayList.clear();
        leedModelArrayList.addAll(leedsModelArrayList);
        notifyDataSetChanged();
    }
}