package nishant.nearbyrestaurants.network;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nishant pardamwar on 20/5/17.
 */

public class NetworkClient {
    private static NetworkClient networkClient;
    private RetrofitInterface apis;

    private NetworkClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(interceptor).build();

        GsonBuilder gsonBuilder = new GsonBuilder();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(client)
                .baseUrl("https://maps.googleapis.com/")
                .build();

        apis = retrofit.create(RetrofitInterface.class);
    }

    public static NetworkClient instance() {
        if (networkClient == null) {
            networkClient = new NetworkClient();
        }
        return networkClient;
    }

    public Observable<Response<JsonObject>> getPlaces(String location, String type, int radius) {
        return apis.getPlaces(location, type, radius, RetrofitInterface.API_KEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
