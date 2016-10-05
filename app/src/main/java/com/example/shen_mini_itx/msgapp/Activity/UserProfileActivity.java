package com.example.shen_mini_itx.msgapp.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shen_mini_itx.msgapp.Common.ImageResource;
import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by shen-mini-itx on 03-Sep-16.
 */
public class UserProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    final static String TAG = "UserProfileActivity";

    private UserModel userModel;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 1f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll);
        scrollView.setFillViewport(true);

        Session session = new Session(this);

        userModel = getIntent().getParcelableExtra("user");

        if (userModel != null) {

            setupToolbar();

            if (userModel._id == session.getUserId())
                setupTab(true);
            else
                setupTab(false);
        }

    }

    private void setupToolbar() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout.setTitle("   ");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView user_email = (TextView) findViewById(R.id.user_email);
        ImageView user_image = (ImageView) findViewById(R.id.user_image);
        final ImageView user_mini_image = (ImageView) findViewById(R.id.user_mini_image);
        user_mini_image.setVisibility(View.INVISIBLE);

        RelativeLayout blur_layout = (RelativeLayout) findViewById(R.id.blur_layout);
        Blurry.with(this)
                .radius(25)
                .sampling(10)
                .color(Color.argb(66, 255, 255, 0))
                .async()
                .onto(blur_layout);

        user_name.setText(userModel.username);
        user_email.setText(userModel.useremail);
        if (userModel.image != null) {
            Picasso.with(this).load(userModel.image).centerCrop().fit().into(user_image);
            Picasso.with(this).load(userModel.image).into(user_mini_image);
        }


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                if (percentage == PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {


                    if (!isShow) {
                        //collapsingToolbarLayout.setTitleEnabled(true);
                        ImageResource.startAlphaAnimation(user_mini_image, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                        collapsingToolbarLayout.setTitle(userModel.username);
                        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);

                        isShow = true;
                    }

                } else {

                    if (isShow) {
                        //collapsingToolbarLayout.setTitleEnabled(false);
                        collapsingToolbarLayout.setTitle("   ");

                        ImageResource.startAlphaAnimation(user_mini_image, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                        isShow = false;

                    }

                }
            }
        });


    }

    private void setupTab(Boolean ownProfile) {


        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        UserProfileFragmentPagerAdapter userProfileFragmentPagerAdapter = new UserProfileFragmentPagerAdapter(getSupportFragmentManager(), userModel, ownProfile);

        viewPager.setAdapter(userProfileFragmentPagerAdapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
