/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.util;

/**
 *
 * @author papp
 */
public class SimpleDate {
    private int year;
    private int month;
    private int day;

    public SimpleDate(int year) {
        this.year = year;
        this.month = 0;
        this.day = 0;
    }

    public SimpleDate(int year, int month) {
        this.year = year;
        this.month = month;
        this.day = 0;
    }

    public SimpleDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(year);
        if(month != 0)
            result.append("-").append(month);
        if(day != 0)
            result.append("-").append(day);
        return result.toString();
    }
}
