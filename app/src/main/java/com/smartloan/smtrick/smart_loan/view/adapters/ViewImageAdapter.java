package com.smartloan.smtrick.smart_loan.view.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.interfaces.ItemRemoveListner;
import com.smartloan.smtrick.smart_loan.models.ViewImageModel;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.activites.ImageSwipZoomActivity;
import com.smartloan.smtrick.smart_loan.view.holders.ImageViewHolder;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import static com.smartloan.smtrick.smart_loan.constants.Constant.CURRENT_PAGE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.IMAGE_URI_LIST;

public class ViewImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private ArrayList<ViewImageModel> imagesList;
    public Context context;
    public int count = 0;
    private ItemRemoveListner itemRemoveListner;
    private boolean isFromDatabase;

    public ViewImageAdapter(Context context, ArrayList<ViewImageModel> imagesList, ItemRemoveListner itemRemoveListner, boolean isFromDatabase) {
        this.context = context;
        this.imagesList = imagesList;
        this.itemRemoveListner = itemRemoveListner;
        this.isFromDatabase = isFromDatabase;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewdetail_imageslider_adapterlayout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        try {
            final Uri item = imagesList.get(holder.getAdapterPosition()).getImageUri();
            if (item != null && !Utility.isEmptyOrNull(item.getPath()))
                Picasso.with(context).load(item).resize(200, 200).centerCrop().placeholder(R.drawable.dummy_image).into(holder.iv_businessimage);
            else
                holder.iv_businessimage.setImageResource(R.drawable.dummy_image);
            holder.iv_businessimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageSwipZoomActivity(holder.getAdapterPosition());
                }
            });
            holder.iv_cancel_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(holder.getAdapterPosition());
                }
            });

        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private void removeItem(int position) {
        if (isFromDatabase) {
            itemRemoveListner.itemRemoveFromDatabase(imagesList.get(position).getLeedId(), imagesList.get(position).getDocumentId());
            imagesList.remove(position);
            notifyItemRemoved(position);
        } else {
            imagesList.remove(position);
            notifyItemRemoved(position);
            itemRemoveListner.itemRemoved(position);
        }
    }

    public void reload(ArrayList<ViewImageModel> post) {
        try {
            imagesList = post;
            notifyDataSetChanged();
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    /*
     *start Zoom swip Activity activity
     */
    private void startImageSwipZoomActivity(int pos) {
        try {
            Intent intent = new Intent(context, ImageSwipZoomActivity.class);
            Bundle bundle = new Bundle();
            intent.putParcelableArrayListExtra(IMAGE_URI_LIST,imagesList);
            bundle.putInt(CURRENT_PAGE,pos);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }
}//end of class
