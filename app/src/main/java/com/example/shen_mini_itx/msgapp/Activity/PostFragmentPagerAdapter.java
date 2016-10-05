package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shen_mini_itx.msgapp.Models.PostModel;

/**
 * Created by shen-mini-itx on 17-Aug-16.
 */
public class PostFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = "PostFragmentPagerAdapter";

    final private String tabTitles[] = new String[]{"COMMENTS", "LIKED"};
    private PostModel postModel;

    public PostFragmentPagerAdapter(FragmentManager fm, PostModel postModel) {
        super(fm);
        this.postModel = postModel;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return PostFragmentComments.newInstance(postModel._id);
            case 1:
            default:
                return PostFragmentLiked.newInstance(postModel);
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
