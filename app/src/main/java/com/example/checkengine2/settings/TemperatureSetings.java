package com.example.checkengine2.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.example.checkengine2.controller.AverageControllerActivity;
import com.example.checkengine2.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class TemperatureSetings extends AppCompatActivity {

    private EditText et_maxY, et_minY, et_stepY, et_stepX;
    private RadioButton rb_auto, rb_manual;
    private Button btn_save;
    private Toolbar toolbar;

    private int typeOfSettings;

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
                Intent intent1 = new Intent(TemperatureSetings.this, MainActivity.class);
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
        setContentView(R.layout.activity_temperature_setings);

        et_maxY = (EditText) findViewById(R.id.et_maxY);
        et_minY = (EditText) findViewById(R.id.et_minY);
        et_stepY = (EditText) findViewById(R.id.et_stepY);
        et_stepX = (EditText) findViewById(R.id.et_stepX);
        rb_auto = (RadioButton) findViewById(R.id.rb_auto);
        rb_manual = (RadioButton) findViewById(R.id.rb_manual);
        btn_save = (Button) findViewById(R.id.btn_save);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Ustawienia temperatury");
        ab.setDisplayHomeAsUpEnabled(true);

        rb_auto.setChecked(true);
        setGoneVisibility();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPreferences();
            }
        });


    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rb_auto:
                if (checked) {
                    setGoneVisibility();
                    typeOfSettings = 1;
                }
                break;
            case R.id.rb_manual:
                if (checked) {
                    setVisibleVisibility();
                    typeOfSettings = 2;
                }
                break;
        }
    }

    private void setSharedPreferences() {
        SharedPreferences sharedPreferences = TemperatureSetings.this.getSharedPreferences("TemperatureData",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        int yMax, yMin, yStep, xStep;
        String yMaxS, yMinS, yStepS, xStepS;

        if (typeOfSettings == 2) {
            try {
                yMaxS = et_maxY.getText().toString();
                yMinS = et_minY.getText().toString();
                yStepS = et_stepY.getText().toString();
                xStepS = et_stepX.getText().toString();
                yMax = Integer.valueOf(yMaxS);
                yMin = Integer.valueOf(yMinS);
                yStep = Integer.valueOf(yStepS);
                xStep = Integer.valueOf(xStepS);
                if (yMin < yMax && yStep < yMax) {
                    editor.putInt("typeOfSettings", typeOfSettings);
                    editor.putInt("yMax", yMax);
                    editor.putInt("yMin", yMin);
                    editor.putInt("yStep", yStep);
                    editor.putInt("xStep", xStep);
                    editor.commit();
                    Intent intent = new Intent(TemperatureSetings.this, AverageControllerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(TemperatureSetings.this,
                            "Wartości minimalne muszą być mniejsze od maksymalnych", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(TemperatureSetings.this,
                        "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            }
        } else {
            editor.putInt("typeOfSettings", typeOfSettings);
            editor.commit();
            Intent intent = new Intent(TemperatureSetings.this, AverageControllerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void setVisibleVisibility() {
        et_maxY.setVisibility(View.VISIBLE);
        et_minY.setVisibility(View.VISIBLE);
        et_stepY.setVisibility(View.VISIBLE);
        et_stepX.setVisibility(View.VISIBLE);
    }

    private void setGoneVisibility() {
        et_maxY.setVisibility(View.GONE);
        et_minY.setVisibility(View.GONE);
        et_stepY.setVisibility(View.GONE);
        et_stepX.setVisibility(View.GONE);
    }
}
