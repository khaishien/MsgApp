package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.Requests.UserSyncRequest;
import com.example.shen_mini_itx.msgapp.Responses.UserSyncResponse;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    private void signIn() {
        showProgressBar();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.d(TAG, "user:" + acct.getDisplayName());
            Log.d(TAG, "user:" + acct.getEmail());
            Log.d(TAG, "user:" + acct.getId());
            Log.d(TAG, "user:" + acct.getIdToken());
            Log.d(TAG, "user:" + acct.getPhotoUrl());
            //updateUI(true);

            syncUserDataToServer(acct);


        } else {
            // Signed out, show unauthenticated UI.
            signOut();
            Toast.makeText(getApplicationContext(), "Failed to login, try again.",
                    Toast.LENGTH_SHORT).show();
            hideProgressBar();
        }


    }

    private void syncUserDataToServer(GoogleSignInAccount acct) {

        final UserSyncRequest u = new UserSyncRequest();
        u.setId(acct.getId());
        u.setEmail(acct.getEmail());
        u.setName(acct.getDisplayName());
        if (acct.getPhotoUrl() != null)
            u.setImage(acct.getPhotoUrl().toString());

        Call<UserSyncResponse> call = RestClient.get().syncUser(u);

        call.enqueue(new Callback<UserSyncResponse>() {
            @Override
            public void onResponse(Call<UserSyncResponse> call, Response<UserSyncResponse> response) {

                Toast.makeText(getApplicationContext(), "Login successful.",
                        Toast.LENGTH_SHORT).show();
                hideProgressBar();
                if (response.body().isNewUser()) {
                    //go new user setup
                    Log.d(TAG, "new user add");

                    naviagteToMain();
                } else {
                    Log.d(TAG, "old user login");

                    naviagteToMain();
                }


                new Session(getApplicationContext()).createUserLoginSession(response.body().getId(),
                        u.getId(), u.getName(), u.getEmail(), u.getImage(),
                        response.body().getApi());

            }

            @Override
            public void onFailure(Call<UserSyncResponse> call, Throwable t) {
                signOut();
                Toast.makeText(getApplicationContext(), "Failed to login, try again.",
                        Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });

    }

    private void showProgressBar() {
        ImageView progressbar = (ImageView) findViewById(R.id.login_progress);
        ((AnimationDrawable) progressbar.getBackground()).start();
        progressbar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        ImageView progressbar = (ImageView) findViewById(R.id.login_progress);
        progressbar.setVisibility(View.GONE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        navigateToLogin();
                    }
                });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("status", "session invalid");
        startActivity(intent);
        finish();
    }

    private void naviagteToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
