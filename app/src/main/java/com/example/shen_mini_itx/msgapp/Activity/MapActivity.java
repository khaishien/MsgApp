package com.example.shen_mini_itx.msgapp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shen_mini_itx.msgapp.Common.Session;
import com.example.shen_mini_itx.msgapp.Models.PostModel;
import com.example.shen_mini_itx.msgapp.R;
import com.example.shen_mini_itx.msgapp.Requests.PostRequest;
import com.example.shen_mini_itx.msgapp.Responses.CommonResponse;
import com.example.shen_mini_itx.msgapp.WebCall.RestClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MapActivity";
    final double DEFAULT_RADIUS = 1000;
    final double RADIUS_OF_EARTH_METERS = 6371009;
    LatLng currentLocation = new LatLng(3.215152, 101.643966);
    private GoogleMap mMap;
    private MapView mMapView;
    private Circle cirle;
    private Marker marker;
    private ListViewCompat mListView;
    private GoogleApiClient mGoogleApiClient;
    private PostListingAdapter mAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("Geo Posts");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        context = this;

        mListView = (ListViewCompat) findViewById(R.id.list);


        mMapView = (MapView) findViewById(R.id.f_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setUpMap(googleMap);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_post:
                popDialogPost();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void popDialogPost() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box_new_post_input, null);

        TextView dialogTitle = (TextView) mView.findViewById(R.id.dialogTitle);
        dialogTitle.setText("New Post");

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput.setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String postContent = userInputDialogEditText.getText().toString();
                        //create post here
                        restCallNewPost(postContent);
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

    private void restCallNewPost(String postContent) {


        PostRequest request = new PostRequest();
        request.content = postContent;
        request.lat = currentLocation.latitude;
        request.lng = currentLocation.longitude;
        Session session = new Session(this);

        Call<CommonResponse> call = RestClient.get().newPostBasedLocation(session.getApi(), session.getUserId(), request);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.body().getStatus().equals("success"))
                    Toast.makeText(getApplicationContext(), "success add new post...",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "failed add new post...try again!",
                            Toast.LENGTH_SHORT).show();

                loadPostListView(currentLocation);

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "failed add new post...try again!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

        if (mGoogleApiClient != null) {

            Log.d(TAG, "onResume mGoogleApiClient");
            getLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void setUpMap(GoogleMap map) {
        Log.d(TAG, "map ready");
        mMap = map;
        //current location check
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);


        marker = mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .draggable(true)
                .alpha(0.5f)
                .title("long click to drag me"));


        cirle = mMap.addCircle(new CircleOptions()
                .center(currentLocation)
                .radius(DEFAULT_RADIUS)
                .strokeWidth(5)
                .strokeColor(Color.TRANSPARENT)
                .fillColor(Color.argb(120, 118, 118, 118)));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

                cirle.setVisible(false);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {


                currentLocation = marker.getPosition();
                cirle.setCenter(currentLocation);
                cirle.setVisible(true);
                loadPostListView(currentLocation);


            }
        });
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d(TAG, "onConnected");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }

    }

    private void getLocation() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation == null) {
            Log.d(TAG, "null mLastLocation");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("please turn on location service...")
                    .setPositiveButton("Location Service", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(myIntent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();

        } else {
            Log.d(TAG, "not null mLastLocation");

            drawnOnMap(mLastLocation);
        }
    }

    private void drawnOnMap(Location mLastLocation) {

        currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        Log.d(TAG, "onConnected mLastLocation updated: " + mLastLocation.getLatitude() + " , " + mLastLocation.getLongitude());


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14.0f));
        loadPostListView(currentLocation);

        marker.setPosition(currentLocation);
        cirle.setCenter(currentLocation);



    }

    private void loadPostListView(LatLng lot) {


        Session session = new Session(this);

        double radiusAngle = Math.toDegrees(DEFAULT_RADIUS / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(lot.latitude));

        Call<List<PostModel>> call = RestClient.get().getPostsBasedLocation(session.getApi(),
                session.getUserId(), lot.latitude, lot.longitude, radiusAngle);

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                Toast.makeText(getApplicationContext(), "success loading posts...",
                        Toast.LENGTH_SHORT).show();

                Log.d(TAG, "loadPostListView");
                updatePostListView(response.body());


            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d(TAG, "loadPostListView failed: " + t.getMessage());

                Toast.makeText(getApplicationContext(), "failed to load posts...",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updatePostListView(List<PostModel> postModel) {
        Log.d(TAG, "updatePostListView");


        Marker temp = mMap.addMarker(new MarkerOptions()
                .draggable(false)
                .visible(false)
                .position(currentLocation)
                .title("long click to make it gone"));


        mAdapter = new PostListingAdapter(this, temp, postModel);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



}

