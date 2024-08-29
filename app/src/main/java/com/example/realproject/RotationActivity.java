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

import java.util.Locale;
//Compass
public class RotationActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private TextView rotationNumbers;
    private ImageView compass;
    private static final String TAG = "RotationAngle";

    //Save the rotation data and orientation / offsets
    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];
    private float[] calibrationOffsets = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        rotationNumbers = findViewById(R.id.rotation);
        compass = findViewById(R.id.compass);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            //Note: Below is powered by chatGPT / Claude AI as this goes well beyond my own expertise

            // Get the rotation matrix from the rotation vector
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            // Get the orientation angles from the rotation matrix
            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            // Apply calibration offsets and convert to degrees
            float azimuth = (float) Math.toDegrees(orientationAngles[0] - calibrationOffsets[0]);
            float pitch = (float) Math.toDegrees(orientationAngles[1] - calibrationOffsets[1]);
            float roll = (float) Math.toDegrees(orientationAngles[2] - calibrationOffsets[2]);

            // Normalize angles
            azimuth = (azimuth + 360) % 360;
            pitch = (pitch + 180) % 360 - 180;
            roll = (roll + 180) % 360 - 180;

            // Log the values
            //Log.d(TAG, String.format(Locale.getDefault(), "Z-Rotation: %.2f, Pitch: %.2f, Roll: %.2f", azimuth, pitch, roll));

            // Display the values in TextView
            if (rotationNumbers != null) {
                rotationNumbers.setText(String.format(Locale.getDefault(),
                        "Z-Rotation: %.2f°\nPitch: %.2f°\nRoll: %.2f°", azimuth, pitch, roll));
            }

            // Apply rotation to the image
            if (compass != null) {
                compass.setRotation(-azimuth);
            }
        }
    }
}