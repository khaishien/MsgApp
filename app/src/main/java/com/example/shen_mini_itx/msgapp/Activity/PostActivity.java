package com.example.shen_mini_itx.msgapp.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shen_mini_itx.msgapp.Common.ImageResource;
import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.PostModel;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.Requests.CommentRequest;
import com.example.shen_mini_itx.msgapp.Requests.PostRequest;
import com.example.shen_mini_itx.msgapp.Responses.CommonResponse;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shen-mini-itx on 17-Aug-16.
 */
public class PostActivity extends AppCompatActivity {

    public static final String TAG = "PostActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private boolean miniTitleVisible = false;
    private PostModel postModel;
    private PostFragmentPagerAdapter postFragmentPagerAdapter;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 1f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll);
        scrollView.setFillViewport(true);

        postModel = getIntent().getParcelableExtra("post");

        if (postModel != null) {

            setUpToolbar(postModel.owner.username, postModel.content);

            ImageView post_owner_icon = (ImageView) findViewById(R.id.post_owner_icon);
            ImageView post_owner_mini_icon = (ImageView) findViewById(R.id.post_owner_mini_icon);
            Picasso.with(this).load(postModel.owner.image).into(post_owner_icon);
            Picasso.with(this).load(postModel.owner.image).into(post_owner_mini_icon);

            TextView post_content = (TextView) findViewById(R.id.post_content);
            post_content.setText(postModel.content);


            viewPager = (ViewPager) findViewById(R.id.view_pager);
            tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

            postFragmentPagerAdapter = new PostFragmentPagerAdapter(getSupportFragmentManager(), postModel);
            viewPager.setAdapter(postFragmentPagerAdapter);
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                }
            });


            FloatingActionButton add_comment_fab = (FloatingActionButton) findViewById(R.id.add_comment_fab);
            add_comment_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popDialogComment(postModel._id);
                }
            });
        }


    }

    private void popDialogComment(final String post_id) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box_new_post_input, null);

        TextView dialogTitle = (TextView) mView.findViewById(R.id.dialogTitle);
        dialogTitle.setText("New Comment");

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput.setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String postContent = userInputDialogEditText.getText().toString();
                        //create post here
                        restCallNewComment(postContent,post_id);
                        postFragmentPagerAdapter = new PostFragmentPagerAdapter(getSupportFragmentManager(), postModel);
                        viewPager.setAdapter(postFragmentPagerAdapter);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        alertDialogBuilderUserInput.create().show();

    }

    private void restCallNewComment(String postContent, String post_id) {

        Session session = new Session(this);
        CommentRequest comment = new CommentRequest();
        comment.content = postContent;
        comment.post_id = post_id;

        Call<CommonResponse> call = RestClient.get().newCommentOnPost(session.getApi(), session.getUserId(), comment);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals("success")){
                    Toast.makeText(getApplicationContext(), "success add new comment...",
                            Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "failed add new comment...try again!",
                            Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "failed add new comment...try again!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpToolbar(final String title, final String subTitle) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        final ImageView post_owner_mini_icon = (ImageView) findViewById(R.id.post_owner_mini_icon);
        post_owner_mini_icon.setVisibility(View.INVISIBLE);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleMarginEnd(0);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                if (percentage == PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {


                    if (!miniTitleVisible) {
                        ImageResource.startAlphaAnimation(post_owner_mini_icon, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                        collapsingToolbarLayout.setTitle(subTitle);
                        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);

                        miniTitleVisible = true;
                    }

                } else {

                    if (miniTitleVisible) {
                        ImageResource.startAlphaAnimation(post_owner_mini_icon, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                        miniTitleVisible = false;
                        collapsingToolbarLayout.setTitle(title);

                    }

                }

            }
        });

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


}
