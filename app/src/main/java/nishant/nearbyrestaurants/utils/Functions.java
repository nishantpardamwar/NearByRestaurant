package nishant.nearbyrestaurants.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;

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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //return activeNetworkInfo != null && activeNetworkInfo.isConnected() && Connectivity.isConnectedFast(context);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void openAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
