package com.example.checkengine2.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TemperatureDao {

    private Connection connection;
    private Integer lastDay, lastWeek, lastMonth, lastYear;
    private Integer secondMonth, thirdMonth;
    private float tempMaxValue;
    private List<Float> temperature;
    private List<Float> temperatureAll = new ArrayList<>();
    private List<String> date;
    private Boolean databaseConection;

    private ResultSet rs_notification;

    //Metoda realizująca połączenie z bazą danych
    public void executeConnection() throws Exception{

        //Rejestracja sterownika JDBC
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        //Nawiązanie połączenia z systemem zarządzania bazą danych:
        final String url = "jdbc:mysql://192.168.8.116:3306/temperaturaDB";
        final String user = "uzytkownik3";
        final String password = "haslo3";
        connection = DriverManager.getConnection(url, user, password);
        databaseConection = true;
    }

    //Metoda pobierająca pomiary z ostatniego dnia:
    public List<Float> getTemperatureDataForLastDay() {

        temperature = new ArrayList<>();

            try {
                Statement st = connection.createStatement();
                final ResultSet rs = st.executeQuery(Queries.getTemperatureLastDay);
                if (rs.next()) {
                    lastDay = rs.getInt(1);
                    rs.close();
                }
                final ResultSet rs2 = st.executeQuery(Queries.getTemperatureLastWeek);
                if (rs2.next()) {
                    lastWeek = rs2.getInt(1);
                    rs2.close();
                }
                final ResultSet rs3 = st.executeQuery(Queries.getTemperatureLastMonth);
                if (rs3.next()) {
                    lastMonth = rs3.getInt(1);
                    rs3.close();
                }
                final ResultSet rs4 = st.executeQuery(Queries.getTemperatureLastYear);
                if (rs4.next()) {
                    lastYear = rs4.getInt(1);
                    rs4.close();
                }
                final ResultSet rs5 = st.executeQuery("select * from temperatura where day(data) = " + lastDay +
                        " and week(data) = " + lastWeek + " and month(data) = " + lastMonth + " and year(data) = " + lastYear);
                while (rs5.next()) {
                    temperature.add(rs5.getFloat(3));
                }
                rs5.close();
            } catch (Exception e) {
                e.printStackTrace();
                temperature = null;
            }
        return temperature;
    }

    //Metoda pobierająca pomiary z ostatniego miesiąca:
    public List<Float> getTemperatureDataForLastMonth() {

        temperature = new ArrayList<>();

            try {
                Statement st = connection.createStatement();
                final ResultSet rs = st.executeQuery(Queries.getTemperatureLastMonth);
                if (rs.next()) {
                    lastMonth = rs.getInt(1);
                    rs.close();
                }
                final ResultSet rs2 = st.executeQuery(Queries.getTemperatureLastYear);
                if (rs2.next()) {
                    lastYear = rs2.getInt(1);
                    rs2.close();
                }
                final ResultSet rs3 = st.executeQuery("select * from temperatura where month(data) = " + lastMonth +
                                                        " and year(data) = " + lastYear);
                while (rs3.next()) {
                    temperature.add(rs3.getFloat(3));
                    temperatureAll.add(rs3.getFloat(3));
                }
                rs3.close();
            } catch (Exception e) {
                e.printStackTrace();
                temperature = null;
            }

        return temperature;
    }

    public List<Float> getTemperatureDataForSecondMonth() {
        secondMonth = lastMonth - 1;
        temperature = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("select * from temperatura where month(data) = " + secondMonth +
                    " and year(data) = " + lastYear);
            while (rs.next()) {
                temperature.add(rs.getFloat(3));
                temperatureAll.add(rs.getFloat(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
            temperature = null;
        }
        return temperature;
    }

    public List<Float> getTemperatureDataForThirdMonth() {
        thirdMonth = lastMonth - 2;
        temperature = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("select * from temperatura where month(data) = " + thirdMonth +
                    " and year(data) = " + lastYear);
            while (rs.next()) {
                temperature.add(rs.getFloat(3));
                temperatureAll.add(rs.getFloat(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
            temperature = null;
        }
        return temperature;
    }

    //Metoda pobierająca pomiary z ostatniego roku:
    public List<Float> getTemperatureDataForLastYear() {

        temperature = new ArrayList<>();

            try {
                Statement st = connection.createStatement();
                final ResultSet rs = st.executeQuery(Queries.getTemperatureLastYear);
                if (rs.next()) {
                    lastYear = rs.getInt(1);
                    rs.close();
                }
                final ResultSet rs2 = st.executeQuery("select * from temperatura where year(data) = " + lastYear);
                while (rs2.next()) {
                    temperature.add(rs2.getFloat(3));
                }
                rs2.close();
            } catch (Exception e) {
                e.printStackTrace();
                temperature = null;
            }

        return temperature;
    }

    //Metoda pobierająca pomiary z podanego przez użytkownika dnia:
    public List<Float> getDailyTemperatureData(Integer day, Integer month, Integer year) {
        temperature = new ArrayList<>();
        date = new ArrayList<>();
            try {
                Statement st = connection.createStatement();
                final String getTemperatureForDay =
                        "select * from temperatura where day(data) = " + day +
                                " and month(data) = " + month + " and year(data) = " + year;
                final ResultSet rs = st.executeQuery(getTemperatureForDay);
                while (rs.next()) {
                    temperature.add(rs.getFloat(3));
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                temperature = null;
            }
        return temperature;
    }

    //Metoda pobierająca pomiary z miesiąca podanego przez użytkownika:
    public List<Float> getMonthlyTemperatureData(Integer month, Integer year) {
        temperature = new ArrayList<>();
        date = new ArrayList<>();
            try {
                Statement st = connection.createStatement();
                final String getTemperatureForMonth =
                        "select * from temperatura where month(data) = " + month + " and year(data) = " + year;
                final ResultSet rs = st.executeQuery(getTemperatureForMonth);
                while (rs.next()) {
                    temperature.add(rs.getFloat(3));
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                temperature = null;
            }
        return temperature;
    }

    //Metoda pobierająca pomiary z roku podanego przez użytkownika:
    public List<Float> getYearlyTemperatureData(Integer year) {
        temperature = new ArrayList<>();
        date = new ArrayList<>();
            try {
                Statement st = connection.createStatement();
                final String getTemperatureForYear =
                        "select * from temperatura where year(data) = " + year;
                final ResultSet rs = st.executeQuery(getTemperatureForYear);
                while (rs.next()) {
                    temperature.add(rs.getFloat(3));
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                temperature = null;
            }
        return temperature;
    }

    //Metoda pobierająca wszystkie pomiary z bazy danych:
    public List<Float> getAllTemperatureData() {
        temperature = new ArrayList<>();
        date = new ArrayList<>();
            try {
                Statement st = connection.createStatement();
                final ResultSet rs = st.executeQuery(Queries.getAllTemperatureData);
                while (rs.next()) {
                    temperature.add(rs.getFloat(3));
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                temperature = null;
            }
        return temperature;
    }

    public Float getLastTempValue() {
        float tempValue = 0;
        try {
            Statement st = connection.createStatement();
            rs_notification = st.executeQuery(Queries.geTemperaturetLastIdValue);
            if (rs_notification.next()) {
                tempValue = rs_notification.getFloat(3);
            }
            tempValue = tempValue * 10;
            tempValue = Math.round(tempValue);
            tempValue = tempValue / 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempValue;
    }

    public void closeNotification() throws Exception{
        rs_notification.close();
    }

    public Integer getLastId() {
        Integer idValue = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(Queries.geTemperaturetLastIdValue);
            if (rs.next()) {
                idValue = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idValue;
    }

    public List<Float> getTemperatureAll() {
        return temperatureAll;
    }

    public void closeConnection() throws Exception{
        connection.close();
    }
}
