package com.example.realproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;;

public class NavFragment extends Fragment {
    public NavFragment() {
        super(R.layout.fragment_nav);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Context context = getContext();
        TextView acc = view.findViewById(R.id.acc);
        TextView gyro = view.findViewById(R.id.gyro);
        TextView gravity = view.findViewById(R.id.gravity);

        acc.setOnClickListener(e -> {
            Log.i("LINUS", "clicked button 1");
            this.startActivity(new Intent(context, MainActivity.class));
        });

        gyro.setOnClickListener(e -> {
            Log.i("LINUS2", "clicked button 2");
            this.startActivity(new Intent(context, GyroActivity.class));

        });

        gravity.setOnClickListener(e -> {
            Log.i("LINUS3", "clicked button 3");
        });

    }
}