package com.example.shen_mini_itx.msgapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.shen_mini_itx.msgapp.Models.UserModel;

/**
 * Created by shen-mini-itx on 03-Sep-16.
 */
public class UserProfileFragmentGallery extends Fragment {

    public static Fragment newInstance(UserModel userModel) {

        Bundle args = new Bundle();
        args.putParcelable("user", userModel);
        UserProfileFragmentGallery fragment = new UserProfileFragmentGallery();
        fragment.setArguments(args);
        return fragment;
    }
}
