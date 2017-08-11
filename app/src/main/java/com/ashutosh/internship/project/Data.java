package com.ashutosh.internship.project;


import android.os.Parcel;
import android.os.Parcelable;

class Data implements Parcelable {
  private  String postId;
    private  String userId;
    private  String postTitle;
    private String description;
    private String latLng;
    private String address;
    private String postStatus;
    private String postedOn;

    public Data(String postId, String userId, String postTitle, String description, String latLng, String address, String postStatus, String postedOn) {
        this.postId = postId;
        this.userId = userId;
        this.postTitle = postTitle;
        this.description = description;
        this.latLng = latLng;
        this.address=address;
        this.postStatus = postStatus;
        this.postedOn = postedOn;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postId);
        dest.writeString(this.userId);
        dest.writeString(this.postTitle);
        dest.writeString(this.description);
        dest.writeString(this.latLng);
        dest.writeString(this.address);
        dest.writeString(this.postStatus);
        dest.writeString(this.postedOn);
    }

    protected Data(Parcel in) {
        this.postId = in.readString();
        this.userId = in.readString();
        this.postTitle = in.readString();
        this.description = in.readString();
        this.latLng = in.readString();
        this.address = in.readString();
        this.postStatus = in.readString();
        this.postedOn = in.readString();
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
