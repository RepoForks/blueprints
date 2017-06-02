package com.shellmonger.weatherapp;

/**
 * Exception for reporting problems in the Weather API
 */
public class WeatherException extends Exception {
    /**
     * Create a new weather exception object
     * @param message message
     * @param inner inner exception
     */
    public WeatherException(String message, Exception inner) {
        super(message, inner);
    }
}
