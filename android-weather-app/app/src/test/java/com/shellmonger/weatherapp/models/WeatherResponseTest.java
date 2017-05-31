package com.shellmonger.weatherapp.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class WeatherResponseTest {
    @Test
    public void deserializationWorks() {
        String input = "{\n" +
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
                "    \"deg\": 80\n" +
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
        WeatherResponse response = WeatherResponse.fromJson(input);

        // Check the output
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
        assertEquals(1012, response.getMain().getPressure().getCurrentPressure());
        assertEquals(81, response.getMain().getHumidity());

        assertEquals(4.1, response.getWind().getSpeed(), 0.001);
        assertEquals(80, response.getWind().getDirection());

        assertEquals(90, response.getClouds().getCloudiness());

        assertEquals("GB", response.getSysInfo().getCountry());

        assertEquals(10000, response.getVisibility());
        assertEquals("London", response.getCityName());
        assertEquals(2643743, response.getCityId());
        assertEquals(200, response.getResponseCode());
    }
}
