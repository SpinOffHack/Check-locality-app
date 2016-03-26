package kpi.pti.spinoffhackkpi.app;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import kpi.pti.spinoffhackkpi.app.utils.DBHelper;
import kpi.pti.spinoffhackkpi.app.utils.Locality;
import kpi.pti.spinoffhackkpi.app.utils.User;


public class MainActivity extends Activity {
    TextView tvEnabledGPS;
    TextView tvLocationGPS;
    TextView tvEnabledNet;
    TextView tvLocationNet;

    private LocationManager locationManager;
    private User user;
    private Location location;
    private Locality currentLocate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
        tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentLocate = new Locality();
        DBHelper connector = new DBHelper(this);
        connector.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 2, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000 * 2, 1, locationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }



    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //add to db
            currentLocate.updateData(location);
            if (currentLocate.ckeckEnabledNetwork()) {
                tvLocationNet.setText("Network "+currentLocate.toString());
            } else if (currentLocate.checkEnabledGPS()){
                tvLocationNet.setText("GPS "+currentLocate.toString());
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (currentLocate.checkEnabledGPS()|| currentLocate.ckeckEnabledNetwork()){
                currentLocate.updateData(locationManager.getLastKnownLocation(provider));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

}