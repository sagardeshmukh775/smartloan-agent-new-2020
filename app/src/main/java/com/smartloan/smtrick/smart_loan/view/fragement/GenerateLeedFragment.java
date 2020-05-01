package com.smartloan.smtrick.smart_loan.view.fragement;

import android.Manifest;
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
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.databinding.FragmentGenerateleadBinding;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.helper.RuntimePermissionHelper;
import com.smartloan.smtrick.smart_loan.interfaces.ItemRemoveListner;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.History;
import com.smartloan.smtrick.smart_loan.models.LeadActivitiesModel;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.models.ViewImageModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.service.ImageUploadIntentService;
import com.smartloan.smtrick.smart_loan.singleton.AppSingleton;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapter.ViewImageAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;
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

import static android.app.Activity.RESULT_OK;
import static com.smartloan.smtrick.smart_loan.constants.Constant.CALANDER_DATE_OF_BIRTH_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.FEMALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEEDS_TABLE_REF;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_PREFIX;
import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_GENERATED;

public class GenerateLeedFragment extends RuntimePermissionHelper implements AdapterView.OnItemSelectedListener, ItemRemoveListner {
    private OnFragmentInteractionListener mListener;
    FragmentGenerateleadBinding fragmentGenerateleadBinding;
    AppSharedPreference appSharedPreference;
    LeedRepository leedRepository;
    AppSingleton appSingleton;
    ProgressDialogClass progressDialogClass;
    ArrayList<ViewImageModel> imagesUriList;
    Context context;
    ImageUploadReceiver imageUploadReceiver;
    ViewImageAdapter viewImageAdapter;
    private Uri profileUri;
    private static final int REQUEST_PERMISSIONS = 7000;
    int fromYear, fromMonth, fromDay;
    private long fromDate;

    public GenerateLeedFragment() {
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
            String[] balanceTransferType = appSingleton.getBalanceTransferType();
            fragmentGenerateleadBinding.spinnerselecttypeofemployee.setOnItemSelectedListener(this);
            fragmentGenerateleadBinding.spinnerBalanceTransferType.setOnItemSelectedListener(this);
            // ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this, sppinner_layout_listitem,loanType);

            ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, loanType) {
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    //change the color to which ever you want
                    ((TextView) view).setTextColor(Color.WHITE);
                    return view;
                }
            };
            spinnerArrayAdapterloantype.setDropDownViewResource(R.layout.spinner_item);
            fragmentGenerateleadBinding.spinnerselectloantype.setAdapter(spinnerArrayAdapterloantype);
            fragmentGenerateleadBinding.spinnerselectloantype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (position == 3) {
                        fragmentGenerateleadBinding.spinnerBalanceTransferType.setVisibility(View.VISIBLE);
                    } else {
                        fragmentGenerateleadBinding.spinnerBalanceTransferType.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            ArrayAdapter<String> spinnerAdapterBalanceTransferType = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, balanceTransferType) {
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    //change the color to which ever you want
                    ((TextView) view).setTextColor(Color.WHITE);
                    return view;
                }
            };
            spinnerAdapterBalanceTransferType.setDropDownViewResource(R.layout.spinner_item);
            fragmentGenerateleadBinding.spinnerBalanceTransferType.setAdapter(spinnerAdapterBalanceTransferType);

            ArrayAdapter<String> spinnerArrayAdapteremptype = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, empType) {
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    //change the color to which ever you want
                    ((TextView) view).setTextColor(Color.WHITE);
                    return view;
                }
            };
            spinnerArrayAdapteremptype.setDropDownViewResource(R.layout.spinner_item);
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
            setFromDateClickListner();
            formatNumber();
        }
        return fragmentGenerateleadBinding.getRoot();
    }//end of onCreateView

    private void formatNumber() {
        fragmentGenerateleadBinding.edittextexloanammount.addTextChangedListener(new TextWatcher() {
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
                ((DecimalFormat) nf2).applyPattern("##,##,###.##");
                s.replace(0, s.length(), nf2.format(s1));

                if (s.toString().equals("0")) {
                    fragmentGenerateleadBinding.edittextexloanammount.setText("");
                }
                isEdiging = false;
            }
        });
    }

    private void setImageViewAdapter(ArrayList<ViewImageModel> imagesUriList) {
        if (imagesUriList == null || imagesUriList.isEmpty()) {
            fragmentGenerateleadBinding.rvDocumentImages.setVisibility(View.GONE);
        } else {
            fragmentGenerateleadBinding.rvDocumentImages.setVisibility(View.VISIBLE);
            if (viewImageAdapter == null) {
                viewImageAdapter = new ViewImageAdapter(context, imagesUriList, this, false);
                fragmentGenerateleadBinding.rvDocumentImages.setAdapter(viewImageAdapter);
            } else {
                viewImageAdapter.reload(imagesUriList);
            }
        }
    }

    @Override
    public void itemRemoved(int postion) {
        setImageCount();
    }

    @Override
    public void itemRemoveFromDatabase(String leedId, String documentId) {

    }

    private void onClickAttachDocuments() {
        fragmentGenerateleadBinding.layoutattachdocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FishBun.with(GenerateLeedFragment.this)
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
                    createLeadActivity(leedsModel);
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

    private void createLeadActivity(LeedsModel leedsModel) {
        leedRepository.createLeadActivity(getLeadActivityModel(leedsModel), new CallBack() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void OnSuccessResult() {
        Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
        mListener.changeFragement(new LeedsFragment());
        progressDialogClass.dismissDialog();
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
                    Intent intentToUpload = new Intent(getActivity(), ImageUploadIntentService.class);
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
            } else if (fragmentGenerateleadBinding.spinnerselectloantype.getSelectedItemPosition() == 3) {
                if (fragmentGenerateleadBinding.spinnerBalanceTransferType.getSelectedItemPosition() == 0) {
                    validationMessage = getString(R.string.balance_transfer_loan_type_error);
                    Utility.showSnackBar(getActivity(), fragmentGenerateleadBinding.edittextmobile, validationMessage);
                    isValid = false;
                }
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
        if (fragmentGenerateleadBinding.spinnerselectloantype.getSelectedItemPosition() == 3) {
            leedsModel.setBalanceTransferLoanType(fragmentGenerateleadBinding.spinnerBalanceTransferType.getSelectedItem().toString());
        } else {
            leedsModel.setBalanceTransferLoanType(null);
        }
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
       /* History history = new History();
        history.setStatus(STATUS_GENERATED);
        history.setUpdatedByName(appSharedPreference.getUserName());
        history.setUpdatedbyId(appSharedPreference.getAgeniId());
        Map<String, History> historyMap = new HashMap<>();
        historyMap.put(LEEDS_TABLE_REF.push().getKey(), history);
        leedsModel.setHistory(historyMap);*/
        return leedsModel;
    }

    private LeadActivitiesModel getLeadActivityModel(LeedsModel leedsModel) {
        LeadActivitiesModel leadActivitiesModel = new LeadActivitiesModel();
        leadActivitiesModel.setActivityDoneByName(appSharedPreference.getUserName());
        leadActivitiesModel.setActivityTitle(Constant.LEAD_GENERATED);
        leadActivitiesModel.setActivityType(Constant.ACTIVITY_GENERATED);
        leadActivitiesModel.setActivityNote(Constant.LEAD_GENERATED_NOTE + leedsModel.getLoanType());
        leadActivitiesModel.setLeadId(leedsModel.getLeedId());
        return leadActivitiesModel;
    }

    private void setImageCount() {
        if (imagesUriList != null && !imagesUriList.isEmpty()) {
            fragmentGenerateleadBinding.rvDocumentImages.setVisibility(View.VISIBLE);
            fragmentGenerateleadBinding.textviewAttachedFileCount.setVisibility(View.VISIBLE);
            fragmentGenerateleadBinding.textviewAttachedFileCount.setText((context.getString(R.string.file_attached) + imagesUriList.size()));
        } else {
            fragmentGenerateleadBinding.rvDocumentImages.setVisibility(View.GONE);
            fragmentGenerateleadBinding.textviewAttachedFileCount.setVisibility(View.GONE);
        }
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
                        setImageCount();
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

    private void setFromDateClickListner() {
        setFromCurrentDate();
        fragmentGenerateleadBinding.edittextdob.setOnClickListener(new View.OnClickListener() {
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
                        fragmentGenerateleadBinding.edittextdob.setText(formatedDate);
                        fromDay = selectedday;
                        fromMonth = selectedmonth;
                        fromYear = selectedyear;
                        fromDate = Utility.convertFormatedDateToMilliSeconds(formatedDate, CALANDER_DATE_OF_BIRTH_FORMATE);
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
