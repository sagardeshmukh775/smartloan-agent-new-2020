package com.smartloan.smtrick.smart_loan.view.holders;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.smartloan.smtrick.smart_loan.R;

public class FoldingLeedsViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewAppliedLoan;
    public TextView textViewAppliedLoanValue;
    public TextView contentRequestBtn;
    public TextView textviewApproved;
    public TextView textviewApprovedValue;
    public TextView textviewDateLabel;
    public TextView textviewTimeValue;
    public TextView textViewCustomerName;
    public TextView textViewStatus;
    public TextView textViewStatusValue;
    public TextView textloantypetxt;
    public TextView textloantype;
    public TextView textbanknametxt;
    public TextView textbankname;
    public TextView layoutstickerbottom;
    public TextView textpayouttxt;
    public TextView payoutammount;
    public TextView contentAvatarTitle;
    public TextView textviewDetailCustomerName;
    public TextView textviewDetailAppliedText;
    public TextView textviewDetailLoanType;
    public TextView content_from_badge;
    public TextView textviewDetailMobilleNumber;
    public TextView textviewDetailMobilleNumberValue;
    public TextView textviewDetailEmailid;
    public TextView textviewDetailEmailidValue;
    public TextView textDetailaddress;
    public TextView txtinfoaddress;
    public TextView txtDetailloantype;
    public TextView txtDetailloantypevalue;
    public TextView txtDetailbank;
    public TextView txtDetailbankvalue;
    public TextView txDetailtpayout;
    public TextView txtDetailpayoutvalue;
    public TextView txtagent;
    public TextView txtagentvalue;
    public TextView txtapprovedloan;
    public TextView txtapprovedloanvalue;
    public TextView title_request;
    public TextView textviewDetailLoanTypeStatus;
    public ImageView imageviewAvatar;
    public LinearLayout topLeftLayout;
    public LinearLayout baseLeftLayout;
    public RelativeLayout rlDetailHeaderLayout;
    public FoldingCell cell;

    public FoldingLeedsViewHolder(FoldingCell cell) {
        super(cell);
        this.cell = cell;
        textViewAppliedLoan = cell.findViewById(R.id.text_view_applied_loan);
        textViewAppliedLoanValue = cell.findViewById(R.id.text_view_applied_loan_value);
        textviewApproved = cell.findViewById(R.id.textview_approved);
        textviewApprovedValue = cell.findViewById(R.id.textview_approved_value);
        textviewDateLabel = cell.findViewById(R.id.textview_date_label);
        textviewTimeValue = cell.findViewById(R.id.textview_time_value);
        textViewCustomerName = cell.findViewById(R.id.text_view_customer_name);
        textViewStatusValue = cell.findViewById(R.id.text_view_status_value);
        textloantype = cell.findViewById(R.id.textloantype);
        textbankname = cell.findViewById(R.id.textbankname);
        payoutammount = cell.findViewById(R.id.payoutammount);
        topLeftLayout = cell.findViewById(R.id.topLeftLayout);
        baseLeftLayout = cell.findViewById(R.id.baseLeftLayout);
        //detail views
        rlDetailHeaderLayout = cell.findViewById(R.id.rlDetailHeaderLayout);
        textviewDetailCustomerName = cell.findViewById(R.id.textview_customer_name);
        textviewDetailLoanTypeStatus = cell.findViewById(R.id.textview_loan_type);
        textviewDetailMobilleNumberValue = cell.findViewById(R.id.textview_mobille_number_value);
        textviewDetailEmailidValue = cell.findViewById(R.id.textview_emailid_value);
        txtinfoaddress = cell.findViewById(R.id.txtinfoaddress);
        txtDetailloantypevalue = cell.findViewById(R.id.txtloantypevalue);
        txtDetailbankvalue = cell.findViewById(R.id.txtbankvalue);
        txtDetailpayoutvalue = cell.findViewById(R.id.txtpayoutvalue);
//        txtagentvalue = cell.findViewById(R.id.txtagentvalue);
        txtapprovedloanvalue = cell.findViewById(R.id.txtapprovedloanvalue);
        contentRequestBtn = cell.findViewById(R.id.title_request);
        cell.setTag(this);
    }
}
