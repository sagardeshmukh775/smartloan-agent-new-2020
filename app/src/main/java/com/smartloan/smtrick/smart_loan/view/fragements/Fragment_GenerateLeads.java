package com.smartloan.smtrick.smart_loan.view.fragements;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.databinding.FragmentGenerateleadBinding;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.singleton.AppSingleton;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

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

    public Fragment_GenerateLeads() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentGenerateleadBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_generatelead, container, false);
        if (mListener != null) {
            mListener.onFragmentInteraction("New Lead");
        }
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
        return fragmentGenerateleadBinding.getRoot();
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
                    Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
                    mListener.changeFragement(new Fragment_LeadsActivity());
                    progressDialogClass.dismissDialog();
                }

                @Override
                public void onError(Object object) {
                    progressDialogClass.dismissDialog();
                    Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_fails_message));
                }
            });
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
}
