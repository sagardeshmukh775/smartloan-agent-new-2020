package com.smartloan.smtrick.smart_loan.view.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;


public class LeedsViewHolder extends RecyclerView.ViewHolder {
    public TextView txtLeedId;
    public TextView txtCustomerName;
    public TextView txtBank;
    public TextView txtStatus;
    public TextView txtDate;
    public TextView txtAgentID;
    public TextView txtTotalAmount;
    public TextView txtLoanType;

    public LeedsViewHolder(View itemView) {
        super(itemView);
        try {
            txtLeedId = (TextView) itemView.findViewById(R.id.txt_id_value);
            txtCustomerName = (TextView) itemView.findViewById(R.id.txtcnamevalue);
            txtBank = (TextView) itemView.findViewById(R.id.txt_bank_value);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txt_amount_value);
            txtAgentID = (TextView) itemView.findViewById(R.id.txt_agent_value);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date_value);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status_value);
            txtLoanType = (TextView) itemView.findViewById(R.id.txt_loan_type_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
