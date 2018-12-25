package com.smartloan.smtrick.smart_loan.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.foldingcell.FoldingCell;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.holders.FoldingLeedsViewHolder;

import java.util.ArrayList;
import java.util.HashSet;

import static com.smartloan.smtrick.smart_loan.constants.Constant.DAY_DATE_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_DATE_FORMATE;

public class FoldingLeedsRecycleAdapter extends RecyclerView.Adapter<FoldingLeedsViewHolder> {
    private ArrayList<LeedsModel> leedsModelList;
    private Context context;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public FoldingLeedsRecycleAdapter(Context context, ArrayList<LeedsModel> data) {
        this.leedsModelList = data;
        this.context = context;
    }

    @Override
    public FoldingLeedsViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        LayoutInflater vi = LayoutInflater.from(parent.getContext());
        FoldingCell cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
        return new FoldingLeedsViewHolder(cell);
    }

    private LeedsModel getModel(int position) {
        return (leedsModelList.get(leedsModelList.size() - 1 - position));
    }

    @Override
    public void onBindViewHolder( FoldingLeedsViewHolder viewHolder, final int listPosition) {
        try {
            LeedsModel leedsModel = getModel(listPosition);
            if (unfoldedIndexes.contains(listPosition)) {
                viewHolder.cell.unfold(true);
            } else {
                viewHolder.cell.fold(true);
            }
            viewHolder = (FoldingLeedsViewHolder) viewHolder.cell.getTag();
            if (!Utility.isEmptyOrNull(leedsModel.getExpectedLoanAmount()))
                viewHolder.textViewAppliedLoanValue.setText(leedsModel.getExpectedLoanAmount());
            else
                viewHolder.textViewAppliedLoanValue.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedsModel.getApprovedLoan())) {
                viewHolder.textviewApprovedValue.setText(leedsModel.getApprovedLoan());
                viewHolder.txtapprovedloanvalue.setText(leedsModel.getApprovedLoan());
            } else {
                viewHolder.textviewApprovedValue.setText(getString(R.string.na));
                viewHolder.txtapprovedloanvalue.setText(getString(R.string.na));
            }
            if (!Utility.isEmptyOrNull(leedsModel.getCustomerName())) {
                viewHolder.textViewCustomerName.setText(leedsModel.getCustomerName());
                viewHolder.textviewDetailCustomerName.setText(leedsModel.getCustomerName());
            } else {
                viewHolder.textViewCustomerName.setText(getString(R.string.na));
                viewHolder.textviewDetailCustomerName.setText(getString(R.string.na));
            }
            if (!Utility.isEmptyOrNull(leedsModel.getStatus()))
                viewHolder.textViewStatusValue.setText(leedsModel.getStatus());
            else
                viewHolder.textViewStatusValue.setText(getString(R.string.na));
            if (!Utility.isEmptyOrNull(leedsModel.getLoanType())) {
                viewHolder.textloantype.setText(leedsModel.getLoanType());
                viewHolder.textviewDetailLoanTypeStatus.setText(leedsModel.getLoanType());
                viewHolder.txtDetailloantypevalue.setText(leedsModel.getLoanType());
            } else {
                viewHolder.textloantype.setText(getString(R.string.na));
                viewHolder.textviewDetailLoanTypeStatus.setText(getString(R.string.na));
                viewHolder.txtDetailloantypevalue.setText(getString(R.string.na));
            }
            if (!Utility.isEmptyOrNull(leedsModel.getBankName())) {
                viewHolder.textbankname.setText(leedsModel.getBankName());
                viewHolder.txtDetailbankvalue.setText(leedsModel.getBankName());
            } else {
                viewHolder.textbankname.setText(getString(R.string.na));
                viewHolder.txtDetailbankvalue.setText(getString(R.string.na));
            }
            if (!Utility.isEmptyOrNull(leedsModel.getPayout())) {
                viewHolder.payoutammount.setText(leedsModel.getPayout());
                viewHolder.txtDetailpayoutvalue.setText(leedsModel.getPayout());
            } else {
                viewHolder.payoutammount.setText(getString(R.string.na));
                viewHolder.txtDetailpayoutvalue.setText(getString(R.string.na));
            }
            if (!Utility.isEmptyOrNull(leedsModel.getMobileNumber()))
                viewHolder.textviewDetailMobilleNumberValue.setText(leedsModel.getMobileNumber());
            else
                viewHolder.textviewDetailMobilleNumberValue.setText(getString(R.string.na));

            if (!Utility.isEmptyOrNull(leedsModel.getEmail()))
                viewHolder.textviewDetailEmailidValue.setText(leedsModel.getEmail());
            else
                viewHolder.textviewDetailEmailidValue.setText(getString(R.string.na));

            if (!Utility.isEmptyOrNull(leedsModel.getAddress()))
                viewHolder.txtinfoaddress.setText(leedsModel.getAddress());
            else
                viewHolder.txtinfoaddress.setText(getString(R.string.na));

            if (!Utility.isEmptyOrNull(leedsModel.getAgentName()))
                viewHolder.txtagentvalue.setText(leedsModel.getAgentName());
            else
                viewHolder.txtagentvalue.setText(getString(R.string.na));

            if (leedsModel.getRequestBtnClickListener() != null) {
                viewHolder.contentRequestBtn.setOnClickListener(leedsModel.getRequestBtnClickListener());
            } else {
                viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
            }

            if (leedsModel.getCreatedDateTimeLong() > 0) {
                viewHolder.textviewDateLabel.setText(Utility.convertMilliSecondsToFormatedDate(leedsModel.getCreatedDateTimeLong(), DAY_DATE_FORMATE));
                viewHolder.textviewTimeValue.setText(Utility.convertMilliSecondsToFormatedDate(leedsModel.getCreatedDateTimeLong(), LEED_DATE_FORMATE));
            } else {
                viewHolder.textviewDateLabel.setText(getString(R.string.na));
                viewHolder.textviewTimeValue.setText(getString(R.string.na));
            }
            viewHolder.rlDetailHeaderLayout.setBackgroundColor(leedsModel.getColorCode());
            viewHolder.baseLeftLayout.setBackgroundColor(leedsModel.getColorCode());
            viewHolder.contentRequestBtn.setBackgroundColor(leedsModel.getColorCode());
            if (leedsModel.getShowColor() != null && leedsModel.getShowColor()) {
                viewHolder.topLeftLayout.setBackgroundColor(leedsModel.getColorCode());
                viewHolder.textViewAppliedLoan.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.textviewApproved.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.textviewDateLabel.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.textViewAppliedLoanValue.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.textviewApprovedValue.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.textviewTimeValue.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                viewHolder.topLeftLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.textViewAppliedLoan.setTextColor(context.getResources().getColor(R.color.darkgraycolor));
                viewHolder.textviewApproved.setTextColor(context.getResources().getColor(R.color.darkgraycolor));
                viewHolder.textviewDateLabel.setTextColor(context.getResources().getColor(R.color.darkgraycolor));
                viewHolder.textViewAppliedLoanValue.setTextColor(context.getResources().getColor(R.color.darkgraycolor));
                viewHolder.textviewApprovedValue.setTextColor(context.getResources().getColor(R.color.darkgraycolor));
                viewHolder.textviewTimeValue.setTextColor(context.getResources().getColor(R.color.darkgraycolor));
            }
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    @Override
    public int getItemCount() {
        return leedsModelList.size();
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position)) {
            registerFold(position);
        } else {
            changeSeenStatus();
            getModel(position).setShowColor(true);
            notifyDataSetChanged();
            registerUnfold(position);
        }
    }

    private void changeSeenStatus() {
        for (LeedsModel leedsModel : leedsModelList) {
            leedsModel.setShowColor(false);
        }
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }


    private String getString(int id) {
        return context.getString(id);
    }

    public void reload(ArrayList<LeedsModel> arrayList) {
        leedsModelList.clear();
        leedsModelList.addAll(arrayList);
        notifyDataSetChanged();
    }
}