package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFriendActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        setupSearchToolbar();

    }

    private void setupSearchToolbar() {

        EditText friend_search_term = (EditText) findViewById(R.id.friend_search_term);
        friend_search_term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String s = charSequence.toString().trim();
                if(s.length() > 0){
                    lookupSearchTerm(s);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void lookupSearchTerm(String s) {
        Session session = new Session(this);
        Call<List<UserModel>> call = RestClient.get().searchFriendList(session.getApi(), session.getUserId(), s);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                if (response.isSuccessful()) {
                    setupRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }
        });
    }


    private void setupRecyclerView(List<UserModel> body) {
        RecyclerView found_friend_recycler_view = (RecyclerView) findViewById(R.id.found_friend_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        FoundFriendAdapter foundFriendAdapter = new FoundFriendAdapter(SearchFriendActivity.this, body);
        found_friend_recycler_view.setLayoutManager(mLayoutManager);
        found_friend_recycler_view.setItemAnimator(new DefaultItemAnimator());
        found_friend_recycler_view.setAdapter(foundFriendAdapter);


    }


}
