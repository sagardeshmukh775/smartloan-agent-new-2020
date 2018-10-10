package com.smartloan.smtrick.smart_loan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Fragment_Reports extends Fragment implements AdapterView.OnItemSelectedListener {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;
    ArrayList<GetterSetterInvoice> searchResults = GetSearchResults();

    public Fragment_Reports() {}

   ListView Report;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_report, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Reports");
        }
        Report = (ListView) view.findViewById(R.id.listreport);
        Report.setAdapter(new CustumadapterReports(getActivity(), searchResults));

        return view;
        }



    private ArrayList<GetterSetterInvoice> GetSearchResults(){
        ArrayList<GetterSetterInvoice> results = new ArrayList<GetterSetterInvoice>();

        GetterSetterInvoice sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
        results.add(sr);

        sr = new GetterSetterInvoice();
        sr.setName("2345");
        sr.setCityState("3456");
        sr.setPhone("Approved");
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
