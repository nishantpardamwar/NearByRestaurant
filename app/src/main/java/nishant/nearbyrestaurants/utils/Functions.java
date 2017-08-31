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
}
