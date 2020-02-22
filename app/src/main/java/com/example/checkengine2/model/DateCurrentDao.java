package com.example.checkengine2.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateCurrentDao {

    private Connection connection;
    private Integer lastDay, lastWeek, lastMonth, lastYear;
    private Integer secondMonth, thirdMonth;
    private List<String> date;
    private List<String> dateAll = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat;
    private Date dateVariable;
    private String dateString;
    private Boolean databaseConection;


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
    public List<String> getDateDataForLastDay() {

        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("HH:mm");

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery(Queries.getCurrentLastDay);
            if (rs.next()) {
                lastDay = rs.getInt(1);
            }
            rs.close();
            final ResultSet rs2 = st.executeQuery(Queries.getCurrentLastWeek);
            if (rs2.next()) {
                lastWeek = rs2.getInt(1);
            }
            rs2.close();
            final ResultSet rs3 = st.executeQuery(Queries.getCurrentLastMonth);
            if (rs3.next()) {
                lastMonth = rs3.getInt(1);
            }
            rs3.close();
            final ResultSet rs4 = st.executeQuery(Queries.getCurrentLastYear);
            if (rs4.next()) {
                lastYear = rs4.getInt(1);
            }
            rs4.close();
            final ResultSet rs5 = st.executeQuery("select * from prad where day(data) = " + lastDay +
                    " and week(data) = " + lastWeek + " and month(data) = " + lastMonth + " and year(data) = " + lastYear);
            while (rs5.next()) {
                dateVariable = rs5.getTimestamp(2);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
            }
            rs5.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    //Metoda pobierająca pomiary z podanego przez użytkownika dnia:
    public List<String> getDailyCurrentData(Integer day, Integer month, Integer year) {

        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("HH:mm");

        try {
            Statement st = connection.createStatement();
            final String getCurrentForDay =
                    "select * from prad where day(data) = " + day +
                            " and month(data) = " + month + " and year(data) = " + year;
            final ResultSet rs = st.executeQuery(getCurrentForDay);
            while (rs.next()) {
                dateVariable = rs.getTimestamp(2);
                System.out.println(dateVariable);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    //Metoda pobierająca pomiary z miesiąca podanego przez użytkownika:
    public List<String> getMonthlyCurrentData(Integer month, Integer year) {

        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("MM:dd");

        try {
            Statement st = connection.createStatement();
            final String getCurrentForMonth =
                    "select * from prad where month(data) = " + month + " and year(data) = " + year;
            final ResultSet rs = st.executeQuery(getCurrentForMonth);
            while (rs.next()) {
                dateVariable = rs.getTimestamp(2);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    //Metoda pobierająca pomiary z ostatniego miesiąca:
    public List<String> getDateDataForLastMonth() {

        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("MM:dd");

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
                dateVariable = rs3.getTimestamp(2);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
                dateAll.add(dateString);
            }
            rs3.close();
        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }

        return date;
    }

    public List<String> getDateDataForSecondMonth() {
        secondMonth = lastMonth - 1;
        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("MM:dd");

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("select * from prad where month(data) = " + secondMonth +
                    " and year(data) = " + lastYear);
            while (rs.next()) {
                dateVariable = rs.getTimestamp(2);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
                dateAll.add(dateString);
            }

        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    public List<String> getDateDataForThirdMonth() {
        thirdMonth = lastMonth - 2;
        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("MM:dd");

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("select * from prad where month(data) = " + thirdMonth +
                    " and year(data) = " + lastYear);
            while (rs.next()) {
                dateVariable = rs.getTimestamp(2);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
                dateAll.add(dateString);
            }

        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    //Metoda pobierająca pomiary z roku podanego przez użytkownika:
    public List<String> getYearlyCurrentData(Integer year) {

        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("YY:MM");

        try {
            Statement st = connection.createStatement();
            final String getCurrentForYear =
                    "select * from prad where year(data) = " + year;
            final ResultSet rs = st.executeQuery(getCurrentForYear);
            while (rs.next()) {
                dateVariable = rs.getTimestamp(2);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    //Metoda pobierająca wszystkie pomiary z bazy danych:
    public List<String> getAllCurrentData() {

        date = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("YY:MM");

        try {
            Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery(Queries.getAllCurrentData);
            while (rs.next()) {
                dateVariable = rs.getTimestamp(2);
                dateString = simpleDateFormat.format(dateVariable);
                date.add(dateString);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    public Integer getLastMonth() {
        return lastMonth;
    }

    public Integer getSecondMonth() {
        return secondMonth;
    }

    public Integer getThirdMonth() {
        return thirdMonth;
    }

    public List<String> getDateAll() {
        return dateAll;
    }

    public void closeConnection() throws Exception{
        connection.close();
    }
}
