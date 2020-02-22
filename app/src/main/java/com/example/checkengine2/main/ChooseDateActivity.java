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
import android.widget.TextView;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.example.checkengine2.controller.CurrentPlotControllerActivity;
import com.example.checkengine2.controller.TempPlotControllerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ChooseDateActivity extends AppCompatActivity {

    private int drawer_position, userChoose;
    private int day, month, year;
    private boolean checkFields;

    private EditText et_setDay, et_setMonth, et_setYear;
    private TextView tv_choose_day, tv_choose_month, tv_choose_year;
    private Button btn_accept;
    private Toolbar toolbar;

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
                Intent intent1 = new Intent(ChooseDateActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_choose_date);

        et_setDay = (EditText) findViewById(R.id.et_setDay);
        et_setMonth = (EditText) findViewById(R.id.et_setMonth);
        et_setYear = (EditText) findViewById(R.id.et_setYear);
        tv_choose_day = (TextView) findViewById(R.id.tv_choose_day);
        tv_choose_month = (TextView) findViewById(R.id.tv_choose_month);
        tv_choose_year = (TextView) findViewById(R.id.tv_choose_year);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Wprowadź datę");
        ab.setDisplayHomeAsUpEnabled(true);

        getIntentExtra();
        System.out.println("drawer_position = " + drawer_position);

        setVisibility();

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentExtra();
                et_setDay.setText("");
                et_setMonth.setText("");
                et_setYear.setText("");

            }
        });

    }

    private void setVisibility() {
        if (drawer_position == 0) {

            tv_choose_day.setVisibility(View.VISIBLE);
            tv_choose_month.setVisibility(View.VISIBLE);
            tv_choose_year.setVisibility(View.VISIBLE);
            et_setDay.setVisibility(View.VISIBLE);
            et_setMonth.setVisibility(View.VISIBLE);
            et_setYear.setVisibility(View.VISIBLE);

        } else if (drawer_position == 1) {

            tv_choose_day.setVisibility(View.GONE);
            tv_choose_month.setVisibility(View.VISIBLE);
            tv_choose_year.setVisibility(View.VISIBLE);
            et_setDay.setVisibility(View.GONE);
            et_setMonth.setVisibility(View.VISIBLE);
            et_setYear.setVisibility(View.VISIBLE);

        }  else if (drawer_position == 3) {

            tv_choose_day.setVisibility(View.GONE);
            tv_choose_month.setVisibility(View.GONE);
            tv_choose_year.setVisibility(View.VISIBLE);
            et_setDay.setVisibility(View.GONE);
            et_setMonth.setVisibility(View.GONE);
            et_setYear.setVisibility(View.VISIBLE);

        }
    }

    private void setIntentExtra() {
        Intent intent;
        if (userChoose == 1) {
            intent = new Intent(ChooseDateActivity.this, TempPlotControllerActivity.class);
        } else {
            intent = new Intent(ChooseDateActivity.this, CurrentPlotControllerActivity.class);
        }
        try {
            if (drawer_position == 0) {
                day = Integer.valueOf(et_setDay.getText().toString());
                month = Integer.valueOf(et_setMonth.getText().toString());
                year = Integer.valueOf(et_setYear.getText().toString());
            } else if (drawer_position == 1) {
                month = Integer.valueOf(et_setMonth.getText().toString());
                year = Integer.valueOf(et_setYear.getText().toString());
            } else {
                year = Integer.valueOf(et_setYear.getText().toString());
            }
            checkFields = true;
        } catch (Exception e) {
            e.printStackTrace();
            checkFields = false;
        }
        if (checkFields) {
            if (drawer_position == 0) {
                if ((day > 0 && day <= 31) && (month > 0 && month <= 12) && (year > 2019)) {
                    intent.putExtra("DAY", day);
                    intent.putExtra("MONTH", month);
                    intent.putExtra("YEAR", year);
                    intent.putExtra("DRAWER_POSITION", drawer_position);
                    intent.putExtra("userChoose", userChoose);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChooseDateActivity.this, "Dzień musi być z przedziału od 1-31, miesiąc 1-12, rok powyżej 2019",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (drawer_position == 1) {
                if ((month > 0 && month <= 12) && (year > 2019)) {
                    intent.putExtra("MONTH", month);
                    intent.putExtra("YEAR", year);
                    intent.putExtra("DRAWER_POSITION", drawer_position);
                    intent.putExtra("userChoose", userChoose);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChooseDateActivity.this, "Miesiąc musi być z przedziału 1-12, rok powyżej 2019",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                if (year >= 2019) {
                    intent.putExtra("YEAR", year);
                    intent.putExtra("DRAWER_POSITION", drawer_position);
                    intent.putExtra("userChoose", userChoose);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChooseDateActivity.this, "Podany rok musi być powyżej 2019",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(ChooseDateActivity.this, "Proszę uzupełnić wszystkie pola",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        drawer_position = intent.getIntExtra("DRAWER_POSITION", 1);
        userChoose = intent.getIntExtra("userChoose", 1);
        System.out.println("Choose Date: userChoose = " + userChoose);
    }
}
