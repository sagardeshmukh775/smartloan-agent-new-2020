package com.smartloan.smtrick.smart_loan.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.activite.LeadActivitiesActivity;
import com.smartloan.smtrick.smart_loan.view.activite.LeedHistoryActivity;
import com.smartloan.smtrick.smart_loan.view.activite.MainActivity;
import com.smartloan.smtrick.smart_loan.view.activite.UpdateLeedActivity;
import com.smartloan.smtrick.smart_loan.view.activite.UpdateProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

import static com.smartloan.smtrick.smart_loan.constants.Constant.DAY_DATE_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_DATE_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_ID;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_MODEL;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_NUMBER;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_APPROVED;
import static com.smartloan.smtrick.smart_loan.constants.Constant.SUPPORT_PHONE_NUMBER;

@SuppressWarnings({"WeakerAccess", "unused"})
public class LeedsAdapter extends ArrayAdapter<LeedsModel> implements Filterable {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    Context context;
    private ArrayList<LeedsModel> leedsModelList;
    private LeedFilter leedFilter;
    private ArrayList<LeedsModel> filterLeedsList;
    private AppSharedPreference appSharedPreference;
    private boolean isKycDetailsFilled;

    public LeedsAdapter(Context context, ArrayList<LeedsModel> leedsModelList) {
        super(context, 0, leedsModelList);
        this.context = context;
        this.filterLeedsList = leedsModelList;
        this.leedsModelList = leedsModelList;
        appSharedPreference = new AppSharedPreference(context);
        isKycDetailsFilled = isKYCFilled();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (leedFilter == null) {
            leedFilter = new LeedFilter();
        }
        return leedFilter;
    }

    private class LeedFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<LeedsModel> tempList = new ArrayList();
                // search content in customer list
                for (LeedsModel leedsModel : filterLeedsList) {
                    if (leedsModel.getCustomerName().toLowerCase().contains(constraint.toString().toLowerCase())
                            || (leedsModel.getMobileNumber() != null && leedsModel.getMobileNumber().contains(constraint.toString().toLowerCase()))
                            || (leedsModel.getAlternetMobileNumber() != null && leedsModel.getAlternetMobileNumber().contains(constraint.toString().toLowerCase()))) {
                        tempList.add(leedsModel);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = filterLeedsList.size();
                filterResults.values = filterLeedsList;
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            leedsModelList = (ArrayList<LeedsModel>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (leedsModelList != null)
            return leedsModelList.size();
        else return 0;
    }

    private LeedsModel getModel(int position) {
        return leedsModelList.get(getCount() - 1 - position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final LeedsModel leedsModel = getModel(position);
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            viewHolder.textViewAppliedLoan = cell.findViewById(R.id.text_view_applied_loan);
            viewHolder.textViewAppliedLoanValue = cell.findViewById(R.id.text_view_applied_loan_value);
            viewHolder.textviewApproved = cell.findViewById(R.id.textview_approved);
            viewHolder.textviewApprovedValue = cell.findViewById(R.id.textview_approved_value);
            viewHolder.textviewDateLabel = cell.findViewById(R.id.textview_date_label);
            viewHolder.textviewTimeValue = cell.findViewById(R.id.textview_time_value);
            viewHolder.textViewCustomerName = cell.findViewById(R.id.text_view_customer_name);
            viewHolder.textViewStatusValue = cell.findViewById(R.id.text_view_status_value);
            viewHolder.textloantype = cell.findViewById(R.id.textloantype);
            viewHolder.textbankname = cell.findViewById(R.id.textbankname);
            viewHolder.payoutammount = cell.findViewById(R.id.payoutammount);
            viewHolder.topLeftLayout = cell.findViewById(R.id.topLeftLayout);
            viewHolder.baseLeftLayout = cell.findViewById(R.id.baseLeftLayout);
            viewHolder.ivProfile = cell.findViewById(R.id.imageview_avatar);
            viewHolder.title_from_to_dots = cell.findViewById(R.id.title_from_to_dots);
            //detail views
            viewHolder.rlDetailHeaderLayout = cell.findViewById(R.id.rlDetailHeaderLayout);
            viewHolder.textviewDetailCustomerName = cell.findViewById(R.id.textview_customer_name);
            viewHolder.textviewDetailLoanTypeStatus = cell.findViewById(R.id.textview_loan_type);
            viewHolder.textviewDetailMobilleNumberValue = cell.findViewById(R.id.textview_mobille_number_value);
            viewHolder.textviewDetailEmailidValue = cell.findViewById(R.id.textview_emailid_value);
            viewHolder.txtinfoaddress = cell.findViewById(R.id.txtinfoaddress);
            viewHolder.txtDetailloantypevalue = cell.findViewById(R.id.txtloantypevalue);
            viewHolder.txtDetailbankvalue = cell.findViewById(R.id.txtbankvalue);
            viewHolder.txtDetailpayoutvalue = cell.findViewById(R.id.txtpayoutvalue);
//            viewHolder.txtagentvalue = cell.findViewById(R.id.txtagentvalue);
            viewHolder.txtapprovedloanvalue = cell.findViewById(R.id.txtapprovedloanvalue);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.title_request);
            viewHolder.tvLeadActivity = cell.findViewById(R.id.tvLeadActivity);
            viewHolder.llUpdateLayout = cell.findViewById(R.id.ll_update_layout);
            viewHolder.llHistoryLayout = cell.findViewById(R.id.ll_history_layout);
            viewHolder.ll_client_call_layout = cell.findViewById(R.id.ll_client_call_layout);
            viewHolder.txtBalanceTransferTypeValue = cell.findViewById(R.id.txt_balance_transfer_type_value);
            viewHolder.llRequestForPayout = cell.findViewById(R.id.ll_request_for_payout);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }
        if (null == leedsModel)
            return cell;
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
            viewHolder.textViewCustomerName.setText(Utility.capitalize(leedsModel.getCustomerName()));
            viewHolder.textviewDetailCustomerName.setText(Utility.capitalize(leedsModel.getCustomerName()));
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
            viewHolder.textviewDetailLoanTypeStatus.setText(getLoanType(leedsModel.getLoanType()));
            viewHolder.txtDetailloantypevalue.setText(getLoanType(leedsModel.getLoanType()));
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
        if (!Utility.isEmptyOrNull(leedsModel.getAlternetMobileNumber()))
            viewHolder.textviewDetailMobilleNumberValue.setText(leedsModel.getMobileNumber() + ", " + leedsModel.getAlternetMobileNumber());
        if (!Utility.isEmptyOrNull(leedsModel.getEmail()))
            viewHolder.textviewDetailEmailidValue.setText(leedsModel.getEmail());
        else
            viewHolder.textviewDetailEmailidValue.setText(getString(R.string.na));
        if (!Utility.isEmptyOrNull(leedsModel.getAddress()))
            viewHolder.txtinfoaddress.setText(leedsModel.getAddress());
        else
            viewHolder.txtinfoaddress.setText(getString(R.string.na));
        if (!Utility.isEmptyOrNull(leedsModel.getBalanceTransferLoanType()))
            viewHolder.txtBalanceTransferTypeValue.setText(leedsModel.getBalanceTransferLoanType());
        else
            viewHolder.txtBalanceTransferTypeValue.setText(getString(R.string.na));
//        if (!Utility.isEmptyOrNull(leedsModel.getAgentName()))
//            viewHolder.txtagentvalue.setText(leedsModel.getAgentName());
//        else
//            viewHolder.txtagentvalue.setText(getString(R.string.na));
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
        if (!Utility.isEmptyOrNull(leedsModel.getCustomerImagelarge())) {
            Picasso.with(context).load(leedsModel.getCustomerImagelarge()).resize(200, 200).centerCrop().placeholder(R.drawable.dummy_user_profile).into(viewHolder.ivProfile);
        } else
            viewHolder.ivProfile.setImageResource(R.drawable.dummy_user_profile);
        viewHolder.rlDetailHeaderLayout.setBackgroundColor(leedsModel.getColorCode());
        viewHolder.baseLeftLayout.setBackgroundColor(leedsModel.getColorCode());
        viewHolder.contentRequestBtn.setBackgroundColor(leedsModel.getColorCode());
        viewHolder.tvLeadActivity.setBackgroundColor(leedsModel.getColorCode());
        if (leedsModel.getColorCode() == context.getResources().getColor(R.color.hederbackground))
            viewHolder.title_from_to_dots.setImageResource(R.drawable.from_to_purple);
        else if (leedsModel.getColorCode() == context.getResources().getColor(R.color.yello))
            viewHolder.title_from_to_dots.setImageResource(R.drawable.from_to_yellow);
        else if (leedsModel.getColorCode() == context.getResources().getColor(R.color.red))
            viewHolder.title_from_to_dots.setImageResource(R.drawable.from_to_red);
        else if (leedsModel.getColorCode() == context.getResources().getColor(R.color.blue))
            viewHolder.title_from_to_dots.setImageResource(R.drawable.from_to_blue);
        else if (leedsModel.getColorCode() == context.getResources().getColor(R.color.green))
            viewHolder.title_from_to_dots.setImageResource(R.drawable.from_to_green);
        if (leedsModel.getShowColor() != null && leedsModel.getShowColor()) {
            viewHolder.topLeftLayout.setBackgroundColor(leedsModel.getColorCode());
            viewHolder.textViewAppliedLoan.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textviewApproved.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textviewDateLabel.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textViewAppliedLoanValue.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textviewApprovedValue.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textviewTimeValue.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.topLeftLayout.setBackgroundColor(context.getResources().getColor(R.color.black_color));
            viewHolder.textViewAppliedLoan.setTextColor(context.getResources().getColor(R.color.light_white));
            viewHolder.textviewApproved.setTextColor(context.getResources().getColor(R.color.light_white));
            viewHolder.textviewDateLabel.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textViewAppliedLoanValue.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textviewApprovedValue.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.textviewTimeValue.setTextColor(context.getResources().getColor(R.color.white));
        }
        if (leedsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
            viewHolder.contentRequestBtn.setVisibility(View.VISIBLE);
        } else {
            viewHolder.contentRequestBtn.setVisibility(View.GONE);
        }
        viewHolder.llUpdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateLeedActivity.class);
                intent.putExtra(LEED_MODEL, leedsModel);
                context.startActivity(intent);
            }
        });
        viewHolder.llHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber(SUPPORT_PHONE_NUMBER);
            }
        });
        viewHolder.ll_client_call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber(leedsModel.getMobileNumber());
            }
        });
        viewHolder.contentRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isKycDetailsFilled) {
                    showAlertDialog();
                }
            }
        });
        viewHolder.tvLeadActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LeadActivitiesActivity.class);
                intent.putExtra(LEED_ID, leedsModel.getLeedId());
                intent.putExtra(LEED_NUMBER, leedsModel.getLeedNumber());
                context.startActivity(intent);
            }
        });
        return cell;
    }

    private void callNumber(String number) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)));
    }

    private boolean isKYCFilled() {
        boolean isFilled = false;
        if (!appSharedPreference.getAccountNumber().isEmpty()
                && !appSharedPreference.getAccountHolderName().isEmpty()
                && !appSharedPreference.getBankName().isEmpty()
                && !appSharedPreference.getBranchName().isEmpty()
                && !appSharedPreference.getIfsc().isEmpty())
            isFilled = true;
        return isFilled;
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.fill_kyc_details))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok_message), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(context, UpdateProfileActivity.class));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String getLoanType(String type) {
        switch (type) {
            case "HL":
                return "Home Loan";
            case "LAP":
                return "Loan Against Property";
            default:
                return type;
        }
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

    public void reload(ArrayList<LeedsModel> leedsModels) {
        filterLeedsList = leedsModels;
        if (getFilter() != null)
            getFilter().filter(MainActivity.searchText);
    }

    private String getString(int id) {
        return context.getString(id);
    }

    public void updateKYCDetailsValue() {
        isKycDetailsFilled = isKYCFilled();
    }

    // View lookup cache
    private static class ViewHolder {
        TextView textViewAppliedLoan;
        TextView textViewAppliedLoanValue;
        TextView contentRequestBtn;
        TextView tvLeadActivity;
        TextView textviewApproved;
        TextView textviewApprovedValue;

        TextView textviewDateLabel;
        TextView textviewTimeValue;
        TextView textViewCustomerName;
        TextView textViewStatus;
        TextView textViewStatusValue;
        TextView textloantypetxt;
        TextView textloantype;
        TextView textbanknametxt;
        TextView textbankname;
        TextView layoutstickerbottom;
        TextView textpayouttxt;
        TextView payoutammount;

        TextView contentAvatarTitle;
        TextView textviewDetailCustomerName;
        TextView textviewDetailAppliedText;
        TextView textviewDetailLoanType;
        TextView content_from_badge;
        TextView textviewDetailMobilleNumber;
        TextView textviewDetailMobilleNumberValue;
        TextView textviewDetailEmailid;
        TextView textviewDetailEmailidValue;
        TextView textDetailaddress;
        TextView txtinfoaddress;
        TextView txtDetailloantype;
        TextView txtDetailloantypevalue;
        TextView txtDetailbank;
        TextView txtDetailbankvalue;
        TextView txDetailtpayout;
        TextView txtDetailpayoutvalue;
        TextView txtagent;
//        TextView txtagentvalue;
        TextView txtapprovedloan;
        TextView txtapprovedloanvalue;
        TextView title_request;
        TextView textviewDetailLoanTypeStatus;
        TextView txtBalanceTransferTypeValue;
        ImageView imageviewAvatar;
        ImageView title_from_to_dots;
        LinearLayout topLeftLayout;
        LinearLayout baseLeftLayout;
        LinearLayout llRequestForPayout;
        RelativeLayout rlDetailHeaderLayout;
        ImageView ivProfile;
        public LinearLayout llUpdateLayout;
        public LinearLayout llHistoryLayout;
        public LinearLayout ll_client_call_layout;
    }
}
