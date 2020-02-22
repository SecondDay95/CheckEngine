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

public class CurrentSettings extends AppCompatActivity {

    private EditText et_maxY_current, et_minY_current, et_stepY_current, et_stepX_current;
    private RadioButton rb_auto_current, rb_manual_current;
    private Button btn_save_current;
    private Toolbar toolbar;

    private int typeOfSettings;
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
                Intent intent1 = new Intent(CurrentSettings.this, MainActivity.class);
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
        setContentView(R.layout.activity_current_settings);

        et_maxY_current = (EditText) findViewById(R.id.et_maxY_current);
        et_minY_current = (EditText) findViewById(R.id.et_minY_current);
        et_stepY_current = (EditText) findViewById(R.id.et_stepY_current);
        et_stepX_current = (EditText) findViewById(R.id.et_stepX_current);
        rb_auto_current = (RadioButton) findViewById(R.id.rb_auto_current);
        rb_manual_current = (RadioButton) findViewById(R.id.rb_manual_current);
        btn_save_current = (Button) findViewById(R.id.btn_save_current);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Ustawienia prądu");
        ab.setDisplayHomeAsUpEnabled(true);

        rb_auto_current.setChecked(true);
        setGoneVisibility();

        Intent intent = getIntent();
        userChoose = intent.getIntExtra("userChoose", 1);
        System.out.println("Current Settings: userChoose = " + userChoose);

        btn_save_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPreferences();
            }
        });


    }

    public void onRadioButtonClickedCurrent(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rb_auto_current:
                if (checked) {
                    setGoneVisibility();
                    typeOfSettings = 1;
                }
                break;
            case R.id.rb_manual_current:
                if (checked) {
                    setVisibleVisibility();
                    typeOfSettings = 2;
                }
                break;
        }
    }

    private void setSharedPreferences() {
        SharedPreferences sharedPreferences = CurrentSettings.this.getSharedPreferences("CurrentData",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        int yMax, yMin, yStep, xStep;
        String yMaxS, yMinS, yStepS, xStepS;

        if (typeOfSettings == 2) {
            try {
                yMaxS = et_maxY_current.getText().toString();
                yMinS = et_minY_current.getText().toString();
                yStepS = et_stepY_current.getText().toString();
                xStepS = et_stepX_current.getText().toString();
                yMax = Integer.valueOf(yMaxS);
                yMin = Integer.valueOf(yMinS);
                yStep = Integer.valueOf(yStepS);
                xStep = Integer.valueOf(xStepS);
                if (yMin < yMax && yStep < yMax) {
                    editor.putInt("typeOfSettingsCurrent", typeOfSettings);
                    editor.putInt("yMaxCurrent", yMax);
                    editor.putInt("yMinCurrent", yMin);
                    editor.putInt("yStepCurrent", yStep);
                    editor.putInt("xStepCurrent", xStep);
                    editor.commit();
                    Intent intent = new Intent(CurrentSettings.this, AverageControllerActivity.class);
                    intent.putExtra("userChoose", userChoose);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(CurrentSettings.this,
                            "Wartości minimalne muszą być mniejsze od maksymalnych", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CurrentSettings.this,
                        "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            }
        } else {
            editor.putInt("typeOfSettingsCurrent", typeOfSettings);
            editor.commit();
            Intent intent = new Intent(CurrentSettings.this, AverageControllerActivity.class);
            intent.putExtra("userChoose", userChoose);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void setVisibleVisibility() {
        et_maxY_current.setVisibility(View.VISIBLE);
        et_minY_current.setVisibility(View.VISIBLE);
        et_stepY_current.setVisibility(View.VISIBLE);
        et_stepX_current.setVisibility(View.VISIBLE);
    }

    private void setGoneVisibility() {
        et_maxY_current.setVisibility(View.GONE);
        et_minY_current.setVisibility(View.GONE);
        et_stepY_current.setVisibility(View.GONE);
        et_stepX_current.setVisibility(View.GONE);
    }
}
