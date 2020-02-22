package com.example.checkengine2.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrentDao {

    private Connection connection;
    private Integer lastDay, lastWeek, lastMonth, lastYear;
    private Integer secondMonth, thirdMonth;
    private float tempMaxValue;
    private List<Float> current;
    private List<Float> currentAll = new ArrayList<>();
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
    public List<Float> getCurrentDataForLastDay() {

        current = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery(Queries.getCurrentLastDay);
            if (rs.next()) {
                lastDay = rs.getInt(1);
                rs.close();
            }
            final ResultSet rs2 = st.executeQuery(Queries.getCurrentLastWeek);
            if (rs2.next()) {
                lastWeek = rs2.getInt(1);
                rs2.close();
            }
            final ResultSet rs3 = st.executeQuery(Queries.getCurrentLastMonth);
            if (rs3.next()) {
                lastMonth = rs3.getInt(1);
                rs3.close();
            }
            final ResultSet rs4 = st.executeQuery(Queries.getCurrentLastYear);
            if (rs4.next()) {
                lastYear = rs4.getInt(1);
                rs4.close();
            }
            final ResultSet rs5 = st.executeQuery("select * from prad where day(data) = " + lastDay +
                    " and week(data) = " + lastWeek + " and month(data) = " + lastMonth + " and year(data) = " + lastYear);
            while (rs5.next()) {
                current.add(rs5.getFloat(3));
            }
            rs5.close();
        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }
        return current;
    }

    //Metoda pobierająca pomiary z ostatniego miesiąca:
    public List<Float> getCurrentDataForLastMonth() {

        current = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery(Queries.getCurrentLastMonth);
            if (rs.next()) {
                lastMonth = rs.getInt(1);
                rs.close();
            }
            final ResultSet rs2 = st.executeQuery(Queries.getCurrentLastYear);
            if (rs2.next()) {
                lastYear = rs2.getInt(1);
                rs2.close();
            }
            final ResultSet rs3 = st.executeQuery("select * from prad where month(data) = " + lastMonth +
                    " and year(data) = " + lastYear);
            while (rs3.next()) {
                current.add(rs3.getFloat(3));
                currentAll.add(rs3.getFloat(3));
            }
            rs3.close();
        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }

        return current;
    }

    public List<Float> getCurrentDataForSecondMonth() {
        secondMonth = lastMonth - 1;
        current = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("select * from prad where month(data) = " + secondMonth +
                    " and year(data) = " + lastYear);
            while (rs.next()) {
                current.add(rs.getFloat(3));
                currentAll.add(rs.getFloat(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }
        return current;
    }

    public List<Float> getCurrentDataForThirdMonth() {
        thirdMonth = lastMonth - 2;
        current = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("select * from prad where month(data) = " + thirdMonth +
                    " and year(data) = " + lastYear);
            while (rs.next()) {
                current.add(rs.getFloat(3));
                currentAll.add(rs.getFloat(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }
        return current;
    }

    //Metoda pobierająca pomiary z ostatniego roku:
    public List<Float> getCurrentDataForLastYear() {

        current = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery(Queries.getCurrentLastYear);
            if (rs.next()) {
                lastYear = rs.getInt(1);
                rs.close();
            }
            final ResultSet rs2 = st.executeQuery("select * from prad where year(data) = " + lastYear);
            while (rs2.next()) {
                current.add(rs2.getFloat(3));
            }
            rs2.close();
        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }

        return current;
    }

    //Metoda pobierająca pomiary z podanego przez użytkownika dnia:
    public List<Float> getDailyCurrentData(Integer day, Integer month, Integer year) {
        current = new ArrayList<>();
        date = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            final String getCurrentForDay =
                    "select * from prad where day(data) = " + day +
                            " and month(data) = " + month + " and year(data) = " + year;
            final ResultSet rs = st.executeQuery(getCurrentForDay);
            while (rs.next()) {
                current.add(rs.getFloat(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }
        return current;
    }

    //Metoda pobierająca pomiary z miesiąca podanego przez użytkownika:
    public List<Float> getMonthlyCurrentData(Integer month, Integer year) {
        current = new ArrayList<>();
        date = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            final String getCurrentForMonth =
                    "select * from prad where month(data) = " + month + " and year(data) = " + year;
            final ResultSet rs = st.executeQuery(getCurrentForMonth);
            while (rs.next()) {
                current.add(rs.getFloat(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }
        return current;
    }

    //Metoda pobierająca pomiary z roku podanego przez użytkownika:
    public List<Float> getYearlyCurrentData(Integer year) {
        current = new ArrayList<>();
        date = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            final String getCurrentForYear =
                    "select * from prad where year(data) = " + year;
            final ResultSet rs = st.executeQuery(getCurrentForYear);
            while (rs.next()) {
                current.add(rs.getFloat(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }
        return current;
    }

    //Metoda pobierająca wszystkie pomiary z bazy danych:
    public List<Float> getAllCurrentData() {
        current = new ArrayList<>();
        date = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery(Queries.getAllCurrentData);
            while (rs.next()) {
                current.add(rs.getFloat(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            current = null;
        }
        return current;
    }

    public Float getLastTempValue() {
        float currentValue = 0;
        try {
            Statement st = connection.createStatement();
            rs_notification = st.executeQuery(Queries.getCurrentLastIdValue);
            if (rs_notification.next()) {
                currentValue = rs_notification.getFloat(3);
            }
            currentValue = currentValue * 10;
            currentValue = Math.round(currentValue);
            currentValue = currentValue / 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentValue;
    }

    public void closeNotification() throws Exception{
        rs_notification.close();
    }

    public Integer getLastId() {
        Integer idValue = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(Queries.getCurrentLastIdValue);
            if (rs.next()) {
                idValue = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idValue;
    }

    public List<Float> getCurrentAll() {
        return currentAll;
    }

    public void closeConnection() throws Exception{
        connection.close();
    }
}
