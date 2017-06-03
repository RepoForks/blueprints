package com.shellmonger.weatherapp;

import java.text.SimpleDateFormat;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Typeface;
import android.text.Html;
import android.widget.TextView;
import android.os.AsyncTask;
import android.os.Bundle;
import com.shellmonger.weatherapp.models.WeatherResponse;

public class MainActivity extends AppCompatActivity {
    TextView c_city, c_details, c_temperature, c_updated, weatherIcon;
    Typeface weatherFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ID of each field that we want to modify
        c_city = (TextView)findViewById(R.id.city_field);
        c_details = (TextView)findViewById(R.id.details_field);
        c_temperature = (TextView)findViewById(R.id.current_temperature_field);
        c_updated = (TextView)findViewById(R.id.updated_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);

        // Set the font to be used by the weather icon area
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);

        // Get the weather from the API
        GetWeatherAsyncTask task = new GetWeatherAsyncTask();
        task.execute(new String[] { "Seattle" });
    }

    class GetWeatherAsyncTask extends AsyncTask<String,Void,WeatherResponse> {
        @Override
        protected void onPreExecute() {
            c_updated.setText("Updating...");
        }

        @Override
        protected WeatherResponse doInBackground(String... cities) {
            try {
                WeatherManager manager = new WeatherManager();
                return manager.getWeatherByCityName(cities[0]);
            } catch (WeatherException ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(WeatherResponse response) {
            if (response != null) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

                    c_city.setText(response.getCityName() + ", " + response.getSysInfo().getCountry());
                    c_details.setText(response.getWeatherConditions()[0].getLongDescription());
                    double temp = response.getMain().getTemperature().getCurrentValue();
                    double convertedTemp = temp * (9.0/5.0) - 459.67;
                    c_temperature.setText(String.format("%.2f°F", convertedTemp));
                    c_updated.setText(formatter.format(response.getTimestamp()));
                    String weatherCode = convertWeather(response.getWeatherConditions()[0].getIcon());
                    // API Level < 24
                    weatherIcon.setText(Html.fromHtml(weatherCode));
                    // API Level 24+
                    // weatherIcon.setText(Html.fromHtml(weatherCode, Html.FROM_HTML_MODE_LEGACY));
                } catch (Exception ex) {
                    c_updated.setText("Formatting Failed");
                }
            } else {
                c_updated.setText("Fetch Failed");
            }
        }

        private String convertWeather(String iconCode) {
            boolean isDay = iconCode.charAt(2) == 'd';
            String weatherIcon = "&#xf07b;";
            int icon = Integer.parseInt(iconCode.substring(0,2), 10);

            switch (icon) {
                case 1:
                    weatherIcon = isDay ? "&#xf00d;" : "&#xf02e;";
                    break;
                case 2:
                case 3:
                case 4:
                    weatherIcon = isDay ? "&#xf002;" : "&#xf086;";
                    break;
                case 9:
                    weatherIcon = isDay ? "&#xf009;" : "&#xf029;";
                    break;
                case 10:
                    weatherIcon = isDay ? "&#xf008;" : "&#xf028;";
                    break;
                case 11:
                    weatherIcon = isDay ? "&#xf005;" : "&#xf025;";
                    break;
                case 13:
                    weatherIcon = isDay ? "&#xf00a;" : "&#xf02a;";
                    break;
                case 50:
                    weatherIcon = "&#xf014;";
                    break;
            }
            return  weatherIcon;
        }
    }
}
