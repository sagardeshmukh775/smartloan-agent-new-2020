package com.smartloan.smtrick.smart_loan.repository.impl;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.smartloan.smtrick.smart_loan.repository.StorageRepository;
import com.squareup.picasso.Picasso;

public class StorageRepositoryImpl implements StorageRepository {
    @Override
    public void loadImage(final Context context, StorageReference storageReference, final int placeHolerId, final ImageView imageView) {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).resize(200, 200).centerCrop().placeholder(placeHolerId).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
