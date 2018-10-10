package com.smartloan.smtrick.smart_loan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Fragment_GenerateLeads extends Fragment implements AdapterView.OnItemSelectedListener {

   // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    public Fragment_GenerateLeads() {}

    Spinner spinloantype,spinemptype,spinincome;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_generatelead, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("New Lead");
        }

        // Here we will can create click listners etc for all the gui elements on the fragment.
        //Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(...

        String[] loanType = new String[]{"Select Loan Type","HL","LAP"};
       // String[] incomeCriteria = new String[]{"Select Income","below 200000","200000 to 500000","above 500000"};
        String[] empType = new String[]{"Select Occupation","Salaried","Businessman"};

        spinloantype = (Spinner) view.findViewById(R.id.spinnerselectloantype);
        spinemptype = (Spinner) view.findViewById(R.id.spinnerselecttypeofemployee);


        spinloantype.setOnItemSelectedListener(this);
        spinemptype.setOnItemSelectedListener(this);


       // ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this, sppinner_layout_listitem,loanType);
        ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, loanType);
        spinnerArrayAdapterloantype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinloantype.setAdapter(spinnerArrayAdapterloantype);

        ArrayAdapter<String> spinnerArrayAdapteremptype = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, empType);
        spinnerArrayAdapteremptype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinemptype.setAdapter(spinnerArrayAdapteremptype);



        return view;
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


    public interface OnFragmentInteractionListener {
       // NOTE : We changed the Uri to String.
        void onFragmentInteraction(String title);
    }
}
