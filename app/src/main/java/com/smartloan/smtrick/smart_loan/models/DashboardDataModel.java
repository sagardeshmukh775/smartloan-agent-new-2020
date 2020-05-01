package com.smartloan.smtrick.smart_loan.models;

import com.github.mikephil.charting.data.Entry;

import java.io.Serializable;
import java.util.ArrayList;

public class DashboardDataModel implements Serializable {
    private String dataTitle;
    private String homeLoanLabel;
    private String homeLoanValue;
    private String loanAgainstPropertyLabel;
    private String loanAgainstPropertyValue;
    private String balanceTransferLabel;
    private String balanceTransferValue;
    private boolean isShowDivider;
    private int backgroundColor;
    private ArrayList<Entry> HLEntries;
    private ArrayList<Entry> LAPEntries;
    private ArrayList<Entry> BTEntries;
    private String[] xAxisValues;
    private String totalValue = "0";
    private String yearData;

    public DashboardDataModel() {
        homeLoanLabel = "HL";
        loanAgainstPropertyLabel = "LAP";
        balanceTransferLabel = "BT";
        HLEntries = new ArrayList<>();
        LAPEntries = new ArrayList<>();
        BTEntries = new ArrayList<>();
        xAxisValues = new String[35];
        for (int i = 0; i <= 12; i++) {
            xAxisValues[i] = "" + (i + 1);
        }
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public String getHomeLoanLabel() {
        return homeLoanLabel;
    }

    public void setHomeLoanLabel(String homeLoanLabel) {
        this.homeLoanLabel = homeLoanLabel;
    }

    public String getHomeLoanValue() {
        return homeLoanValue;
    }

    public void setHomeLoanValue(String homeLoanValue) {
        this.homeLoanValue = homeLoanValue;
    }

    public String getLoanAgainstPropertyLabel() {
        return loanAgainstPropertyLabel;
    }

    public void setLoanAgainstPropertyLabel(String loanAgainstPropertyLabel) {
        this.loanAgainstPropertyLabel = loanAgainstPropertyLabel;
    }

    public String getLoanAgainstPropertyValue() {
        return loanAgainstPropertyValue;
    }

    public void setLoanAgainstPropertyValue(String loanAgainstPropertyValue) {
        this.loanAgainstPropertyValue = loanAgainstPropertyValue;
    }

    public String getBalanceTransferLabel() {
        return balanceTransferLabel;
    }

    public void setBalanceTransferLabel(String balanceTransferLabel) {
        this.balanceTransferLabel = balanceTransferLabel;
    }

    public String getBalanceTransferValue() {
        return balanceTransferValue;
    }

    public void setBalanceTransferValue(String balanceTransferValue) {
        this.balanceTransferValue = balanceTransferValue;
    }

    public boolean isShowDivider() {
        return isShowDivider;
    }

    public void setShowDivider(boolean showDivider) {
        isShowDivider = showDivider;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ArrayList<Entry> getHLEntries() {
        return HLEntries;
    }

    public void setHLEntries(ArrayList<Entry> HLEntries) {
        this.HLEntries = HLEntries;
    }

    public ArrayList<Entry> getLAPEntries() {
        return LAPEntries;
    }

    public void setLAPEntries(ArrayList<Entry> LAPEntries) {
        this.LAPEntries = LAPEntries;
    }

    public ArrayList<Entry> getBTEntries() {
        return BTEntries;
    }

    public void setBTEntries(ArrayList<Entry> BTEntries) {
        this.BTEntries = BTEntries;
    }

    public String[] getxAxisValues() {
        return xAxisValues;
    }

    public void setxAxisValues(String[] xAxisValues) {
        this.xAxisValues = xAxisValues;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getYearData() {
        return yearData;
    }

    public void setYearData(String yearData) {
        this.yearData = yearData;
    }
}
