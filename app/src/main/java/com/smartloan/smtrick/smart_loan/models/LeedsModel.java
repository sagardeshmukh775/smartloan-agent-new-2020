package com.smartloan.smtrick.smart_loan.models;

import android.view.View;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class LeedsModel implements Serializable {
    private String customerName;
    private String mobileNumber;
    private String address;
    private String gender;
    private String agentId;
    private String agentUserId;
    private Long createdDateTime, updatedDateTime;
    private String loanType;
    private String panCardNumber;
    private String email;
    private String dateOfBirth;
    private String expectedLoanAmount;
    private String occupation;
    private String agentName;
    private String leedId;
    private String status;
    private String customerImageSmall;
    private String customerImagelarg;
    private String leedNumber;
    private String bankName;
    private String payout;
    private String createdBy;
    private Map<String, ImagesModel> documentImages;
    private String approvedLoan;
    private int colorCode;
    private Boolean isShowColor;

    private View.OnClickListener requestBtnClickListener;

    public LeedsModel() {
    }

    public LeedsModel(int id) {
        this.customerName = "Prateek Patel";
        this.mobileNumber = "84121211";
        this.address = "Pune";
        this.gender = "Male";
        this.agentId = "Ag-56465";
        this.loanType = "Home Loan";
        this.panCardNumber = "jds45";
        this.email = "kjsdj@jhjdf.sdf";
        this.expectedLoanAmount = "2565656";
        this.occupation = "vdvf";
        this.agentName = "Aikk";
        this.leedId = "dfgdfg";
        this.status = "Generated";
        this.leedNumber = "LD_56654";
        this.bankName = "SBI";
        this.payout = "325454";
        this.approvedLoan = "3564545";
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

    public String getCustomerImagelarg() {
        return customerImagelarg;
    }

    public void setCustomerImagelarg(String customerImagelarg) {
        this.customerImagelarg = customerImagelarg;
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

    public static ArrayList<LeedsModel> getLeeds() {
        ArrayList<LeedsModel> leedsModelArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LeedsModel leedsModel = new LeedsModel(i);
            leedsModelArrayList.add(leedsModel);
        }
        return leedsModelArrayList;
    }
}
