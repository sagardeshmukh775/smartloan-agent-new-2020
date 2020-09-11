package com.smartloan.smtrick.smart_loan.view.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.databinding.FragmentDashboardBinding;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;

public class DashbordFragment_new extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;
    private TextView txtNumberOfLeeds, txtLoanRequiredAmount, txtTotalFilesLogin, txtFilesRejected, txtFilesSanctioned, txtSanctionedAmount, txtFilesDisbursed,
            txtTotalDisbursementAmount, txtTotalDisbursedPayoutAmt, txtTotalPayoutPaid, txtTotalBalancePayout;
    private FloatingActionButton fabAddLead;

    public DashbordFragment_new() {
    }

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_new, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Dash Bord");
        }

        txtNumberOfLeeds = view.findViewById(R.id.txtTotalLeeds);
        txtLoanRequiredAmount = view.findViewById(R.id.txtTotalLoanRequirementAmount);
        txtTotalFilesLogin = view.findViewById(R.id.txtTotalfilesLogin);
        txtFilesRejected = view.findViewById(R.id.txtrejectedfiles);
        txtFilesSanctioned = view.findViewById(R.id.txtTotalfilessanctioned);
        txtSanctionedAmount = view.findViewById(R.id.txtTotalSanctionedAmount);
        txtFilesDisbursed = view.findViewById(R.id.txtTotaldisbursedfiles);
        txtTotalDisbursementAmount = view.findViewById(R.id.txtTotalDisburstmentAmount);
        txtTotalDisbursedPayoutAmt = view.findViewById(R.id.txtTotaldisbursepayoutamount);
        txtTotalPayoutPaid = view.findViewById(R.id.txtTotalpayoutpaid);
        txtTotalBalancePayout = view.findViewById(R.id.txtTotalbalancepayout);

        fabAddLead = view.findViewById(R.id.fab_add_lead);

        addNewLeadClick();

        return view;
    }

    private void addNewLeadClick() {
        fabAddLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changeFragement(new GenerateLeedFragment());
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
}
