package com.example.shen_mini_itx.msgapp.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.Responses.CommonResponse;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shen-mini-itx on 27-Aug-16.
 */
public class FoundFriendAdapter extends RecyclerView.Adapter<FoundFriendAdapter.ViewHolder> {

    private Activity activity;
    private List<UserModel> userModels;

    public FoundFriendAdapter(Activity activity, List<UserModel> userModels) {
        this.activity = activity;
        this.userModels = userModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserModel item = userModels.get(position);

        if (item.image != null)
            Picasso.with(activity).load(item.image).into(holder.friend_owner_icon);
        holder.friend_owner_name.setText(item.username);
        holder.friend_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add friend

                showConfirmFriendDialog(item);
            }
        });
    }

    private void showConfirmFriendDialog(final UserModel item) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_box_alert);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText("Confirm add " + item.username + " as friend?");

        Button dialogLeftButton = (Button) dialog.findViewById(R.id.button_left_dialog);
        dialogLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(item._id);
                dialog.dismiss();
            }
        });

        Button dialogRightButton = (Button) dialog.findViewById(R.id.button_right_dialog);
        dialogRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addFriend(String user_id) {

        Session session = new Session(activity);
        Call<CommonResponse> call = RestClient.get().addFriend(session.getApi(), session.getUserId(), user_id);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("added")) {
                        activity.onBackPressed();
                    } else if (response.body().getStatus().equals("failed")) {
                        Toast.makeText(activity, "failed to add friend..", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "failed to add friend..", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(activity, "failed to add friend..", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView friend_owner_icon;
        public TextView friend_owner_name;
        public LinearLayout friend_add_button;

        public ViewHolder(View itemView) {
            super(itemView);
            friend_owner_icon = (ImageView) itemView.findViewById(R.id.friend_owner_icon);
            friend_owner_name = (TextView) itemView.findViewById(R.id.friend_owner_name);
            friend_add_button = (LinearLayout) itemView.findViewById(R.id.friend_add_button);
        }
    }
}
