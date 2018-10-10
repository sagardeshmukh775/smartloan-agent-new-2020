package com.smartloan.smtrick.smart_loan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GenerateLeads extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    Spinner spinloantype,spinemptype,spinincome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_generate_leads);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] loanType = new String[]{"Select Loan Type"};
        String[] incomeCriteria = new String[]{"Select Income","below 200000","200000 to 500000","above 500000"};
        String[] empType = new String[]{"Select Occupation","Salaried","Self Employed","Businessman"};

        spinloantype = (Spinner) findViewById(R.id.spinnerselectloantype);
        spinemptype = (Spinner) findViewById(R.id.spinnerselecttypeofemployee);
        spinincome = (Spinner) findViewById(R.id.spinnerselectincome);

        spinloantype.setOnItemSelectedListener(this);
        spinemptype.setOnItemSelectedListener(this);
        spinincome.setOnItemSelectedListener(this);

        ArrayAdapter<String> spinnerArrayAdapterloantype = new ArrayAdapter<String>(this,R.layout.sppinner_layout_listitem,loanType);
        spinnerArrayAdapterloantype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinloantype.setAdapter(spinnerArrayAdapterloantype);

        ArrayAdapter<String> spinnerArrayAdapteremptype = new ArrayAdapter<String>(this,R.layout.sppinner_layout_listitem,empType);
        spinnerArrayAdapteremptype.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinemptype.setAdapter(spinnerArrayAdapteremptype);

        ArrayAdapter<String> spinnerArrayAdapterincome = new ArrayAdapter<String>(this,R.layout.sppinner_layout_listitem,incomeCriteria);
        spinnerArrayAdapterincome.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinincome.setAdapter(spinnerArrayAdapterincome);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
