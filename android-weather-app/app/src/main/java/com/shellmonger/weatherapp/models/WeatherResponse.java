package com.shellmonger.weatherapp.models;

import android.location.Location;
import java.util.Date;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Model for describing the response from the OpenWeatherMap API
 */
public class WeatherResponse {
    @SerializedName("coord") private Coord coord;
    @SerializedName("weather") private ArrayList<Weather> weather;
    @SerializedName("base") private String base;
    @SerializedName("main") private Main main;
    @SerializedName("visibility") private int visibility;
    @SerializedName("wind") private Wind wind;
    @SerializedName("clouds") private Clouds clouds;
    @SerializedName("rain") private Precipitation rain;
    @SerializedName("snow") private Precipitation snow;
    @SerializedName("dt") private long unixTimestamp;
    @SerializedName("sys") private Sys sys;
    @SerializedName("id") private long city_id;
    @SerializedName("name") private String city_name;
    @SerializedName("cod") private int response_code;

    public Coord getCoordinates() { return coord; }

    /**
     * Returns the array of weather conditions
     */
    public Weather[] getWeatherConditions() {
        Weather[] arr = new Weather[weather.size()];
        return weather.toArray(arr);
    }

    public Main getMain() { return main; }

    public int getVisibility() { return visibility; }

    public Wind getWind() { return wind; }

    public Clouds getClouds() { return clouds; }

    public Precipitation getRain() { return rain; }

    public Precipitation getSnow() { return snow; }

    /**
     * Returns java.util.Date representation of the timestamp
     */
    public Date getTimestamp() {
        return new Date((long)(unixTimestamp * 1000));
    }

    public Sys getSysInfo() { return sys; }

    public long getCityId() { return city_id; }

    public String getCityName() { return city_name; }

    public int getResponseCode() { return response_code; }

    /**
     * Convert the current object to JSON string format
     * @return a JSON representation of the object
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Creates a new WeatherResponse object from a JSON string
     * @param json the JSON string needed to deserialize
     * @return a WeatherResponse object
     */
    public static WeatherResponse fromJson(String json) {
        return new Gson().fromJson(json, WeatherResponse.class);
    }

    /**
     * The internal representation of the "clouds" portion of the API
     */
    public class Clouds {
        @SerializedName("all") private int cloudiness;

        public int getCloudiness() { return cloudiness; }
    }

    /**
     * The internal representation of the "coord" portion of the API
     */
    public class Coord {
        @SerializedName("lon") private double lon;
        @SerializedName("lat") private double lat;

        public double getLongitude() { return lon; }
        public double getLatitude() { return lat; }
    }

    /**
     * The internal representation of the "main" portion of the API
     */
    public class Main {
        @SerializedName("temp") private double temp;
        @SerializedName("pressure") private int pressure;
        @SerializedName("humidity") private int humidity;
        @SerializedName("temp_min") private double temp_min;
        @SerializedName("temp_max") private double temp_max;
        @SerializedName("sea_level") private int sea_level;
        @SerializedName("grnd_level") private int ground_level;

        public Range getTemperature() { return new Range(temp, temp_min, temp_max); }
        public Pressure getPressure() { return new Pressure(pressure, sea_level, ground_level); }
        public int getHumidity() { return humidity; }
    }

    /**
     * The internal representation of the "rain" and "snow" portion of the API
     */
    public class Precipitation {
        @SerializedName("3h") private double volume;

        public double getVolume() { return volume; }
    }

    public class Pressure {
        private int current, sealevel, groundlevel;

        public Pressure(int current, int sealevel, int groundlevel) {
            this.current = current;
            this.sealevel = sealevel;
            this.groundlevel = groundlevel;
        }

        public int getCurrentPressure() { return current; }
        public int getSeaLevelPressure() { return sealevel; }
        public int getGroundLevelPressure() { return groundlevel; }
    }

    public class Range {
        private double current, min, max;

        public Range(double current, double min, double max) {
            this.current = current;
            this.min = min;
            this.max = max;
        }

        public double getCurrentValue() { return current; }
        public double getMinimumValue() { return min; }
        public double getMaximumValue() { return max; }
    }

    /**
     * The internal representation of the "sys" portion of the API
     */
    public class Sys {
        @SerializedName("type") private int type;
        @SerializedName("id") private int id;
        @SerializedName("message") private double message;
        @SerializedName("country") private String country;
        @SerializedName("sunrise") private long sunrise;
        @SerializedName("sunset") private long sunset;

        public String getCountry() { return country; }
        public Date getSunrise() { return new Date((long)(sunrise * 1000)); }
        public Date getSunset() { return new Date((long)(sunset * 1000)); }
    }

    /**
     * The internal representation of the "weather" list portion of the API
     */
    public class Weather {
        @SerializedName("id") private int id;
        @SerializedName("main") private String main;
        @SerializedName("description") private String description;
        @SerializedName("icon") private String icon;

        public int getId() { return id; }
        public String getShortDescription() { return main; }
        public String getLongDescription() { return description; }
        public String getIcon() { return icon; }
    }

    /**
     * The internal representation of the "wind" portion of the API
     */
    public class Wind {
        @SerializedName("speed") private double speed;
        @SerializedName("deg") private int direction;

        public double getSpeed() { return speed; }
        public int getDirection() { return direction; }
    }
}
