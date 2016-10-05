package com.example.shen_mini_itx.msgapp.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shen_mini_itx.msgapp.Models.UserModel;

/**
 * Created by shen-mini-itx on 03-Sep-16.
 */
public class UserProfileFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = "UserProfileFragmentPagerAdapter";

    final private String tabTitles[] = new String[]{"Profile", "Gallery", "Setting"};
    UserModel userModel;
    Boolean ownProfile = false;


    public UserProfileFragmentPagerAdapter(FragmentManager fm, UserModel userModel, Boolean ownProfile) {
        super(fm);
        this.userModel = userModel;
        this.ownProfile = ownProfile;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            default:
                return UserProfileFragmentProfile.newInstance(userModel);
            case 1:
                return UserProfileFragmentGallery.newInstance(userModel);
            case 2:
                return UserProfileFragmentSetting.newInstance(userModel);

        }
    }

    @Override
    public int getCount() {
        if(ownProfile)
            return tabTitles.length;
        else
            return tabTitles.length - 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
