package com.smartloan.smtrick.smart_loan.view.activite;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.databinding.ActivityUpdateProfileBinding;

public class UpdateUserProfileActivity extends AppCompatActivity {

    ImageView imgCoverImage, imgProfileImage;
    TextView txtTotalLeeds, txtTotalEarning, txtAgentId, txtAgentName, txtAgentAddress;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        imgCoverImage = findViewById(R.id.coverphoto);
        imgProfileImage = findViewById(R.id.profile_photo);
        txtTotalLeeds = findViewById(R.id.total_lead);
        txtTotalEarning = findViewById(R.id.total_earning);
        txtAgentId = findViewById(R.id.agent_id);
        txtAgentName = findViewById(R.id.agent_name);
        txtAgentAddress = findViewById(R.id.address);
        viewPager = findViewById(R.id.viewpager);
    }
}
