package com.smartloan.smtrick.smart_loan.view.activites;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.view.adapters.ImageSwipZoomAdapter;

import java.util.ArrayList;

public class ImageSwipZoomActivity extends AppCompatActivity {
    private ViewPager mPager;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    Toolbar toolbar;
    private ArrayList<Uri> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_image_swip_zoom);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // add back arrow to toolbar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                imagesList = (ArrayList<Uri>) extras.getSerializable(Constant.IMAGE_URI_LIST);
                currentPage = extras.getInt(Constant.CURRENT_PAGE, 0);
            }
            if (imagesList != null)
                init(imagesList, currentPage);
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }//end of oncreate

    private void init(ArrayList<Uri> imagesList, int pos) {
        try {
            currentPage = pos;
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(new ImageSwipZoomAdapter(this, imagesList));
            NUM_PAGES = imagesList.size();
            if (currentPage < NUM_PAGES) {
                mPager.setCurrentItem(currentPage, true);
            }
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }//end of init

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}//end of activity
