package com.example.coms309;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private View parent;
    private TextView textMovin;
    private float speedX;
    private float speedY;

    private int bounceCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textMovin = findViewById(R.id.textV);
        parent = findViewById(R.id.parent);
        final Random r = new Random();
        speedX = r.nextFloat() * 515;
        speedY = r.nextFloat() * 515;
        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int width = parent.getWidth() - textMovin.getWidth();
                final int height = parent.getHeight() - textMovin.getHeight();

                final int period = 50;
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        textMovin.post(new TimerTask() {
                            @Override
                            public void run() {
                                final int min = 75;
                                final int max = 200;
                                final int random = new Random().nextInt((max - min) + 1) + min;

                                textMovin.setX(speedX * period / 1000.0f + textMovin.getX());
                                textMovin.setY(speedY * period / 1000.0f + textMovin.getY());
                                if (textMovin.getY() <= 0 || textMovin.getY() >= height) {
                                    speedY *= -1;
                                    textMovin.setTextColor(Color.rgb(r.nextInt(),r.nextInt(),r.nextInt()));
                                    bounceCount++;
                                }
                                if (textMovin.getX() <= 0 || textMovin.getX() >= width) {
                                    speedX *= -1;
                                    textMovin.setTextColor(Color.rgb(r.nextInt(),r.nextInt(),r.nextInt()));
                                    bounceCount++;
                                }
                            }
                        });
                    }
                }, 50, period);

            }
        });

        findViewById(R.id.random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Random r = new Random();
                speedX = r.nextFloat() * 2000;
                speedY = r.nextFloat() * 2000;
            }
        });
        Button yourButton = (Button) findViewById(R.id.switchActivity);
        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
                myIntent.putExtra("bounceCount", bounceCount);
                startActivity(myIntent);

            }
        });
    }}