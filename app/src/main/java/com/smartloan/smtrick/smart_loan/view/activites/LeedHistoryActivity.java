package com.smartloan.smtrick.smart_loan.view.activites;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.ActivityLeedHistoryBinding;
import com.smartloan.smtrick.smart_loan.models.History;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapters.LeedHistoryAdapter;

import java.util.ArrayList;

import static com.smartloan.smtrick.smart_loan.utilities.Utility.sortListByCreatedDate;

public class LeedHistoryActivity extends AppCompatActivity {
    ActivityLeedHistoryBinding activityLeedHistoryBinding;
    private LeedHistoryAdapter leedHistoryAdapter;
    private ArrayList<History> historyArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLeedHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_leed_history);
        activityLeedHistoryBinding.recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLeedHistoryBinding.recyclerView.setLayoutManager(layoutManager);
        activityLeedHistoryBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        LeedsModel leedsModel = (LeedsModel) getIntent().getSerializableExtra(Constant.LEED_MODEL);
        if (leedsModel.getHistory() != null && !leedsModel.getHistory().isEmpty())
            historyArrayList = new ArrayList<>(leedsModel.getHistory().values());
        setToolBar(leedsModel.getLeedNumber());
        setHistoryLAdapter(historyArrayList);
    }//end of oncreate

    public void setToolBar(String title) {
        Toolbar tb = activityLeedHistoryBinding.toolbar;
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            if (!Utility.isEmptyOrNull(title))
                ab.setTitle(title);
            else
                ab.setTitle("Leed History");
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

    private void setHistoryLAdapter(ArrayList<History> historyArrayList) {
        if (historyArrayList != null && !historyArrayList.isEmpty()) {
            sortListByCreatedDate(historyArrayList);
            activityLeedHistoryBinding.recyclerView.setVisibility(View.VISIBLE);
            if (leedHistoryAdapter == null) {
                leedHistoryAdapter = new LeedHistoryAdapter(this, historyArrayList);
                activityLeedHistoryBinding.recyclerView.setAdapter(leedHistoryAdapter);
            } else {
                leedHistoryAdapter.reload(historyArrayList);
            }
        } else {
            Utility.showMessage(this, "No History Avilable");
            finish();
        }
    }
}
