package com.example.shen_mini_itx.msgapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.CommentModel;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shen-mini-itx on 19-Aug-16.
 */
public class PostFragmentComments extends Fragment {

    public static final String TAG = "PostFragmentComments";

    public static final String POST_ID = "post_id";

    private String post_id;
    private PostCommentAdapter postCommentAdapter;

    public static PostFragmentComments newInstance(String post_id) {

        Bundle args = new Bundle();
        args.putString(POST_ID, post_id);
        PostFragmentComments fragment = new PostFragmentComments();
        fragment.setArguments(args);
        return fragment;
    }


//    public void refresh(){
//        postCommentAdapter = new PostCommentAdapter(getActivity().getApplicationContext(),);
//        list.setAdapter(adapter);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post_id = getArguments().getString(POST_ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment_comments, container, false);

        if (post_id != null)
            getCommentsFromPostId(post_id, view);


        return view;
    }

    private void getCommentsFromPostId(String post_id, final View view) {

        Session session = new Session(getActivity().getApplicationContext());

        Call<List<CommentModel>> call = RestClient.get().getCommentsByPostId(session.getApi(), session.getUserId(), post_id);
        call.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {

                List<CommentModel> commentModelList = response.body();
                loadCommentList(commentModelList, view);
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });

    }

    private void loadCommentList(List<CommentModel> commentModelList, View view) {

        if (commentModelList != null) {
            RecyclerView comment_recycler_view = (RecyclerView) view.findViewById(R.id.comment_recycler_view);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            postCommentAdapter = new PostCommentAdapter(getActivity().getApplicationContext(), commentModelList);
            comment_recycler_view.setLayoutManager(mLayoutManager);
            comment_recycler_view.setItemAnimator(new DefaultItemAnimator());
            comment_recycler_view.setAdapter(postCommentAdapter);

        }

    }
}
