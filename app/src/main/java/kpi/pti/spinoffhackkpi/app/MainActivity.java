package kpi.pti.spinoffhackkpi.app;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import kpi.pti.spinoffhackkpi.app.utils.DBHelper;
import kpi.pti.spinoffhackkpi.app.utils.Locality;
import kpi.pti.spinoffhackkpi.app.utils.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {
    TextView tvEnabled;
    TextView tvLocation;
    TextView UserLabel;
    TextView LocationsView;

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
        UserLabel = (TextView) findViewById(R.id.userLabel);
        LocationsView = (TextView) findViewById(R.id.Locations);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentLocate = new Locality();
        //connector = new DBHelper(this);
        //user = new User("pyshankov", "12321");
        new ParseTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, locationListener);
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
                tvLocation.setText("Network " + currentLocate.toString());
            } else if (currentLocate.checkEnabledGPS(locationManager)) {
                tvLocation.setText("GPS " + currentLocate.toString());
            }
            //add to db
            //connector.addLocate(currentLocate, user);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (currentLocate.ckeckEnabledNetwork(locationManager)) {
                tvEnabled.setText("GPS");
                currentLocate.updateData(locationManager.getLastKnownLocation(provider));
            } else if (currentLocate.checkEnabledGPS(locationManager)) {
                tvEnabled.setText("Network");
                currentLocate.updateData(locationManager.getLastKnownLocation(provider));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://tisom.herokuapp.com");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            Log.d("ALL", strJson);
            try {
                JSONArray array= new JSONArray(strJson);
                /*
                JSONObject firstLocation=array.getJSONObject(0);
                JSONArray localities=new JSONArray(firstLocation.getString("localities"));

                ArrayList<String> list=new ArrayList<String>();
                for (int i=0;i<localities.length();i++){
                    String latitude = localities.getJSONObject(i).getString("latitude");
                    String longitude = localities.getJSONObject(i).getString("longitude");
                    list.add("("+latitude+" , "+longitude+")");
                }
                */
                ArrayList<User> userList= new ArrayList<User>();

                for (int i=0;i<array.length();i++){
                    ArrayList<Locality> locationsList= new ArrayList<Locality>();
                    JSONObject entity=array.getJSONObject(i);
                    JSONArray localities=new JSONArray(entity.getString("localities"));
                    for (int j=0;j<localities.length();j++){
                        Double latitude = localities.getJSONObject(i).getDouble("latitude");
                        Double longitude = localities.getJSONObject(i).getDouble("longitude");
                        Long timestamp = localities.getJSONObject(i).getLong("timestamp");
                        locationsList.add(new Locality(latitude,longitude,new Date(timestamp*1000L)));
                        //LocationsView.setText(new Locality(latitude,longitude,new Date(timestamp*1000L)).toString());
                    }
                    userList.add(new User(entity.getString("login"),entity.getString("password"),locationsList));
                    //LocationsView.setText("his locations : "+locationsList.toString());
                }


               // tvEnabled.setText(firstLocation.getString("localities"));
                UserLabel.setText("users: "+userList.toString());
                //LocationsView.setText(new Date(1478736000000L*1000L).toString());
                //LocationsView.setText(list.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}