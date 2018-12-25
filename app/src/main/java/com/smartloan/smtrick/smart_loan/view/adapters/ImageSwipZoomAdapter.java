package com.smartloan.smtrick.smart_loan.view.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ImageSwipZoomAdapter extends PagerAdapter {
    private ArrayList<Uri> imagesList;
    private LayoutInflater inflater;
    private Context context;

    public ImageSwipZoomAdapter(Context context, ArrayList<Uri> imagesList) {
        try {
            this.context = context;
            this.imagesList = imagesList;
            inflater = LayoutInflater.from(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.image_swip_zoom_adapterlayout, view, false);
        try {
            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img);
            final Uri item = imagesList.get(position);
            try {
                if (item != null && !Utility.isEmptyOrNull(item.getPath()))
                    Picasso.with(context).load(item).placeholder(R.drawable.transparentimage).into(imageView);
                else
                    imageView.setImageResource(R.drawable.dummy_image);
            } catch (Exception e) {
                ExceptionUtil.logException(e);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
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

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            try {
                //((RelativeLayout) imageLayout.findViewById(R.id.bottom)).setVisibility(View.GONE);
            } catch (Exception e) {
                ExceptionUtil.logException(e);
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}

