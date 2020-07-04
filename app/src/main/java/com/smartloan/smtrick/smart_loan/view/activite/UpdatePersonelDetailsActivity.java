package com.smartloan.smtrick.smart_loan.view.activite;

import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.firebasestorage.StorageService;
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

public class UpdatePersonelDetailsActivity extends AppCompatActivity {
    //    ActivityUpdateProfileBinding activityUpdateProfileBinding;
    ImageView imgProfile,imgCancleprofile;
    EditText edtName, edtMobile, edtAddress, edtEmail;
    RadioGroup GroupGender;
    RadioButton radiogender, radioMale, radioFemale;
    Button btnUpdate;
    AppSharedPreference appSharedPreference;
    private ProgressDialogClass progressDialogClass;
    private Uri profileUri;
    Bitmap bitmap;
    UserRepository userRepository;
    private String profileImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_details);

        appSharedPreference = new AppSharedPreference(this);
        progressDialogClass = new ProgressDialogClass(this);
        userRepository = new UserRepositoryImpl(this);

        Toolbar tb =findViewById(R.id.toolbar);
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

        edtName = findViewById(R.id.edittextname);
        edtMobile = findViewById(R.id.edittextmobile);
        edtAddress = findViewById(R.id.edittextaddress);
        edtEmail = findViewById(R.id.edittextemailid);
        GroupGender = findViewById(R.id.radiogender);
        radioMale = findViewById(R.id.radiomale);
        radioFemale = findViewById(R.id.radiofemale);
        btnUpdate = findViewById(R.id.buttonsubmit);
        imgProfile = findViewById(R.id.ivProfile);
        imgCancleprofile = findViewById(R.id.iv_cancel_profile);



        setProfileData();
        setUpdateClickListner();
        onClickSelectProfile();
        onClickCancelProfile();
    }//end of activity


    private void setProfileData() {
        edtName.setText(appSharedPreference.getUserName());
        edtAddress.setText(appSharedPreference.getAddress());
        edtMobile.setText(appSharedPreference.getMobileNo());
        edtEmail.setText(appSharedPreference.getEmaiId());
        profileImage = appSharedPreference.getProfileLargeImage();
        if (appSharedPreference.getGender().equalsIgnoreCase(MALE))
            radioMale.setChecked(true);
        else
            radioFemale.setChecked(true);
        if (!Utility.isEmptyOrNull(appSharedPreference.getProfileLargeImage())) {
            Picasso.with(this).load(appSharedPreference.getProfileLargeImage()).resize(200, 200).centerCrop().placeholder(R.drawable.dummy_user_profile).into(imgProfile);
            imgCancleprofile.setVisibility(View.VISIBLE);
        } else
            imgProfile.setImageResource(R.drawable.dummy_user_profile);


    }

    private void setUpdateClickListner() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
        user.setUserName(edtName.getText().toString());
        user.setMobileNumber(edtMobile.getText().toString());
        user.setAddress(edtAddress.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setUserProfileImageLarge(profileImage);
        user.setUserProfileImageSmall(profileImage);
        if (radioMale.isChecked())
            user.setGender(MALE);
        else
            user.setGender(FEMALE);

        return user;
    }

    private boolean validate(User user) {
        String validationMessage;
        boolean isValid = true;
        try {
            if (Utility.isEmptyOrNull(user.getUserName())) {
                validationMessage = getString(R.string.PLEASE_ENTER_NAME);
                edtName.setError(validationMessage);
                isValid = false;
            }
            if (Utility.isEmptyOrNull(user.getMobileNumber())) {
                validationMessage = getString(R.string.MOBILE_NUMBER_SHOULD_NOT_BE_EMPTY);
                edtMobile.setError(validationMessage);
                isValid = false;
            } else if (!Utility.isValidMobileNumber(user.getMobileNumber())) {
                validationMessage = getMessage(R.string.INVALID_MOBILE_NUMBER);
                edtAddress.setError(validationMessage);
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
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropImageActivity();
            }
        });
    }

    private void onClickCancelProfile() {
        imgCancleprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgProfile.setImageResource(R.drawable.dummy_user_profile);
                profileUri = null;
                bitmap = null;
                profileImage = "";
                imgCancleprofile.setVisibility(View.GONE);
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
                                imgCancleprofile.setVisibility(View.VISIBLE);
                                if (bitmapImg != null)
                                    imgProfile.setImageBitmap(bitmapImg);
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
                AppSingleton.getInstance(UpdatePersonelDetailsActivity.this).updateProgress(1, 1, 100);
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(getApplicationContext(), getMessage(R.string.data_updation_fails_message));
            }
        });
    }
}//rnd of activity
