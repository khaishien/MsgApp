package com.example.shen_mini_itx.msgapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LAU on 02-Aug-16.
 */
public class PostModel implements Parcelable {

    public String _id;
    public String content;
    public Double latitude;
    public Double longitude;
    public UserModel owner;
    public Date timestamp;
    public List<UserModel> liked;

    public PostModel() {
    }

    public PostModel(Parcel in) {
        // Read Long value and convert to date
        _id = in.readString();
        content = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        owner = in.readParcelable(UserModel.class.getClassLoader());
        timestamp = new Date(in.readLong());
        liked = new ArrayList<>();
        in.readList(liked, UserModel.class.getClassLoader());
//        liked = new ArrayList<UserModel>();
//        liked = in.readArrayList(UserModel.class.getClassLoader());

    }

    public PostModel(PostModel model) {
        this._id = model._id;
        this.content = model.content;
        this.latitude = model.latitude;
        this.longitude = model.longitude;
        this.owner = model.owner;
        this.timestamp = model.timestamp;
        this.liked = model.liked;
    }

    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(_id);
        dest.writeString(content);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeParcelable(owner, flags);
        dest.writeLong(timestamp.getTime());
        dest.writeList(liked);

    }


}
