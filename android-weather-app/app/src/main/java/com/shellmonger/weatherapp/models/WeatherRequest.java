package com.shellmonger.weatherapp.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.location.Location;
import com.shellmonger.weatherapp.WeatherException;
import com.shellmonger.weatherapp.WeatherManager;

public class WeatherRequest {
    public WeatherRequestType requestType;
    public WeatherManager manager;
    public long cityId;
    public String cityData;
    public Location location;

    enum WeatherRequestType {
        BY_CITYNAME,
        BY_CITYID,
        BY_LOCATION,
        BY_ZIPCODE
    }

    public WeatherRequest(String request) {
        manager = new WeatherManager();
        cityData = request;
        requestType = isZipCode(request) ? WeatherRequestType.BY_ZIPCODE : WeatherRequestType.BY_CITYNAME;
    }

    public WeatherRequest(long request) {
        manager = new WeatherManager();
        cityId = request;
        requestType = WeatherRequestType.BY_CITYID;
    }

    public WeatherRequest(Location request) {
        manager = new WeatherManager();
        location = request;
        requestType = WeatherRequestType.BY_LOCATION;
    }

    public WeatherResponse getWeather() throws WeatherException {
        switch (requestType) {
            case BY_CITYID:
                return manager.getWeatherByCityId(cityId);
            case BY_CITYNAME:
                return manager.getWeatherByCityName(cityData);
            case BY_LOCATION:
                return manager.getWeatherByGps(location);
            case BY_ZIPCODE:
                return manager.getWeatherByZipCode(cityData);
        }
        return null;
    }

    private boolean isZipCode(String test) {
        Pattern p = Pattern.compile("^[0-9]{5}$");
        Matcher m = p.matcher(test);
        return m.matches();
    }
}
