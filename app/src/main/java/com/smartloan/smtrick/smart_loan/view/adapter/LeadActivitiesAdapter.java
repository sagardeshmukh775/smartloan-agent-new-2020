package com.smartloan.smtrick.smart_loan.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.LeadActivitieAdapterBinding;
import com.smartloan.smtrick.smart_loan.models.LeadActivitiesModel;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.holders.LeadActivitiesViewHolder;

import java.util.ArrayList;

import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_CALL;
import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_COORDINATOR;
import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_EMAIL;
import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_GENERATED;
import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_SALES_PERSON;
import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_TELECALLER;
import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_VERIFIED;
import static com.smartloan.smtrick.smart_loan.constants.Constant.ACTIVITY_WHATS_UP;

public class LeadActivitiesAdapter extends RecyclerView.Adapter<LeadActivitiesViewHolder> {

    private ArrayList<LeadActivitiesModel> leadActivitiesModels;
    private Context context;

    public LeadActivitiesAdapter(Context context, ArrayList<LeadActivitiesModel> data) {
        this.leadActivitiesModels = data;
        this.context = context;
    }

    @Override
    public LeadActivitiesViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        LeadActivitieAdapterBinding leadActivitieAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.lead_activitie_adapter, parent, false);
        return new LeadActivitiesViewHolder(leadActivitieAdapterBinding);
    }

    private LeadActivitiesModel getModel(int position) {
        return (leadActivitiesModels.get(position));
    }

    @Override
    public void onBindViewHolder(final LeadActivitiesViewHolder holder, final int listPosition) {
        try {
            LeadActivitiesModel leadActivitiesModel = getModel(listPosition);
            if (listPosition == leadActivitiesModels.size() - 1) {
                holder.leadActivitieAdapterBinding.ivStart.setVisibility(View.VISIBLE);
                holder.leadActivitieAdapterBinding.ivContinue.setVisibility(View.GONE);
            } else {
                holder.leadActivitieAdapterBinding.ivStart.setVisibility(View.GONE);
                holder.leadActivitieAdapterBinding.ivContinue.setVisibility(View.VISIBLE);
            }
            if (!Utility.isEmptyOrNull(leadActivitiesModel.getActivityDoneByName()))
                holder.leadActivitieAdapterBinding.txtDoneByName.setText("By  " + leadActivitiesModel.getActivityDoneByName());
            else
                holder.leadActivitieAdapterBinding.txtDoneByName.setText("By  " +
                        getMessage(R.string.na));
            if (!Utility.isEmptyOrNull(leadActivitiesModel.getActivityTitle()))
                holder.leadActivitieAdapterBinding.txtActivityTitle.setText(leadActivitiesModel.getActivityTitle());
            else
                holder.leadActivitieAdapterBinding.txtActivityTitle.setText(getMessage(R.string.na));
            if (!Utility.isEmptyOrNull(leadActivitiesModel.getActivityNote()))
                holder.leadActivitieAdapterBinding.txtActivityNote.setText(leadActivitiesModel.getActivityNote());
            else
                holder.leadActivitieAdapterBinding.txtActivityNote.setText(getMessage(R.string.na));
            if (leadActivitiesModel.getCreatedDateTimeLong() != null && leadActivitiesModel.getCreatedDateTimeLong() > 0)
                holder.leadActivitieAdapterBinding.txtDateTime.setText(Utility.convertMilliSecondsToFormatedDate(leadActivitiesModel.getCreatedDateTimeLong(), Constant.GLOBAL_DATE_FORMATE));
            else
                holder.leadActivitieAdapterBinding.txtDateTime.setText(getMessage(R.string.na));
            if (!Utility.isEmptyOrNull(leadActivitiesModel.getActivityType())) {
                int icon = R.drawable.clock_outline;
                switch (leadActivitiesModel.getActivityType()) {
                    case ACTIVITY_EMAIL:
                        icon = R.drawable.emai_with_color;
                        break;
                    case ACTIVITY_CALL:
                        icon = R.drawable.phone_classic;
                        break;
                    case ACTIVITY_GENERATED:
                        icon = R.drawable.clock_start;
                        break;
                    case ACTIVITY_WHATS_UP:
                        icon = R.drawable.chat_outline;
                        break;
                    case ACTIVITY_TELECALLER:
                        icon = R.drawable.card_account_phone;
                        break;
                    case ACTIVITY_COORDINATOR:
                        icon = R.drawable.account_tie;
                        break;
                    case ACTIVITY_SALES_PERSON:
                        icon = R.drawable.account_hard_hat;
                        break;
                    case ACTIVITY_VERIFIED:
                        icon = R.drawable.shield_check;
                        break;
                }
                holder.leadActivitieAdapterBinding.ivActivityType.setImageResource(icon);
            } else {
                holder.leadActivitieAdapterBinding.ivActivityType.setImageResource(R.drawable.clock_outline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return leadActivitiesModels.size();
    }

    private String getMessage(int id) {
        return context.getString(id);
    }

    public void reload(ArrayList<LeadActivitiesModel> arrayList) {
        leadActivitiesModels.clear();
        leadActivitiesModels.addAll(arrayList);
        notifyDataSetChanged();
    }
}