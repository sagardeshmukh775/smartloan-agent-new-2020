package com.smartloan.smtrick.smart_loan.view.fragement;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.databinding.ShareMessageDialogLayoutBinding;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.utilities.Utility;

import static com.smartloan.smtrick.smart_loan.utilities.Utility.formatString;
import static com.smartloan.smtrick.smart_loan.utilities.Utility.removeDecimalPoint;

public class CalculatorFragment extends Fragment {
    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    public CalculatorFragment() {
    }

    Spinner spinloantype, spinemptype, spinincome;
    Button emiCalcBtn, btn_share;
    ProgressBar progressBar;
    private TextView tvEmi, tvTotalInterest, tvTotalLoan;
    private EditText et_mobile_Number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Loan Calculator");
        }
        final EditText P = view.findViewById(R.id.principal);
        final EditText I = view.findViewById(R.id.interest);
        final EditText Y = view.findViewById(R.id.years);
        tvEmi = view.findViewById(R.id.tv_emi);
        tvTotalInterest = view.findViewById(R.id.tv_total_interest);
        tvTotalLoan = view.findViewById(R.id.tv_total_loan);
        emiCalcBtn = view.findViewById(R.id.btn_calculate2);
        btn_share = view.findViewById(R.id.btn_share);
        progressBar = view.findViewById(R.id.circularProgressBar);
        et_mobile_Number = view.findViewById(R.id.et_mobile_Number);
        emiCalcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st1 = P.getText().toString();
                String st2 = I.getText().toString();
                String st3 = Y.getText().toString();
                if (TextUtils.isEmpty(st1)) {
                    P.setError("Enter Prncipal Amount");
                    P.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(st2)) {
                    I.setError("Enter Interest Rate");
                    I.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(st3)) {
                    Y.setError("Enter Years");
                    Y.requestFocus();
                    return;
                }
                double p = Double.valueOf(st1);
                double i = Double.valueOf(st2);
                double y = Double.valueOf(st3);
                double Principal = calPric(p);
                double Rate = calInt(i);
                double Months = calMonth(y);
                double Dvdnt = calDvdnt(Rate, Months);
                double FD = calFinalDvdnt(Principal, Rate, Dvdnt);
                double D = calDivider(Dvdnt);
                double emi = calEmi(FD, D);
                double TA = calTa(emi, Months);
                double ti = calTotalInt(TA, Principal);
                tvEmi.setText(removeDecimalPoint(emi));
                tvTotalInterest.setText(removeDecimalPoint(ti));
                tvTotalLoan.setText(removeDecimalPoint(ti + Principal));
                int percentage = (int) (((double) ti / Principal) * 100);
                progressBar.setProgress(percentage);
            }
        });
        btn_share.setOnClickListener((View view1) -> {
            try {
                if (Utility.isEmptyOrNull(et_mobile_Number.getText().toString())) {
                    et_mobile_Number.setError(getString(R.string.MOBILE_NUMBER_SHOULD_NOT_BE_EMPTY));
                } else if (!Utility.isValidMobileNumber(et_mobile_Number.getText().toString())) {
                    et_mobile_Number.setError(getMessage(R.string.INVALID_MOBILE_NUMBER));
                } else {
                    String message = "Principal Amount ₹: " + formatString(P.getText().toString())
                            + "\n" + "Interest rate per Year (in Per): " + I.getText().toString()
                            + "\n" + "Number of Years: " + Y.getText().toString()
                            + "\n" + "------------------------"
                            + "\n" + "EMI ₹: " + formatString(tvEmi.getText().toString())
                            + "\n" + "Total Interest on Loan ₹: " + formatString(tvTotalInterest.getText().toString())
                            + "\n" + "Total loan Amount ₹: " + formatString(tvTotalLoan.getText().toString())
                            + "\n\n" + "--------Smart Loan -----------";
                   showDialog(message);
                }
            } catch (Exception e) {
                ExceptionUtil.logException(e);
                Utility.showLongMessage(getActivity(), "Failed to share");
            }
        });
        return view;
    }

    public double calPric(double p) {
        return (double) (p);
    }

    public double calInt(double i) {
        return (double) (i / 12 / 100);
    }

    public double calMonth(double y) {
        return (double) (y * 12);
    }

    public double calDvdnt(double Rate, double Months) {
        return (double) (Math.pow(1 + Rate, Months));
    }

    public double calFinalDvdnt(double Principal, double Rate, double Dvdnt) {
        return (double) (Principal * Rate * Dvdnt);
    }

    public double calDivider(double Dvdnt) {
        return (double) (Dvdnt - 1);
    }

    public double calEmi(double FD, Double D) {
        return (double) (FD / D);
    }

    public double calTa(double emi, Double Months) {
        return (double) (emi * Months);
    }

    public double calTotalInt(double TA, double Principal) {
        return (double) (TA - Principal);
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

    public void shareToWhatsApp(String message) {
        try {
            String phoneNumberWithCountryCode = "+91" + et_mobile_Number.getText().toString();
            startActivity(
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message)
                            )
                    )
            );
        } catch (Exception e) {
            ExceptionUtil.logException(e);
            Utility.showLongMessage(getActivity(), "Failed to share");
        }
    }

    private String getMessage(int id) {
        return getString(id);
    }

    private void showDialog(String message) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            ShareMessageDialogLayoutBinding callSmsDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.share_message_dialog_layout, null, false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(callSmsDialogLayoutBinding.getRoot());
            dialog.setCancelable(true);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
            callSmsDialogLayoutBinding.layoutWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                      shareToWhatsApp(message);
                    } catch (Exception e) {
                        ExceptionUtil.logException(e);
                    }
                }
            });
            callSmsDialogLayoutBinding.layoutSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        String phoneNumberWithCountryCode = "+91" + et_mobile_Number.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumberWithCountryCode));
                        intent.putExtra("sms_body", message);
                        startActivity(intent);
                    } catch (Exception e) {
                        Utility.showLongMessage(getActivity(), "Failed to share");
                        ExceptionUtil.logException(e);
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

}
