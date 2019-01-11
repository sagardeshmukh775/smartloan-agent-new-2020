package com.smartloan.smtrick.smart_loan.view.activites;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.ActivityUpdateLeedBinding;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.interfaces.ItemRemoveListner;
import com.smartloan.smtrick.smart_loan.models.History;
import com.smartloan.smtrick.smart_loan.models.ImagesModel;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.models.ViewImageModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.service.ImageUploadIntentService;
import com.smartloan.smtrick.smart_loan.singleton.AppSingleton;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapters.ViewImageAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.smartloan.smtrick.smart_loan.constants.Constant.CALANDER_DATE_OF_BIRTH_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.FEMALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEEDS_TABLE_REF;
import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_UPDATED;

public class UpdateLeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ItemRemoveListner {
    ActivityUpdateLeedBinding activityUpdateLeedBinding;
    AppSharedPreference appSharedPreference;
    LeedRepository leedRepository;
    AppSingleton appSingleton;
    ProgressDialogClass progressDialogClass;
    ArrayList<ViewImageModel> imagesUriList;
    Context context;
    ImageUploadReceiver imageUploadReceiver;
    ViewImageAdapter viewImageAdapter, preViewImageAdapter;
    private Uri profileUri;
    private LeedsModel leedsModel;
    int fromYear, fromMonth, fromDay;
    private long fromDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpdateLeedBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_leed);
        context = this;
        progressDialogClass = new ProgressDialogClass(this);
        appSingleton = AppSingleton.getInstance(this);
        leedRepository = new LeedRepositoryImpl();
        appSharedPreference = new AppSharedPreference(this);
        String[] loanType = appSingleton.getLoanType();
        String[] empType = appSingleton.getEmployeeType();
        activityUpdateLeedBinding.spinnerselectloantype.setOnItemSelectedListener(this);
        activityUpdateLeedBinding.spinnerselecttypeofemployee.setOnItemSelectedListener(this);
        // ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this, sppinner_layout_listitem,loanType);
        ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, loanType);
        spinnerArrayAdapterloantype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        activityUpdateLeedBinding.spinnerselectloantype.setAdapter(spinnerArrayAdapterloantype);
        ArrayAdapter<String> spinnerArrayAdapteremptype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, empType);
        spinnerArrayAdapteremptype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        activityUpdateLeedBinding.spinnerselecttypeofemployee.setAdapter(spinnerArrayAdapteremptype);
        activityUpdateLeedBinding.rvDocumentImages.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        activityUpdateLeedBinding.rvDocumentImages.setLayoutManager(layoutManager);
        activityUpdateLeedBinding.rvDocumentImages.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        activityUpdateLeedBinding.rvPreDocumentImages.setHasFixedSize(true);
        activityUpdateLeedBinding.rvPreDocumentImages.setLayoutManager(layoutManager2);
        activityUpdateLeedBinding.rvPreDocumentImages.setItemAnimator(new DefaultItemAnimator());
        leedsModel = (LeedsModel) getIntent().getSerializableExtra(Constant.LEED_MODEL);
        setToolBar();
        onClickGenerateLead();
        onClickAttachDocuments();
        onClickSelectProfile();
        onClickCancelProfile();
        setFromDateClickListner();
        formatNumber();
        setLeedData(leedsModel);
    }//end of activity

    private void formatNumber() {
        activityUpdateLeedBinding.edittextexloanammount.addTextChangedListener(new TextWatcher() {
            boolean isEdiging;

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (isEdiging) return;
                isEdiging = true;
                String str = s.toString().replaceAll("[^\\d]", "");
                double s1 = 0;
                try {
                    s1 = Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
                ((DecimalFormat) nf2).applyPattern("###,###.###");
                s.replace(0, s.length(), nf2.format(s1));

                if (s.toString().equals("0")) {
                    activityUpdateLeedBinding.edittextexloanammount.setText("");
                }
                isEdiging = false;
            }
        });
    }

    public void setToolBar() {
        Toolbar tb = activityUpdateLeedBinding.toolbar;
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            if (leedsModel != null && !Utility.isEmptyOrNull(leedsModel.getLeedNumber()))
                ab.setTitle(leedsModel.getLeedNumber());
            else
                ab.setTitle("Update Leed");
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setLeedData(LeedsModel leedsModel) {
        if (leedsModel != null) {
            if (!Utility.isEmptyOrNull(leedsModel.getCustomerName()))
                activityUpdateLeedBinding.edittextname.setText(leedsModel.getCustomerName());
            if (!Utility.isEmptyOrNull(leedsModel.getMobileNumber()))
                activityUpdateLeedBinding.edittextmobile.setText(leedsModel.getMobileNumber());
            if (!Utility.isEmptyOrNull(leedsModel.getAlternetMobileNumber()))
                activityUpdateLeedBinding.etAlternetMobile.setText(leedsModel.getAlternetMobileNumber());
            if (!Utility.isEmptyOrNull(leedsModel.getAddress()))
                activityUpdateLeedBinding.edittextaddress.setText(leedsModel.getAddress());
            if (!Utility.isEmptyOrNull(leedsModel.getEmail()))
                activityUpdateLeedBinding.edittextemailid.setText(leedsModel.getEmail());
            if (!Utility.isEmptyOrNull(leedsModel.getPanCardNumber()))
                activityUpdateLeedBinding.edittextpannumber.setText(leedsModel.getPanCardNumber());
            if (!Utility.isEmptyOrNull(leedsModel.getExpectedLoanAmount()))
                activityUpdateLeedBinding.edittextpannumber.setText(leedsModel.getExpectedLoanAmount());
            if (!Utility.isEmptyOrNull(leedsModel.getDateOfBirth()))
                activityUpdateLeedBinding.edittextdob.setText(leedsModel.getDateOfBirth());
            if (!Utility.isEmptyOrNull(leedsModel.getCustomerImagelarge())) {
                Picasso.with(context).load(leedsModel.getCustomerImagelarge()).resize(200, 200).centerCrop().placeholder(R.drawable.dummy_user_profile).into(activityUpdateLeedBinding.ivProfile);
                activityUpdateLeedBinding.ivCancelProfile.setVisibility(View.VISIBLE);
            } else
                activityUpdateLeedBinding.ivProfile.setImageResource(R.drawable.dummy_user_profile);
            if (!Utility.isEmptyOrNull(leedsModel.getGender())) {
                if (leedsModel.getGender().equalsIgnoreCase(MALE))
                    activityUpdateLeedBinding.radiomale.setChecked(true);
                else
                    activityUpdateLeedBinding.radiofemale.setChecked(true);
            }
            if (!Utility.isEmptyOrNull(leedsModel.getLoanType())) {
                for (int i = 0; i < appSingleton.getLoanType().length; i++) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(appSingleton.getLoanType()[i])) {
                        activityUpdateLeedBinding.spinnerselectloantype.setSelection(i);
                        break;
                    }
                }
            }
            if (!Utility.isEmptyOrNull(leedsModel.getOccupation())) {
                for (int i = 0; i < appSingleton.getEmployeeType().length; i++) {
                    if (leedsModel.getOccupation().equalsIgnoreCase(appSingleton.getEmployeeType()[i])) {
                        activityUpdateLeedBinding.spinnerselecttypeofemployee.setSelection(i);
                        break;
                    }
                }
            }
            if (leedsModel.getDocumentImages() != null) {
                ArrayList<ViewImageModel> imagesUriList = new ArrayList<>();
                for (Map.Entry<String, ImagesModel> entry : leedsModel.getDocumentImages().entrySet()) {
                    imagesUriList.add(new ViewImageModel(Uri.parse(entry.getValue().getLargImage()), leedsModel.getLeedId(), entry.getKey()));
                }
                setPreviewImageAdapter(imagesUriList);
            }
        }
    }//end of setleed model

    private void setPreviewImageAdapter(ArrayList<ViewImageModel> imagesUriList) {

        if (imagesUriList == null || imagesUriList.isEmpty()) {
            activityUpdateLeedBinding.rvPreDocumentImages.setVisibility(View.GONE);
        } else {
            activityUpdateLeedBinding.rvPreDocumentImages.setVisibility(View.VISIBLE);
            if (preViewImageAdapter == null) {
                preViewImageAdapter = new ViewImageAdapter(context, imagesUriList, this, true);
                activityUpdateLeedBinding.rvPreDocumentImages.setAdapter(preViewImageAdapter);
            } else {
                preViewImageAdapter.reload(imagesUriList);
            }
        }
    }

    @Override
    public void itemRemoved(int postion) {
        setImageCount();
    }

    @Override
    public void itemRemoveFromDatabase(String leedId, String documentId) {
        removeLeedDocumentImage(leedId, documentId);
    }

    private void setImageCount() {
        if (imagesUriList != null && !imagesUriList.isEmpty()) {
            activityUpdateLeedBinding.rvDocumentImages.setVisibility(View.VISIBLE);
            activityUpdateLeedBinding.textviewAttachedFileCount.setVisibility(View.VISIBLE);
            activityUpdateLeedBinding.textviewAttachedFileCount.setText((context.getString(R.string.file_attached) + imagesUriList.size()));
        } else {
            activityUpdateLeedBinding.rvDocumentImages.setVisibility(View.GONE);
            activityUpdateLeedBinding.textviewAttachedFileCount.setVisibility(View.GONE);
        }
    }

    private void removeLeedDocumentImage(String leedId, String documentId) {
        if (!Utility.isEmptyOrNull(leedId) && !Utility.isEmptyOrNull(documentId)) {
            Map<String, String> map = new HashMap<>();
            map.put(documentId, null);
            leedRepository.updateLeedDocuments(leedId, map, new CallBack() {
                @Override
                public void onSuccess(Object object) {

                }

                @Override
                public void onError(Object object) {

                }
            });
        }
    }

    private void setImageViewAdapter(ArrayList<ViewImageModel> imagesUriList) {
        if (imagesUriList == null || imagesUriList.isEmpty()) {
            activityUpdateLeedBinding.rvDocumentImages.setVisibility(View.GONE);
        } else {
            activityUpdateLeedBinding.rvDocumentImages.setVisibility(View.VISIBLE);
            if (viewImageAdapter == null) {
                viewImageAdapter = new ViewImageAdapter(context, imagesUriList, this, false);
                activityUpdateLeedBinding.rvDocumentImages.setAdapter(viewImageAdapter);
            } else {
                viewImageAdapter.reload(imagesUriList);
            }
        }
    }

    private void onClickAttachDocuments() {
        activityUpdateLeedBinding.layoutattachdocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FishBun.with(UpdateLeedActivity.this)
                        .setImageAdapter(new GlideAdapter())
                        .setIsUseDetailView(false)
                        .setPickerCount(5) //Deprecated
                        .setMaxCount(30)
                        .setMinCount(1)
                        .setPickerSpanCount(6)
                        .setActionBarColor(Color.parseColor("#594691"), Color.parseColor("#ffbf12"), false)
                        .setActionBarTitleColor(Color.parseColor("#ffffff"))
                        .setAlbumSpanCount(2, 4)
                        .setButtonInAlbumActivity(false)
                        .setCamera(true)
                        .setReachLimitAutomaticClose(true)
                        .setAllViewTitle("All")
                        .setActionBarTitle("Image Library")
                        .textOnImagesSelectionLimitReached("Limit Reached!")
                        .textOnNothingSelected("Nothing Selected")
                        .startAlbum();
            }
        });
    }

    private void onClickGenerateLead() {
        activityUpdateLeedBinding.buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateLeed();
            }
        });
    }

    private void onClickSelectProfile() {
        activityUpdateLeedBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropImageActivity();
            }
        });
    }

    private void onClickCancelProfile() {
        activityUpdateLeedBinding.ivCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leedsModel != null) {
                    leedsModel.setCustomerImageSmall("");
                    leedsModel.setCustomerImagelarge("");
                }
                activityUpdateLeedBinding.ivProfile.setImageResource(R.drawable.dummy_user_profile);
                profileUri = null;
                activityUpdateLeedBinding.ivCancelProfile.setVisibility(View.GONE);
            }
        });
    }

    private void generateLeed() {
        LeedsModel leedsModel = fillUserModel();
        if (validate(leedsModel)) {
            progressDialogClass.showDialog(this.getString(R.string.updating_leed), this.getString(R.string.PLEASE_WAIT));
            updateLeedHistory();
            leedRepository.updateLeed(leedsModel.getLeedId(), leedsModel.getUpdateLeedMap(), new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    // if (imagesUriList == null || imagesUriList.isEmpty())
                    OnSuccessResult();
                }

                @Override
                public void onError(Object object) {
                    if (progressDialogClass != null)
                        progressDialogClass.dismissDialog();
                    Utility.showLongMessage(context, getString(R.string.lead_generated_fails_message));
                }
            });
            uploadImages(leedsModel.getLeedId());
        }
    }


    private void updateLeedHistory() {
        leedRepository.updateLeedHistory(leedsModel.getLeedId(), getHistoryMap(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    private void OnSuccessResult() {
        Utility.showLongMessage(context, getString(R.string.lead_updated_success_message));
        progressDialogClass.dismissDialog();
        finish();
    }

    private void uploadImages(String leedId) {
        try {
            if (profileUri != null) {
                if (imagesUriList == null)
                    imagesUriList = new ArrayList<>();
                imagesUriList.add(new ViewImageModel(profileUri));
            }
            if (imagesUriList != null && !imagesUriList.isEmpty()) {
                int count = 0;
                for (ViewImageModel imageModel : imagesUriList) {
                    count += 1;
                    Intent intentToUpload = new Intent(context, ImageUploadIntentService.class);
                    intentToUpload.putExtra(Constant.BITMAP_IMG, imageModel.getImageUri());
                    if (profileUri != null && imagesUriList.size() == count)
                        intentToUpload.putExtra(Constant.STORAGE_PATH, Constant.CUSROMER_PROFILE_PATH);
                    else
                        intentToUpload.putExtra(Constant.STORAGE_PATH, Constant.DOCUMENTS_PATH);
                    intentToUpload.putExtra(Constant.LEED_ID, leedId);
                    intentToUpload.putExtra(Constant.IMAGE_COUNT, count);
                    intentToUpload.putExtra(Constant.TOTAL_IMAGE_COUNT, imagesUriList.size());
                    context.startService(intentToUpload);
                }
            }
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private boolean validate(LeedsModel leedsModel) {
        String validationMessage;
        boolean isValid = true;
        try {
            if (Utility.isEmptyOrNull(leedsModel.getCustomerName())) {
                validationMessage = getString(R.string.customer_name_should_be_empty);
                activityUpdateLeedBinding.edittextname.setError(validationMessage);
                isValid = false;
            }
            if (!Utility.isValidMobileNumber(leedsModel.getMobileNumber())) {
                validationMessage = getString(R.string.INVALID_MOBILE_NUMBER);
                activityUpdateLeedBinding.edittextmobile.setError(validationMessage);
                isValid = false;
            }
            if (!Utility.isEmptyOrNull(leedsModel.getEmail()) && !Utility.isValidEmail(leedsModel.getEmail())) {
                validationMessage = getString(R.string.INVALID_EMAIL);
                activityUpdateLeedBinding.edittextemailid.setError(validationMessage);
                isValid = false;
            }
            if (activityUpdateLeedBinding.spinnerselectloantype.getSelectedItemPosition() == 0) {
                validationMessage = getString(R.string.loan_type_error);
                Utility.showSnackBar(context, activityUpdateLeedBinding.edittextmobile, validationMessage);
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
            ExceptionUtil.logException(e);
        }
        return isValid;
    }

    private Map<String, History> getHistoryMap() {
        History history = new History();
        history.setStatus(STATUS_UPDATED);
        history.setUpdatedByName(appSharedPreference.getUserName());
        history.setUpdatedbyId(appSharedPreference.getAgeniId());
        Map<String, History> historyMap = new HashMap<>();
        historyMap.put(LEEDS_TABLE_REF.push().getKey(), history);
        return historyMap;
    }

    private LeedsModel fillUserModel() {
        LeedsModel leedsModel = new LeedsModel();
        if (this.leedsModel != null && !Utility.isEmptyOrNull(this.leedsModel.getLeedId())) {
            leedsModel.setLeedId(this.leedsModel.getLeedId());
            leedsModel.setCustomerImagelarge(this.leedsModel.getCustomerImagelarge());
            leedsModel.setCustomerImageSmall(this.leedsModel.getCustomerImageSmall());
        } else
            leedsModel.setLeedId(LEEDS_TABLE_REF.push().getKey());
        leedsModel.setCustomerName(activityUpdateLeedBinding.edittextname.getText().toString());
        leedsModel.setMobileNumber(activityUpdateLeedBinding.edittextmobile.getText().toString());
        leedsModel.setAlternetMobileNumber(activityUpdateLeedBinding.etAlternetMobile.getText().toString());
        leedsModel.setAddress(activityUpdateLeedBinding.edittextaddress.getText().toString());
        leedsModel.setEmail(activityUpdateLeedBinding.edittextemailid.getText().toString());
        leedsModel.setPanCardNumber(activityUpdateLeedBinding.edittextpannumber.getText().toString());
        leedsModel.setDateOfBirth(activityUpdateLeedBinding.edittextdob.getText().toString());
        leedsModel.setExpectedLoanAmount(activityUpdateLeedBinding.edittextexloanammount.getText().toString());
        leedsModel.setLoanType(activityUpdateLeedBinding.spinnerselectloantype.getSelectedItem().toString());
        leedsModel.setOccupation(activityUpdateLeedBinding.spinnerselecttypeofemployee.getSelectedItem().toString());
        if (activityUpdateLeedBinding.radiomale.isChecked())
            leedsModel.setGender(MALE);
        else
            leedsModel.setGender(FEMALE);
        return leedsModel;
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        try {
            switch (requestCode) {
                case Define.ALBUM_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                        // you can get an image path(ArrayList<String>) on <0.6.2
                        ArrayList<Uri> uriList = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                        if (imagesUriList == null)
                            imagesUriList = new ArrayList<>();
                        imagesUriList.clear();
                        if (uriList != null && !uriList.isEmpty()) {
                            for (Uri uri : uriList) {
                                imagesUriList.add(new ViewImageModel(uri));
                            }
                        }
                        setImageViewAdapter(imagesUriList);
                        setImageCount();                        // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
                        break;
                    }
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(imageData);
                    if (resultCode == RESULT_OK) {
                        if (imageData != null) {
                            Bundle extras = imageData.getExtras();
                            if (extras != null) {
                                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(context.getContentResolver(), result.getUri());
                                profileUri = result.getUri();
                                activityUpdateLeedBinding.ivCancelProfile.setVisibility(View.VISIBLE);
                                if (bitmapImg != null)
                                    activityUpdateLeedBinding.ivProfile.setImageBitmap(bitmapImg);
                            }
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Utility.showMessage(context, "Cropping failed: " + result.getError());
                    }
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private void setReceiver() {
        try {
            IntentFilter filter = new IntentFilter(ImageUploadReceiver.PROCESS_RESPONSE);
            imageUploadReceiver = new ImageUploadReceiver();
            LocalBroadcastManager.getInstance(context).registerReceiver(imageUploadReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        setReceiver();
        super.onStart();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(imageUploadReceiver);
        super.onStop();
    }

    public class ImageUploadReceiver extends BroadcastReceiver {
        public static final String PROCESS_RESPONSE = "com.smartloan.smtrick.smart_loan.intent.action.UPLOADIMAGE";

        @Override
        public void onReceive(Context context, Intent intent) {
            int imageCount = intent.getIntExtra(Constant.IMAGE_COUNT, 0);
            if (imageCount == imagesUriList.size() - 1) {
                OnSuccessResult();
            }
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

    private void setFromDateClickListner() {
        setFromCurrentDate();
        activityUpdateLeedBinding.edittextdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_OF_BIRTH_FORMATE, Locale.FRANCE);
                        String formatedDate = sdf.format(myCalendar.getTime());
                        activityUpdateLeedBinding.edittextdob.setText(formatedDate);
                        fromDay = selectedday;
                        fromMonth = selectedmonth;
                        fromYear = selectedyear;
                    }
                }, fromYear, fromMonth, fromDay);
                mDatePicker.show();
            }
        });
    }

    private void setFromCurrentDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        fromYear = mcurrentDate.get(Calendar.YEAR);
        fromMonth = mcurrentDate.get(Calendar.MONTH);
        fromDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }
}
