package com.smartloan.smtrick.smart_loan.view.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.models.DashBoardData;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DashboardAdapter extends PagerAdapter {
    private ArrayList<DashBoardData> imagesList;
    private LayoutInflater inflater;
    private Context context;

    public DashboardAdapter(Context context, ArrayList<DashBoardData> imagesList) {
        try {
            this.context = context;
            this.imagesList = imagesList;
            inflater = LayoutInflater.from(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.dashboard_adapter_layout, view, false);
        try {
            assert imageLayout != null;
            final ImageView imageView = imageLayout.findViewById(R.id.img);
            final TextView tvDescription = imageLayout.findViewById(R.id.tv_description);
            final String item = imagesList.get(position).getImagePath();
            final String description = imagesList.get(position).getDescription();
            try {
                if (!Utility.isEmptyOrNull(item))
                    Picasso.with(context).load(item).placeholder(R.drawable.transparentimage).into(imageView);
                else
                    imageView.setImageResource(R.drawable.dummy_image);
            } catch (Exception e) {
                ExceptionUtil.logException(e);
            }
            if (!Utility.isEmptyOrNull(description)) {
                tvDescription.setText(description);
                tvDescription.setVisibility(View.VISIBLE);
            } else
                tvDescription.setVisibility(View.GONE);

            view.addView(imageLayout, 0);
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
        return imageLayout;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}