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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        user = new User("pyshankov", "12321");
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
            connector.addLocate(currentLocate, user);

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
            // получаем данные с внешнего ресурса
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
            // выводим целиком полученную json-строку
            Log.d("ALL", strJson);

            JSONObject dataJsonObj = null;
            String secondName = "";

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray friends = dataJsonObj.getJSONArray("");

                JSONObject first = friends.getJSONObject(1);
                tvEnabled.setText(first.getString("login"));
                /*
                // 2. перебираем и выводим контакты каждого друга
                for (int i = 0; i < friends.length(); i++) {
                    JSONObject friend = friends.getJSONObject(i);

                    JSONObject contacts = friend.getJSONObject("contacts");

                    String phone = contacts.getString("mobile");
                    String email = contacts.getString("email");
                    String skype = contacts.getString("skype");
                    tvEnabled.setText(skype);

                    Log.d("MY", "phone: " + phone);
                    Log.d("MY", "email: " + email);
                    Log.d("MY", "skype: " + skype);
                }
                */
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}