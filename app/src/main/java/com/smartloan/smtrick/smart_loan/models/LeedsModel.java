package com.smartloan.smtrick.smart_loan.models;

import android.view.View;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeedsModel implements Serializable {
    private String customerName;
    private String mobileNumber;
    private String alternetMobileNumber;
    private String address;
    private String gender;
    private String agentId;
    private String agentUserId;
    private Long createdDateTime, updatedDateTime;
    private String loanType;
    private String homeLoanType;
    private String balanceTransferLoanType;
    private String panCardNumber;
    private String email;
    private String dateOfBirth;
    private String expectedLoanAmount = "0";
    private String occupation;
    private String agentName;
    private String leedId;
    private String status;
    private String customerImageSmall;
    private String customerImagelarge;
    private String leedNumber;
    private String bankName;
    private String payout;
    private String createdBy;
    private Map<String, ImagesModel> documentImages;
    private String approvedLoan;
    private int colorCode;
    private Boolean isShowColor;
    private Map<String, History> history;
    private View.OnClickListener requestBtnClickListener;
    private String approvedLoanAmount = "0";
    private String disbursedLoanAmount = "0";
    private String payOutOnDisbursementAmount = "0";
    private String totalPayoutAmount = "0";
    private String balancePayout = "0";

    public LeedsModel() {
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
    public Long getUpdatedDateTimeLong() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Long updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public Map<String, String> getUpdatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getHomeLoanType() {
        return homeLoanType;
    }

    public void setHomeLoanType(String homeLoanType) {
        this.homeLoanType = homeLoanType;
    }

    public String getBalanceTransferLoanType() {
        return balanceTransferLoanType;
    }

    public void setBalanceTransferLoanType(String balanceTransferLoanType) {
        this.balanceTransferLoanType = balanceTransferLoanType;
    }

    public String getPanCardNumber() {
        return panCardNumber;
    }

    public void setPanCardNumber(String panCardNumber) {
        this.panCardNumber = panCardNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getExpectedLoanAmount() {
        if (expectedLoanAmount == null || expectedLoanAmount.isEmpty())
            expectedLoanAmount = "0";
        return expectedLoanAmount;
    }

    public void setExpectedLoanAmount(String expectedLoanAmount) {
        this.expectedLoanAmount = expectedLoanAmount;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerImageSmall() {
        return customerImageSmall;
    }

    public void setCustomerImageSmall(String customerImageSmall) {
        this.customerImageSmall = customerImageSmall;
    }

    public String getCustomerImagelarge() {
        return customerImagelarge;
    }

    public void setCustomerImagelarge(String customerImagelarge) {
        this.customerImagelarge = customerImagelarge;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }

    public Map<String, ImagesModel> getDocumentImages() {
        return documentImages;
    }

    public void setDocumentImages(Map<String, ImagesModel> documentImages) {
        this.documentImages = documentImages;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApprovedLoan() {
        return approvedLoan;
    }

    public void setApprovedLoan(String approvedLoan) {
        this.approvedLoan = approvedLoan;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    public String getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(String agentUserId) {
        this.agentUserId = agentUserId;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public Boolean getShowColor() {
        return isShowColor;
    }

    public void setShowColor(Boolean showColor) {
        isShowColor = showColor;
    }

    public String getAlternetMobileNumber() {
        return alternetMobileNumber;
    }

    public void setAlternetMobileNumber(String alternetMobileNumber) {
        this.alternetMobileNumber = alternetMobileNumber;
    }

    public Map<String, History> getHistory() {
        return history;
    }

    public void setHistory(Map<String, History> history) {
        this.history = history;
    }

    public String getApprovedLoanAmount() {
        return approvedLoanAmount;
    }

    public void setApprovedLoanAmount(String approvedLoanAmount) {
        this.approvedLoanAmount = approvedLoanAmount;
    }

    public String getDisbursedLoanAmount() {
        return disbursedLoanAmount;
    }

    public void setDisbursedLoanAmount(String disbursedLoanAmount) {
        this.disbursedLoanAmount = disbursedLoanAmount;
    }

    public String getPayOutOnDisbursementAmount() {
        return payOutOnDisbursementAmount;
    }

    public void setPayOutOnDisbursementAmount(String payOutOnDisbursementAmount) {
        this.payOutOnDisbursementAmount = payOutOnDisbursementAmount;
    }

    public String getTotalPayoutAmount() {
        return totalPayoutAmount;
    }

    public void setTotalPayoutAmount(String totalPayoutAmount) {
        this.totalPayoutAmount = totalPayoutAmount;
    }

    public String getBalancePayout() {
        return balancePayout;
    }

    public void setBalancePayout(String balancePayout) {
        this.balancePayout = balancePayout;
    }

    @Exclude
    public Map<String, Object> getUpdateLeedMap() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("customerName", customerName);
        objectMap.put("mobileNumber", mobileNumber);
        objectMap.put("address", address);
        objectMap.put("gender", gender);
        objectMap.put("loanType", loanType);
        objectMap.put("homeLoanType", homeLoanType);
        objectMap.put("balanceTransferLoanType", balanceTransferLoanType);
        objectMap.put("panCardNumber", panCardNumber);
        objectMap.put("email", email);
        objectMap.put("expectedLoanAmount", expectedLoanAmount);
        objectMap.put("occupation", occupation);
        objectMap.put("alternetMobileNumber", alternetMobileNumber);
        objectMap.put("dateOfBirth", dateOfBirth);
        objectMap.put("customerImageSmall", customerImageSmall);
        objectMap.put("customerImagelarge", customerImagelarge);
        return objectMap;
    }
}
