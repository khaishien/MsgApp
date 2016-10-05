package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.shen_mini_itx.msgapp.Common.ChatHeadService;
import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.SectionModel;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSetting = new Intent(context, SearchFriendActivity.class);
                startActivity(intentSetting);
            }
        });

        Session session = new Session(this);

        Call<List<UserModel>> call = RestClient.get().getFriendListByUserId(session.getApi(), session.getUserId());
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {


                setupRecycleView(response.body());
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }
        });

    }

    private void setupRecycleView(List<UserModel> body) {

        RecyclerView friend_recycler_view = (RecyclerView) findViewById(R.id.friend_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        FriendRecycleViewAdapter friendRecycleViewAdapter = new FriendRecycleViewAdapter(getApplicationContext(), body);
        friend_recycler_view.setLayoutManager(mLayoutManager);
        friend_recycler_view.setItemAnimator(new DefaultItemAnimator());
//        friend_recycler_view.addItemDecoration(
//                new DividerItemDecoration(this, null));
        friend_recycler_view.setAdapter(friendRecycleViewAdapter);
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_friend, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.action_add_friend:
//                Intent intentSetting = new Intent(this, SearchFriendActivity.class);
//                startActivity(intentSetting);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
