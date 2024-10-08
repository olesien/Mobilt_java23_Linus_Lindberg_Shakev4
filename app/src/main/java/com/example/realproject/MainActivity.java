package com.example.realproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

//Accelerometer
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    int level = 1;
    float exp = 1;
    private final float[] accelerometerReading = new float[3];
    TextView accNumbers;
    TextView levelText;
    TextView experienceText;
    TextView progressText;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize views
        accNumbers = findViewById(R.id.accnumbers);
        levelText = findViewById(R.id.level);
        experienceText = findViewById(R.id.exp);
        progressText = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressBar);

        // Set up insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.i("ACCFAIL", "There is no accelerometer");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.i("ACCFAIL", "There is no accelerometer");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onStop() {
        super.onStop();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    // Get readings from accelerometer.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float diff = (Math.max(event.values[0], accelerometerReading[0]) - Math.min(event.values[0], accelerometerReading[0])) +
                    (Math.max(event.values[1], accelerometerReading[1]) - Math.min(event.values[1], accelerometerReading[1])) +
                    (Math.max(event.values[2], accelerometerReading[2]) - Math.min(event.values[2], accelerometerReading[2]));
            exp += diff;
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);

            updateUI();
        }
    }

    private void updateUI() {
        if (accNumbers != null) {
            accNumbers.setText(String.format(Locale.getDefault(), "X: %.3f Y: %.3f Z: %.3f ",
                    accelerometerReading[0], accelerometerReading[1], accelerometerReading[2]));

            level = (int)(Math.log(exp - 1) / Math.log(2)) + 1;

            levelText.setText(String.format(Locale.getDefault(),"Level: %d", level));
            double currentProgressInNewLevel = exp - Math.pow(2, level - 1);
            double expToNextLevel = Math.pow(2, level) - Math.pow(2, level - 1);
            progressBar.setProgress((int) currentProgressInNewLevel);
            progressBar.setMax((int) expToNextLevel);
            progressText.setText(String.format(Locale.getDefault(), "Progress (%.0f/%.0f)",
                    currentProgressInNewLevel, expToNextLevel));
            experienceText.setText(String.format(Locale.getDefault(),"Experience: %.0f", exp));
        }
    }

}