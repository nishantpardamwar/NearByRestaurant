package nishant.nearbyrestaurants.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nishant.nearbyrestaurants.utils.Functions;

/**
 * Created by nishant pardamwar on 31/8/17.
 */

public class Places {

    private List<Place> placeList;

    public Places(LatLng location, JSONObject object) {
        placeList = new ArrayList<>();
        parseJson(location, object);
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    private void parseJson(LatLng latLng, JSONObject object) {
        if (object != null) {
            try {
                if (!object.isNull("results") && object.getJSONArray("results").length() > 0) {
                    JSONArray array = object.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        double lat = obj.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        double lng = obj.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        placeList.add(new Place.Builder()
                                .name(!obj.isNull("name") ? obj.getString("name") : "N/A")
                                .rating(!obj.isNull("rating") ? obj.getInt("rating") + "" : "Not Available")
                                .vicinity(!obj.isNull("vicinity") ? obj.getString("vicinity") : "N/A")
                                .iconUrl(!obj.isNull("icon") ? obj.getString("icon") : null)
                                .distance(Functions.distance(latLng.latitude, latLng.longitude, lat, lng))
                                .build()
                        );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
