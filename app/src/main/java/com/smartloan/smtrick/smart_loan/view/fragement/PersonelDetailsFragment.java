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
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.activite.UpdatePersonelDetailsActivity;
import com.squareup.picasso.Picasso;

public class PersonelDetailsFragment extends Fragment implements View.OnClickListener {
    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    public PersonelDetailsFragment() {
    }

    TextView txtname, txtmobilenumber, txtadress, txtemailaddress, txtgender;
    ImageView imgEdit;
    AppSharedPreference appSharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_personel_details, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Loan Calculator");
        }

        appSharedPreference = new AppSharedPreference(getContext());

        txtname = view.findViewById(R.id.txtnamevalue);
        txtmobilenumber = view.findViewById(R.id.txtmobilevalue);
        txtadress = view.findViewById(R.id.txtaddressvalue);
        txtemailaddress = view.findViewById(R.id.txtemailvalue);
        txtgender = view.findViewById(R.id.txtgendervalue);
        imgEdit = view.findViewById(R.id.edit);

        getUser();
        imgEdit.setOnClickListener(this);
        return view;
    }

    private void getUser() {
        txtname.setText(appSharedPreference.getUserName());
        txtmobilenumber.setText(appSharedPreference.getMobileNo());
        txtadress.setText(appSharedPreference.getAddress());
        txtemailaddress.setText(appSharedPreference.getEmaiId());
        txtgender.setText(appSharedPreference.getGender());

    }


    @Override
    public void onClick(View v) {
        if (v == imgEdit){
            Intent intent = new Intent(getContext(), UpdatePersonelDetailsActivity.class);
            startActivity(intent);
        }
    }
}
