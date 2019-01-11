package com.smartloan.smtrick.smart_loan.interfaces;

public interface ItemRemoveListner {
    void itemRemoved(int postion);

    void itemRemoveFromDatabase(String leedId, String documentId);
}
