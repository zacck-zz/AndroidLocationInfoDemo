package com.zacck.androidlocationinfodemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager mLocationManager;
    String mLocationProvider;
    //make UI elememts
    TextView tvLat, tvLong, tvAlt, tvBearing, tvAccuracy, tvSpeed, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init the location manager and provider
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationProvider = mLocationManager.getBestProvider(new Criteria(), false);

        //init the UI elements
        tvAccuracy = (TextView) findViewById(R.id.tvAccu);
        tvBearing = (TextView) findViewById(R.id.tvBearing);
        tvAlt = (TextView) findViewById(R.id.tvAltitude);
        tvLong = (TextView) findViewById(R.id.tvLong);
        tvLat = (TextView) findViewById(R.id.tvLat);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        //call on location changed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location now = mLocationManager.getLastKnownLocation(mLocationProvider);
        if (now != null) {
            onLocationChanged(now);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        //lets provide the info
        tvAccuracy.setText("Accuracy: " + String.valueOf(location.getAccuracy()));
        tvBearing.setText("Bearing: "+ String.valueOf(location.getBearing()));
        tvAlt.setText("Altitude: "+ String.valueOf(location.getAltitude()));
        tvLong.setText("Longitude: " + String.valueOf(location.getLongitude()));
        tvLat.setText("Latitude: "+ String.valueOf(location.getLatitude()));
        tvSpeed.setText("Speed: "+ String.valueOf(location.getSpeed()+"m/s"));

        //use a Geo coder to get the first address
        Geocoder mGeocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            List<Address> listAddresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0) {
                //Toast.makeText(getApplicationContext(),listAddresses.get(0).toString(),Toast.LENGTH_LONG).show();
                String AddressHolder = "";
                for(int i = 0; i<=listAddresses.get(0).getMaxAddressLineIndex(); i++)
                {
                   AddressHolder += listAddresses.get(0).getAddressLine(i)+"\n";
                }
                tvAddress.setText("Address: " + "\r\n" + AddressHolder);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //implement onpause and onresume for better resource usage

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(mLocationProvider, 400, 1, this);
    }
}
