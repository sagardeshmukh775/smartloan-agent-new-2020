package com.smartloan.smtrick.smart_loan.repository.impl;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.models.Invoice;
import com.smartloan.smtrick.smart_loan.repository.FirebaseTemplateRepository;
import com.smartloan.smtrick.smart_loan.repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.Map;

public class InvoiceRepositoryImpl extends FirebaseTemplateRepository implements InvoiceRepository {

    @Override
    public void readAllInvoices(final CallBack callBack) {
        final Query query = Constant.INVOICE_TABLE_REF;
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Invoice> invoiceArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Invoice invoice = suggestionSnapshot.getValue(Invoice.class);
                            invoiceArrayList.add(invoice);
                        }
                        callBack.onSuccess(invoiceArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void readInvoicesByAgentId(String agentId, final CallBack callBack) {
        final Query query = Constant.INVOICE_TABLE_REF.orderByChild("agentId").equalTo(agentId);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Invoice> invoiceArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Invoice invoice = suggestionSnapshot.getValue(Invoice.class);
                            invoiceArrayList.add(invoice);
                        }
                        callBack.onSuccess(invoiceArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void readInvoicesByUserId(String userId, final CallBack callBack) {
        final Query query = Constant.INVOICE_TABLE_REF.orderByChild("agentUserId").equalTo(userId);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Invoice> invoiceArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Invoice invoice = suggestionSnapshot.getValue(Invoice.class);
                            invoiceArrayList.add(invoice);
                        }
                        callBack.onSuccess(invoiceArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void createInvoice(Invoice invoice, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.INVOICE_TABLE_REF.child(invoice.getInvoiceId());
        fireBaseCreate(databaseReference, invoice, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void deleteInvoice(String invoiceId, CallBack callBack) {
        DatabaseReference databaseReference = Constant.INVOICE_TABLE_REF.child(invoiceId);
        fireBaseDelete(databaseReference, callBack);
    }

    @Override
    public void updateInvoice(String invoiceId, Map invoiceMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constant.INVOICE_TABLE_REF.child(invoiceId);
        fireBaseUpdateChildren(databaseReference, invoiceMap, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void readInvoiceByInvoiceId(String invoiceId, final CallBack callBack) {
        final Query query = Constant.INVOICE_TABLE_REF.child(invoiceId);
        fireBaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        DataSnapshot child = dataSnapshot.getChildren().iterator().next();
                        Invoice invoice = child.getValue(Invoice.class);
                        callBack.onSuccess(invoice);
                    } else
                        callBack.onSuccess(null);
                } else
                    callBack.onSuccess(null);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void readInvoiceByStatus(String status, final CallBack callBack) {
        final Query query = Constant.INVOICE_TABLE_REF.orderByChild("status").equalTo(status);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Invoice> invoiceArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Invoice invoice = suggestionSnapshot.getValue(Invoice.class);
                            invoiceArrayList.add(invoice);
                        }
                        callBack.onSuccess(invoiceArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }
}
