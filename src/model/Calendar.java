package model;

import java.util.Observable;

/**
 * Created by Sam on 5/26/2017.
 * @author Sam Bagdan, sambagdan@gmail.com
 * Desc: Represents the model of the calendar
 */
public class Calendar extends Observable{
    private int month;
    private int year;
    public Boolean[] calendar;

    /**
     * Calendar model constructor
     */
    public Calendar(){
        //default values for the calendar:
        month = 1;
        year = 2017;
    }

    /**
     * populates calendar based on the models set year and month
     * @param year - set year
     * @param month - set month
     */
    public void calculateMonth (int year, int month){
        calendar = new Boolean[getMonthDays(month)];
        int yearsAfter17 = year - 2017;
        int leapYears = (yearsAfter17 + 1)/4;
        int daysAfter17 = 365*(yearsAfter17 - leapYears) + 366*(leapYears);
        for (int i = 1; i != month; i++){
            daysAfter17 += getMonthDays(i);
        }
        int accountedFor = 0;
        int start = daysAfter17;
        while (daysAfter17 < start + getMonthDays(month)){
            daysAfter17++;
            calendar[accountedFor] = (daysAfter17 % 6 == 1 || daysAfter17 % 6 == 2);
            accountedFor++;
        }
    }

    /**
     * finds the first day of the set date for the View
     * @return - number of the first day in the week
     */
    public int calculateFirstDay(){
        int remainderYears = year - 1 - 2000;
        int leapYears = remainderYears / 4;
        int addDaysPrev = (remainderYears + leapYears) % 7;
        int addDaysNow = 1;
        for (int i = 1; i < month; i++){
            addDaysNow += getMonthDays(i);
        }
        addDaysNow %= 7;
        return (addDaysPrev + addDaysNow) % 7;
    }

    //month setter
    public void setMonth(int month) {
        this.month = month;
    }

    //year setter
    public void setYear(int year) {
        this.year = year;
    }

    //month getter
    public int getMonth() {
        return month;
    }

    //year getter
    public int getYear() {
        return year;
    }

    //calendar getter
    public Boolean[] getCalendar() {
        return calendar;
    }

    /**
     * returns the number of days in the provided month
     * @param month - month to use
     * @return int - number of days in the month
     */
    private int getMonthDays(int month){
        int days;
        switch (month) {
            case 1:
                days = 31;
                break;
            case 2:
                if (year % 4 != 0){
                    days = 28;
                } else{
                    days = 29;
                }
                break;
            case 3:
                days = 31;
                break;
            case 4:
                days = 30;
                break;
            case 5:
                days = 31;
                break;
            case 6:
                days = 30;
                break;
            case 7:
                days = 31;
                break;
            case 8:
                days = 31;
                break;
            case 9:
                days = 30;
                break;
            case 10:
                days = 31;
                break;
            case 11:
                days = 30;
                break;
            default:
                days = 31;
                break;
        }
        return days;
    }
}
