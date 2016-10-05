package com.example.shen_mini_itx.msgapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.PostModel;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.Responses.CommonResponse;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shen-mini-itx on 10-Aug-16.
 */
public class PostListingAdapter extends BaseAdapter {
    private static final String TAG = "PostListingAdapter";


    private Context mContext;
    private List<PostModel> postModelList;
    private LayoutInflater layoutInflater;
    private Marker marker;


    public PostListingAdapter(Context context, Marker marker, List<PostModel> postModelList) {
        this.postModelList = postModelList;
        this.mContext = context;
        this.marker = marker;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (postModelList != null)
            return postModelList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return postModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder; // without initialized
        final PostModel item = postModelList.get(position);
        Session session = new Session(mContext);


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.post_listing_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.msg = (TextView) convertView.findViewById(R.id.msg);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.like = (ImageView) convertView.findViewById(R.id.like);
            holder.top_wrapper = (LinearLayout) convertView.findViewById(R.id.top_wrapper);
            holder.like_wrapper = (RelativeLayout) convertView.findViewById(R.id.like_wrapper);

            holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_layout_post);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Picasso.with(mContext).load(item.owner.image).into(holder.icon);
        holder.title.setText(item.owner.username);
        holder.msg.setText(item.content);
        holder.time.setText(new SimpleDateFormat("dd/M/yyyy").format(item.timestamp));
        holder.like.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        holder.clicked = false;

        //check status like button
        for (UserModel user : item.liked) {
            Log.d(TAG, user._id + " : " + session.getUserId());
            if (user._id.equals(session.getUserId())) {
                holder.like.setImageResource(R.drawable.ic_favorite_white_24dp);
                holder.clicked = true;
            }
        }

        holder.top_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                marker.setPosition(new LatLng(item.latitude, item.longitude));
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(item.owner.image)));
                marker.setVisible(true);
            }
        });

        holder.top_wrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra("post", item);
                mContext.startActivity(intent);


                return true;
            }
        });

        holder.like_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost(item._id);

                Drawable drawable = mContext.getDrawable(R.drawable.ic_favorite_border_white_24dp);
                Drawable drawable2 = mContext.getDrawable(R.drawable.ic_favorite_white_24dp);

                if (holder.clicked) {
                    holder.like.setImageDrawable(drawable);
                    holder.clicked = false;
                } else {
                    holder.like.setImageDrawable(drawable2);
                    holder.clicked = true;
                }

                holder.swipeLayout.close(true);
            }
        });


        return convertView;
    }

    private void likePost(String _id) {

        Session session = new Session(mContext);
        Call<CommonResponse> call;

        call = RestClient.get().setLikePostStatus(session.getApi(), session.getUserId(), _id);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals("liked")) {
                    Toast.makeText(mContext, "liked", Toast.LENGTH_SHORT).show();

                } else if (response.body().getStatus().equals("unlike")) {
                    Toast.makeText(mContext, "unlike :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(mContext, "failed to like.. :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView msg;
        TextView time;
        ImageView like;
        LinearLayout top_wrapper;
        RelativeLayout like_wrapper;
        SwipeLayout swipeLayout;
        Boolean clicked;
    }

    private Bitmap getMarkerBitmapFromView(String url) {

        View customMarkerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);

        if (url != null)
            Picasso.with(mContext).load(url).into(markerImageView);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap editMarker(String string_url) throws IOException {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        URL url = new URL(string_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        //Bitmap bmImg = BitmapFactory.decodeStream(is);

        // modify canvas

        canvas1.drawBitmap(BitmapFactory.decodeStream(is), 0, 0, color);
        canvas1.drawText("User Name!", 30, 40, color);

        return bmp;
    }
}
