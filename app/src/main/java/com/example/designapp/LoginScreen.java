package com.example.designapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity {
    private EditText username, password;
    private Button login;
    private TextView changeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
        init();
        findViewById(R.id.login_state_change).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),SignUpScreen.class);
            startActivity(intent);
            finish();
        });
        login.setOnClickListener(v -> {
            String pass = password.getText().toString();
            if (pass.length()>7)
            {
                password.setError("Invalid Password Length");
            }
            if (!username.getText().toString().isEmpty())
            {
                if (username.getText().toString().length() > 40)
                {
                    username.setError("Invalid Email Length");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches())
                {
                    username.setError("Invalid Email Form");
                }
            }
            if (username.getText().toString().isEmpty()&&password.getText().toString().isEmpty())
            {
                username.setError("Field is Empty");
                password.setError("Field is Empty");
            }


        });
    }

    private void init() {

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_btn);
        changeMode = findViewById(R.id.login_state_change);
    }
}
