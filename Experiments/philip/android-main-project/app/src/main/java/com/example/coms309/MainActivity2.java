package com.example.coms309;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    private int bounceCount;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button yourButton = (Button) findViewById(R.id.button4);
        text = findViewById(R.id.textView2);

        Intent intent = getIntent();
        int bounceCount = intent.getIntExtra("bounceCount", 0);

        text.setText("bounces:" + bounceCount);
        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
            }
        });
    }
}