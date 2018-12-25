package com.smartloan.smtrick.smart_loan.view.fragements;

import android.Manifest;
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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.FragmentGenerateleadBinding;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.helper.RuntimePermissionHelper;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.History;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.service.ImageUploadIntentService;
import com.smartloan.smtrick.smart_loan.singleton.AppSingleton;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapters.ViewImageAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.smartloan.smtrick.smart_loan.constants.Constant.FEMALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEEDS_TABLE_REF;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_PREFIX;
import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_GENERATED;

public class Fragment_GenerateLeads extends RuntimePermissionHelper implements AdapterView.OnItemSelectedListener {
    private OnFragmentInteractionListener mListener;
    FragmentGenerateleadBinding fragmentGenerateleadBinding;
    AppSharedPreference appSharedPreference;
    LeedRepository leedRepository;
    AppSingleton appSingleton;
    ProgressDialogClass progressDialogClass;
    ArrayList<Uri> imagesUriList;
    Context context;
    ImageUploadReceiver imageUploadReceiver;
    ViewImageAdapter viewImageAdapter;
    private Uri profileUri;
    private static final int REQUEST_PERMISSIONS = 7000;

    public Fragment_GenerateLeads() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentGenerateleadBinding == null) {
            fragmentGenerateleadBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_generatelead, container, false);
            if (mListener != null) {
                mListener.onFragmentInteraction("New Lead");
            }
            context = getActivity();
            progressDialogClass = new ProgressDialogClass(getActivity());
            appSingleton = AppSingleton.getInstance(getActivity());
            leedRepository = new LeedRepositoryImpl();
            appSharedPreference = new AppSharedPreference(getActivity());
            String[] loanType = appSingleton.getLoanType();
            String[] empType = appSingleton.getEmployeeType();
            fragmentGenerateleadBinding.spinnerselectloantype.setOnItemSelectedListener(this);
            fragmentGenerateleadBinding.spinnerselecttypeofemployee.setOnItemSelectedListener(this);
            // ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this, sppinner_layout_listitem,loanType);
            ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, loanType);
            spinnerArrayAdapterloantype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            fragmentGenerateleadBinding.spinnerselectloantype.setAdapter(spinnerArrayAdapterloantype);
            ArrayAdapter<String> spinnerArrayAdapteremptype = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, empType);
            spinnerArrayAdapteremptype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            fragmentGenerateleadBinding.spinnerselecttypeofemployee.setAdapter(spinnerArrayAdapteremptype);
            fragmentGenerateleadBinding.rvDocumentImages.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            fragmentGenerateleadBinding.rvDocumentImages.setLayoutManager(layoutManager);
            fragmentGenerateleadBinding.rvDocumentImages.setItemAnimator(new DefaultItemAnimator());
           /* fragmentGenerateleadBinding.rvDocumentImages.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.HORIZONTAL));*/
            onClickGenerateLead();
            onClickCallExpart();
            onClickAttachDocuments();
            onClickSelectProfile();
            onClickCancelProfile();
        }
        return fragmentGenerateleadBinding.getRoot();
    }//end of onCreateView

    private void setImageViewAdapter(ArrayList<Uri> imagesUriList) {
        if (imagesUriList == null || imagesUriList.isEmpty()) {
            fragmentGenerateleadBinding.rvDocumentImages.setVisibility(View.GONE);
        } else {
            fragmentGenerateleadBinding.rvDocumentImages.setVisibility(View.VISIBLE);
            if (viewImageAdapter == null) {
                viewImageAdapter = new ViewImageAdapter(context, imagesUriList);
                fragmentGenerateleadBinding.rvDocumentImages.setAdapter(viewImageAdapter);
            } else {
                viewImageAdapter.reload(imagesUriList);
            }
        }
    }

    private void onClickAttachDocuments() {
        fragmentGenerateleadBinding.layoutattachdocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FishBun.with(Fragment_GenerateLeads.this)
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // NOTE: This is the part that usually gives you the error
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void onClickGenerateLead() {
        fragmentGenerateleadBinding.buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateLeed();
            }
        });
    }

    private void onClickCallExpart() {
        fragmentGenerateleadBinding.buttonCallExpart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", Constant.SUPPORT_PHONE_NUMBER, null)));
            }
        });
    }

    private void onClickSelectProfile() {
        fragmentGenerateleadBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropImageActivity();
            }
        });
    }

    private void onClickCancelProfile() {
        fragmentGenerateleadBinding.ivCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateleadBinding.ivProfile.setImageResource(R.drawable.dummy_user_profile);
                profileUri = null;
                fragmentGenerateleadBinding.ivCancelProfile.setVisibility(View.GONE);
            }
        });
    }

    private void generateLeed() {
        LeedsModel leedsModel = fillUserModel();
        if (validate(leedsModel)) {
            progressDialogClass.showDialog(this.getString(R.string.leed_In_loading), this.getString(R.string.PLEASE_WAIT));
            leedRepository.createLeed(leedsModel, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    // if (imagesUriList == null || imagesUriList.isEmpty())
                    OnSuccessResult();
                }

                @Override
                public void onError(Object object) {
                    if (progressDialogClass != null)
                        progressDialogClass.dismissDialog();
                    Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_fails_message));
                }
            });
            uploadImages(leedsModel.getLeedId());
        }
    }

    private void OnSuccessResult() {
        Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
        mListener.changeFragement(new Fragment_LeadsActivity());
        progressDialogClass.dismissDialog();
    }

    private void uploadImages(String leedId) {
        try {
            if (profileUri != null) {
                if (imagesUriList == null)
                    imagesUriList = new ArrayList<>();
                imagesUriList.add(profileUri);
            }
            if (imagesUriList != null && !imagesUriList.isEmpty()) {
                int count = 0;
                for (Uri uri : imagesUriList) {
                    count += 1;
                    Intent intentToUpload = new Intent(getActivity(), ImageUploadIntentService.class);
                    intentToUpload.putExtra(Constant.BITMAP_IMG, uri);
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
                fragmentGenerateleadBinding.edittextname.setError(validationMessage);
                isValid = false;
            }
            if (!Utility.isValidMobileNumber(leedsModel.getMobileNumber())) {
                validationMessage = getString(R.string.INVALID_MOBILE_NUMBER);
                fragmentGenerateleadBinding.edittextmobile.setError(validationMessage);
                isValid = false;
            }
            if (!Utility.isEmptyOrNull(leedsModel.getEmail()) && !Utility.isValidEmail(leedsModel.getEmail())) {
                validationMessage = getString(R.string.INVALID_EMAIL);
                fragmentGenerateleadBinding.edittextemailid.setError(validationMessage);
                isValid = false;
            }
            if (fragmentGenerateleadBinding.spinnerselectloantype.getSelectedItemPosition() == 0) {
                validationMessage = getString(R.string.loan_type_error);
                Utility.showSnackBar(getActivity(), fragmentGenerateleadBinding.edittextmobile, validationMessage);
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
            ExceptionUtil.logException(e);
        }
        return isValid;
    }


    private LeedsModel fillUserModel() {
        LeedsModel leedsModel = new LeedsModel();
        leedsModel.setLeedId(LEEDS_TABLE_REF.push().getKey());
        leedsModel.setCustomerName(fragmentGenerateleadBinding.edittextname.getText().toString());
        leedsModel.setMobileNumber(fragmentGenerateleadBinding.edittextmobile.getText().toString());
        leedsModel.setAlternetMobileNumber(fragmentGenerateleadBinding.etAlternetMobile.getText().toString());
        leedsModel.setAddress(fragmentGenerateleadBinding.edittextaddress.getText().toString());
        leedsModel.setEmail(fragmentGenerateleadBinding.edittextemailid.getText().toString());
        leedsModel.setPanCardNumber(fragmentGenerateleadBinding.edittextpannumber.getText().toString());
        leedsModel.setDateOfBirth(fragmentGenerateleadBinding.edittextdob.getText().toString());
        leedsModel.setExpectedLoanAmount(fragmentGenerateleadBinding.edittextexloanammount.getText().toString());
        leedsModel.setLoanType(fragmentGenerateleadBinding.spinnerselectloantype.getSelectedItem().toString());
        leedsModel.setOccupation(fragmentGenerateleadBinding.spinnerselecttypeofemployee.getSelectedItem().toString());
        if (fragmentGenerateleadBinding.radiomale.isChecked())
            leedsModel.setGender(MALE);
        else
            leedsModel.setGender(FEMALE);
        leedsModel.setLeedNumber(Utility.generateAgentId(LEED_PREFIX));
        leedsModel.setAgentId(appSharedPreference.getAgeniId());
        leedsModel.setAgentUserId(appSharedPreference.getUserId());
        leedsModel.setAgentName(appSharedPreference.getUserName());
        leedsModel.setCreatedBy(appSharedPreference.getUserId());
        leedsModel.setStatus(STATUS_GENERATED);
        History history = new History();
        history.setStatus(STATUS_GENERATED);
        history.setUpdatedByName(appSharedPreference.getUserName());
        history.setUpdatedbyId(appSharedPreference.getAgeniId());
        Map<String, History> historyMap = new HashMap<>();
        historyMap.put(LEEDS_TABLE_REF.push().getKey(), history);
        leedsModel.setHistory(historyMap);
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
                        imagesUriList = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                        setImageViewAdapter(imagesUriList);
                        if (imagesUriList != null && !imagesUriList.isEmpty()) {
                            fragmentGenerateleadBinding.textviewAttachedFileCount.setVisibility(View.VISIBLE);
                            fragmentGenerateleadBinding.textviewAttachedFileCount.setText((context.getString(R.string.file_attached) + imagesUriList.size()));
                        } else {
                            fragmentGenerateleadBinding.textviewAttachedFileCount.setVisibility(View.GONE);
                        }
                        // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
                        break;
                    }
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(imageData);
                    if (resultCode == RESULT_OK) {
                        if (imageData != null) {
                            Bundle extras = imageData.getExtras();
                            if (extras != null) {
                                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                                profileUri = result.getUri();
                                fragmentGenerateleadBinding.ivCancelProfile.setVisibility(View.VISIBLE);
                                if (bitmapImg != null)
                                    fragmentGenerateleadBinding.ivProfile.setImageBitmap(bitmapImg);
                            }
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Utility.showMessage(getActivity(), "Cropping failed: " + result.getError());
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
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(imageUploadReceiver, filter);
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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(imageUploadReceiver);
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
                    .start(getActivity());
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    public void permissionCheck() {
        super.requestAppPermissions(getActivity(), new String[]{Manifest.permission.CAMERA
        }, Constant.REQUEST_CODE, REQUEST_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(final int requestCode) {
        if (checkPermissionGranted(Manifest.permission.CAMERA, getActivity()) && requestCode == REQUEST_PERMISSIONS) {
        }
    }
}
