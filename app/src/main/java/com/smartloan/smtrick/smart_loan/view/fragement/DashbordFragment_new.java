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
import java.util.HashMap;
import java.util.TimeZone;

import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_DATE_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_BALANCE_TRANSFER;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_HL;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_LAP;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_APPROVED;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_LOGIN;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_REJECTED;
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
        getDashboardData();

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
                } else {
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
            addLoginFilesData(leedsModelArrayList);
            addSanctionedFileData(leedsModelArrayList);
            addDisbursedFileData(leedsModelArrayList);
            addPayoutData(leedsModelArrayList);
        }
    }

    private void addLoanData(ArrayList<LeedsModel> leedsModelArrayList) {
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

    private void addLoginFilesData(ArrayList<LeedsModel> leedsModelArrayList) {
        int homeLoanFileCount = 0;
        int loanAgainstPropertyFileCount = 0;
        int balanceTransferFileCount = 0;
        int rejectedHomeLoanFileCount = 0;
        int rejectedLoanAgainstPropertyFileCount = 0;
        int rejectedBalanceTransferFileCount = 0;

        if (leedsModelArrayList != null)
            for (LeedsModel leedsModel : leedsModelArrayList) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(leedsModel.getCreatedDateTimeLong());
                int mMonth = c.get(Calendar.MONTH);
                if (leedsModel.getStatus().equalsIgnoreCase(STATUS_LOGIN)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        homeLoanFileCount++;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyFileCount++;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferFileCount++;

                    }
                }//end of status if
                else if (leedsModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        rejectedHomeLoanFileCount++;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        rejectedLoanAgainstPropertyFileCount++;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        rejectedBalanceTransferFileCount++;

                    }
                }//end of status if
            }//end of for

        txtTotalFilesLogin.setText(String.valueOf((homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount)));
        txtFilesRejected.setText(String.valueOf((rejectedHomeLoanFileCount + rejectedLoanAgainstPropertyFileCount + rejectedBalanceTransferFileCount)));

    }

    private void addSanctionedFileData(ArrayList<LeedsModel> leedsModelArrayList) {
        int homeLoanFileCount = 0;
        long homeLoanAmount = 0;
        int loanAgainstPropertyFileCount = 0;
        long loanAgainstPropertyAmount = 0;
        int balanceTransferFileCount = 0;
        long balanceTransferAmount = 0;
        long totalAmount = 0;

        if (leedsModelArrayList != null)
            for (LeedsModel leedsModel : leedsModelArrayList) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(leedsModel.getCreatedDateTimeLong());
                int mMonth = c.get(Calendar.MONTH);
                if (leedsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        homeLoanFileCount++;
                        long amount = Long.parseLong(leedsModel.getApprovedLoanAmount().replaceAll(",", ""));
                        homeLoanAmount += amount;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyFileCount++;
                        long amount = Long.parseLong(leedsModel.getApprovedLoanAmount().replaceAll(",", ""));
                        loanAgainstPropertyAmount += amount;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferFileCount++;
                        long amount = Long.parseLong(leedsModel.getApprovedLoanAmount().replaceAll(",", ""));
                        balanceTransferAmount += amount;

                    }
                    totalAmount += Double.parseDouble(leedsModel.getApprovedLoanAmount().replaceAll(",", ""));
                }
            }//end of for
        txtFilesSanctioned.setText(String.valueOf((homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount)));
        txtSanctionedAmount.setText(String.valueOf(totalAmount));

    }

    private void addDisbursedFileData(ArrayList<LeedsModel> leedsModelArrayList) {
        int homeLoanFileCount = 0;
        long homeLoanAmount = 0;
        int loanAgainstPropertyFileCount = 0;
        long loanAgainstPropertyAmount = 0;
        int balanceTransferFileCount = 0;
        long balanceTransferAmount = 0;
        long totalAmount = 0;


        if (leedsModelArrayList != null)
            for (LeedsModel leedsModel : leedsModelArrayList) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(leedsModel.getCreatedDateTimeLong());
                int mMonth = c.get(Calendar.MONTH);
                if (leedsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        homeLoanFileCount++;
                        long amount = Long.parseLong(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        homeLoanAmount += amount;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyFileCount++;
                        long amount = Long.parseLong(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        loanAgainstPropertyAmount += amount;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferFileCount++;
                        long amount = Long.parseLong(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        balanceTransferAmount += amount;

                    }
                    totalAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                }
            }//end of for

        txtFilesDisbursed.setText(String.valueOf((homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount)));
        txtTotalDisbursementAmount.setText(String.valueOf(totalAmount));
    }

    private void addPayoutData(ArrayList<LeedsModel> leedsModelArrayList) {
        long homeLoanAmount = 0;
        int homeLoanPayoutAmount = 0;
        int homeLoanPayoutOnDisbursementAmount = 0;
        int homeLoanBalancePayoutAmount = 0;
        int loanAgainstPropertyPayoutAmount = 0;
        int loanAgainstPropertyPayoutOnDisbursementAmount = 0;
        int loanAgainstPropertyBalancePayoutAmount = 0;
        long loanAgainstPropertyAmount = 0;
        int balanceTransferPayoutAmount = 0;
        int balanceTransferBalancePayoutAmount = 0;
        int balanceTransferPayoutOnDisbursementAmount = 0;
        long balanceTransferAmount = 0;


        if (leedsModelArrayList != null)
            for (LeedsModel leedsModel : leedsModelArrayList) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(leedsModel.getCreatedDateTimeLong());

                if (leedsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        homeLoanAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        long amount = Long.parseLong(leedsModel.getPayOutOnDisbursementAmount().replaceAll(",", ""));
                        homeLoanPayoutOnDisbursementAmount += amount;
                        amount = Long.parseLong(leedsModel.getTotalPayoutAmount().replaceAll(",", ""));
                        homeLoanPayoutAmount += amount;
                        amount = Long.parseLong(leedsModel.getBalancePayout().replaceAll(",", ""));
                        homeLoanBalancePayoutAmount += amount;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        long amount = Long.parseLong(leedsModel.getPayOutOnDisbursementAmount().replaceAll(",", ""));
                        loanAgainstPropertyPayoutOnDisbursementAmount += amount;
                        amount = Long.parseLong(leedsModel.getTotalPayoutAmount().replaceAll(",", ""));
                        loanAgainstPropertyPayoutAmount += amount;
                        amount = Long.parseLong(leedsModel.getBalancePayout().replaceAll(",", ""));
                        loanAgainstPropertyBalancePayoutAmount += amount;

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        long amount = Long.parseLong(leedsModel.getPayOutOnDisbursementAmount().replaceAll(",", ""));
                        balanceTransferPayoutOnDisbursementAmount += amount;
                        amount = Long.parseLong(leedsModel.getTotalPayoutAmount().replaceAll(",", ""));
                        balanceTransferPayoutAmount += amount;
                        amount = Long.parseLong(leedsModel.getBalancePayout().replaceAll(",", ""));
                        balanceTransferBalancePayoutAmount += amount;

                    }
                }
            }//end of for
        txtTotalDisbursedPayoutAmt.setText(String.valueOf((homeLoanPayoutOnDisbursementAmount + loanAgainstPropertyPayoutOnDisbursementAmount + balanceTransferPayoutOnDisbursementAmount)));
        txtTotalPayoutPaid.setText(String.valueOf((homeLoanPayoutAmount + loanAgainstPropertyPayoutAmount + balanceTransferPayoutAmount)));
        txtTotalBalancePayout.setText(String.valueOf((homeLoanBalancePayoutAmount + loanAgainstPropertyBalancePayoutAmount + balanceTransferBalancePayoutAmount)));
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
