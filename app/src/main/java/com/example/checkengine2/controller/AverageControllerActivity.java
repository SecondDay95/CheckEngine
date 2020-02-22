package com.example.checkengine2.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.example.checkengine2.main.ChooseDateActivity;
import com.example.checkengine2.main.ChooseMaxValue;
import com.example.checkengine2.main.MainActivity;
import com.example.checkengine2.main.UserProfileActivity;
import com.example.checkengine2.model.CurrentDao;
import com.example.checkengine2.model.TemperatureDao;
import com.example.checkengine2.settings.CurrentSettings;
import com.example.checkengine2.settings.TemperatureSetings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AverageControllerActivity extends AppCompatActivity {

    private TemperatureDao temperatureDao;
    private CurrentDao currentDao;
    private List<Float> dailyData, monthlyData, yearlyData;
    private int userChoose = 1;
    private String averageDailyValueString;
    private String averageMonthlyValueString;
    private String averageYearlyValueString;
    private Boolean databaseConnection;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private Toolbar toolbar;
    private TextView tv_day, tv_month, tv_year;
    private TextView tv_text_day, tv_text_month, tv_text_year;
    private TextView tv_user_name;
    private ImageView iv_onClick;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_controller);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Srednie wartości");
        ab.setDisplayHomeAsUpEnabled(true);

        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_text_day = (TextView) findViewById(R.id.tv_text_day);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_text_month = (TextView) findViewById(R.id.tv_text_month);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_text_year = (TextView) findViewById(R.id.tv_text_year);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        t = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(t);
        t.syncState();
        nv = (NavigationView)findViewById(R.id.nv);
        setUserName();
        onClickImage();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                int position = 0;
                Intent intent;
                switch(id)
                {
                    case R.id.it_dayPlot:
                        position = 0;
                        intent = new Intent(AverageControllerActivity.this, ChooseDateActivity.class);
                        intent.putExtra("DRAWER_POSITION", position);
                        intent.putExtra("userChoose", userChoose);
                        startActivity(intent);
                        break;
                    case R.id.it_monthPlot:
                        position = 1;
                        intent = new Intent(AverageControllerActivity.this, ChooseDateActivity.class);
                        intent.putExtra("DRAWER_POSITION", position);
                        intent.putExtra("userChoose", userChoose);
                        startActivity(intent);
                        break;
                    case R.id.it_threeMonthPlot:
                        position = 2;
                        if (userChoose == 1) {
                            intent = new Intent(AverageControllerActivity.this, TempPlotControllerActivity.class);
                            intent.putExtra("DRAWER_POSITION", position);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        } else {
                            intent = new Intent(AverageControllerActivity.this, CurrentPlotControllerActivity.class);
                            intent.putExtra("DRAWER_POSITION", position);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        }
                        break;
                    case R.id.it_yearPlot:
                        position = 3;
                        intent = new Intent(AverageControllerActivity.this, ChooseDateActivity.class);
                        intent.putExtra("DRAWER_POSITION", position);
                        intent.putExtra("userChoose", userChoose);
                        startActivity(intent);
                        break;
                    case R.id.it_allPlot:
                        position = 4;
                        if (userChoose == 1) {
                            intent = new Intent(AverageControllerActivity.this, TempPlotControllerActivity.class);
                            intent.putExtra("DRAWER_POSITION", position);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        } else {
                            intent = new Intent(AverageControllerActivity.this, CurrentPlotControllerActivity.class);
                            intent.putExtra("DRAWER_POSITION", position);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        }
                        break;
                    case R.id.it_notification:
                        position = 5;
                        if (userChoose == 1) {
                            intent = new Intent(AverageControllerActivity.this, ChooseMaxValue.class);
                            intent.putExtra("DRAWER_POSITION", position);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        } else {
                            intent = new Intent(AverageControllerActivity.this, ChooseMaxValue.class);
                            intent.putExtra("DRAWER_POSITION", position);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        }
                        break;
                    case R.id.it_settings:
                        position = 6;
                        if (userChoose == 1) {
                            intent = new Intent(AverageControllerActivity.this, TemperatureSetings.class);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        } else {
                            intent = new Intent(AverageControllerActivity.this, CurrentSettings.class);
                            intent.putExtra("userChoose", userChoose);
                            startActivity(intent);
                        }
                        break;
                    default:
                        return true;
                }


                return true;
            }
        });

        Intent intent = getIntent();
        userChoose = intent.getIntExtra("userChoose", 1);
        System.out.println("User Choose = " + userChoose);

        new MyTask().execute();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();

                //Powrót do MainActivity:
                Intent intent1 = new Intent(AverageControllerActivity.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            dailyData = new ArrayList<>();
            monthlyData = new ArrayList<>();
            yearlyData = new ArrayList<>();
            temperatureDao = new TemperatureDao();
            currentDao = new CurrentDao();

            try {
                if (userChoose == 1) {
                    temperatureDao.executeConnection();
                    databaseConnection = true;
                    dailyData = temperatureDao.getTemperatureDataForLastDay();
                    monthlyData = temperatureDao.getTemperatureDataForLastMonth();
                    yearlyData = temperatureDao.getTemperatureDataForLastYear();
                    averageDailyValueString = averageValue(dailyData);
                    averageMonthlyValueString = averageValue(monthlyData);
                    averageYearlyValueString = averageValue(yearlyData);
                    System.out.println(averageDailyValueString);
                    temperatureDao.closeConnection();
                } else {
                    currentDao.executeConnection();
                    databaseConnection = true;
                    dailyData = currentDao.getCurrentDataForLastDay();
                    monthlyData = currentDao.getCurrentDataForLastMonth();
                    yearlyData = currentDao.getCurrentDataForLastYear();
                    averageDailyValueString = averageValue(dailyData);
                    averageMonthlyValueString = averageValue(monthlyData);
                    averageYearlyValueString = averageValue(yearlyData);
                    currentDao.closeConnection();
                }
            } catch (Exception e) {
                e.printStackTrace();
                databaseConnection = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!databaseConnection) {
                Toast toast = Toast.makeText(AverageControllerActivity.this, "Brak połączenia z bazą danych",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                setTextViewValues();
                setText(averageDailyValueString, averageMonthlyValueString, averageYearlyValueString);
            }
        }
    }

    private String averageValue (List<Float> data) {
        float tempValue = 0, tempSum = 0, tempAverage = 0;
        String tempAverageString;
        if (data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                tempValue = data.get(i);
                tempSum += tempValue;
            }
            tempAverage = tempSum / data.size();
            tempAverage = tempAverage * 100;
            tempAverage = Math.round(tempAverage);
            tempAverage = tempAverage / 100;
            tempAverageString = Float.toString(tempAverage);
        } else {
            tempAverageString = "Brak danych";
        }
        return tempAverageString;
    }

    private void setText (String averageDailyValue, String averageMonthlyValue, String averageYearlyValue) {
        tv_day.setText(averageDailyValue);
        tv_month.setText(averageMonthlyValue);
        tv_year.setText(averageYearlyValue);
    }

    private void setUserName() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userName = firebaseUser.getEmail();
        //Activity activity = (Activity)getApplicationContext();
        View header = nv.getHeaderView(0);
        tv_user_name = (TextView)header.findViewById(R.id.tv_user_name);
        iv_onClick = (ImageView)header.findViewById(R.id.iv_onClick);
        //tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_name.setText(userName);
    }

    public void onClickImage() {
        iv_onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AverageControllerActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setTextViewValues() {
        if (userChoose == 1) {
            tv_text_day.setText("Srednia wartość temperatury z ostatniego dnia:");
            tv_text_month.setText("Srednia wartość temperatury z ostatniego miesiąca:");
            tv_text_year.setText("Srednia wartość temperatury z ostatniego roku:");
        } else {
            tv_text_day.setText("Srednia wartość prądu z ostatniego dnia:");
            tv_text_month.setText("Srednia wartość prądu z ostatniego miesiąca:");
            tv_text_year.setText("Srednia wartość prądu z ostatniego roku:");
        }

    }
}
