package com.smartloan.smtrick.smart_loan.view.fragement;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.Entry;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.databinding.FragmentDashboardBinding;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.DashboardDataModel;
import com.smartloan.smtrick.smart_loan.models.LeedsModel;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapter.DashboardDataAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.smartloan.smtrick.smart_loan.constants.Constant.DATE_MONTH_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LEED_DATE_FORMATE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_BALANCE_TRANSFER;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_HL;
import static com.smartloan.smtrick.smart_loan.constants.Constant.LOAN_TYPE_LAP;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_APPROVED;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_LOGIN;
import static com.smartloan.smtrick.smart_loan.constants.Constant.STATUS_REJECTED;
import static com.smartloan.smtrick.smart_loan.constants.Constant.YEAR_DATE_FORMAT;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding fragmentDashboardBinding;
    private ProgressDialogClass progressDialogClass;
    private Context context;
    private OnFragmentInteractionListener mListener;
    private LeedRepository leedRepository;
    private AppSharedPreference appSharedPreference;
    private DashboardDataAdapter dashboardDataAdapter;
    private long year;
    private String yearData = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
//
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        if (fragmentDashboardBinding == null) {
            if (mListener != null) {
                mListener.onFragmentInteraction("Dashboard");
            }
            fragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
            progressDialogClass = new ProgressDialogClass(getActivity());
            leedRepository = new LeedRepositoryImpl();
            appSharedPreference = new AppSharedPreference(context);
            fragmentDashboardBinding.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            fragmentDashboardBinding.recyclerView.setLayoutManager(layoutManager);
            fragmentDashboardBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            //fragmentDashboardBinding.recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);
            yearData = "Year " + currentYear;
            year = Utility.convertFormatedDateToMilliSeconds("01, " + currentYear, YEAR_DATE_FORMAT);
            //year = Utility.convertFormatedDateToMilliSeconds("1, 2018", YEAR_DATE_FORMAT);
            getDashboardData();
            addNewLeadClick();
        }
        return fragmentDashboardBinding.getRoot();
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
            addLoanData(leedsModelArrayList, dashboardDataModelArrayList);
            addLoginFilesData(leedsModelArrayList, dashboardDataModelArrayList);
            addSanctionedFileData(leedsModelArrayList, dashboardDataModelArrayList);
            addDisbursedFileData(leedsModelArrayList, dashboardDataModelArrayList);
            addPayoutData(leedsModelArrayList, dashboardDataModelArrayList);
        }
        setAdapter(dashboardDataModelArrayList);
    }

    private void addLoanData(ArrayList<LeedsModel> leedsModelArrayList, ArrayList<DashboardDataModel> dashboardDataModelArrayList) {
        int homeLoanFileCount = 0;
        long homeLoanAmount = 0;
        int loanAgainstPropertyFileCount = 0;
        long loanAgainstPropertyAmount = 0;
        int balanceTransferFileCount = 0;
        long balanceTransferAmount = 0;
        long totalAmount = 0;
        HashMap<Integer, Long> HMEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTEntryMap = new HashMap<>();

        HashMap<Integer, Long> HMDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTDataEntryMap = new HashMap<>();
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
                    setEntryData(HMEntryMap, mMonth, 1);
                    setEntryData(HMDataEntryMap, mMonth, amount);
                } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                    loanAgainstPropertyFileCount++;
                    long amount = Long.parseLong(leedsModel.getExpectedLoanAmount().replaceAll(",", ""));
                    loanAgainstPropertyAmount += amount;
                    setEntryData(LAPEntryMap, mMonth, 1);
                    setEntryData(LAPDataEntryMap, mMonth, amount);
                } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                    balanceTransferFileCount++;
                    long amount = Long.parseLong(leedsModel.getExpectedLoanAmount().replaceAll(",", ""));
                    balanceTransferAmount += amount;
                    setEntryData(BTEntryMap, mMonth, 1);
                    setEntryData(BTDataEntryMap, mMonth, amount);
                }
                totalAmount += Double.parseDouble(leedsModel.getExpectedLoanAmount().replaceAll(",", ""));
            }//end of for

        DashboardDataModel dashboardDataModel = new DashboardDataModel();
        dashboardDataModel.setDataTitle("Total Number of Lead");
        dashboardDataModel.setTotalValue(String.valueOf(leedsModelArrayList.size()));
        dashboardDataModel.setHomeLoanValue(String.valueOf(homeLoanFileCount));
        dashboardDataModel.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyFileCount));
        dashboardDataModel.setBalanceTransferValue(String.valueOf(balanceTransferFileCount));
        dashboardDataModel.setBackgroundColor(getResources().getColor(R.color.dark_1));
        dashboardDataModel.setYearData(yearData);
        dashboardDataModel.setHLEntries(getEntryList(HMEntryMap));
        dashboardDataModel.setLAPEntries(getEntryList(LAPEntryMap));
        dashboardDataModel.setBTEntries(getEntryList(BTEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel);

        DashboardDataModel dashboardDataModel2 = new DashboardDataModel();
        dashboardDataModel2.setDataTitle("Total Loan Required Amount (" + leedsModelArrayList.size() + ")");
        dashboardDataModel2.setTotalValue(String.valueOf(totalAmount));
        dashboardDataModel2.setHomeLoanLabel(getMessage(R.string.home_loan) + " (" + homeLoanFileCount + ")");
        dashboardDataModel2.setHomeLoanValue(String.valueOf(homeLoanAmount));
        dashboardDataModel2.setLoanAgainstPropertyLabel(getMessage(R.string.loan_against_property) + " (" + loanAgainstPropertyFileCount + ")");
        dashboardDataModel2.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyAmount));
        dashboardDataModel2.setBalanceTransferLabel(getMessage(R.string.balance_transfer) + " (" + balanceTransferFileCount + ")");
        dashboardDataModel2.setBalanceTransferValue(String.valueOf(balanceTransferAmount));
        dashboardDataModel2.setBackgroundColor(getResources().getColor(R.color.dark_2));
        dashboardDataModel2.setYearData(yearData);
        dashboardDataModel2.setHLEntries(getEntryList(HMDataEntryMap));
        dashboardDataModel2.setLAPEntries(getEntryList(LAPDataEntryMap));
        dashboardDataModel2.setBTEntries(getEntryList(BTDataEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel2);
    }

    private void addLoginFilesData(ArrayList<LeedsModel> leedsModelArrayList, ArrayList<DashboardDataModel> dashboardDataModelArrayList) {
        int homeLoanFileCount = 0;
        int loanAgainstPropertyFileCount = 0;
        int balanceTransferFileCount = 0;
        int rejectedHomeLoanFileCount = 0;
        int rejectedLoanAgainstPropertyFileCount = 0;
        int rejectedBalanceTransferFileCount = 0;
        HashMap<Integer, Long> HMEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTEntryMap = new HashMap<>();

        HashMap<Integer, Long> HMDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTDataEntryMap = new HashMap<>();
        if (leedsModelArrayList != null)
            for (LeedsModel leedsModel : leedsModelArrayList) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(leedsModel.getCreatedDateTimeLong());
                int mMonth = c.get(Calendar.MONTH);
                if (leedsModel.getStatus().equalsIgnoreCase(STATUS_LOGIN)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        homeLoanFileCount++;
                        setEntryData(HMEntryMap, mMonth, 1);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyFileCount++;
                        setEntryData(LAPEntryMap, mMonth, 1);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferFileCount++;
                        setEntryData(BTEntryMap, mMonth, 1);
                    }
                }//end of status if
                else if (leedsModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        rejectedHomeLoanFileCount++;
                        setEntryData(HMDataEntryMap, mMonth, 1);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        rejectedLoanAgainstPropertyFileCount++;
                        setEntryData(LAPDataEntryMap, mMonth, 1);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        rejectedBalanceTransferFileCount++;
                        setEntryData(BTDataEntryMap, mMonth, 1);
                    }
                }//end of status if
            }//end of for

        DashboardDataModel dashboardDataModel = new DashboardDataModel();
        dashboardDataModel.setDataTitle("Total Number of files Login");
        dashboardDataModel.setTotalValue(String.valueOf((homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount)));
        dashboardDataModel.setHomeLoanValue(String.valueOf(homeLoanFileCount));
        dashboardDataModel.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyFileCount));
        dashboardDataModel.setBalanceTransferValue(String.valueOf(balanceTransferFileCount));
        dashboardDataModel.setShowDivider(true);
        dashboardDataModel.setBackgroundColor(getResources().getColor(R.color.dark_3));
        dashboardDataModel.setYearData(yearData);
        dashboardDataModel.setHLEntries(getEntryList(HMEntryMap));
        dashboardDataModel.setLAPEntries(getEntryList(LAPEntryMap));
        dashboardDataModel.setBTEntries(getEntryList(BTEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel);

        DashboardDataModel dashboardDataModel2 = new DashboardDataModel();
        dashboardDataModel2.setDataTitle("Files Rejected");
        dashboardDataModel2.setTotalValue(String.valueOf((rejectedHomeLoanFileCount + rejectedLoanAgainstPropertyFileCount + rejectedBalanceTransferFileCount)));
        dashboardDataModel2.setHomeLoanValue(String.valueOf(rejectedHomeLoanFileCount));
        dashboardDataModel2.setLoanAgainstPropertyValue(String.valueOf(rejectedLoanAgainstPropertyFileCount));
        dashboardDataModel2.setBalanceTransferValue(String.valueOf(rejectedBalanceTransferFileCount));
        dashboardDataModel2.setBackgroundColor(getResources().getColor(R.color.dark_4));
        dashboardDataModel2.setYearData(yearData);
        dashboardDataModel2.setHLEntries(getEntryList(HMDataEntryMap));
        dashboardDataModel2.setLAPEntries(getEntryList(LAPDataEntryMap));
        dashboardDataModel2.setBTEntries(getEntryList(BTDataEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel2);
    }

    private void addSanctionedFileData(ArrayList<LeedsModel> leedsModelArrayList, ArrayList<DashboardDataModel> dashboardDataModelArrayList) {
        int homeLoanFileCount = 0;
        long homeLoanAmount = 0;
        int loanAgainstPropertyFileCount = 0;
        long loanAgainstPropertyAmount = 0;
        int balanceTransferFileCount = 0;
        long balanceTransferAmount = 0;
        long totalAmount = 0;

        HashMap<Integer, Long> HMEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTEntryMap = new HashMap<>();

        HashMap<Integer, Long> HMDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTDataEntryMap = new HashMap<>();

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
                        setEntryData(HMEntryMap, mMonth, 1);
                        setEntryData(HMDataEntryMap, mMonth, amount);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyFileCount++;
                        long amount = Long.parseLong(leedsModel.getApprovedLoanAmount().replaceAll(",", ""));
                        loanAgainstPropertyAmount += amount;
                        setEntryData(LAPEntryMap, mMonth, 1);
                        setEntryData(LAPDataEntryMap, mMonth, amount);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferFileCount++;
                        long amount = Long.parseLong(leedsModel.getApprovedLoanAmount().replaceAll(",", ""));
                        balanceTransferAmount += amount;
                        setEntryData(BTEntryMap, mMonth, 1);
                        setEntryData(BTDataEntryMap, mMonth, amount);
                    }
                    totalAmount += Double.parseDouble(leedsModel.getApprovedLoanAmount().replaceAll(",", ""));
                }
            }//end of for

        DashboardDataModel dashboardDataModel = new DashboardDataModel();
        dashboardDataModel.setDataTitle("Total Number of File Sanctioned");
        dashboardDataModel.setTotalValue(String.valueOf((homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount)));
        dashboardDataModel.setHomeLoanValue(String.valueOf(homeLoanFileCount));
        dashboardDataModel.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyFileCount));
        dashboardDataModel.setBalanceTransferValue(String.valueOf(balanceTransferFileCount));
        dashboardDataModel.setShowDivider(true);
        dashboardDataModel.setBackgroundColor(getResources().getColor(R.color.dark_5));
        dashboardDataModel.setYearData(yearData);
        dashboardDataModel.setHLEntries(getEntryList(HMEntryMap));
        dashboardDataModel.setLAPEntries(getEntryList(LAPEntryMap));
        dashboardDataModel.setBTEntries(getEntryList(BTEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel);

        DashboardDataModel dashboardDataModel2 = new DashboardDataModel();
        dashboardDataModel2.setDataTitle("Total Sanctioned Amount  (" + (homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount) + ")");
        dashboardDataModel2.setTotalValue(String.valueOf(totalAmount));
        dashboardDataModel2.setHomeLoanLabel(getMessage(R.string.home_loan) + " (" + homeLoanFileCount + ")");
        dashboardDataModel2.setHomeLoanValue(String.valueOf(homeLoanAmount));
        dashboardDataModel2.setLoanAgainstPropertyLabel(getMessage(R.string.loan_against_property) + " (" + loanAgainstPropertyFileCount + ")");
        dashboardDataModel2.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyAmount));
        dashboardDataModel2.setBalanceTransferLabel(getMessage(R.string.balance_transfer) + " (" + balanceTransferFileCount + ")");
        dashboardDataModel2.setBalanceTransferValue(String.valueOf(balanceTransferAmount));
        dashboardDataModel2.setBackgroundColor(getResources().getColor(R.color.dark_1));
        dashboardDataModel2.setYearData(yearData);
        dashboardDataModel2.setHLEntries(getEntryList(HMDataEntryMap));
        dashboardDataModel2.setLAPEntries(getEntryList(LAPDataEntryMap));
        dashboardDataModel2.setBTEntries(getEntryList(BTDataEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel2);
    }

    private void addDisbursedFileData(ArrayList<LeedsModel> leedsModelArrayList, ArrayList<DashboardDataModel> dashboardDataModelArrayList) {
        int homeLoanFileCount = 0;
        long homeLoanAmount = 0;
        int loanAgainstPropertyFileCount = 0;
        long loanAgainstPropertyAmount = 0;
        int balanceTransferFileCount = 0;
        long balanceTransferAmount = 0;
        long totalAmount = 0;

        HashMap<Integer, Long> HMEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTEntryMap = new HashMap<>();

        HashMap<Integer, Long> HMDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTDataEntryMap = new HashMap<>();

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
                        setEntryData(HMEntryMap, mMonth, 1);
                        setEntryData(HMDataEntryMap, mMonth, amount);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyFileCount++;
                        long amount = Long.parseLong(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        loanAgainstPropertyAmount += amount;
                        setEntryData(LAPEntryMap, mMonth, 1);
                        setEntryData(LAPDataEntryMap, mMonth, amount);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferFileCount++;
                        long amount = Long.parseLong(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        balanceTransferAmount += amount;
                        setEntryData(BTEntryMap, mMonth, 1);
                        setEntryData(BTDataEntryMap, mMonth, amount);
                    }
                    totalAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                }
            }//end of for

        DashboardDataModel dashboardDataModel = new DashboardDataModel();
        dashboardDataModel.setDataTitle("Total Files Disbursed");
        dashboardDataModel.setTotalValue(String.valueOf((homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount)));
        dashboardDataModel.setHomeLoanValue(String.valueOf(homeLoanFileCount));
        dashboardDataModel.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyFileCount));
        dashboardDataModel.setBalanceTransferValue(String.valueOf(balanceTransferFileCount));
        dashboardDataModel.setShowDivider(true);
        dashboardDataModel.setBackgroundColor(getResources().getColor(R.color.dark_2));
        dashboardDataModel.setYearData(yearData);
        dashboardDataModel.setHLEntries(getEntryList(HMEntryMap));
        dashboardDataModel.setLAPEntries(getEntryList(LAPEntryMap));
        dashboardDataModel.setBTEntries(getEntryList(BTEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel);

        DashboardDataModel dashboardDataModel2 = new DashboardDataModel();
        dashboardDataModel2.setDataTitle("Total Disbursement Amount  (" + (homeLoanFileCount + loanAgainstPropertyFileCount + balanceTransferFileCount) + ")");
        dashboardDataModel2.setTotalValue(String.valueOf(totalAmount));
        dashboardDataModel2.setHomeLoanLabel(getMessage(R.string.home_loan) + " (" + homeLoanFileCount + ")");
        dashboardDataModel2.setHomeLoanValue(String.valueOf(homeLoanAmount));
        dashboardDataModel2.setLoanAgainstPropertyLabel(getMessage(R.string.loan_against_property) + " (" + loanAgainstPropertyFileCount + ")");
        dashboardDataModel2.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyAmount));
        dashboardDataModel2.setBalanceTransferLabel(getMessage(R.string.balance_transfer) + " (" + balanceTransferFileCount + ")");
        dashboardDataModel2.setBalanceTransferValue(String.valueOf(balanceTransferAmount));
        dashboardDataModel2.setBackgroundColor(getResources().getColor(R.color.dark_3));
        dashboardDataModel2.setYearData(yearData);
        dashboardDataModel2.setHLEntries(getEntryList(HMDataEntryMap));
        dashboardDataModel2.setLAPEntries(getEntryList(LAPDataEntryMap));
        dashboardDataModel2.setBTEntries(getEntryList(BTDataEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel2);
    }

    private void addPayoutData(ArrayList<LeedsModel> leedsModelArrayList, ArrayList<DashboardDataModel> dashboardDataModelArrayList) {
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

        HashMap<Integer, Long> HMEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTEntryMap = new HashMap<>();

        HashMap<Integer, Long> HMDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> LAPDataEntryMap = new HashMap<>();
        HashMap<Integer, Long> BTDataEntryMap = new HashMap<>();

        HashMap<Integer, Long> HMDataEntryMap2 = new HashMap<>();
        HashMap<Integer, Long> LAPDataEntryMap2 = new HashMap<>();
        HashMap<Integer, Long> BTDataEntryMap2 = new HashMap<>();

        if (leedsModelArrayList != null)
            for (LeedsModel leedsModel : leedsModelArrayList) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(leedsModel.getCreatedDateTimeLong());
                int mMonth = c.get(Calendar.MONTH);
                if (leedsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
                    if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_HL)) {
                        homeLoanAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        long amount = Long.parseLong(leedsModel.getPayOutOnDisbursementAmount().replaceAll(",", ""));
                        homeLoanPayoutOnDisbursementAmount += amount;
                        amount = Long.parseLong(leedsModel.getTotalPayoutAmount().replaceAll(",", ""));
                        homeLoanPayoutAmount += amount;
                        amount = Long.parseLong(leedsModel.getBalancePayout().replaceAll(",", ""));
                        homeLoanBalancePayoutAmount += amount;
                        setEntryData(HMEntryMap, mMonth, homeLoanPayoutOnDisbursementAmount);
                        setEntryData(HMDataEntryMap, mMonth, homeLoanPayoutAmount);
                        setEntryData(HMDataEntryMap2, mMonth, homeLoanBalancePayoutAmount);
                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_LAP)) {
                        loanAgainstPropertyAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        long amount = Long.parseLong(leedsModel.getPayOutOnDisbursementAmount().replaceAll(",", ""));
                        loanAgainstPropertyPayoutOnDisbursementAmount += amount;
                        amount = Long.parseLong(leedsModel.getTotalPayoutAmount().replaceAll(",", ""));
                        loanAgainstPropertyPayoutAmount += amount;
                        amount = Long.parseLong(leedsModel.getBalancePayout().replaceAll(",", ""));
                        loanAgainstPropertyBalancePayoutAmount += amount;

                        setEntryData(LAPEntryMap, mMonth, loanAgainstPropertyPayoutOnDisbursementAmount);
                        setEntryData(LAPDataEntryMap, mMonth, loanAgainstPropertyPayoutAmount);
                        setEntryData(LAPDataEntryMap2, mMonth, loanAgainstPropertyBalancePayoutAmount);

                    } else if (leedsModel.getLoanType().equalsIgnoreCase(LOAN_TYPE_BALANCE_TRANSFER)) {
                        balanceTransferAmount += Double.parseDouble(leedsModel.getDisbursedLoanAmount().replaceAll(",", ""));
                        long amount = Long.parseLong(leedsModel.getPayOutOnDisbursementAmount().replaceAll(",", ""));
                        balanceTransferPayoutOnDisbursementAmount += amount;
                        amount = Long.parseLong(leedsModel.getTotalPayoutAmount().replaceAll(",", ""));
                        balanceTransferPayoutAmount += amount;
                        amount = Long.parseLong(leedsModel.getBalancePayout().replaceAll(",", ""));
                        balanceTransferBalancePayoutAmount += amount;
                        setEntryData(BTEntryMap, mMonth, balanceTransferPayoutOnDisbursementAmount);
                        setEntryData(BTDataEntryMap, mMonth, balanceTransferPayoutAmount);
                        setEntryData(BTDataEntryMap2, mMonth, balanceTransferBalancePayoutAmount);
                    }
                }
            }//end of for

        DashboardDataModel dashboardDataModel2 = new DashboardDataModel();
        dashboardDataModel2.setDataTitle("Total Payout on Disbustment Amount 0.03%");
        dashboardDataModel2.setTotalValue(String.valueOf((homeLoanPayoutOnDisbursementAmount + loanAgainstPropertyPayoutOnDisbursementAmount + balanceTransferPayoutOnDisbursementAmount)));
        dashboardDataModel2.setHomeLoanLabel(getMessage(R.string.home_loan) + " (" + homeLoanAmount + ")");
        dashboardDataModel2.setHomeLoanValue(String.valueOf(homeLoanPayoutOnDisbursementAmount));
        dashboardDataModel2.setLoanAgainstPropertyLabel(getMessage(R.string.loan_against_property) + " (" + loanAgainstPropertyAmount + ")");
        dashboardDataModel2.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyPayoutOnDisbursementAmount));
        dashboardDataModel2.setBalanceTransferLabel(getMessage(R.string.balance_transfer) + " (" + balanceTransferAmount + ")");
        dashboardDataModel2.setBalanceTransferValue(String.valueOf(balanceTransferPayoutOnDisbursementAmount));
        dashboardDataModel2.setShowDivider(true);
        dashboardDataModel2.setBackgroundColor(getResources().getColor(R.color.dark_4));
        dashboardDataModel2.setYearData(yearData);
        dashboardDataModel2.setHLEntries(getEntryList(HMEntryMap));
        dashboardDataModel2.setLAPEntries(getEntryList(LAPEntryMap));
        dashboardDataModel2.setBTEntries(getEntryList(BTEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel2);

        DashboardDataModel dashboardDataModel = new DashboardDataModel();
        dashboardDataModel.setDataTitle("Total Payout Paid Till Now");
        dashboardDataModel.setTotalValue(String.valueOf((homeLoanPayoutAmount + loanAgainstPropertyPayoutAmount + balanceTransferPayoutAmount)));
        dashboardDataModel.setHomeLoanValue(String.valueOf(homeLoanPayoutAmount));
        dashboardDataModel.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyPayoutAmount));
        dashboardDataModel.setBalanceTransferValue(String.valueOf(balanceTransferPayoutAmount));
        dashboardDataModel.setBackgroundColor(getResources().getColor(R.color.dark_5));
        dashboardDataModel.setYearData(yearData);
        dashboardDataModel.setHLEntries(getEntryList(HMDataEntryMap));
        dashboardDataModel.setLAPEntries(getEntryList(LAPDataEntryMap));
        dashboardDataModel.setBTEntries(getEntryList(BTDataEntryMap));
        dashboardDataModelArrayList.add(dashboardDataModel);

        DashboardDataModel dashboardDataModel3 = new DashboardDataModel();
        dashboardDataModel3.setDataTitle("Total Balance Payout");
        dashboardDataModel3.setTotalValue(String.valueOf((homeLoanBalancePayoutAmount + loanAgainstPropertyBalancePayoutAmount + balanceTransferBalancePayoutAmount)));
        dashboardDataModel3.setHomeLoanValue(String.valueOf(homeLoanBalancePayoutAmount));
        dashboardDataModel3.setLoanAgainstPropertyValue(String.valueOf(loanAgainstPropertyBalancePayoutAmount));
        dashboardDataModel3.setBalanceTransferValue(String.valueOf(balanceTransferBalancePayoutAmount));
        dashboardDataModel3.setBackgroundColor(getResources().getColor(R.color.dark_1));
        dashboardDataModel3.setYearData(yearData);
        dashboardDataModel3.setHLEntries(getEntryList(HMDataEntryMap2));
        dashboardDataModel3.setLAPEntries(getEntryList(LAPDataEntryMap2));
        dashboardDataModel3.setBTEntries(getEntryList(BTDataEntryMap2));
        dashboardDataModelArrayList.add(dashboardDataModel3);
    }

    private void setAdapter(ArrayList<DashboardDataModel> dashboardDataModelArrayList) {
        if (dashboardDataModelArrayList != null) {
            if (dashboardDataAdapter == null) {
                dashboardDataAdapter = new DashboardDataAdapter(context, dashboardDataModelArrayList);
                fragmentDashboardBinding.recyclerView.setAdapter(dashboardDataAdapter);
            } else {
                ArrayList<DashboardDataModel> arrayList = new ArrayList<>();
                arrayList.addAll(dashboardDataModelArrayList);
                dashboardDataAdapter.reload(arrayList);
            }
        }
    }

    private String getMessage(int id) {
        return context.getString(id);
    }

    private void addNewLeadClick() {
        fragmentDashboardBinding.fabAddLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changeFragement(new GenerateLeedFragment());
            }
        });
    }

    private void setEntryData(HashMap<Integer, Long> entryDataMap, int key, long value) {
        if (entryDataMap.containsKey(key)) {
            long preValue = entryDataMap.get(key);
            entryDataMap.put(key, (preValue + value));
        } else {
            entryDataMap.put(key, value);
        }
    }

    private ArrayList<Entry> getEntryList(HashMap<Integer, Long> entryDataMap) {
        ArrayList<Entry> entryArrayList = new ArrayList<>();
        if (entryDataMap != null && (entryDataMap.isEmpty() || !entryDataMap.containsKey(0)))
            entryArrayList.add(new Entry(0, 0));

        for (Map.Entry<Integer, Long> entry : entryDataMap.entrySet()) {
            entryArrayList.add(new Entry(entry.getKey(), entry.getValue()));
        }
        return entryArrayList;
    }
}
