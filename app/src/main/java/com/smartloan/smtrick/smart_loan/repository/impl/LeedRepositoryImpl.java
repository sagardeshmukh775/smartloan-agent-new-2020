package com.smartloan.smtrick.smart_loan.repository.impl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.models.LeadActivitiesModel;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.repository.FirebaseTemplateRepository;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.utilities.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.smartloan.smtrick.smart_loan.constants.Constant.LEAD_ACTIVITY_TABLE_REF;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_DATE_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.YEAR_DATE_FORMAT;

public class LeedRepositoryImpl extends FirebaseTemplateRepository implements LeedRepository {
    @Override
    public void readAllLeeds(final CallBack callBack) {
        final Query query = Constant.LEEDS_TABLE_REF;
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<LeedsModel> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            LeedsModel leedsModel = suggestionSnapshot.getValue(LeedsModel.class);
                            leedsModelArrayList.add(leedsModel);
                        }
                        callBack.onSuccess(leedsModelArrayList);
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
    public void readLeedsByUserId(final Context context, String userId, final CallBack callBack) {
        final Query query = Constant.LEEDS_TABLE_REF.orderByChild("createdBy").equalTo(userId);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<LeedsModel> leedsModelArrayList = new ArrayList<>();
                        int colorCount = 0;
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            LeedsModel leedsModel = suggestionSnapshot.getValue(LeedsModel.class);
                            if (colorCount % 5 == 0)
                                colorCount = 0;
                            setColor(context, leedsModel, colorCount);
                            colorCount++;
                            leedsModelArrayList.add(leedsModel);
                        }
                        callBack.onSuccess(leedsModelArrayList);
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

    private void setColor(final Context context, LeedsModel leedsModel, int count) {
        switch (count) {
            case 0:
                leedsModel.setColorCode(context.getResources().getColor(R.color.hederbackground));
                break;
            case 1:
                leedsModel.setColorCode(context.getResources().getColor(R.color.yello));
                break;
            case 2:
                leedsModel.setColorCode(context.getResources().getColor(R.color.red));
                break;
            case 3:
                leedsModel.setColorCode(context.getResources().getColor(R.color.blue));
                break;
            case 4:
                leedsModel.setColorCode(context.getResources().getColor(R.color.green));
                break;
            default:
                leedsModel.setColorCode(context.getResources().getColor(R.color.hederbackground));
                break;
        }
    }

    @Override
    public void createLeed(LeedsModel leedsModel, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.LEEDS_TABLE_REF.child(leedsModel.getLeedId());
        fireBaseCreate(databaseReference, leedsModel, new CallBack() {
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
    public void deleteLeed(String leedId, CallBack callback) {
        DatabaseReference databaseReference = Constant.LEEDS_TABLE_REF.child(leedId);
        fireBaseDelete(databaseReference, callback);
    }

    @Override
    public void updateLeed(String leedId, Map leedMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constant.LEEDS_TABLE_REF.child(leedId);
        fireBaseUpdateChildren(databaseReference, leedMap, new CallBack() {
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
    public void updateLeedDocuments(String leedId, Map leedMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constant.LEEDS_TABLE_REF.child(leedId).child("documentImages");
        fireBaseUpdateChildren(databaseReference, leedMap, new CallBack() {
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
    public void updateLeedHistory(String leedId, Map leedMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constant.LEEDS_TABLE_REF.child(leedId).child("history");
        fireBaseUpdateChildren(databaseReference, leedMap, new CallBack() {
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
    public void readLeedByLeedId(String leedId, final CallBack callBack) {
        final Query query = Constant.LEEDS_TABLE_REF.child(leedId);
        fireBaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        DataSnapshot child = dataSnapshot.getChildren().iterator().next();
                        LeedsModel leedsModel = child.getValue(LeedsModel.class);
                        callBack.onSuccess(leedsModel);
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
    public void readLeedsByUserIdOfYear(final Context context, String userId, final long year, final CallBack callBack) {
        int inputYear = Integer.parseInt(Utility.convertMilliSecondsToFormatedDate(year, "yyyy"));
        inputYear++;
        final long yearEnd = Utility.convertFormatedDateToMilliSeconds("01, " + inputYear, YEAR_DATE_FORMAT);
        Log.i("LeadRepo", "Generated date year Start date::::::" + Utility.convertMilliSecondsToFormatedDate(year, LEED_DATE_FORMATE));
        Log.i("LeadRepo", "Generated date year End date::::::" + Utility.convertMilliSecondsToFormatedDate(yearEnd, LEED_DATE_FORMATE));
        final Query query = Constant.LEEDS_TABLE_REF.orderByChild("createdBy").equalTo(userId);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<LeedsModel> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            LeedsModel leedsModel = suggestionSnapshot.getValue(LeedsModel.class);
                            if (leedsModel != null && leedsModel.getCreatedDateTimeLong() != null) {
                                Log.i("LeadRepo", "Generated date year::::::" + Utility.convertMilliSecondsToFormatedDate(leedsModel.getCreatedDateTimeLong(), LEED_DATE_FORMATE));
                            }
                            if (leedsModel != null
                                    && leedsModel.getCreatedDateTimeLong() != null
                                    && leedsModel.getCreatedDateTimeLong() > year
                                    && leedsModel.getCreatedDateTimeLong() < yearEnd)
                                leedsModelArrayList.add(leedsModel);
                        }
                        callBack.onSuccess(leedsModelArrayList);
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
    public void createLeadActivity(LeadActivitiesModel leadActivitiesModel, final CallBack callBack) {
        leadActivitiesModel.setActivityKey(LEAD_ACTIVITY_TABLE_REF.push().getKey());
        DatabaseReference databaseReference = LEAD_ACTIVITY_TABLE_REF.child(leadActivitiesModel.getActivityKey());
        fireBaseCreate(databaseReference, leadActivitiesModel, new CallBack() {
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
    public void readLeadActivityByLeadId(String leadId, CallBack callBack) {
        final Query query = Constant.LEAD_ACTIVITY_TABLE_REF.orderByChild("leadId").equalTo(leadId);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<LeadActivitiesModel> leadActivitiesModels = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            LeadActivitiesModel leadActivitiesModel = suggestionSnapshot.getValue(LeadActivitiesModel.class);
                            if (leadActivitiesModel != null)
                                leadActivitiesModels.add(leadActivitiesModel);
                        }
                        callBack.onSuccess(leadActivitiesModels);
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
