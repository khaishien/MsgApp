package com.example.shen_mini_itx.msgapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shen_mini_itx.msgapp.Models.PostModel;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;

import java.util.List;

/**
 * Created by shen-mini-itx on 17-Aug-16.
 */
public class PostFragmentLiked extends Fragment {

    public static final String TAG = "PostFragmentLiked";
    public static final String LIKED_BUNDLE_TAG = "user_liked_list";

    private PostModel postModel;


    public static PostFragmentLiked newInstance(PostModel postModel) {

        Bundle args = new Bundle();
        args.putParcelable(LIKED_BUNDLE_TAG, postModel);
        PostFragmentLiked fragment = new PostFragmentLiked();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postModel = getArguments().getParcelable(LIKED_BUNDLE_TAG);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment_liked, container, false);

        if (postModel != null) {

            List<UserModel> liked = postModel.liked;

            if (liked != null) {

                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(new PostLikedAdapter(liked, getActivity().getApplicationContext()));


            }

        }


        return view;
    }
}
