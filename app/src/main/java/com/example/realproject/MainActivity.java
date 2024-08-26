package com.example.realproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            String text = "Hello World";
            Log.i("ALRIK", text);
            //Log.println(log.DEBUG)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            TextView text1 = findViewById(R.id.text1);
            final float[] i = {14.0F};
            TextView button1 = findViewById(R.id.button1);
            TextView button2 = findViewById(R.id.button2);
            TextView button3 = findViewById(R.id.button3);

            button1.setOnClickListener(v12 -> {
                i[0] = i[0] + 2.0F;
                text1.setTextSize(i[0]);
                Log.i("LINUS", "clicked button 1");
            });

            button2.setOnClickListener(v12 -> {
                i[0] = i[0] - 2.0F;
                text1.setTextSize(i[0]);
                Log.i("LINUS", "clicked button 2");
            });
            button3.setOnClickListener(v1 -> {
                Animation anim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.translate);
                text1.setAnimation(anim);
                text1.startAnimation(anim);
                Log.i("LINUS2", "clicked button 3");
            });

            return insets;
        });

    }
}