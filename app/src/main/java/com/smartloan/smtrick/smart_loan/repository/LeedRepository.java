package com.smartloan.smtrick.smart_loan.repository;

import android.content.Context;

import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.models.LeadActivitiesModel;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;

import java.util.Map;

public interface LeedRepository {
    void readAllLeeds(final CallBack callback);

    void readLeedsByUserId(final Context context, final String userId, final CallBack callBack);

    void createLeed(final LeedsModel leedsModel, final CallBack callback);

    void createLeadActivity(final LeadActivitiesModel leadActivitiesModel, final CallBack callback);

    void deleteLeed(final String leedId, final CallBack callback);

    void updateLeed(final String leedId, final Map leedMap, final CallBack callback);

    void readLeedByLeedId(final String leedId, final CallBack callBack);

    void readLeadActivityByLeadId(final String leadId, final CallBack callBack);

    void updateLeedDocuments(final String leedId, final Map leedMap, final CallBack callback);

    void updateLeedHistory(final String leedId, final Map leedMap, final CallBack callback);

    void readLeedsByUserIdOfYear(final Context context, final String userId, long year, final CallBack callBack);

    void readLeedByAgentId(final String agentId, final CallBack callBack);
}
