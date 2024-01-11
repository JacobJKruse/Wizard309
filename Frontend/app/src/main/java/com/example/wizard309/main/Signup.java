package com.example.wizard309.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.R;
import com.example.wizard309.volley.net_utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private EditText  etEmail, etAge, etUsername, etPassword;
    private ImageButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etAge = findViewById(R.id.editAge);
        etEmail = findViewById(R.id.editEmail);
        etUsername = findViewById(R.id.editUsername);
        etPassword = findViewById(R.id.editPassword);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSignup();
            }
        });

        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Hide the system bars.
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    private void checkSignup() {
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String age = etAge.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        int intValue;
        if(username.isEmpty()){
            etUsername.setError("Please enter a username");
            etUsername.requestFocus();
        }
        else if(password.isEmpty()){
            etPassword.setError("Please enter a password");
            etPassword.requestFocus();
        }
        else if(age.isEmpty()){
            etAge.setError("Please enter a age");
            etAge.requestFocus();
        }


        else if(email.isEmpty()){
            etEmail.setError("Please enter a email");
            etEmail.requestFocus();
        }

        else{
            try {
                intValue = Integer.parseInt(age);
            } catch (NumberFormatException e) {
                etAge.setError("Please enter a valid age");
                etAge.requestFocus();
                return;
            }
            // Define a regular expression pattern for a valid email address
            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

            // Create a Pattern object
            Pattern pattern = Pattern.compile(regex);

            // Create a Matcher object
            Matcher matcher = pattern.matcher(email);

            // Check if the email matches the pattern
            if (!matcher.matches()) {
                etEmail.setError("Please enter a valid email");
                etEmail.requestFocus();
                return;
            }
            JSONObject userData = new JSONObject();
            try {

                userData.put("email", email);
                userData.put("userName", username);
                userData.put("password", password);
                userData.put("age",intValue);
                userData.put("userlevel",0);
                // Add other user data fields as needed
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Create a JsonObjectRequest with a POST method
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Const.URL_USERS_JSON_ARRAY,
                    userData, // The JSON object containing user data
                    (Response.Listener<JSONObject>) response -> {
                        // Handle the successful response here
                        // For example, you can parse the response JSON
                        // and display a success message to the user
                        Toast.makeText(Signup.this, "User Created", Toast.LENGTH_LONG).show();
                    },
                    (Response.ErrorListener) error -> {
                        // Handle errors here
                        // For example, you can display an error message to the user
                    }
            );
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            // Add the request to the RequestQueue.
            queue.add(request);

            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);



        }
    }

}
