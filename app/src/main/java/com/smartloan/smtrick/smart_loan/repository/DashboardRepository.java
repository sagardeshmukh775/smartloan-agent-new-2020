package com.smartloan.smtrick.smart_loan.repository;

import com.smartloan.smtrick.smart_loan.callback.CallBack;

public interface DashboardRepository {
    void readDashBoardData(final CallBack callBack);
}
