package nishant.nearbyrestaurants.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import nishant.nearbyrestaurants.R;
import nishant.nearbyrestaurants.models.Place;
import nishant.nearbyrestaurants.models.Places;
import nishant.nearbyrestaurants.network.NetworkClient;
import nishant.nearbyrestaurants.ui.adapters.RestaurantAdapter;
import nishant.nearbyrestaurants.utils.Functions;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;

public class MainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private ReactiveLocationProvider rxLocProvider;
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyTv;
    private RadioGroup sortGroup;
    private Places places = null;
    private Location currentLocation;
    private Comparator<Place> sortByDistance = (o1, o2) -> {
        if (o1.getDistance() > o2.getDistance())
            return 1;
        else if (o1.getDistance() < o2.getDistance())
            return -1;
        else
            return 0;
    };
    private Comparator<Place> sortByRating = (o1, o2) -> {
        Integer r1 = null, r2 = null;
        try {
            r1 = Integer.valueOf(o1.getRating());
        } catch (Exception e) {
        }
        try {
            r2 = Integer.valueOf(o2.getRating());
        } catch (Exception e) {
        }

        if (r1 != null && r2 != null) {
            if (r1 < r2) return 1;
            else if (r1 > r2) return -1;
            else return 0;
        } else if (r1 == null && r2 == null) return 0;
        else if (r1 == null) return 1;
        else return -1;
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        emptyTv = (TextView) findViewById(R.id.tv_empty);
        sortGroup = (RadioGroup) findViewById(R.id.rd_grp);
        setupRecyclerView();
        rxLocProvider = new ReactiveLocationProvider(this);
        setupGoogleApiClient();
        findViewById(R.id.btn_sort).setOnClickListener(v -> {
            if (sortGroup.getVisibility() == View.VISIBLE) {
                sortGroup.setVisibility(View.GONE);
            } else {
                sortGroup.setVisibility(View.VISIBLE);
            }
        });
        sortGroup.setOnCheckedChangeListener((group, checkedId) -> {
            sortGroup.setVisibility(View.GONE);
            if (places != null)
                if (checkedId == R.id.rdo_distance) {
                    Collections.sort(places.getPlaceList(), sortByDistance);
                    adapter.notifyDataSetChanged();
                } else {
                    Collections.sort(places.getPlaceList(), sortByRating);
                    adapter.notifyDataSetChanged();
                }
        });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestaurantAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!Functions.isLocationEnabled(this)) {
            enableLocationService();
        } else {
            getCurrentLocation().subscribe(location -> {
                loadNearByPlaces(location);
            }, error -> {
                error.printStackTrace();
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    private void enableLocationService() {
        if (rxLocProvider != null) {
            LocationRequest request = new LocationRequest();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
                    .addLocationRequest(request).build();
            rxLocProvider.checkLocationSettings(settingsRequest).subscribe(result -> {
                resolveLocationSettingResponse(result);
            });
        }
    }

    private Observable<Location> getCurrentLocation() {
        return Observable.create(subscriber -> {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastKnowLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnowLocation != null && Math.abs(lastKnowLocation.getTime() - System.currentTimeMillis()) < TimeUnit.MINUTES.toMillis(10)) {
                subscriber.onNext(lastKnowLocation);
                subscriber.onCompleted();
            } else {
                LocationRequest request = new LocationRequest();
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(1)
                        .setFastestInterval(0)
                        .setInterval(0);
                rxLocProvider.getUpdatedLocation(request)
                        .take(1)
                        .timeout(30, TimeUnit.SECONDS)
                        .subscribe(location -> {
                            subscriber.onNext(location);
                            subscriber.onCompleted();
                        }, error -> {
                            subscriber.onError(error);
                        });
            }
        });
    }

    private void loadNearByPlaces(Location location) {
        String locString = location.getLatitude() + "," + location.getLongitude();
        NetworkClient.instance().getPlaces(locString, "restaurant", 10000)
                .subscribe(res -> {
                    JsonObject object = res.body();
                    try {
                        places = new Places(new LatLng(location.getLatitude(), location.getLongitude()),
                                new JSONObject(object.toString()));
                        Collections.sort(places.getPlaceList(), sortByDistance);
                        adapter.setNewList(places.getPlaceList());
                        showList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    error.printStackTrace();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            getCurrentLocation().subscribe(location -> {
                loadNearByPlaces(location);
            }, error -> {
                error.printStackTrace();
            });
        }
    }

    private void resolveLocationSettingResponse(LocationSettingsResult result) {
        final Status status = result.getStatus();
        final LocationSettingsStates state = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                getCurrentLocation().subscribe(location -> {
                    loadNearByPlaces(location);
                }, error -> {
                    error.printStackTrace();
                });
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. But could be fixed by showing the user
                // a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(this, 1000);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.
                break;
        }
    }

    private void showList() {
        progressBar.setVisibility(View.GONE);
        emptyTv.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        emptyTv.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showEmptyMsg() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        emptyTv.setVisibility(View.VISIBLE);
    }
}
