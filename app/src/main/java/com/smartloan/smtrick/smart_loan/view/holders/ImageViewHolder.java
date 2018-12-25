package com.smartloan.smtrick.smart_loan.view.holders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.smartloan.smtrick.smart_loan.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView iv_businessimage;
    public ImageViewHolder(View itemView) {
        super(itemView);
        iv_businessimage = (ImageView) itemView.findViewById(R.id.iv_businessimage);
    }
}//end of holder
