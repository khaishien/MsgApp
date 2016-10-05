package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shen-mini-itx on 22-Aug-16.
 */
public class PostLikedAdapter extends RecyclerView.Adapter<PostLikedAdapter.ViewHolder> {

    private final static String TAG = "PostLikedAdapter";

    private List<UserModel> userModels;
    private Context context;

    public PostLikedAdapter(List<UserModel> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;
    }

    @Override
    public PostLikedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_liked_user_grid_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostLikedAdapter.ViewHolder holder, int position) {

        final UserModel item = userModels.get(position);
        Log.d(TAG, "onBindViewHolder " + item._id);

        holder.grid_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent or dialog show profile user
                Log.d(TAG, "grid onclick " + item.username);
            }
        });
        Picasso.with(context).load(item.image).into(holder.liked_owner_icon);
        holder.liked_owner_name.setText(item.username);
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout grid_layout;
        public ImageView liked_owner_icon;
        public TextView liked_owner_name;

        public ViewHolder(View itemView) {
            super(itemView);

            grid_layout = (RelativeLayout) itemView.findViewById(R.id.grid_layout);
            liked_owner_icon = (ImageView) itemView.findViewById(R.id.liked_owner_icon);
            liked_owner_name = (TextView) itemView.findViewById(R.id.liked_owner_name);
        }
    }
}
