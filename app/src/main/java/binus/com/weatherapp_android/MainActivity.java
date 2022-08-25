package binus.com.weatherapp_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.core.splashscreen.SplashScreen;

public class MainActivity extends AppCompatActivity {
    EditText location;
    TextView city, temp, humidity, wind, feels, pressure,parameter;
    Button searchButton;
    RelativeLayout background;
    String API_KEY = "2695c02f70c98c398ff756ff8d0cc4da";
    String BASE_URL = "http://api.openweathermap.org/data/2.5/";


    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    String locationProvider = LocationManager.GPS_PROVIDER;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen =SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        location = findViewById(R.id.search);
        temp = findViewById(R.id.temp);
        city = findViewById(R.id.city);
        humidity = findViewById(R.id.humidity_value);
        wind = findViewById(R.id.wind_value);
        feels = findViewById(R.id.feels_like_value);
        pressure = findViewById(R.id.pressure_value);
        searchButton = findViewById(R.id.search_button);
        parameter = findViewById(R.id.weather_text);
        background = findViewById(R.id.background_screen);

        searchButton.setOnClickListener(view -> {
            getWeather();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }

    public void getWeather() {
        WeatherClient call = new WeatherClient();

        call.getApi().getWeather(location.getText().toString().trim(), API_KEY).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response);
                } else {
                    Toast.makeText(MainActivity.this, "Please search location name correctly", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                getCurrentWeather(latitude,longitude);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Location Get Successfuly", Toast.LENGTH_LONG).show();
                getCurrentLocation();
            }else{
                //the user is denied premission
            }
        }
    }

    public void getCurrentWeather(String lat,String lon){
        WeatherClient call = new WeatherClient();

        call.getApi().getCurrentWeather(lat,lon, API_KEY).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful() && response.body() != null) {
                   updateUI(response);
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateUI(Response<Example>response){
        Weather main = response.body().getMain();
        Integer tempValue = Integer.valueOf((int) Math.round(main.getTemp()) - 273);

        Integer feelsValue = Integer.valueOf((int) Math.round(main.getFeels_like()) - 273);

        city.setText(response.body().getName());
        temp.setText(tempValue.toString() + "°C");
        String condition = response.body().getWeatherDesc().get(0).getCondition();
        parameter.setText(condition);
        Log.d("TAG",condition);
        if(tempValue>30){
            background.setBackgroundResource(R.drawable.gradient_orange_rectangle);
        }else if(condition.equals("Rain")){
            background.setBackgroundResource(R.drawable.gradient_rain_rectangle);
        }else{
            background.setBackgroundResource(R.drawable.gradient_blue_rectangle);
        }
        wind.setText(response.body().getWind().getSpeed().toString() + "m/s");
        feels.setText(feelsValue.toString() + "°C");
        humidity.setText(main.getHumidity().toString() + "%");
        pressure.setText(main.getPressure().toString() + "hPa");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }
}