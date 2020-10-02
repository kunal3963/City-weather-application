package kunal.weather.data.source.remote;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import kunal.weather.data.model.currentweather.CurrentWeatherResponse;
import kunal.weather.data.model.sevendayweather.SevenDayResponse;
import kunal.weather.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RestService {

    enum Client {
        INSTANT;

        private OkHttpClient client;
        private Retrofit retrofit;

        public Retrofit obtain() {
            if (client == null) {
                client = new OkHttpClient.Builder()
                        .connectTimeout(9, TimeUnit.SECONDS)
                        .build();
            }
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    interface Service {

        @GET("weather")
        Flowable<CurrentWeatherResponse> getCurrentWeather(
                @Query("q") String q,
                @Query("appid") String appId
        );

        @GET("forecast")
        Flowable<SevenDayResponse> getFiveDaysWeather(
                @Query("q") String q,
                @Query("appid") String appId
        );
    }
}
