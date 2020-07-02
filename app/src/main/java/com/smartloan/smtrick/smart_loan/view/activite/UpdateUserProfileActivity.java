package com.smartloan.smtrick.smart_loan.view.activite;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.databinding.ActivityUpdateProfileBinding;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapter.ViewPagerAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;
import com.smartloan.smtrick.smart_loan.view.fragement.ApprovedInvoiceFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.InvoiceFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.PaidInvoiceFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.RejectedInvoiceFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;

public class UpdateUserProfileActivity extends AppCompatActivity {

    ImageView imgCoverImage, imgProfileImage;
    TextView txtTotalLeeds, txtTotalEarning, txtAgentId, txtAgentName, txtAgentAddress;
    ViewPager viewPager;
    TabLayout tabLayout;
//    Toolbar tb;
    private String profileImage = "";
    AppSharedPreference appSharedPreference;
    private ProgressDialogClass progressDialogClass;
    private Uri profileUri;
    Bitmap bitmap;
    ViewPagerAdapter viewPagerAdapter;
    LeedRepository leedRepository;
    ArrayList<LeedsModel> leedsModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        appSharedPreference = new AppSharedPreference(this);
        leedRepository = new LeedRepositoryImpl();

        Toolbar tb = findViewById(R.id.toolbar);
//        setSupportActionBar(tb);
        setSupportActionBar(tb);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            ab.setTitle(appSharedPreference.getAgeniId());
            ab.setDisplayHomeAsUpEnabled(true);
        }
        tb.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        leedsModelArrayList = new ArrayList<>();


        imgCoverImage = findViewById(R.id.coverphoto);
        imgProfileImage = findViewById(R.id.profile_photo);
        txtTotalLeeds = findViewById(R.id.total_lead);
        txtTotalEarning = findViewById(R.id.total_earning);
        txtAgentId = findViewById(R.id.agent_id);
        txtAgentName = findViewById(R.id.agent_name);
        txtAgentAddress = findViewById(R.id.address);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragement(new InvoiceFragment(), "Personel Details");
        viewPagerAdapter.addFragement(new ApprovedInvoiceFragment(), "Bank Details");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(1);
        tabLayout.setupWithViewPager(viewPager);

        setProfileData();
        getAetLeeds();
    }

    private void getAetLeeds() {
        leedRepository.readLeedByAgentId(appSharedPreference.getAgeniId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null){
                    leedsModelArrayList = (ArrayList<LeedsModel>) object;

                    setReports(leedsModelArrayList);
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void setReports(ArrayList<LeedsModel> leedsModelArrayList) {
        int totalLeeds = leedsModelArrayList.size();
        Double TotalEarnings = 0.00;

        txtTotalLeeds.setText(String.valueOf(totalLeeds));
    }

    private void setProfileData() {

        txtAgentName.setText(appSharedPreference.getUserName());
        txtAgentAddress.setText(appSharedPreference.getAddress());
        txtAgentId.setText(appSharedPreference.getMobileNo());

        profileImage = appSharedPreference.getProfileLargeImage();

        if (!Utility.isEmptyOrNull(appSharedPreference.getProfileLargeImage())) {
            Picasso.with(this).load(appSharedPreference.getProfileLargeImage()).resize(200, 200).centerCrop().placeholder(R.drawable.dummy_user_profile).into(imgProfileImage);
            imgProfileImage.setVisibility(View.VISIBLE);
        } else
            imgProfileImage.setImageResource(R.drawable.dummy_user_profile);


    }
}
