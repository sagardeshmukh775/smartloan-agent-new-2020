package com.smartloan.smtrick.smart_loan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Fragment_Invices extends Fragment implements AdapterView.OnItemSelectedListener {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    public Fragment_Invices() {}
    ArrayList<GetterSetterInvoice> searchResults = GetSearchResults();

    ListView listinvoices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_invices, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Invoices");
        }
        listinvoices = (ListView) view.findViewById(R.id.listinvoices);
        listinvoices.setAdapter(new CustumadapterInvoices(getActivity(), searchResults));

        listinvoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listinvoices.getItemAtPosition(position);


                listinvoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        Object o = listinvoices.getItemAtPosition(position);
                        // custom dialog
                        final Dialog dialog = new Dialog(getActivity());

                        dialog.setContentView(R.layout.invoicedialog);
                        dialog.setTitle("Title...");

                        // set the custom dialog components - text, image and button
                      //  TextView text = (TextView) dialog.findViewById(R.id.text);
                       // text.setText("Android custom dialog example!");


                        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonaccept);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonreject);
                        // if button is clicked, close the custom dialog
                        dialogButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();


                    }
                });

            }
        });


        return view;
    }

    private ArrayList<GetterSetterInvoice> GetSearchResults(){
        ArrayList<GetterSetterInvoice> results = new ArrayList<GetterSetterInvoice>();

        GetterSetterInvoice sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("Axis Bank");
        sr.setPhone("Mr Pratik Patel");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("Axis Bank");
        sr.setPhone("Mr Pratik Patel");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("Axis Bank");
        sr.setPhone("Mr Pratik Patel");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("Axis Bank");
        sr.setPhone("Mr Pratik Patel");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("Axis Bank");
        sr.setPhone("Mr Pratik Patel");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("Axis Bank");
        sr.setPhone("Mr Pratik Patel");
        results.add(sr);


        return results;
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
