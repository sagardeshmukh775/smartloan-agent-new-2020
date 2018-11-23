package com.smartloan.smtrick.smart_loan.view.fragements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.service.ImageUploadIntentService;
import com.smartloan.smtrick.smart_loan.singleton.AppSingleton;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.smartloan.smtrick.smart_loan.constants.Constant.FEMALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEEDS_TABLE_REF;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_PREFIX;
import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_GENERATED;

public class Fragment_GenerateLeads extends Fragment implements AdapterView.OnItemSelectedListener {
    private OnFragmentInteractionListener mListener;
    FragmentGenerateleadBinding fragmentGenerateleadBinding;
    AppSharedPreference appSharedPreference;
    LeedRepository leedRepository;
    AppSingleton appSingleton;
    ProgressDialogClass progressDialogClass;
    ArrayList<Uri> imagesUriList;
    Context context;
    ImageUploadReceiver imageUploadReceiver;

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
            onClickGenerateLead();
            onClickAttachDocuments();
        }
        return fragmentGenerateleadBinding.getRoot();
    }//end of onCreateView

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
            if (imagesUriList != null && !imagesUriList.isEmpty()) {
                int count = 0;
                for (Uri uri : imagesUriList) {
                    count += 1;
                    Intent intentToUpload = new Intent(getActivity(), ImageUploadIntentService.class);
                    intentToUpload.putExtra(Constant.BITMAP_IMG, uri);
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
        return leedsModel;
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2
                    imagesUriList = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);

                    if (imagesUriList != null && !imagesUriList.isEmpty()) {
                        fragmentGenerateleadBinding.textviewAttachedFileCount.setVisibility(View.VISIBLE);
                        fragmentGenerateleadBinding.textviewAttachedFileCount.setText((context.getString(R.string.file_attached) + imagesUriList.size()));
                    } else {
                        fragmentGenerateleadBinding.textviewAttachedFileCount.setVisibility(View.GONE);
                    }
                    // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
                    break;
                }
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
}
