package com.example.javamail.executarNextMonth;

public class TimeBean {

    private final int sec;
    private final int min;
    private final int hour;
    private final int day;
    private final int month;

    private TimeBean(final Builder builder) {
        this.sec = builder.sec;
        this.min = builder.min;
        this.hour = builder.hour;
        this.day = builder.day;
        this.month = builder.month;
    }

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

    public static class Builder {
        private int sec;
        private int min;
        private int hour;
        private int day;
        private int month;

        public Builder withSec(final int sec) {
            this.sec = sec;
            return this;
        }
        public Builder withMin(final int min) {
            this.min = min;
            return this;
        }
        public Builder withHour(final int hour) {
            this.hour = hour;
            return this;
        }
        public Builder withDay(final int day) {
            this.day = day;
            return this;
        }
        public Builder withMonth(final int month) {
            this.month = month;
            return this;
        }
        public TimeBean build() {
            return new TimeBean(this);
        }
    }
}
