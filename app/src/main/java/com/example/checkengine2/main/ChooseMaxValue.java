package com.example.checkengine2.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.example.checkengine2.controller.AverageControllerActivity;
import com.example.checkengine2.notification.CurrentMessageService;
import com.example.checkengine2.notification.TempMessageService;
import com.google.firebase.auth.FirebaseAuth;

public class ChooseMaxValue extends AppCompatActivity {

    private EditText et_maxValue;
    private Toolbar toolbar;
    private Button button;

    private int userChoose;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();

                //Powrót do MainActivity:
                Intent intent1 = new Intent(ChooseMaxValue.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_max_value);

        et_maxValue = (EditText) findViewById(R.id.et_setMaxValue);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button = (Button) findViewById(R.id.btn_accept_maxValue);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Wartość powiadomień");
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userChoose = intent.getIntExtra("userChoose", 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentExtra();
                et_maxValue.setText("");
            }
        });

    }

    private void setIntentExtra() {

        Intent intent;
        if (userChoose == 1) {
            intent = new Intent(ChooseMaxValue.this, TempMessageService.class);
        } else {
            intent = new Intent(ChooseMaxValue.this, CurrentMessageService.class);
        }
        float maxValue = 0;
        try {
            maxValue = Integer.valueOf(et_maxValue.getText().toString());
            intent.putExtra("MaxValue", maxValue);
            startService(intent);
            Intent intent1 = new Intent(ChooseMaxValue.this, AverageControllerActivity.class);
            intent1.putExtra("userChoose", userChoose);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ChooseMaxValue.this, "Proszę uzupełnić wszystkie pola",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
