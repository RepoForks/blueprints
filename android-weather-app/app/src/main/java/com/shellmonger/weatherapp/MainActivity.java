package com.shellmonger.weatherapp;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.shellmonger.weatherapp.models.WeatherRequest;
import com.shellmonger.weatherapp.models.WeatherResponse;

public class MainActivity extends AppCompatActivity implements LocationListener {
    // Constant for validating that permissions are available
    private static final int PERMS_REQUEST_GPS_ACCESS = 2001;

    ImageView c_refresh;
    TextView c_city, c_details, c_temperature, c_updated, weatherIcon;
    Typeface weatherFont;
    Timer refreshTimer;
    Handler timerHandler;
    WeatherRequest currentRequest;
    LocationManager locationManager;

    boolean updateInProgress = false,
        isLocationEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        }

        // Get the ID of each field that we want to modify
        c_city = (TextView)findViewById(R.id.city_field);
        c_details = (TextView)findViewById(R.id.details_field);
        c_temperature = (TextView)findViewById(R.id.current_temperature_field);
        c_updated = (TextView)findViewById(R.id.updated_field);
        c_refresh = (ImageView)findViewById(R.id.refresh_button);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);

        // Set the font to be used by the weather icon area
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);

        // Set up the default request
        currentRequest = new WeatherRequest("Seattle,US");

        // Determine if the app has permission - if so, then set up the location manager, otherwise ask for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMS_REQUEST_GPS_ACCESS);
        } else {
            setUpLocationManager();
        }

        // Set up the timer
        timerHandler = new Handler() {
            public void handleMessage(Message msg) {
                onRefreshClick(null);
            }
        };
        refreshTimer = new Timer();
        TimerTask refreshTimerTask = new TimerTask() {
            public void run() { timerHandler.obtainMessage(1).sendToTarget(); }
        };
        refreshTimer.schedule(refreshTimerTask, 0L, 300000L);
    }

    public void setUpLocationManager() {
        // Set up the location manager
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isLocationEnabled) {
            this.onProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            Log.w("Location", "Location/GPS is not enabled");
        }
    }

    public void onRefreshClick(View view) {
        if (!updateInProgress) {
            GetWeatherAsyncTask task = new GetWeatherAsyncTask();
            task.execute(new WeatherRequest[] { currentRequest });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMS_REQUEST_GPS_ACCESS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.w("Location", "Permission was granted");
            setUpLocationManager();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider == LocationManager.GPS_PROVIDER) {
            Log.w("Location", "Enabling GPS Provider");
            isLocationEnabled = true;
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000L, 100, this);
                onLocationChanged(locationManager.getLastKnownLocation(provider));
            } catch (SecurityException ex) {
                Toast.makeText(this, "You need the GPS permission", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider == LocationManager.GPS_PROVIDER) {
            Log.w("Location", "Disabling GPS Provider");
            isLocationEnabled = false;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.w("Location", "Location changed - refreshing data");
        currentRequest = new WeatherRequest(location);
        onRefreshClick(null);
    }

    class GetWeatherAsyncTask extends AsyncTask<WeatherRequest,Void,WeatherResponse> {
        @Override
        protected void onPreExecute() {
            c_updated.setText("Updating...");
            setUpdateInProgress(true);
        }

        @Override
        protected WeatherResponse doInBackground(WeatherRequest... cities) {
            try {
                return cities[0].getWeather();
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
            setUpdateInProgress(false);
        }

        private void setUpdateInProgress(boolean v) {
            updateInProgress = v;
            c_refresh.setClickable(!v);
            c_refresh.setColorFilter(v ? Color.GRAY : Color.WHITE);
        }

        private String convertWeather(String iconCode) {
            boolean isDay = iconCode.charAt(2) == 'd';
            String weatherIcon = "f07b";
            int icon = Integer.parseInt(iconCode.substring(0,2), 10);

            switch (icon) {
                case 1:
                    weatherIcon = isDay ? "f00d" : "f02e";
                    break;
                case 2:
                case 3:
                case 4:
                    weatherIcon = isDay ? "f002" : "f086";
                    break;
                case 9:
                    weatherIcon = isDay ? "f009" : "f029";
                    break;
                case 10:
                    weatherIcon = isDay ? "f008" : "f028";
                    break;
                case 11:
                    weatherIcon = isDay ? "f005" : "f025";
                    break;
                case 13:
                    weatherIcon = isDay ? "f00a" : "f02a";
                    break;
                case 50:
                    weatherIcon = "&#xf014";
                    break;
            }
            return  "&#x" + weatherIcon + ";";
        }
    }
}
