package com.smartloan.smtrick.smart_loan.view.activite;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.databinding.ActivityUpdateProfileBinding;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.firebasestorage.StorageService;
import com.smartloan.smtrick.smart_loan.models.KYCDetails;
import com.smartloan.smtrick.smart_loan.models.User;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.UserRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.UserRepositoryImpl;
import com.smartloan.smtrick.smart_loan.service.ImageCompressionService;
import com.smartloan.smtrick.smart_loan.service.impl.ImageCompressionServiceImp;
import com.smartloan.smtrick.smart_loan.singleton.AppSingleton;
import com.smartloan.smtrick.smart_loan.utilities.FileUtils;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.util.HashMap;

import static com.smartloan.smtrick.smart_loan.constants.Constant.FEMALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.RESULT_CODE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.USER_PROFILE_PATH;

public class UpdateBankDetailsActivity extends AppCompatActivity {
//    ActivityUpdateProfileBinding activityUpdateProfileBinding;


    AppSharedPreference appSharedPreference;
    private ProgressDialogClass progressDialogClass;
    private Uri profileUri;
    Bitmap bitmap;
    UserRepository userRepository;
    private String profileImage = "";
    private ImageView ivCancelProfile, ivProfile;
    EditText etAccountName, etAccountNumber, etIfsc, etBranchName, etBankName;
    Button buttonsubmit;
//    LinearLayout llKycDetailsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bank_details);
        appSharedPreference = new AppSharedPreference(this);
        progressDialogClass = new ProgressDialogClass(this);
        userRepository = new UserRepositoryImpl(this);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
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

        etAccountName = findViewById(R.id.et_account_name);
        etAccountNumber = findViewById(R.id.et_account_number);
        etIfsc = findViewById(R.id.et_ifsc);
        etBranchName = findViewById(R.id.et_branch_name);
        etBankName = findViewById(R.id.et_bank_name);
        ivCancelProfile = findViewById(R.id.iv_cancel_profile);
        ivProfile = findViewById(R.id.ivProfile);
        buttonsubmit = findViewById(R.id.buttonsubmit);
//        llKycDetailsLayout = findViewById(R.id.ll_kyc_details_layout);

        setProfileData();
        setUpdateClickListner();
        onClickSelectProfile();
        onClickCancelProfile();
    }//end of activity


    private void setProfileData() {

        if (!Utility.isEmptyOrNull(appSharedPreference.getProfileLargeImage())) {
            Picasso.with(this).load(appSharedPreference.getProfileLargeImage()).resize(200, 200).centerCrop().placeholder(R.drawable.dummy_user_profile).into(ivProfile);
            ivCancelProfile.setVisibility(View.VISIBLE);
        } else
            ivProfile.setImageResource(R.drawable.dummy_user_profile);

        if (!Utility.isEmptyOrNull(appSharedPreference.getUserId())) {
//            llKycDetailsLayout.setVisibility(View.VISIBLE);
            etAccountName.setText(appSharedPreference.getAccountHolderName());
            etAccountNumber.setText(appSharedPreference.getAccountNumber());
            etIfsc.setText(appSharedPreference.getIfsc());
            etBranchName.setText(appSharedPreference.getBranchName());
            etBankName.setText(appSharedPreference.getBankName());
        }
//        else
//            llKycDetailsLayout.setVisibility(View.VISIBLE);

    }

    private void setUpdateClickListner() {
        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndCreateUser();
            }
        });
    }

    private void validateAndCreateUser() {
        User user = fillUserModel();
        if (validate(user))
            updateUser(user);
    }

    private User fillUserModel() {
        User user = new User();

        user.setUserProfileImageLarge(profileImage);

        if (!Utility.isEmptyOrNull(appSharedPreference.getUserId())) {
            KYCDetails kycDetails = new KYCDetails();
            kycDetails.setAccountHolderName(etAccountName.getText().toString());
            kycDetails.setAccountNumber(etAccountNumber.getText().toString());
            kycDetails.setIfsc(etIfsc.getText().toString());
            kycDetails.setBranchName(etBranchName.getText().toString());
            kycDetails.setBankName(etBankName.getText().toString());
            user.setKycDetails(kycDetails);
        }
        return user;
    }

    private boolean validate(User user) {
        String validationMessage;
        boolean isValid = true;
        try {


            if (user.getKycDetails() != null) {
                if (Utility.isEmptyOrNull(user.getKycDetails().getBankName())) {
                    validationMessage = getString(R.string.bank_name);
                    etBankName.setError(validationMessage);
                    isValid = false;
                }
                if (Utility.isEmptyOrNull(user.getKycDetails().getBranchName())) {
                    validationMessage = getString(R.string.branch_name);
                    etBranchName.setError(validationMessage);
                    isValid = false;
                }
                if (Utility.isEmptyOrNull(user.getKycDetails().getAccountNumber())) {
                    validationMessage = getString(R.string.account_number);
                    etAccountNumber.setError(validationMessage);
                    isValid = false;
                }
                if (Utility.isEmptyOrNull(user.getKycDetails().getIfsc())) {
                    validationMessage = getString(R.string.ifsc_error);
                    etIfsc.setError(validationMessage);
                    isValid = false;
                }
            } else {
                validationMessage = getString(R.string.fill_kyc_details_error);
                Utility.showLongMessage(this, validationMessage);
                isValid = false;
            }

        } catch (Exception e) {
            isValid = false;
            ExceptionUtil.logException(e);
        }
        return isValid;
    }

    private String getMessage(int id) {
        return getString(id);
    }

    private void updateUser(final User user) {
        if (profileUri != null && bitmap != null) {
            uploadImage(bitmap, USER_PROFILE_PATH);
        }
        progressDialogClass.showDialog(getMessage(R.string.loading), getMessage(R.string.please_wait));
        userRepository.updateUser(appSharedPreference.getUserId(), user.getUpdateUserMap(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                addUserDataToPreferences(user);
                progressDialogClass.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                progressDialogClass.dismissDialog();
                Utility.showMessage(getApplicationContext(), getMessage(R.string.data_updation_fails_message));
            }
        });
    }

    private void addUserDataToPreferences(User user) {
        appSharedPreference.addUserDetails(user);
        setResult(RESULT_CODE);
        finish();
    }

    private void onClickSelectProfile() {
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropImageActivity();
            }
        });
    }

    private void onClickCancelProfile() {
        ivCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivProfile.setImageResource(R.drawable.dummy_user_profile);
                profileUri = null;
                bitmap = null;
                profileImage = "";
                ivCancelProfile.setVisibility(View.GONE);
            }
        });
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
                                ivCancelProfile.setVisibility(View.VISIBLE);
                                if (bitmapImg != null)
                                    ivProfile.setImageBitmap(bitmapImg);
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
        map.put("userProfileImageLarge", downloadUrlLarge);
        map.put("userProfileImageSmall", downloadUrlLarge);
        userRepository.updateUser(appSharedPreference.getUserId(), map, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                AppSingleton.getInstance(UpdateBankDetailsActivity.this).updateProgress(1, 1, 100);
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(getApplicationContext(), getMessage(R.string.data_updation_fails_message));
            }
        });
    }
}//rnd of activity
