package com.smartloan.smtrick.smart_loan.view.fragement;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.databinding.FragmentDashboardBinding;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.models.DashBoardData;
import com.smartloan.smtrick.smart_loan.repository.DashboardRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.DashboardRepositoryImpl;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.adapter.DashboardAdapter;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.smartloan.smtrick.smart_loan.constants.Constant.SCROLL_PAGE_TIME;

public class SlidingPagerFragement extends Fragment {
  /*  private FragmentDashboardBinding fragmentDashboardBinding;
    private ProgressDialogClass progressDialogClass;
    private DashboardRepository dashboardRepository;
    ArrayList<DashBoardData> dashBoardDataArrayList;
    private Context context;
    private int currentPage;
    private int NUM_PAGES;
    private OnFragmentInteractionListener mListener;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            dashboardRepository = new DashboardRepositoryImpl();
            timer = new Timer();
            addNewLeadClick();
            getDashboardData();
        }
        return fragmentDashboardBinding.getRoot();
    }

    private void getDashboardData() {
        progressDialogClass.showDialog(this.getString(R.string.loading), this.getString(R.string.PLEASE_WAIT));
        dashboardRepository.readDashBoardData(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    dashBoardDataArrayList = (ArrayList<DashBoardData>) object;
                    init(dashBoardDataArrayList);
                }
                progressDialogClass.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                progressDialogClass.dismissDialog();
                if (isAdded())
                    Utility.showLongMessage(getActivity(), getString(R.string.server_error));
            }
        });
    }

    private void init(ArrayList<DashBoardData> dashBoardDataArrayList) {
        try {
            fragmentDashboardBinding.pager.clearOnPageChangeListeners();
            fragmentDashboardBinding.pager.setAdapter(new DashboardAdapter(context, dashBoardDataArrayList));
            NUM_PAGES = dashBoardDataArrayList.size();
            if (currentPage < NUM_PAGES) {
                fragmentDashboardBinding.pager.setCurrentItem(currentPage, true);
            }
            if (NUM_PAGES > 0)
                setTimer();
            fragmentDashboardBinding.pageIndicatorView.setCount(NUM_PAGES); // specify total count of indicators
            fragmentDashboardBinding.pageIndicatorView.setSelection(0);
            setPageChangeListener();
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }//end of init

    private void setPageChangeListener() {
        fragmentDashboardBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                currentPage = fragmentDashboardBinding.pager.getCurrentItem();
                fragmentDashboardBinding.pageIndicatorView.setSelection(position);
                setTimer();
            }
        });
    }

    private void setTimer() {
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (NUM_PAGES > 0) {
                    if (currentPage < NUM_PAGES) {
                        currentPage++;
                    } else {
                        currentPage = 0;
                    }
                    setCurrentPage();
                } else
                    cancelTimer();
            }
        }, SCROLL_PAGE_TIME, SCROLL_PAGE_TIME);
    }

    private void setCurrentPage() {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragmentDashboardBinding.pager.setCurrentItem(currentPage, true);
                }
            });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void cancelTimer() {
        try {
            timer.cancel();
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private void addNewLeadClick() {
        fragmentDashboardBinding.fabAddLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changeFragement(new GenerateLeedFragment());
            }
        });
    }*/
}
