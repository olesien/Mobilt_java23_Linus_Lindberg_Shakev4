package com.example.realproject;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public abstract class ExtraUtil {
    public static void setNavListeners(Context context, TextView acc, TextView gyro, TextView gravity) {
        acc.setOnClickListener(e -> {
            Log.i("LINUS", "clicked button 1");
            context.startActivity(new Intent(context, MainActivity.class));
        });

        gyro.setOnClickListener(e -> {
            Log.i("LINUS2", "clicked button 2");
            context.startActivity(new Intent(context, GyroActivity.class));

        });

        gravity.setOnClickListener(e -> {
            Log.i("LINUS3", "clicked button 3");
        });
    }
}
