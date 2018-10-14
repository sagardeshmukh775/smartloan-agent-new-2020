package com.smartloan.smtrick.smart_loan.view.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smartloan.smtrick.smart_loan.R;

public class TermsCondition_Activity extends AppCompatActivity {
    Button btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition_);

        btnAccept = (Button) findViewById(R.id.buttonaccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(TermsCondition_Activity.this, Registeractivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

    }
}
