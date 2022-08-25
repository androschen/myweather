package binus.com.weatherapp_android;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<Example> getWeather(@Query("q") String cityName,
                             @Query("appid") String apiKey);

    @GET("weather")
    Call<Example> getCurrentWeather(@Query("lat") String latitude,
                             @Query("lon") String longitude,
                             @Query("appid") String apiKey);

}
