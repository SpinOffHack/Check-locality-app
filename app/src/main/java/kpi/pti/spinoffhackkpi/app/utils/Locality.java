package kpi.pti.spinoffhackkpi.app.utils;

import android.location.Location;
import android.location.LocationManager;
import java.util.Date;

public class Locality {
    private double latitude;
    private double longitude;
    private Date timestamp;
    public Locality(){}
    public Locality(double x,double y,Date timestamp){
        latitude=x;
        longitude=y;
        this.timestamp=timestamp;
    }

    @Override
    public String toString() {

        return "Locale(" +
                "y=" + longitude +
                ", x=" + latitude +
                ", timestamp=" + timestamp.toString() +
                ')';
    }

    public void updateData(Location location) {
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            this.timestamp = new Date(location.getTime());
        }
        //location.hasSpeed()
        //location.getSpeed()

    }
    public boolean checkEnabledGPS(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public boolean ckeckEnabledNetwork(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
