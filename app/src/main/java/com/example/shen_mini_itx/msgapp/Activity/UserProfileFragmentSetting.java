package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by shen-mini-itx on 03-Sep-16.
 */
public class UserProfileFragmentSetting extends Fragment {

    public static final String TAG = "UserProfileFragmentSetting";
    private GoogleApiClient mGoogleApiClient;

    public static Fragment newInstance(UserModel userModel) {

        Bundle args = new Bundle();
        args.putParcelable("user", userModel);
        UserProfileFragmentSetting fragment = new UserProfileFragmentSetting();
        fragment.setArguments(args);
        return fragment;
    }

    private UserModel userModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userModel = getArguments().getParcelable("user");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_fragment_setting, container, false);

        if (userModel != null) {


        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, (GoogleApiClient.OnConnectionFailedListener) getActivity() /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        LinearLayout sign_out_btn = (LinearLayout) view.findViewById(R.id.sign_out_btn);
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                navigateToLogin();
                            }
                        });
            }
        });

        return view;
    }


    private void navigateToLogin() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        intent.putExtra("status", "sign out");
        startActivity(intent);
        getActivity().finish();
    }
}
