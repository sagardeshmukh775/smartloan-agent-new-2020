package com.smartloan.smtrick.smart_loan.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.smartloan.smtrick.smart_loan.constants.Constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Invoice implements Serializable {
    private String invoiceId;
    private String mobileNumber;
    private String status;
    private String customerName;
    private String bankName;
    private String leedId;
    private String leedNumber;
    private String invoiceNumber;
    private String agentId;
    private String agentUserId;
    private String agentName;
    private String loanType;
    private String gst;
    private Long createdDateTime, updatedDateTime;
    private String totalPaymentAmount;
    private String commisionwithtdsAmount;
    private String disbussmentDate;
    private String loanapprovedaamount;
    private String loandisbussedamount;
    private String payoutbussedamount;
    private String pendingdisbussedamount;
    private String tdsAmount;
    private String totalpayoutamount;
    private String payOutOnDisbursementAmount;
    private String balancePayout;
    private String lastPayoutPaidAmount;
    private String lastPayoutPaidDate;
    private String balancePayoutWithTdsAmount;
    private String payoutPayableAfterTdsAmount;
    private Long invoiceApprovedDateTime;

    public Invoice() {
    }

    @Exclude
    public Long getCreatedDateTimeLong() {
        return createdDateTime;
    }

    public Map<String, String> getCreatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = (Long) createdDateTime;
    }

    @Exclude
    public Long getInvoiceApprovedDateTimeLong() {
        return invoiceApprovedDateTime;
    }

    public Map<String, String> getInvoiceApprovedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public void setInvoiceApprovedDateTime(Long invoiceApprovedDateTime) {
        this.invoiceApprovedDateTime = invoiceApprovedDateTime;
    }

    @Exclude
    public Long getUpdatedDateTimeLong() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Long updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public Map<String, String> getUpdatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLeedId() {
        return leedId;
    }

    public void setLeedId(String leedId) {
        this.leedId = leedId;
    }

    public String getLeedNumber() {
        return leedNumber;
    }

    public void setLeedNumber(String leedNumber) {
        this.leedNumber = leedNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(String agentUserId) {
        this.agentUserId = agentUserId;
    }

    public String getBalancePayout() {
        return balancePayout;
    }

    public void setBalancePayout(String balancePayout) {
        this.balancePayout = balancePayout;
    }

    public String getLastPayoutPaidAmount() {
        return lastPayoutPaidAmount;
    }

    public void setLastPayoutPaidAmount(String lastPayoutPaidAmount) {
        this.lastPayoutPaidAmount = lastPayoutPaidAmount;
    }

    public String getLastPayoutPaidDate() {
        return lastPayoutPaidDate;
    }

    public void setLastPayoutPaidDate(String lastPayoutPaidDate) {
        this.lastPayoutPaidDate = lastPayoutPaidDate;
    }

    public String getBalancePayoutWithTdsAmount() {
        return balancePayoutWithTdsAmount;
    }

    public void setBalancePayoutWithTdsAmount(String balancePayoutWithTdsAmount) {
        this.balancePayoutWithTdsAmount = balancePayoutWithTdsAmount;
    }

    public String getPayOutOnDisbursementAmount() {
        return payOutOnDisbursementAmount;
    }

    public void setPayOutOnDisbursementAmount(String payOutOnDisbursementAmount) {
        this.payOutOnDisbursementAmount = payOutOnDisbursementAmount;
    }

    public String getPayoutPayableAfterTdsAmount() {
        return payoutPayableAfterTdsAmount;
    }

    public void setPayoutPayableAfterTdsAmount(String payoutPayableAfterTdsAmount) {
        this.payoutPayableAfterTdsAmount = payoutPayableAfterTdsAmount;
    }

    public String getCommisionwithtdsAmount() {
        return commisionwithtdsAmount;
    }

    public void setCommisionwithtdsAmount(String commisionwithtdsAmount) {
        this.commisionwithtdsAmount = commisionwithtdsAmount;
    }

    public String getDisbussmentDate() {
        return disbussmentDate;
    }

    public void setDisbussmentDate(String disbussmentDate) {
        this.disbussmentDate = disbussmentDate;
    }

    public String getLoanapprovedaamount() {
        return loanapprovedaamount;
    }

    public void setLoanapprovedaamount(String loanapprovedaamount) {
        this.loanapprovedaamount = loanapprovedaamount;
    }

    public String getLoandisbussedamount() {
        return loandisbussedamount;
    }

    public void setLoandisbussedamount(String loandisbussedamount) {
        this.loandisbussedamount = loandisbussedamount;
    }

    public String getPayoutbussedamount() {
        return payoutbussedamount;
    }

    public void setPayoutbussedamount(String payoutbussedamount) {
        this.payoutbussedamount = payoutbussedamount;
    }

    public String getPendingdisbussedamount() {
        return pendingdisbussedamount;
    }

    public void setPendingdisbussedamount(String pendingdisbussedamount) {
        this.pendingdisbussedamount = pendingdisbussedamount;
    }

    public String getTdsAmount() {
        return tdsAmount;
    }

    public void setTdsAmount(String tdsAmount) {
        this.tdsAmount = tdsAmount;
    }

    public String getTotalpayoutamount() {
        return totalpayoutamount;
    }

    public void setTotalpayoutamount(String totalpayoutamount) {
        this.totalpayoutamount = totalpayoutamount;
    }

    public String getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(String totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public Map<String, Object> getPayoutStatusMap(String status, String description) {
        Map<String, Object> payoutStatusMap = new HashMap<>();
        payoutStatusMap.put("status", status);
        payoutStatusMap.put("rejectDescription", description);
        payoutStatusMap.put("updatedDateTime", getUpdatedDateTime());
        if (status.equalsIgnoreCase(Constant.STATUS_ACCEPTED))
            payoutStatusMap.put("invoiceApprovedDateTime", getInvoiceApprovedDateTime());
        return payoutStatusMap;
    }
}
