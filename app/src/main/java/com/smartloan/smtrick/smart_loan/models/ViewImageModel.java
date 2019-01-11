package com.smartloan.smtrick.smart_loan.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ViewImageModel implements Parcelable{
    private Uri imageUri;
    private String leedId,documentId;

    public ViewImageModel() {
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ViewImageModel createFromParcel(Parcel in) {
            return new ViewImageModel(in);
        }

        public ViewImageModel[] newArray(int size) {
            return new ViewImageModel[size];
        }
    };

    public ViewImageModel(Parcel in){
        Uri.Builder builder = new Uri.Builder();
        this.imageUri= builder.path(in.readString()).build();
        this.leedId = in.readString();
        this.documentId =  in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUri.toString());
        dest.writeString(this.leedId);
        dest.writeString(this.documentId);
    }

    public ViewImageModel(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public ViewImageModel(Uri imageUri, String leedId, String documentId) {
        this.imageUri = imageUri;
        this.leedId = leedId;
        this.documentId = documentId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getLeedId() {
        return leedId;
    }

    public void setLeedId(String leedId) {
        this.leedId = leedId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
