package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shen_mini_itx.msgapp.Models.CommentModel;
import com.example.shen_mini_itx.msgapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by shen-mini-itx on 23-Aug-16.
 */
public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {

    private final static String TAG = "PostCommentAdapter";
    private Context context;
    private List<CommentModel> commentModelList;


    public PostCommentAdapter(Context context, List<CommentModel> commentModelList) {
        this.context = context;
        this.commentModelList = commentModelList;
    }

    @Override
    public PostCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comments_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostCommentAdapter.ViewHolder holder, int position) {

        final CommentModel item = commentModelList.get(position);

        Picasso.with(context).load(item.owner.image).into(holder.comment_owner_icon);
        holder.comment_owner_name.setText(item.owner.username);
        holder.comment_content.setText(item.content);
        holder.comment_date.setText(new SimpleDateFormat("dd/M/yyyy hh:mm a").format(item.timestamp));


    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView comment_owner_icon;
        public TextView comment_owner_name;
        public TextView comment_content;
        public TextView comment_date;

        public ViewHolder(View itemView) {
            super(itemView);

            comment_owner_icon = (ImageView) itemView.findViewById(R.id.comment_owner_icon);
            comment_owner_name = (TextView) itemView.findViewById(R.id.comment_owner_name);
            comment_content = (TextView) itemView.findViewById(R.id.comment_content);
            comment_date = (TextView) itemView.findViewById(R.id.comment_date);
        }
    }
}
