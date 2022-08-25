package binus.com.weatherapp_android;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Example {
    @SerializedName("main")
    Weather main;
    @SerializedName("name")
    String name;
    @SerializedName("wind")
    Wind wind;
    @SerializedName("weather")
    ArrayList<WeatherDesc> weatherDesc;

    public Weather getMain() {
        return main;
    }

    public void setMain(Weather main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public ArrayList<WeatherDesc> getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(ArrayList<WeatherDesc> weatherDesc) {
        this.weatherDesc = weatherDesc;
    }
}
