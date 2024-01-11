package com.example.jacob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button button1;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1= (Button)findViewById(R.id.button);
        text = (EditText)findViewById(R.id.editTextText);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MainActivity.this, SecondScreen.class);
                intent.putExtra("Key",text.getText().toString());
                startActivity(intent);
            }

        });
    }

}