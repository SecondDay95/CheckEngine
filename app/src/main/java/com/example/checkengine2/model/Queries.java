package com.example.checkengine2.model;

public class Queries {

    public static final String getTemperatureLastDay =
            "select day(data) from temperatura where id=(select max(id) from temperatura)";
    public static final String getTemperatureLastWeek =
            "select week(data) from temperatura where id=(select max(id) from temperatura)";
    public static final String getTemperatureLastMonth =
            "select month(data) from temperatura where id=(select max(id) from temperatura)";
    public static final String getTemperatureLastYear =
            "select year(data) from temperatura where id=(select max(id) from temperatura)";
    public static final String getAllTemperatureData = "select * from temperatura";
    public static final String geTemperaturetLastIdValue = "select * from temperatura where id=(select max(id) from temperatura)";

    public static final String getCurrentLastDay =
            "select day(data) from prad where id=(select max(id) from prad)";
    public static final String getCurrentLastWeek =
            "select week(data) from prad where id=(select max(id) from prad)";
    public static final String getCurrentLastMonth =
            "select month(data) from prad where id=(select max(id) from prad)";
    public static final String getCurrentLastYear =
            "select year(data) from prad where id=(select max(id) from prad)";
    public static final String getAllCurrentData = "select * from prad";
    public static final String getCurrentLastIdValue = "select * from prad where id=(select max(id) from prad)";


}
