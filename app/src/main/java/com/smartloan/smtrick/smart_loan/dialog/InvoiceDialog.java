package com.smartloan.smtrick.smart_loan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.DialogCallBack;
import com.smartloan.smtrick.smart_loan.databinding.InvoiceDetailsDialogLayoutBinding;
import com.smartloan.smtrick.smart_loan.databinding.InvoicedialogBinding;
import com.smartloan.smtrick.smart_loan.models.Invoice;
import com.smartloan.smtrick.smart_loan.utilities.Utility;


public class InvoiceDialog {

    public static void showInvoiceDetailDialog(Context context, Invoice invoice, final DialogCallBack dialogCallBack) {
        InvoicedialogBinding invoicedialogBinding;
        final Dialog dialog = new Dialog(context);
        invoicedialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.invoicedialog, null, false);
        dialog.setContentView(invoicedialogBinding.getRoot());
        dialog.setTitle("Title...");
        invoicedialogBinding.txtidvalue.setText(invoice.getLeedId());
        invoicedialogBinding.txtcnamevalue.setText(invoice.getCustomerName());
        invoicedialogBinding.txtbankvalue.setText(invoice.getBankName());
        invoicedialogBinding.txtleadidvalue.setText(invoice.getLeedNumber());
        invoicedialogBinding.txtcommisionvalue.setText(invoice.getCommisionwithtdsAmount());
        invoicedialogBinding.txtloanammountvalue.setText(invoice.getTotalPaymentAmount());
        invoicedialogBinding.txtloantypevalue.setText(invoice.getLoanType());
        invoicedialogBinding.txtloandissammount.setText(invoice.getLoandisbussedamount());
        invoicedialogBinding.txtgstvalue.setText(invoice.getGst());
        invoicedialogBinding.textInvoiceNumber.setText(invoice.getInvoiceNumber());
        //invoicedialogBinding.txtidvalue.setText(Utility.convertMilliSecondsToFormatedDate(invoice.getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));
        invoicedialogBinding.dialogButtonaccept.setVisibility(View.VISIBLE);
        invoicedialogBinding.dialogButtonreject.setVisibility(View.VISIBLE);
        invoicedialogBinding.edittextresone.setVisibility(View.VISIBLE);
        invoicedialogBinding.dialogButtonaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogCallBack.onOk("OK");
            }
        });
        invoicedialogBinding.dialogButtonreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogCallBack.onCancel("CANCEL");
            }
        });
        dialog.show();
    }

    public static void showPayoutDialog(final Context context, Invoice invoice, final DialogCallBack dialogCallBack) {
        final InvoiceDetailsDialogLayoutBinding invoiceDetailsDialogLayoutBinding;
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        invoiceDetailsDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.invoice_details_dialog_layout, null, false);
        dialog.setContentView(invoiceDetailsDialogLayoutBinding.getRoot());
        if (invoice != null) {
            if (!Utility.isEmptyOrNull(invoice.getInvoiceNumber()))
                invoiceDetailsDialogLayoutBinding.textLeadNumber.setText(invoice.getInvoiceNumber());
            if (!Utility.isEmptyOrNull(invoice.getCustomerName()))
                invoiceDetailsDialogLayoutBinding.txtClientNameValue.setText(invoice.getCustomerName());
            if (!Utility.isEmptyOrNull(invoice.getBalancePayout()))
                invoiceDetailsDialogLayoutBinding.txtBalancePayout.setText(invoice.getBalancePayout());
            if (!Utility.isEmptyOrNull(invoice.getInvoiceNumber()))
                invoiceDetailsDialogLayoutBinding.txtInvoiceNo.setText(invoice.getInvoiceNumber());
            if (!Utility.isEmptyOrNull(invoice.getDisbussmentDate()))
                invoiceDetailsDialogLayoutBinding.txtDisbursementDate.setText(invoice.getDisbussmentDate());
            if (!Utility.isEmptyOrNull(invoice.getLoanType()))
                invoiceDetailsDialogLayoutBinding.txtLoanType.setText(invoice.getLoanType());
            if (!Utility.isEmptyOrNull(invoice.getLoanapprovedaamount()))
                invoiceDetailsDialogLayoutBinding.txtLoanApproved.setText(invoice.getLoanapprovedaamount());
            if (!Utility.isEmptyOrNull(invoice.getTotalpayoutamount()))
                invoiceDetailsDialogLayoutBinding.txtTotalPayoutAmount.setText(invoice.getTotalpayoutamount());
            if (!Utility.isEmptyOrNull(invoice.getLoandisbussedamount()))
                invoiceDetailsDialogLayoutBinding.txtDisbursementAmount.setText(invoice.getLoandisbussedamount());
            if (!Utility.isEmptyOrNull(invoice.getPendingdisbussedamount()))
                invoiceDetailsDialogLayoutBinding.txtBalanceDisbursementAmount.setText(invoice.getPendingdisbussedamount());
            if (!Utility.isEmptyOrNull(invoice.getPayOutOnDisbursementAmount()))
                invoiceDetailsDialogLayoutBinding.txtPayoutOnDisbursementAmount.setText(invoice.getPayOutOnDisbursementAmount());
            if (!Utility.isEmptyOrNull(invoice.getTdsAmount()))
                invoiceDetailsDialogLayoutBinding.txtTdsOnPayoutAmount.setText(invoice.getTdsAmount());
            if (!Utility.isEmptyOrNull(invoice.getLastPayoutPaidAmount()))
                invoiceDetailsDialogLayoutBinding.txtLastPayoutPaidAmount.setText(invoice.getLastPayoutPaidAmount());
            if (!Utility.isEmptyOrNull(invoice.getLastPayoutPaidDate()))
                invoiceDetailsDialogLayoutBinding.txtLastPayoutDate.setText(invoice.getLastPayoutPaidDate());
            if (!Utility.isEmptyOrNull(invoice.getBalancePayoutWithTdsAmount()))
                invoiceDetailsDialogLayoutBinding.txtBalancePayoutIncludingTdsAmount.setText(invoice.getBalancePayoutWithTdsAmount());
            if (!Utility.isEmptyOrNull(invoice.getPayoutPayableAfterTdsAmount()))
                invoiceDetailsDialogLayoutBinding.txtPayoutPayableAfterTdsAmount.setText(invoice.getPayoutPayableAfterTdsAmount());
            //invoiceDetailsDialogLayoutBinding.txtidvalue.setText(Utility.convertMilliSecondsToFormatedDate(invoice.getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));
        }

        invoiceDetailsDialogLayoutBinding.etRejectReason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == invoiceDetailsDialogLayoutBinding.etRejectReason.getId()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        invoiceDetailsDialogLayoutBinding.txtYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogCallBack.onOk("YES");
            }
        });
        invoiceDetailsDialogLayoutBinding.txtNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isEmptyOrNull(invoiceDetailsDialogLayoutBinding.etRejectReason.getText().toString().trim())) {
                    Utility.showLongMessage(context, "Please enter the rejection description");
                    invoiceDetailsDialogLayoutBinding.etRejectReason.requestFocus();
                } else {
                    dialogCallBack.onCancel(invoiceDetailsDialogLayoutBinding.etRejectReason.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}
