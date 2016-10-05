package com.example.shen_mini_itx.msgapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;

import java.util.List;

/**
 * Created by shen-mini-itx on 03-Sep-16.
 */
public class UserProfileFragmentProfile extends Fragment {

    public static final String TAG = "UserProfileFragmentProfile";

    private UserModel userModel;

    public static Fragment newInstance(UserModel userModel) {

        Bundle args = new Bundle();
        args.putParcelable("user", userModel);
        UserProfileFragmentProfile fragment = new UserProfileFragmentProfile();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userModel = getArguments().getParcelable("user");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_fragment_profile, container, false);

        if (userModel != null) {



        }


        return view;
    }
}
