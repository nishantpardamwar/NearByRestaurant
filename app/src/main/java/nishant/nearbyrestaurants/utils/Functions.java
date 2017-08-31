package nishant.nearbyrestaurants.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by nishant pardamwar on 31/8/17.
 */

public class Functions {
    public static boolean isLocationEnabled(Context context) {
        if (context != null) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            String networkProvider = LocationManager.NETWORK_PROVIDER;
            if (lm.isProviderEnabled(networkProvider)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException("Context cannot be null");
        }
    }

    public static boolean isStringValid(String str) {
        if (str == null) {
            return false;
        }
        if (str.trim().length() == 0) {
            return false;
        }
        return str.trim() != "";

    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
