package com.smartloan.smtrick.smart_loan.repository.impl;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.models.DashBoardData;
import com.smartloan.smtrick.smart_loan.repository.DashboardRepository;
import com.smartloan.smtrick.smart_loan.repository.FirebaseTemplateRepository;

import java.util.ArrayList;

public class DashboardRepositoryImpl extends FirebaseTemplateRepository implements DashboardRepository {
    @Override
    public void readDashBoardData(final CallBack callBack) {
        final Query query = Constant.DASHBOARD_TABLE_REF.orderByChild("index");
        fireBaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<DashBoardData> dashBoardDataArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            DashBoardData dashBoardData = suggestionSnapshot.getValue(DashBoardData.class);
                            if (dashBoardData != null && dashBoardData.isActive())
                                dashBoardDataArrayList.add(dashBoardData);
                        }
                        callBack.onSuccess(dashBoardDataArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                } else {
                    callBack.onSuccess(null);
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }
}
