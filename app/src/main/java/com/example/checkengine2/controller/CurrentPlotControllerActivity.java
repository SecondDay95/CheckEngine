package com.example.checkengine2.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYCoords;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.example.checkengine2.R;
import com.example.checkengine2.main.HomeActivity;
import com.example.checkengine2.main.MainActivity;
import com.example.checkengine2.model.CurrentDao;
import com.example.checkengine2.model.DateCurrentDao;
import com.example.checkengine2.settings.CurrentSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrentPlotControllerActivity extends AppCompatActivity {

    private CurrentDao currentDao;
    private DateCurrentDao dateCurrentDao;
    private int drawer_position, yMin, yMax, yStep, xStep, typeOfSettings2 = 1, userChoose;
    private Integer day, month, year;
    private List<Float> current;
    private List<String> date;
    private List<Float> firstMonthCurrent, secondMonthCurrent, thirdMonthCurrent;
    private List<String> firstMonthDate, secondMonthDate, thirdMonthDate;
    private Boolean databaseConnection;

    private XYPlot currentPlot;
    private SimpleXYSeries currentSeries, trendSeries, trendSeries2, trendSeries3;
    private Redrawer redrawer;
    private Toolbar toolbar;
    private XYGraphWidget.CursorLabelFormatter xy;
    private TextView tv_cursor_current;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plot, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent2 = new Intent(CurrentPlotControllerActivity.this, CurrentSettings.class);
                intent2.putExtra("userChoose", userChoose);
                startActivity(intent2);
                return true;
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();

                //Powrót do MainActivity:
                Intent intent1 = new Intent(CurrentPlotControllerActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_current_plot_controller);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Przebieg pradu");
        ab.setDisplayHomeAsUpEnabled(true);

        currentPlot = (XYPlot) findViewById(R.id.currentPlot);
        tv_cursor_current = (TextView) findViewById(R.id.tv_cursor_current);
        tv_cursor_current.setVisibility(View.GONE);

        getIntentExtra();

        addingSeries();

        currentPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(
                new DecimalFormat("#"));
        currentPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(
                new DecimalFormat("#"));

        redrawer = new Redrawer(Arrays.asList(new Plot[]{currentPlot}), 100, false);

        new MyTask().execute();

        redrawer.start();

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        redrawer.start();

        SharedPreferences sharedPreferences = CurrentPlotControllerActivity.this.getSharedPreferences("CurrentData",
                Context.MODE_PRIVATE);
        typeOfSettings2 = sharedPreferences.getInt("typeOfSettingsCurrent", 2);
        System.out.println("typeOfSettings2 = " + typeOfSettings2);
        yMax = sharedPreferences.getInt("yMaxCurrent", 8);
        System.out.println("yMXcURRENT = " + yMax);
        yMin = sharedPreferences.getInt("yMinCurrent", 2);
        yStep = sharedPreferences.getInt("yStepCurrent", 2);
        xStep = sharedPreferences.getInt("xStepCurrent", 5);

    }

    @Override
    protected void onPause() {
        redrawer.pause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        redrawer.finish();
        super.onDestroy();
    }

    protected class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            currentDao = new CurrentDao();
            dateCurrentDao = new DateCurrentDao();
            current = new ArrayList<>();
            date = new ArrayList<>();

            if (drawer_position == 0) {
                try {
                    currentDao.executeConnection();
                    dateCurrentDao.executeConnection();
                    databaseConnection = true;

                    current = currentDao.getDailyCurrentData(day, month, year);
                    date = dateCurrentDao.getDailyCurrentData(day, month, year);

                    setAxisAuto(current);
                    setLabelName(drawer_position);

                    drawPlot(current, date);
                    drawTrend(current, date);

                    currentDao.closeConnection();
                    dateCurrentDao.closeConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    databaseConnection = false;
                }

            } else if (drawer_position == 1) {
                try {
                    currentDao.executeConnection();
                    dateCurrentDao.executeConnection();
                    databaseConnection = true;

                    current = currentDao.getMonthlyCurrentData(month, year);
                    date = dateCurrentDao.getMonthlyCurrentData(month, year);

                    setAxisAuto(current);
                    setLabelName(drawer_position);

                    drawTrend(current, date);
                    currentDao.closeConnection();
                    dateCurrentDao.closeConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    databaseConnection = false;
                }
            } else if (drawer_position == 2) {
                try {
                    firstMonthCurrent = new ArrayList<>();
                    secondMonthCurrent = new ArrayList<>();
                    thirdMonthCurrent = new ArrayList<>();


                    firstMonthDate = new ArrayList<>();
                    secondMonthDate = new ArrayList<>();
                    thirdMonthDate = new ArrayList<>();

                    currentDao.executeConnection();
                    dateCurrentDao.executeConnection();
                    databaseConnection = true;

                    firstMonthCurrent = currentDao.getCurrentDataForLastMonth();
                    secondMonthCurrent = currentDao.getCurrentDataForSecondMonth();
                    thirdMonthCurrent = currentDao.getCurrentDataForThirdMonth();


                    firstMonthDate = dateCurrentDao.getDateDataForLastMonth();
                    secondMonthDate = dateCurrentDao.getDateDataForSecondMonth();
                    thirdMonthDate = dateCurrentDao.getDateDataForThirdMonth();

                    setAxisAuto(firstMonthCurrent);
                    setLabelName(drawer_position);

                    drawThreeMonthTrend(firstMonthCurrent, secondMonthCurrent, thirdMonthCurrent,
                            firstMonthDate, secondMonthDate, thirdMonthDate);

                    currentDao.closeConnection();
                    dateCurrentDao.closeConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    databaseConnection = false;
                }
            } else if (drawer_position == 3) {
                try {
                    currentDao.executeConnection();
                    dateCurrentDao.executeConnection();
                    databaseConnection = true;

                    current = currentDao.getYearlyCurrentData(year);
                    date = dateCurrentDao.getYearlyCurrentData(year);

                    setAxisAuto(current);
                    setLabelName(drawer_position);
                    drawTrend(current, date);

                    currentDao.closeConnection();
                    dateCurrentDao.closeConnection();
                }catch (Exception e) {
                    e.printStackTrace();
                    databaseConnection = false;
                }
            } else {
                try {
                    currentDao.executeConnection();
                    dateCurrentDao.executeConnection();
                    databaseConnection = true;

                    current = currentDao.getAllCurrentData();
                    date = dateCurrentDao.getAllCurrentData();

                    setAxisAuto(current);
                    setLabelName(drawer_position);
                    drawTrend(current, date);

                    currentDao.closeConnection();
                    dateCurrentDao.closeConnection();
                }catch (Exception e) {
                    e.printStackTrace();
                    databaseConnection = false;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setCursorPosition();
            Intent intent;
            if (!databaseConnection) {
                Toast.makeText(CurrentPlotControllerActivity.this, "Brak połączenia z bazą danych",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(CurrentPlotControllerActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (databaseConnection && drawer_position == 2 &&
                    (firstMonthCurrent.size() == 0 || secondMonthCurrent.size() == 0 || thirdMonthCurrent.size() == 0)) {
                Toast.makeText(CurrentPlotControllerActivity.this, "Brak pomiarów dostępnych dla 3 miesięcy",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(CurrentPlotControllerActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (databaseConnection && drawer_position != 2 && current.size() <= 1) {
                Toast.makeText(CurrentPlotControllerActivity.this, "Zbyt niewielka ilosc pomiarów",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(CurrentPlotControllerActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }


    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        drawer_position = intent.getIntExtra("DRAWER_POSITION", 1);
        userChoose = intent.getIntExtra("userChoose", 1);
        System.out.println("Current Controller: userChoose = " + userChoose);
        day = intent.getIntExtra("DAY", 1);
        month = intent.getIntExtra("MONTH", 1);
        year = intent.getIntExtra("YEAR", 2019);
    }

    private void drawPlot(final List<Float> current, final List<String> date) {

        for (int i = 0; i < current.size(); i++) {
            currentSeries.addLast(i, current.get(i));

        }
    }

    private void setAxisAuto(List<Float> temperature) {
        if (typeOfSettings2 == 1) {
            float temperatureValue = 0, temperatureAvValue = 0;
            for (float i : temperature) {
                temperatureValue = temperatureValue + i;
            }
            temperatureAvValue = temperatureValue / temperature.size();
            currentPlot.setRangeBoundaries(temperatureAvValue - 6, temperatureAvValue + 6,
                    BoundaryMode.FIXED);
            currentPlot.setRangeStepMode(StepMode.INCREMENT_BY_VAL);
            currentPlot.setRangeStepValue(2);
            double increment;
            increment = temperature.size() / 5;
            if (temperature.size() > 5) {
                currentPlot.setDomainStepMode(StepMode.INCREMENT_BY_FIT);
                //tempPlot.setDomainStepValue(10);
                currentPlot.setDomainStepValue(increment);
            } else {
                currentPlot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
                //tempPlot.setDomainStepValue(10);
                currentPlot.setDomainStepValue(1);
            }
        } else {
            currentPlot.setRangeBoundaries(yMin, yMax, BoundaryMode.FIXED);
            currentPlot.setRangeStepMode(StepMode.INCREMENT_BY_VAL);
            currentPlot.setRangeStepValue(yStep);

            currentPlot.setDomainStepMode(StepMode.SUBDIVIDE);
            currentPlot.setDomainStepValue(xStep);
        }
    }

    private List<Double> calculateTrendValues(List<Float> temperature) {
        double a = 0, b = 0, c = 0, d = 0, wspA = 0, e = 0, f = 0, wspB = 0;
        double x1 = 0, x2 = 0, x3 = 0, x4 = 0, x5 = 0, x6 = 0;
        //Dodanie linii trendu:
        for (int i = 0; i < temperature.size(); i++) {
            x1 = temperature.get(i).doubleValue();
            x2 = temperature.indexOf(temperature.get(i));
            a = x1 * x2 + a;
            //Obliczanie wsp. b:
            x3 = x1 + x3;
            x4 = x2 + x4;
            //Obliczanie wsp c:
            x5 = Math.pow(x2, 2) + x5;
        }
        a = temperature.size() * a;
        b = x3 * x4;
        c = temperature.size() * x5;
        d = Math.pow(x4, 2);
        wspA = (a - b) / (c - d);
        e = x3;
        f = wspA * x4;
        wspB = (e - f) / temperature.size();

        List<Double> trendValues = new ArrayList<>();

        for (int i = 0; i < temperature.size(); i++) {
            double y = 0;
            y = wspA * i + wspB;
            trendValues.add(y);
        }
        return trendValues;
    }

    private void drawTrend(final List<Float> temperature, final List<String> date) {
        final List<Double> trend = calculateTrendValues(temperature);

        for (int i = 0; i < trend.size(); i++) {
            double y = 0, idValue;
            trendSeries.addLast(trend.indexOf(trend.get(i)), trend.get(i));
        }
        currentPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                Number number = (Number) obj;
                for (int i = 0; i < trend.size(); i++) {
                    if (i == number.intValue()) {
                        toAppendTo.append(date.get(i));
                    }
                }
                return toAppendTo;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }

    private void drawThreeMonthTrend(final List<Float> temperature, final List<Float> temperature2, final List<Float> temperature3,
                                     final List<String> date, final List<String> date2, final List<String> date3) {
        final List<Double> trend = calculateTrendValues(temperature);
        final List<Double> trend2 = calculateTrendValues(temperature2);
        final List<Double> trend3 = calculateTrendValues(temperature3);
        for (int i = 0; i < trend.size(); i++) {
            trendSeries.addLast(trend.indexOf(trend.get(i)), trend.get(i));
        }
        for (int i = 0; i < trend2.size(); i++) {
            trendSeries2.addLast(trend2.indexOf(trend2.get(i)), trend2.get(i));
        }
        for (int i = 0; i < trend3.size(); i++) {
            trendSeries3.addLast(trend3.indexOf(trend3.get(i)), trend3.get(i));
        }

        final List<String> dateAll = dateCurrentDao.getDateAll();
        final List<Float> temperatureAll = currentDao.getCurrentAll();

        currentPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                Number number = (Number) obj;
                for (int i = 0; i < temperatureAll.size(); i++) {
                    if (i == number.intValue()) {
                        toAppendTo.append(dateAll.get(i));
                    }
                }
                return toAppendTo;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }

    private void addingSeries() {
        if (drawer_position == 2) {
            trendSeries = new SimpleXYSeries("trend");
            trendSeries2 = new SimpleXYSeries("trend2");
            trendSeries3 = new SimpleXYSeries("trend3");
            currentPlot.addSeries(trendSeries, new LineAndPointFormatter(Color.rgb(200, 100, 100),
                    null, null, null));
            currentPlot.addSeries(trendSeries2, new LineAndPointFormatter(Color.rgb(100, 200, 100),
                    null, null, null));
            currentPlot.addSeries(trendSeries3, new LineAndPointFormatter(Color.rgb(100, 100, 200),
                    null, null, null));
        } else if (drawer_position == 0){

            currentSeries = new SimpleXYSeries("prad");
            trendSeries = new SimpleXYSeries("trend");
            currentPlot.addSeries(currentSeries, new LineAndPointFormatter(Color.rgb(100, 200, 100),
                    null, null, null));
            currentPlot.addSeries(trendSeries, new LineAndPointFormatter(Color.rgb(200, 100, 100),
                    null, null, null));
        } else {
            trendSeries = new SimpleXYSeries("trend");
            currentPlot.addSeries(trendSeries, new LineAndPointFormatter(Color.rgb(200, 100, 100),
                    null, null, null));
        }
    }

    private void setLabelName(int drawer_position) {
        if (drawer_position == 0) {
            currentPlot.setDomainLabel("Czas [HH:mm]");
            currentPlot.setRangeLabel("Prad [A]");
        } else if (drawer_position == 1 || drawer_position == 2) {
            currentPlot.setDomainLabel("Czas [MM:dd]");
            currentPlot.setRangeLabel("Prad [A]");
        } else if (drawer_position == 3 || drawer_position == 4) {
            currentPlot.setDomainLabel("Czas [YY:MM]");
            currentPlot.setRangeLabel("Prad [A]");
        }
    }

    private void setCursorPosition() {

            currentPlot.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent me) {
                    float touchX = me.getX();
                    float touchY = me.getY();
                    XYGraphWidget widget = currentPlot.getGraph();
                    RectF gridRect = widget.getGridRect();
                    if (gridRect.contains(touchX, touchY)) { //Check the touch event is in the grid

                        try {

                            int idValX = currentPlot.screenToSeriesX(touchX).intValue();
                            float targetValX = currentPlot.screenToSeriesX(touchX).floatValue();
                            float targetValY = currentPlot.screenToSeriesY(touchY).floatValue();
                            float threshold = (int) (currentPlot.getBounds().getHeight().intValue() * 0.1f); //How close to a trace does the user have to click to move the markers?
                            float targetValYFloat = currentPlot.screenToSeriesY(touchY).floatValue();

                            if (drawer_position != 2) {

                                if ((Double) trendSeries.getY(idValX) - targetValY < 1 &&
                                        targetValY < (Double) trendSeries.getY(idValX) + 1) {
                                    Log.w("targetValY", "" + targetValY);
                                    Log.w("Cursor", "Touched within threshold to trace line");
                                    Log.w("treshold", "treshold = " + threshold);

                                    float valXFloat = Float.valueOf(targetValX);
                                    String valXString = date.get(idValX);
                                    float valYFloat = Float.valueOf(targetValYFloat);
                                    //widget.getDomainCursorPaint();
                                    int cursorX = (Integer) trendSeries.getX(idValX);
                                    double coursorY = (Double) trendSeries.getY(idValX);
                                    XYCoords pointXY = new XYCoords(targetValX, targetValY);
                                    PointF pointF = currentPlot.seriesToScreen(pointXY);
                                    widget.setCursorPosition(pointF);

                                    //widget.setCursorPosition(touchX, touchY);
                                    tv_cursor_current.setVisibility(View.VISIBLE);
                                    tv_cursor_current.setText("x = " + valXFloat + ", " + "date = " + valXString +
                                            " y = " + valYFloat);
                                }
                                currentPlot.invalidate();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d("Cursor", "Touched outside the plot grid");
                    }

                    return false;
                }

            });
        }
}
