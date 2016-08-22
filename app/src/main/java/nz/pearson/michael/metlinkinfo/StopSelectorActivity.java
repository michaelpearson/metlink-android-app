package nz.pearson.michael.metlinkinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import nz.pearson.michael.metlinkinfo.http.StopsNearBy;
import nz.pearson.michael.metlinkinfo.http.model.Stop;
import nz.pearson.michael.metlinkinfo.http.model.StopAdapter;
import nz.pearson.michael.metlinkinfo.location.Listener;

public class StopSelectorActivity extends AppCompatActivity implements Loadable {

    private LocationManager locationManager;
    private ListView stopView;
    private ArrayAdapter<Stop> stopViewAdapter;
    private Animation rotationAnimation = null;
    private ImageView refreshButtonImage = null;
    private boolean isLoading = false;

    @Override
    public void setLoading(boolean loading) {
        isLoading = loading;
        updateLoadingStatus();
    }

    private void updateLoadingStatus() {
        if(refreshButtonImage == null || rotationAnimation == null) {
            return;
        }
        log("Displaying loading status: " + isLoading);
        if(isLoading) {
            if(refreshButtonImage.getAnimation() == null) {
                refreshButtonImage.startAnimation(rotationAnimation);
            }
        } else {
            refreshButtonImage.clearAnimation();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_selector);

        stopView = (ListView)findViewById(R.id.stopView);
        stopViewAdapter = new StopAdapter(this, android.R.layout.simple_list_item_1);
        stopView.setAdapter(stopViewAdapter);
        stopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Stop stop = stopViewAdapter.getItem(i);
                Intent intent = new Intent(StopSelectorActivity.this, StopActivity.class);
                intent.putExtra(StopActivity.KEY_STOP_ID, stop.getStopNumber());
                startActivity(intent);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            log("Permissions not available");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        requestLocation();
    }

    private void requestLocation() {
        try {
            log("requesting location");
            updateLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 500, new Listener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateLocation(location);
                }
            }, getMainLooper());
        } catch (SecurityException ignore) {}
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1:
            default:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        System.exit(0);
                    }
                }
                requestLocation();
                break;
        }
    }

    private void updateLocation(Location l) {
        new StopsNearBy(this).getNearbyStops(l.getLongitude(), l.getLatitude(), new StopsNearBy.DataReady() {
            @Override
            public void ready(Stop[] stops) {
                stopViewAdapter.clear();
                stopViewAdapter.addAll(stops);
                stopViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_stop_menu, menu);
        MenuItem refreshButton = menu.findItem(R.id.refresh_button);
        refreshButton.setActionView(R.layout.spinner_animation);
        refreshButtonImage = (ImageView)refreshButton.getActionView().findViewById(R.id.refresh_image);
        refreshButton.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocation();
            }
        });
        rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.refresh);
        rotationAnimation.setRepeatCount(Animation.INFINITE);
        updateLoadingStatus();
        return true;
    }

    private void log(String m) {
        Log.i("StopSelector", m);
    }
}
