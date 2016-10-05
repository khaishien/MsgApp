package com.example.shen_mini_itx.msgapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by LAU on 04-Aug-16.
 */
public class UserModel implements Parcelable {

    public String _id;
    public String useremail;
    public String username;
    public String userid;
    public String image;
    public Date login_at;
    public Date created_at;
    public Date updated_at;

    public UserModel(){

    }

    public UserModel(Parcel in) {
        _id = in.readString();
        useremail = in.readString();
        username = in.readString();
        userid = in.readString();
        image = in.readString();
        login_at = new Date(in.readLong());
        created_at = new Date(in.readLong());
        updated_at = new Date(in.readLong());
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(_id);
        dest.writeString(useremail);
        dest.writeString(username);
        dest.writeString(userid);
        dest.writeString(image);
        dest.writeLong(login_at.getTime());
        dest.writeLong(created_at.getTime());
        dest.writeLong(updated_at.getTime());
    }
}
