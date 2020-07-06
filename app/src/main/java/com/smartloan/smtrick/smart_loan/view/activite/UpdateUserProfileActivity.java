package com.smartloan.smtrick.smart_loan.view.activite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.firebasestorage.StorageService;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.UserRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.repository.impl.UserRepositoryImpl;
import com.smartloan.smtrick.smart_loan.service.ImageCompressionService;
import com.smartloan.smtrick.smart_loan.service.impl.ImageCompressionServiceImp;
import com.smartloan.smtrick.smart_loan.singleton.AppSingleton;
import com.smartloan.smtrick.smart_loan.utilities.FileUtils;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapter.ViewPagerAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;
import com.smartloan.smtrick.smart_loan.view.fragement.ApprovedInvoiceFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.BankDetailsFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.InvoiceFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.LeedsFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.PaidInvoiceFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.PersonelDetailsFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.RejectedInvoiceFragment;
import com.smartloan.smtrick.smart_loan.view.fragement.ReportsFragment;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.USER_PROFILE_PATH;

public class UpdateUserProfileActivity extends AppCompatActivity implements OnFragmentInteractionListener, View.OnClickListener {

    ImageView imgCoverImage, imgProfileImage, imgEditCover, imgCover;
    TextView txtTotalLeeds, txtTotalEarning, txtAgentId, txtAgentName, txtAgentAddress;
    ViewPager viewPager;
    TabLayout tabLayout;
    UserRepository userRepository;
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
        setContentView(R.layout.activity_update_user_profile1);

        appSharedPreference = new AppSharedPreference(this);
        leedRepository = new LeedRepositoryImpl();
        userRepository = new UserRepositoryImpl(this);

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
        viewPager = findViewById(R.id.viewPager);
        imgEditCover = findViewById(R.id.editcoverphoto);
        imgEditCover = findViewById(R.id.coverphoto);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragement(new PersonelDetailsFragment(), "Personel Details");
        viewPagerAdapter.addFragement(new BankDetailsFragment(), "Bank Details");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(1);
        tabLayout.setupWithViewPager(viewPager);

        setProfileData();
        getAetLeeds();
        imgEditCover.setOnClickListener(this);
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
        txtAgentId.setText(appSharedPreference.getAgeniId());

        profileImage = appSharedPreference.getProfileLargeImage();

        if (!Utility.isEmptyOrNull(appSharedPreference.getProfileLargeImage())) {
            Picasso.with(this).load(appSharedPreference.getProfileLargeImage()).resize(200, 200).centerCrop().placeholder(R.drawable.dummy_user_profile).into(imgProfileImage);
            imgProfileImage.setVisibility(View.VISIBLE);
        } else
            imgProfileImage.setImageResource(R.drawable.dummy_user_profile);


    }

    @Override
    public void onFragmentInteraction(String title) {

    }

    @Override
    public void changeFragement(Fragment fragment) {

    }

    @Override
    public void onClick(View v) {
        if (v == imgCoverImage){
            startCropImageActivity();
        }
    }

    //Start crop image activity for the given image.
    private void startCropImageActivity() {
        try {
            CropImage.activity(null)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        try {
            switch (requestCode) {
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(imageData);
                    if (resultCode == RESULT_OK) {
                        if (imageData != null) {
                            Bundle extras = imageData.getExtras();
                            if (extras != null) {
                                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                                profileUri = result.getUri();
//                                imgCancleprofile.setVisibility(View.VISIBLE);
                                if (bitmapImg != null)
                                    imgCover.setImageBitmap(bitmapImg);
                                compressBitmap(profileUri);
                            }
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Utility.showMessage(this, "Cropping failed: " + result.getError());
                    }
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private void compressBitmap(Uri uri) {
        String path = FileUtils.getPath(this, uri);
        ImageCompressionService imageCompressionService = new ImageCompressionServiceImp();
        imageCompressionService.compressImage(path, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    bitmap = (Bitmap) object;

                    if (profileUri != null && bitmap != null) {
                        uploadImage(bitmap, USER_PROFILE_PATH);
                    }
                }
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    void uploadImage(Bitmap bitmap, String storagePath) {
        try {
            AppSingleton.getInstance(this).setNotificationManager();
            InputStream imageInputStream = Utility.returnInputStreamFromBitmap(bitmap);
            StorageService.uploadImageStreamToFirebaseStorage(imageInputStream, storagePath, new CallBack() {
                public void onSuccess(Object object) {
                    if (object != null) {
                        String downloadUrlLarge = (String) object;
                        try {
                            appSharedPreference.setUserProfileImages(downloadUrlLarge);
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction(MainActivity.ImageUploadReceiver.PROCESS_RESPONSE);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                        } catch (Exception e) {
                            ExceptionUtil.logException(e);
                        }
                        updateUserData(downloadUrlLarge);
                    }
                }

                public void onError(Object object) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserData(final String downloadUrlLarge) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userCoverImage", downloadUrlLarge);

        userRepository.updateUser(appSharedPreference.getUserId(), map, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                AppSingleton.getInstance(UpdateUserProfileActivity.this).updateProgress(1, 1, 100);
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(getApplicationContext(), getMessage(R.string.data_updation_fails_message));
            }
        });
    }

    private String getMessage(int id) {
        return getString(id);
    }
}
