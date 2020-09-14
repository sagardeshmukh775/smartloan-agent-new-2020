package com.smartloan.smtrick.smart_loan.view.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.databinding.FragmentDashboardBinding;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.DashboardDataModel;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_DATE_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_BALANCE_TRANSFER;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_HL;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_LAP;
import static com.smartloan.smtrick.smart_loan.constants.Constant.YEAR_DATE_FORMAT;

public class DashbordFragment_new extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;
    private TextView txtNumberOfLeeds, txtLoanRequiredAmount, txtTotalFilesLogin, txtFilesRejected, txtFilesSanctioned, txtSanctionedAmount, txtFilesDisbursed,
            txtTotalDisbursementAmount, txtTotalDisbursedPayoutAmt, txtTotalPayoutPaid, txtTotalBalancePayout;
    private FloatingActionButton fabAddLead;
    private ProgressDialogClass progressDialogClass;
    private LeedRepository leedRepository;
    AppSharedPreference appSharedPreference;
    private long year;
    private String yearData = "";
    private Context context;

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
        context = getActivity();
        if (mListener != null) {
            mListener.onFragmentInteraction("Dash Bord");
        }

        progressDialogClass = new ProgressDialogClass(getActivity());
        leedRepository = new LeedRepositoryImpl();
        appSharedPreference = new AppSharedPreference(getContext());

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
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        yearData = "Year " + currentYear;
        year = Utility.convertFormatedDateToMilliSeconds("01, " + currentYear, YEAR_DATE_FORMAT);


        addNewLeadClick();

        return view;
    }

    private void getDashboardData() {
        progressDialogClass.showDialog(this.getString(R.string.loading), this.getString(R.string.PLEASE_WAIT));
        leedRepository.readLeedsByUserIdOfYear(getActivity(), appSharedPreference.getUserId(), year, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    ArrayList<LeedsModel> leedsModelArrayList = (ArrayList<LeedsModel>) object;
                    if (getActivity() != null)
                        filterDashboardData(leedsModelArrayList);
                }else{
                    if (getActivity() != null)
                        filterDashboardData(new ArrayList<>());
                }
                if (progressDialogClass != null)
                    progressDialogClass.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                if (progressDialogClass != null)
                    progressDialogClass.dismissDialog();
                if (isAdded() && context != null)
                    Utility.showLongMessage(context, getString(R.string.server_error));
            }
        });
    }

    private void filterDashboardData(ArrayList<LeedsModel> leedsModelArrayList) {
        ArrayList<DashboardDataModel> dashboardDataModelArrayList = new ArrayList<>();
        if (leedsModelArrayList != null) {
            addLoanData(leedsModelArrayList);
//            addLoginFilesData(leedsModelArrayList);
//            addSanctionedFileData(leedsModelArrayList);
//            addDisbursedFileData(leedsModelArrayList);
//            addPayoutData(leedsModelArrayList);
        }
    }

    private void addLoanData(ArrayList<LeedsModel> leedsModelArrayList ) {
        int homeLoanFileCount = 0;
        long homeLoanAmount = 0;
        int loanAgainstPropertyFileCount = 0;
        long loanAgainstPropertyAmount = 0;
        int balanceTransferFileCount = 0;
        long balanceTransferAmount = 0;
        long totalAmount = 0;

        if (leedsModelArrayList != null)
            for (LeedsModel leedsModel : leedsModelArrayList) {
                String dateString = Utility.convertMilliSecondsToFormatedDate(leedsModel.getCreatedDateTimeLong(), LEED_DATE_FORMATE);
                Calendar c = Calendar.getInstance(TimeZone.getDefault());
                c.setTimeInMillis(leedsModel.getCreatedDateTimeLong());
                int mMonth = c.get(Calendar.MONTH);
                Log.i("Dashboard fragment", "Month Formated Date:::" + dateString);
                Log.i("Dashboard fragment", "Month Milliseconds:::" + leedsModel.getCreatedDateTimeLong());
                Log.i("Dashboard fragment", "Month values:::" + (mMonth));
                if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                    homeLoanFileCount++;
                    long amount = Long.parseLong(leedsModel.getExpectedLoanAmount().replaceAll(",", ""));
                    homeLoanAmount += amount;

                } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                    loanAgainstPropertyFileCount++;
                    long amount = Long.parseLong(leedsModel.getExpectedLoanAmount().replaceAll(",", ""));
                    loanAgainstPropertyAmount += amount;

                } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                    balanceTransferFileCount++;
                    long amount = Long.parseLong(leedsModel.getExpectedLoanAmount().replaceAll(",", ""));
                    balanceTransferAmount += amount;

                }
                totalAmount += Double.parseDouble(leedsModel.getExpectedLoanAmount().replaceAll(",", ""));
            }//end of for

        txtNumberOfLeeds.setText(String.valueOf(leedsModelArrayList.size()));
        txtLoanRequiredAmount.setText(String.valueOf(totalAmount));
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
