package com.example.shen_mini_itx.msgapp.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.example.shen_mini_itx.msgapp.Models.SectionModel;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shen-mini-itx on 25-Aug-16.
 */
public class FriendRecycleViewAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {


    final static String TAG = "FriendRecycleViewAdapter";

    private Context mContext;
    private List<SectionModel> sectionModelList;
    private char[] sections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ*".toCharArray();


    public FriendRecycleViewAdapter(Context mContext, List<UserModel> userModelList) {

        sectionModelList = new ArrayList<>();
        this.mContext = mContext;
        if (userModelList.size() > 0) {
            Collections.sort(userModelList, new Comparator<UserModel>() {
                @Override
                public int compare(UserModel userModel, UserModel t1) {
                    return userModel.username.compareTo(t1.username);
                }
            });
        }

        //generate section list

        for (char c : sections) {

            SectionModel sectionModel = new SectionModel();
            sectionModel.setHeaderTitle(Character.toString(c));
            List<UserModel> usersInSectionList = new ArrayList<>();
            for (UserModel userModel : userModelList) {

                char temp = userModel.username.toUpperCase().charAt(0);
                if (temp == c) {

                    usersInSectionList.add(userModel);
                }
                sectionModel.setUserModels(usersInSectionList);
            }
            sectionModelList.add(sectionModel);
        }


    }


    @Override
    public int getSectionCount() {
        return sectionModelList.size();
    }

    @Override
    public int getItemCount(int section) {
        return sectionModelList.get(section).getUserModels().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        String sectionName = sectionModelList.get(section).getHeaderTitle();
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.section_header_text.setText(sectionName);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        List<UserModel> items = sectionModelList.get(section).getUserModels();

        final UserModel item = items.get(relativePosition);

        ViewHolder viewHolder = (ViewHolder) holder;

        Picasso.with(mContext).load(item.image).into(viewHolder.friend_owner_icon);
        viewHolder.friend_owner_name.setText(item.username);
        viewHolder.friend_add_button.setVisibility(View.GONE);

        viewHolder.friend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("user", item);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;

        if (viewType == VIEW_TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_list_item_header, parent, false);
            return new SectionViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_list_item, parent, false);
            return new ViewHolder(v);
        }


//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
//        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout friend_layout;
        public ImageView friend_owner_icon;
        public TextView friend_owner_name;
        public LinearLayout friend_add_button;

        public ViewHolder(View itemView) {
            super(itemView);
            friend_layout = (RelativeLayout) itemView.findViewById(R.id.friend_layout);
            friend_owner_icon = (ImageView) itemView.findViewById(R.id.friend_owner_icon);
            friend_owner_name = (TextView) itemView.findViewById(R.id.friend_owner_name);
            friend_add_button = (LinearLayout) itemView.findViewById(R.id.friend_add_button);
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        final TextView section_header_text;

        public SectionViewHolder(View itemView) {
            super(itemView);
            section_header_text = (TextView) itemView.findViewById(R.id.section_header_text);
        }
    }

}
