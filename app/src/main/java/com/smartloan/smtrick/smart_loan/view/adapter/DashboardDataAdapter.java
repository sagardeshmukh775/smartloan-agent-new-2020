package com.smartloan.smtrick.smart_loan.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.databinding.DashboardDataAdapterLayoutBinding;
import com.smartloan.smtrick.smart_loan.models.DashboardDataModel;
import com.smartloan.smtrick.smart_loan.view.holders.DashboardDataViewHolder;

import java.util.ArrayList;
import java.util.Random;

public class DashboardDataAdapter extends RecyclerView.Adapter<DashboardDataViewHolder> {

    private ArrayList<DashboardDataModel> dashboardDataModelArrayList;
    private Context context;

    public DashboardDataAdapter(Context context, ArrayList<DashboardDataModel> data) {
        this.dashboardDataModelArrayList = data;
        this.context = context;
    }

    @Override
    public DashboardDataViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        DashboardDataAdapterLayoutBinding dashboardDataAdapterLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dashboard_data_adapter_layout, parent, false);
        return new DashboardDataViewHolder(dashboardDataAdapterLayoutBinding);
    }

    public DashboardDataModel getModel(int position) {
        return (dashboardDataModelArrayList.get(position));
    }

    @Override
    public void onBindViewHolder(final DashboardDataViewHolder holder, final int listPosition) {
        try {
            DashboardDataModel dashboardDataModel = getModel(listPosition);
            holder.dashboardDataAdapterLayoutBinding.tvDataTitle.setText(dashboardDataModel.getDataTitle());
            holder.dashboardDataAdapterLayoutBinding.tvHomeLoanLabel.setText(dashboardDataModel.getHomeLoanLabel());
            holder.dashboardDataAdapterLayoutBinding.tvHomeLoanValue.setText(dashboardDataModel.getHomeLoanValue());
            holder.dashboardDataAdapterLayoutBinding.tvLoanAgainstPropertyLabel.setText(dashboardDataModel.getLoanAgainstPropertyLabel());
            holder.dashboardDataAdapterLayoutBinding.tvLoanAgainstPropertyValue.setText(dashboardDataModel.getLoanAgainstPropertyValue());
            holder.dashboardDataAdapterLayoutBinding.tvBalanceTransferLabel.setText(dashboardDataModel.getBalanceTransferLabel());
            holder.dashboardDataAdapterLayoutBinding.tvBalanceTransferValue.setText(dashboardDataModel.getBalanceTransferValue());
            holder.dashboardDataAdapterLayoutBinding.tvDateTime.setText(dashboardDataModel.getYearData());
            holder.dashboardDataAdapterLayoutBinding.tvTotalCount.setText(dashboardDataModel.getTotalValue());
            //holder.dashboardDataAdapterLayoutBinding.llColorLayout.setBackgroundColor(dashboardDataModel.getBackgroundColor());
            if (dashboardDataModel.getHLEntries().size() > 1
                    || dashboardDataModel.getLAPEntries().size() > 1
                    || dashboardDataModel.getBTEntries().size() > 1) {
                if (dashboardDataModel.getHLEntries().isEmpty())
                    dashboardDataModel.getHLEntries().add(new Entry(0, 0));
                LineDataSet dataSet = new LineDataSet(dashboardDataModel.getHLEntries(), "Home Loan");
                dataSet.setColor(getColor(R.color.yello));
                dataSet.setValueTextColor(getColor(R.color.transparent_color));
                dataSet.setCircleColor(getColor(R.color.yello));
                dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                dataSet.setDrawCircles(false);
                dataSet.setLineWidth(2);
                dataSet.setHighlightEnabled(false);
                //dataSet.setV;
                if (dashboardDataModel.getBTEntries().isEmpty())
                    dashboardDataModel.getBTEntries().add(new Entry(0, 0));
                LineDataSet dataSet2 = new LineDataSet(dashboardDataModel.getBTEntries(), "Balance Transfer");
                dataSet2.setColor(getColor(R.color.red));
                dataSet2.setValueTextColor(getColor(R.color.transparent_color));
                dataSet2.setCircleColor(getColor(R.color.red));
                dataSet2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                dataSet2.setDrawCircles(false);
                dataSet2.setLineWidth(2);

                if (dashboardDataModel.getLAPEntries().isEmpty())
                    dashboardDataModel.getLAPEntries().add(new Entry(0, 0));
                LineDataSet dataSet3 = new LineDataSet(dashboardDataModel.getLAPEntries(), "Loan Against Property");
                dataSet3.setColor(getColor(R.color.icon_green));
                dataSet3.setValueTextColor(getColor(R.color.transparent_color));
                dataSet3.setCircleColor(getColor(R.color.icon_green));
                dataSet3.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                dataSet3.setDrawCircles(false);
                dataSet3.setLineWidth(2);
                //****
                // Controlling X axis
                XAxis xAxis = holder.dashboardDataAdapterLayoutBinding.chart.getXAxis();
                holder.dashboardDataAdapterLayoutBinding.chart.getXAxis().setTextColor(Color.WHITE);
                holder.dashboardDataAdapterLayoutBinding.chart.getXAxis().setEnabled(false);
                // Set the xAxis position to bottom. Default is top
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                //xAxis.setAxisMaximum(12);
                //xAxis.setLabelCount(entries.size());
                //Customizing x axis value
                final String[] months = new String[35];
                for (int i = 0; i <= 30; i++) {
                    months[i] = "" + (i + 1);
                }
                IAxisValueFormatter formatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if (value == -1)
                            value = 0;
                        return months[(int) value];
                    }
                };
                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis.setValueFormatter(formatter);
                //***
                // Controlling right side of y axis
                YAxis yAxisRight = holder.dashboardDataAdapterLayoutBinding.chart.getAxisRight();
                yAxisRight.setEnabled(false);
                //***
                // Controlling left side of y axis
                YAxis yAxisLeft = holder.dashboardDataAdapterLayoutBinding.chart.getAxisLeft();
                holder.dashboardDataAdapterLayoutBinding.chart.getAxisLeft().setEnabled(false);
                holder.dashboardDataAdapterLayoutBinding.chart.getAxisLeft().setTextColor(Color.WHITE);
                yAxisLeft.setGranularity(1f);
                // Setting Data
                LineData data = new LineData();
                data.addDataSet(dataSet);
                data.addDataSet(dataSet2);
                data.addDataSet(dataSet3);
                Description description = new Description();
                description.setText("");
                holder.dashboardDataAdapterLayoutBinding.chart.setDescription(description);
                holder.dashboardDataAdapterLayoutBinding.chart.setData(data);
                holder.dashboardDataAdapterLayoutBinding.chart.getLegend().setTextColor(getColor(R.color.blue));
                holder.dashboardDataAdapterLayoutBinding.chart.getLegend().setEnabled(false);
                holder.dashboardDataAdapterLayoutBinding.chart.setScaleEnabled(false);
                holder.dashboardDataAdapterLayoutBinding.chart.getData().setHighlightEnabled(false);
                //holder.dashboardDataAdapterLayoutBinding.chart.animateX(1500);
                //refresh
                holder.dashboardDataAdapterLayoutBinding.chart.invalidate();
            } else {
                holder.dashboardDataAdapterLayoutBinding.chart.clear();
            }
            Paint p = holder.dashboardDataAdapterLayoutBinding.chart.getPaint(Chart.PAINT_INFO);
            p.setColor(getColor(R.color.blue));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMessage(int id) {
        return context.getString(id);
    }

    private int getColor(int id) {
        return ContextCompat.getColor(context, id);
    }

    @Override
    public int getItemCount() {
        return dashboardDataModelArrayList.size();
    }

    public void reload(ArrayList<DashboardDataModel> arrayList) {
        dashboardDataModelArrayList.clear();
        dashboardDataModelArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }
}