package com.smartloan.smtrick.smart_loan.view.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.view.activite.MainActivity;

public class BankDetailsFragment extends Fragment implements View.OnClickListener {
    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    public BankDetailsFragment() {
    }

    TextView txtbankname, txtbranchname, txtbankaccountholdername, txtacountnumber, txtifsc;
    ImageView imgEdit;
    AppSharedPreference appSharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bank_details, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Loan Calculator");
        }

        appSharedPreference = new AppSharedPreference(getContext());

        txtbankname = view.findViewById(R.id.txtbanknamevalue);
        txtbranchname = view.findViewById(R.id.txtbranchnamevalue);
        txtbankaccountholdername = view.findViewById(R.id.txtacctholdervalue);
        txtacountnumber = view.findViewById(R.id.txtacctnumbervalue);
        txtifsc = view.findViewById(R.id.txtifscvalue);
        imgEdit = view.findViewById(R.id.edit);

        getUser();
        imgEdit.setOnClickListener(this);
        return view;
    }

    private void getUser() {
        txtbankname.setText(appSharedPreference.getBankName());
        txtbranchname.setText(appSharedPreference.getBranchName());
        txtbankaccountholdername.setText(appSharedPreference.getAccountHolderName());
        txtacountnumber.setText(appSharedPreference.getAccountNumber());
        txtifsc.setText(appSharedPreference.getIfsc());
    }


    @Override
    public void onClick(View v) {
        if (v == imgEdit) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
