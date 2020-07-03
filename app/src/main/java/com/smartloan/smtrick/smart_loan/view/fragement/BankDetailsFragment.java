package com.smartloan.smtrick.smart_loan.view.fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;

public class BankDetailsFragment extends Fragment {
    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    public BankDetailsFragment() {
    }

    TextView txtbankname, txtbranchname, txtbankaccountholdername, txtacountnumber, txtifsc;

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

        txtbankname = view.findViewById(R.id.txtbanknamevalue);
        txtbranchname = view.findViewById(R.id.txtbranchnamevalue);
        txtbankaccountholdername = view.findViewById(R.id.txtacctholdervalue);
        txtacountnumber = view.findViewById(R.id.txtacctnumbervalue);
        txtifsc = view.findViewById(R.id.txtifscvalue);

        getUser();

        return view;
    }

    private void getUser() {

    }


}
