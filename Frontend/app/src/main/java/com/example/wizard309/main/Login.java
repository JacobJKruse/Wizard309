package com.example.wizard309.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.R;
import com.example.wizard309.helpers.BackgroundAudioPlayer;
import com.example.wizard309.volley.net_utils.Const;

import org.json.JSONObject;

/**
 * login layout
 */
public class Login extends AppCompatActivity {

    public static int screenWidth, screenHeight;

    private JSONObject userAccount = new JSONObject();

    String url = Const.URL_USERS_JSON_ARRAY;
    public BackgroundAudioPlayer mediaPlayer;


    private EditText usernameTxt, passwordTxt;
    private ImageButton loginBtn, signupBtn;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = new BackgroundAudioPlayer(this,R.raw.wizardmenu);
        mediaPlayer.play();
        setContentView(R.layout.activity_login);
        loading = findViewById(R.id.loadingCircle);
        loading.setVisibility(View.INVISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        screenHeight = height;
        screenWidth = width;

        usernameTxt = findViewById(R.id.editUsername);
        passwordTxt = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.signupButton);

        signupBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Login.this, Signup.class);
              startActivity(intent);
           }
       });

        loginBtn.setOnClickListener(v -> {
            checkLogin();

        });



        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Hide the system bars.
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

    }



    private void checkLogin(){
        final String username = usernameTxt.getText().toString().trim();
        final String password = passwordTxt.getText().toString().trim();

        if(username.isEmpty()){
            usernameTxt.setError("Please enter a username");
            usernameTxt.requestFocus();
        }
        else if(password.isEmpty()){
            passwordTxt.setError("Please enter a password");
            passwordTxt.requestFocus();
        }

        else{
            loading.setVisibility(View.VISIBLE);

            // Create a StringRequest to fetch the data.
            url += "/" + username + "/" + password;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            // Parse the response as an integer.
                            int intValue = Integer.parseInt(response);
                            Log.d("RESPONSE",Integer.toString(intValue));
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.INVISIBLE);
                            //Starting Home activity
                            Intent intent = new Intent(Login.this, MainMenuActivity.class);
                            intent.putExtra("PlayerID", intValue);
                            mediaPlayer.stop();
                            startActivity(intent);

                            // Do something with the integer value.
                        } catch (NumberFormatException e) {
                            Toast.makeText(Login.this, "There is an error !!!", Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.INVISIBLE);
                        }
                    },
                    error -> {
                        Toast.makeText(Login.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.INVISIBLE);
                    });

// Add the request to the RequestQueue.
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(stringRequest);
            }
    }





}