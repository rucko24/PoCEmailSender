package com.example.javamail.executarnextmonth;

/**
 * @implSpec probar a fondo la inmutablidad con el GenericBuilder
 */
public final class TimeBean {

    private  int sec;
    private  int min;
    private  int hour;
    private  int day;
    private  int month;

    public int getSec() {
        return sec;
    }

    public int getMin() {
        return min;
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        final StringBuilder expresionCron = new StringBuilder()
                .append(sec)
                .append(" ")
                .append(min)
                .append(" ")
                .append(hour)
                .append(" ")
                .append(day)
                .append(" ")
                .append(month)
                .append(" ")
                .append("?"); // opcional el YEAR XD
        return expresionCron.toString();
    }

}
