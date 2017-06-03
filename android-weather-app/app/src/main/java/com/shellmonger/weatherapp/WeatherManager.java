package com.shellmonger.weatherapp;

import com.shellmonger.weatherapp.models.WeatherResponse;

import android.location.Location;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Manager for the OpenWeatherMap API
 */
public class WeatherManager {
    /**
     * The API Key for the Open Weather Map API.  Replace this with your own key
     */
    private final static String API_KEY = "17f8fdc37d63d8af51696e5b468da655";

    /**
     * The Base URL of the Open Weather Map API.
     */
    private final static String BASE_URL = "http://samples.openweathermap.org/data/2.5/weather";

    /**
     * The HTTP Client implementation to use
     */
    private OkHttpClient httpClient;

    /**
     * Instantiate a new object for handling the OpenWeatherMap API - instantiates a
     * default OkHttpClient()
     */
    public WeatherManager() {
        httpClient = new OkHttpClient();
    }

    /**
     * Version of the constructor that allows the specification of a HTTP Client.  It must
     * be an OkHttpClient implementation
     * @param client The HTTP Client to use.
     */
    public WeatherManager(OkHttpClient client) {
        httpClient = client;
    }

    /**
     * Retrieve the weather baed on a city name
     * @param cityName the name of the city
     * @return the WeatherResponse object
     * @throws WeatherException if the request could not be completed
     */
    public WeatherResponse getWeatherByCityName(String cityName) throws WeatherException {
        return getWeather("q=" + cityName);
    }

    /**
     * Retrieve the weather based on an internal city ID
     * @param id the city ID
     * @return the WeatherResponse object
     * @throws WeatherException if the request could not be completed
     */
    public WeatherResponse getWeatherByCityId(long id) throws WeatherException {
        return getWeather("id=" + id);
    }

    /**
     * Retrieve the weather by GPS coordinates
     * @param longitude The longitude
     * @param latitude The latitude
     * @return the WeatherResponse object
     * @throws WeatherException if the request could not be completed
     */
    public WeatherResponse getWeatherByGps(double longitude, double latitude) throws WeatherException {
        return getWeather("lat=" + latitude + "&lon=" + longitude);
    }

    /**
     * Retrieve the weather by GPS coordinates
     * @param location the GPS location
     * @return the WeatherResponse object
     * @throws WeatherException if the request could not be completed
     */
    public WeatherResponse getWeatherByGps(Location location) throws WeatherException {
        return getWeatherByGps(location.getLatitude(), location.getLongitude());
    }

    /**
     * Retrieve the weather for a ZIP code
     * @param zipcode the ZIP code to use in retrieval
     * @return the WeatherResponse object
     * @throws WeatherException if the request could not be completed
     */
    public WeatherResponse getWeatherByZipCode(String zipcode) throws WeatherException {
        return getWeather("zip=" + zipcode);
    }

    /**
     * Call the OpenWeatherMap API using the specified query.  You should never call this
     * directly - use one of the convenience methods above.
     *
     * @param query the query to send to the OpenWeatherMap API
     * @return A WeatherResponse object
     * @throws WeatherException if the request could not be completed
     */
    private WeatherResponse getWeather(String query) throws WeatherException {
        try {
            URL url = new URL(BASE_URL + "?" + query + "&appid=" + API_KEY);
            Request request = new Request.Builder().url(url).build();
            Response response = httpClient.newCall(request).execute();
            String jsonResponse = response.body().string();
            return WeatherResponse.fromJson(jsonResponse);
        } catch (Exception ex) {
            throw new WeatherException("HTTP GET failed", ex);
        }

    }
}
