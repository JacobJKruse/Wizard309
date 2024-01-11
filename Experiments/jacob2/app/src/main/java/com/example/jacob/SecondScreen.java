package com.example.jacob;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondScreen extends AppCompatActivity {

    Button button2;
    Integer score = 0;
    TextView textView;
    TextView scoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
        textView = findViewById(R.id.textView2);
        scoreView = findViewById(R.id.textView3);
        String text = getIntent().getStringExtra("Key");


        textView.setText(text);
        button2 = (Button)findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score += 1;
                scoreView.setText("Score: " + score.toString());
            }
        });
    }
}