package kpi.pti.spinoffhackkpi.app;


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

import java.util.Date;


public class MainActivity extends Activity {
    TextView tvEnabled;
    TextView tvLocation;

    private LocationManager locationManager;
    private User user;
    private Location location;
    private Locality currentLocate;
    private DBHelper connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        tvEnabled = (TextView) findViewById(R.id.tvEnabledNet);
        tvLocation = (TextView) findViewById(R.id.tvLocationNet);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentLocate = new Locality();
        connector = new DBHelper(this);
        user = new User("Pyshankov","qwerty");




    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 , 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000, 1, locationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connector.close();

    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocate.updateData(location);
            if (currentLocate.ckeckEnabledNetwork(locationManager)) {
                tvLocation.setText("Network "+currentLocate.toString());
            } else if (currentLocate.checkEnabledGPS(locationManager)){
                tvLocation.setText("GPS "+currentLocate.toString());
            }
            //add to db
            connector.addLocate(currentLocate,user);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (currentLocate.ckeckEnabledNetwork(locationManager)){
                tvEnabled.setText("GPS");
                currentLocate.updateData(locationManager.getLastKnownLocation(provider));
            } else if ( currentLocate.checkEnabledGPS(locationManager)){
                tvEnabled.setText("Network");
                currentLocate.updateData(locationManager.getLastKnownLocation(provider));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

}