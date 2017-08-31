package nishant.nearbyrestaurants.network;

import com.google.gson.JsonObject;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nishant pardamwar on 31/8/17.
 */

public interface RetrofitInterface {
    String BASE_URL = "https://maps.googleapis.com/";
    String API_GET_NEARBY_PLACES = "maps/api/place/nearbysearch/json";
    String KEY_LOCATION = "location";
    String KEY_TYPE = "type";
    String KEY_RADIUS = "radius";
    String KEY_API_KEY = "key";
    String API_KEY = "AIzaSyADmOAub-jvSMO40ULVs6MDnBNn8f85IgY";

    @GET(API_GET_NEARBY_PLACES)
    Observable<Response<JsonObject>> getPlaces(@Query(KEY_LOCATION) String location,
                                               @Query(KEY_TYPE) String type,
                                               @Query(KEY_RADIUS) int radiusInMeter,
                                               @Query(KEY_API_KEY) String apiKey);
}
