package com.example.starsgallery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        // Animation rotation complète
        logo.animate().rotation(360f).setDuration(2000);

        // Animation réduction de taille
        logo.animate().scaleX(0.5f).scaleY(0.5f).setDuration(3000);

        // Animation translation vers le bas
        logo.animate().translationYBy(1000f).setDuration(2000);

        // Disparition progressive
        logo.animate().alpha(0f).setDuration(6000);

        // Redirection après animation
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}