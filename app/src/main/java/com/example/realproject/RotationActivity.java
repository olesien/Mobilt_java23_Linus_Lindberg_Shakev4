package com.example.realproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class RotationActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private TextView rotationNumbers;
    private ImageView phoneImage;
    private static final String TAG = "RotationAngle";

    // Variables to hold the gyroscope data
    private float[] gyroRotation = new float[3];
    private float[] accelRotation = new float[3];
    private float[] currentRotation = new float[3]; // For tracking rotation over time
    private float timestamp; // Used to integrate gyroscope data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rotation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            rotationNumbers = findViewById(R.id.rotation);
            phoneImage = findViewById(R.id.imageView);
            return insets;
        });

        // Initialize SensorManager and Sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register listeners for accelerometer and gyroscope
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listeners when the activity is paused
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Implement accuracy changes if needed
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Process accelerometer data
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelRotation = event.values.clone();
        }

        // Process gyroscope data
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0) {
                // Calculate the time difference
                float dt = (event.timestamp - timestamp) * 1.0f / 1000000000.0f;

                // Integrate the gyroscope data to track rotation
                currentRotation[0] += event.values[0] * dt;
                currentRotation[1] += event.values[1] * dt;
                currentRotation[2] += event.values[2] * dt;

                // Convert from radians to degrees
                gyroRotation[0] = (float) Math.toDegrees(currentRotation[0]);
                gyroRotation[1] = (float) Math.toDegrees(currentRotation[1]);
                gyroRotation[2] = (float) Math.toDegrees(currentRotation[2]);

                // Display the current rotation in degrees
                float deviceRotation = gyroRotation[2]; // Adjust this based on axis of interest
                Log.d(TAG, String.format("Device rotation in degrees: %.2f", deviceRotation));

                // Display the values in TextView
                if (rotationNumbers != null) {
                    rotationNumbers.setText(String.format(Locale.getDefault(),
                            "Rotation: %.2f degrees", deviceRotation));
                }

                // Apply reverse rotation to the image
                if (phoneImage != null) {
                    phoneImage.setRotation(-deviceRotation); // Adjust to make the image appear upright
                }
            }
            timestamp = event.timestamp;
        }
    }
}
