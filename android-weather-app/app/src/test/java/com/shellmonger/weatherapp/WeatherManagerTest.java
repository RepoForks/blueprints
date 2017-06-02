package com.shellmonger.weatherapp;

import com.shellmonger.weatherapp.models.WeatherResponse;
import java.io.IOException;
import okhttp3.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class WeatherManagerTest {
    @Test
    public void canGetWeatherByCityName() throws WeatherException {
        WeatherManager mgr = new WeatherManager();
        WeatherResponse response = mgr.getWeatherByCityName("London,UK");
        assertEquals(response.getResponseCode(), 200);
    }

    @Test
    public void canGetWeatherByCityId() throws WeatherException {
        WeatherManager mgr = new WeatherManager();
        WeatherResponse response = mgr.getWeatherByCityId(2172797);
        assertEquals(response.getResponseCode(), 200);
    }

    @Test
    public void canGetWeatherByGps() throws WeatherException {
        WeatherManager mgr = new WeatherManager();
        WeatherResponse response = mgr.getWeatherByGps(145.77, -16.92);
        assertEquals(response.getResponseCode(), 200);
    }

    @Test
    public void canGetWeatherByZipcode() throws WeatherException {
        WeatherManager mgr = new WeatherManager();
        WeatherResponse response = mgr.getWeatherByZipCode("98155");
        assertEquals(response.getResponseCode(), 200);
    }

    @Test
    public void mockWeatherWorks() throws WeatherException {
        WeatherManager mgr = new WeatherManager(getClient());
        WeatherResponse response = mgr.getWeatherByCityName("London,UK");
        assertEquals(response.getResponseCode(), 200);

        assertEquals(51.51, response.getCoordinates().getLatitude(), 0.001);
        assertEquals(-0.13, response.getCoordinates().getLongitude(), 0.001);

        assertEquals(1, response.getWeatherConditions().length);
        assertEquals(300, response.getWeatherConditions()[0].getId());
        assertEquals("Drizzle", response.getWeatherConditions()[0].getShortDescription());
        assertEquals("light intensity drizzle", response.getWeatherConditions()[0].getLongDescription());
        assertEquals("09d", response.getWeatherConditions()[0].getIcon());

        assertEquals(280.32, response.getMain().getTemperature().getCurrentValue(), 0.001);
        assertEquals(279.15, response.getMain().getTemperature().getMinimumValue(), 0.001);
        assertEquals(281.15, response.getMain().getTemperature().getMaximumValue(), 0.001);
        assertEquals(1012.00, response.getMain().getPressure().getCurrentPressure(), 0.001);
        assertEquals(81.00, response.getMain().getHumidity(), 0.001);

        assertEquals(4.1, response.getWind().getSpeed(), 0.001);
        assertEquals(80.001, response.getWind().getDirection(), 0.001);

        assertEquals(90, response.getClouds().getCloudiness());

        assertEquals("GB", response.getSysInfo().getCountry());

        assertEquals(10000, response.getVisibility());
        assertEquals("London", response.getCityName());
        assertEquals(2643743, response.getCityId());
        assertEquals(200, response.getResponseCode());
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new WeatherMockInterceptor())
                .build();
    }

    class WeatherMockInterceptor implements Interceptor {
        private final MediaType MEDIA_JSON = MediaType.parse("application/json");

        @Override
        public Response intercept(Chain chain) throws IOException {
            String standardizedJson = "{\n" +
                    "  \"coord\": {\n" +
                    "    \"lon\": -0.13,\n" +
                    "    \"lat\": 51.51\n" +
                    "  },\n" +
                    "  \"weather\": [\n" +
                    "    {\n" +
                    "      \"id\": 300,\n" +
                    "      \"main\": \"Drizzle\",\n" +
                    "      \"description\": \"light intensity drizzle\",\n" +
                    "      \"icon\": \"09d\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"base\": \"stations\",\n" +
                    "  \"main\": {\n" +
                    "    \"temp\": 280.32,\n" +
                    "    \"pressure\": 1012,\n" +
                    "    \"humidity\": 81,\n" +
                    "    \"temp_min\": 279.15,\n" +
                    "    \"temp_max\": 281.15\n" +
                    "  },\n" +
                    "  \"visibility\": 10000,\n" +
                    "  \"wind\": {\n" +
                    "    \"speed\": 4.1,\n" +
                    "    \"deg\": 80.001\n" +
                    "  },\n" +
                    "  \"clouds\": {\n" +
                    "    \"all\": 90\n" +
                    "  },\n" +
                    "  \"dt\": 1485789600,\n" +
                    "  \"sys\": {\n" +
                    "    \"type\": 1,\n" +
                    "    \"id\": 5091,\n" +
                    "    \"message\": 0.0103,\n" +
                    "    \"country\": \"GB\",\n" +
                    "    \"sunrise\": 1485762037,\n" +
                    "    \"sunset\": 1485794875\n" +
                    "  },\n" +
                    "  \"id\": 2643743,\n" +
                    "  \"name\": \"London\",\n" +
                    "  \"cod\": 200\n" +
                    "}";

            Response response = new Response.Builder()
                    .message("Intercepted")
                    .body(ResponseBody.create(MEDIA_JSON, standardizedJson))
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .code(200)
                    .build();

            return response;
        }
    }
}
