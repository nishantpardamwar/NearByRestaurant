package nishant.nearbyrestaurants.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant pardamwar on 31/8/17.
 */

public class Places {

    private List<Place> placeList;

    public Places(JSONObject object) {
        placeList = new ArrayList<>();
        parseJson(object);
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    private void parseJson(JSONObject object) {
        if (object != null) {
            try {
                if (!object.isNull("results") && object.getJSONArray("results").length() > 0) {
                    JSONArray array = object.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        placeList.add(new Place.Builder()
                                .name(!obj.isNull("name") ? obj.getString("name") : "N/A")
                                .rating(!obj.isNull("rating") ? obj.getInt("rating") + "" : "Not Available")
                                .vicinity(!obj.isNull("vicinity") ? obj.getString("vicinity") : "N/A")
                                .iconUrl(!obj.isNull("icon") ? obj.getString("icon") : null)
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
