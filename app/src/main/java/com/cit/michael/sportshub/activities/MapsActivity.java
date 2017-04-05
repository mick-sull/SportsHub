package com.cit.michael.sportshub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Event event;
    NetworkService service;
    Location location;
    android.support.v7.app.ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        service = RestClient.getSportsHubApiClient();
        Intent intent = getIntent();
        event = intent.getExtras().getParcelable("event");
        location = new Location();
        ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        Log.d("ACTMAP", "  event();" + event.getLocationId() );


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case android.R.id.home:
            // app icon in action bar clicked; goto parent activity.
            finish();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btnMapViewEvent)
    public void submit(View rootView) {
        // TODO submit data to server...
        Intent intent = new Intent(this,Activity_Event_Details.class);
        //Intent intent = new Intent(Activity_Search_Results.this,MapsActivity.class);
        intent.putExtra("eventSelected", event.getEventId());
        startActivity(intent);
        finish();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        service.getLocationByID(event.getLocationId().toString()).enqueue(new Callback<RestLocation>() {
            @Override
            public void onResponse(Call<RestLocation> call, Response<RestLocation> response) {
                location = response.body().getLocation().get(0);
                LatLng eventLoc = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(eventLoc).title(location.getLocationName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLoc,13));
                ab.setTitle(location.getLocationName());
            }

            @Override
            public void onFailure(Call<RestLocation> call, Throwable t) {

            }
        });


    }
}
