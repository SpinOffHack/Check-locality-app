package kpi.pti.spinoffhackkpi.app.utils;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Date;

public class Locality {
    private double latitude;
    private double longitude;
    private Date timestamp;
    private LocationManager locationManager;


    @Override
    public String toString() {

        return "Locale(" +
                "y=" + longitude +
                ", timestamp=" + timestamp +
                ", x=" + latitude +
                ')';
    }

    public void updateData(Location location){
        this.latitude =location.getLatitude();
        this.longitude =location.getLongitude();
        this.timestamp = new Date(location.getTime());
        //location.hasSpeed()
        //location.getSpeed()
    }
    public String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: x = %1$.4f, y = %2$.4f, timestamp = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }
    public String getStringLocation(Location location) {
        if (location == null)
            return "";
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            return formatLocation(location);
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            return formatLocation(location);
        }
        return null;
    }
    public boolean checkEnabledGPS() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public boolean ckeckEnabledNetwork() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

}
