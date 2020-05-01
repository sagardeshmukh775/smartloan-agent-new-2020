package com.smartloan.smtrick.smart_loan.view.activite;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.ActivityLeadActivitiesBinding;
import com.smartloan.smtrick.smart_loan.models.LeadActivitiesModel;
import com.smartloan.smtrick.smart_loan.models.LeadActivitiesModel;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapter.LeadActivitiesAdapter;
import com.smartloan.smtrick.smart_loan.view.adapter.LeedHistoryAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

import java.util.ArrayList;

import static com.smartloan.smtrick.smart_loan.utilities.Utility.sortLeadActivitiesByCreatedDate;
import static com.smartloan.smtrick.smart_loan.utilities.Utility.sortListByCreatedDate;


public class LeadActivitiesActivity extends AppCompatActivity {
    ActivityLeadActivitiesBinding activityLeadActivitiesBinding;
    private LeadActivitiesAdapter leadActivitiesAdapter;
    private ArrayList<LeadActivitiesModel> leadActivitiesModelArrayList;
    private ProgressDialogClass progressDialogClass;
    private LeedRepository leedRepository;
    private String leadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLeadActivitiesBinding = DataBindingUtil.setContentView(this, R.layout.activity_lead_activities);
        leedRepository = new LeedRepositoryImpl();
        activityLeadActivitiesBinding.recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLeadActivitiesBinding.recyclerView.setLayoutManager(layoutManager);
        activityLeadActivitiesBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialogClass = new ProgressDialogClass(this);
        leadId = getIntent().getStringExtra(Constant.LEED_ID);
        String leadNumber = getIntent().getStringExtra(Constant.LEED_NUMBER);
        setToolBar(leadNumber);
        getLeadActivities(leadId);
    }//end of oncreate

    public void setToolBar(String title) {
        Toolbar tb = activityLeadActivitiesBinding.toolbar;
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            if (!Utility.isEmptyOrNull(title))
                ab.setTitle(title);
            else
                ab.setTitle("Lead Activities");
            ab.setDisplayHomeAsUpEnabled(true);
        }
        tb.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getLeadActivities(String leadId) {
        if (!Utility.isEmptyOrNull(leadId)) {
            progressDialogClass.showDialog(this.getString(R.string.loading), this.getString(R.string.PLEASE_WAIT));
            leedRepository.readLeadActivityByLeadId(leadId, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        ArrayList<LeadActivitiesModel> leadActivitiesModels = (ArrayList<LeadActivitiesModel>) object;
                        setLeadActivitiesAdapter(leadActivitiesModels);
                    }
                    if (progressDialogClass != null)
                        progressDialogClass.dismissDialog();
                }

                @Override
                public void onError(Object object) {
                    if (progressDialogClass != null)
                        progressDialogClass.dismissDialog();
                    Utility.showLongMessage(LeadActivitiesActivity.this, getString(R.string.server_error));
                }
            });
        } else {
            setLeadActivitiesAdapter(leadActivitiesModelArrayList);
        }
    }


    private void setLeadActivitiesAdapter(ArrayList<LeadActivitiesModel> leadActivitiesModelArrayList) {
        this.leadActivitiesModelArrayList = leadActivitiesModelArrayList;
        if (leadActivitiesModelArrayList != null && !leadActivitiesModelArrayList.isEmpty()) {
            sortLeadActivitiesByCreatedDate(leadActivitiesModelArrayList);
            activityLeadActivitiesBinding.recyclerView.setVisibility(View.VISIBLE);
            if (leadActivitiesAdapter == null) {
                leadActivitiesAdapter = new LeadActivitiesAdapter(this, leadActivitiesModelArrayList);
                activityLeadActivitiesBinding.recyclerView.setAdapter(leadActivitiesAdapter);
            } else {
                leadActivitiesAdapter.reload(leadActivitiesModelArrayList);
            }
        } else {
            Utility.showMessage(this, "No Lead Activities Available");
            finish();
        }
    }
}
