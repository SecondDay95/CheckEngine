package com.example.checkengine2.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.checkengine2.R;
import com.example.checkengine2.authorization.LoginActivity;
import com.example.checkengine2.authorization.ResetPasswordActivity;
import com.example.checkengine2.authorization.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button signup, login, forgotpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar = findViewById(R.id.toolbar);
        signup = (Button) findViewById(R.id.btnSignup);
        login = (Button) findViewById(R.id.btnLogin);
        forgotpass = (Button) findViewById(R.id.btn_forgot_pass);

        toolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Check Engine");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
