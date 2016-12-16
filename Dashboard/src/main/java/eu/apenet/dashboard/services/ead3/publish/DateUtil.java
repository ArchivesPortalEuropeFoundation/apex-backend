/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author kaisar
 */
public class DateUtil {

    public static List<String> getDates(String strDate, String normalUnitDate) {
        List<String> dates = new ArrayList<>();
        String startdate = "", enddate = "", alterdate = "";
        if (StringUtils.isNotBlank(strDate)) {
            try {
                if (normalUnitDate.contains("/")) {
                    String[] list = normalUnitDate.split("/");
                    String sd = "";
                    String ed = "";
                    String od = "";
                    if (list.length == 2) {// The meaning is:
                        // startdate/enddate
                        sd = list[0].trim();
                        startdate = obtainDate(sd, true);
                        ed = list[1].trim();
                        if ((sd.contains("-")) && (sd.equals(ed))) {
                            enddate = obtainDate(ed, true);
                        } else {
                            enddate = obtainDate(ed, false);
                        }
                        alterdate = strDate;
                    } else { // There is only one date.
                        od = list[0].trim();
                        startdate = obtainDate(od, true);
                        enddate = obtainDate(od, false);
                        alterdate = strDate;
                    }
                } else {// if the date is a year.
                    startdate = obtainDate(normalUnitDate, true);
                    enddate = obtainDate(normalUnitDate, false);
                    alterdate = strDate;
                }
            } catch (Exception ex) {
                alterdate = strDate;
                if (alterdate.contains("\n")) {
                    String[] list = alterdate.split("\n");
                    String alterdate2 = "";
                    for (int x = 0; x < list.length; x++) {
                        alterdate2 = alterdate2 + " " + list[x].trim();
                    }
                    strDate = null;
                    alterdate = alterdate2;
                }
            }
        }
        dates.add(startdate);
        dates.add(enddate);
        dates.add(alterdate);
        return dates;
    }

    private static String obtainDate(String onedate, boolean isStartDate) {
        String year = null;
        String month = null;
        String day = null;
        try {
            if (onedate.contains("-")) {
                String[] list = onedate.split("-");
                if (list.length >= 1) {
                    year = list[0];
                }
                if (list.length >= 2) {
                    month = list[1];
                }
                if (list.length >= 3) {
                    day = list[2];
                }
            } else {
                if (onedate.length() >= 4) {
                    year = onedate.substring(0, 4);
                }
                if (onedate.length() >= 6) {
                    month = onedate.substring(4, 6);
                }
                if (onedate.length() >= 8) {
                    day = onedate.substring(6, 8);
                }
            }
            return obtainDate(year, month, day, isStartDate);
        } catch (Exception ex) {

        }
        return null;
    }

    private static String obtainDate(String yearString, String monthString, String dayString, boolean isStartDate) {
        if (yearString != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Integer year = new Integer(yearString);
            Integer month = null;
            Integer day = null;
            if (monthString != null) {
                month = new Integer(monthString);
            }
            if (dayString != null) {
                day = new Integer(dayString);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(0);
            calendar.set(Calendar.YEAR, year);
            if (isStartDate) {
                if (month == null) {
                    calendar.set(Calendar.MONTH, 0);
                } else {
                    calendar.set(Calendar.MONTH, month - 1);
                }
                if (day == null) {
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                }
                return dateFormat.format(calendar.getTime()) + "T00:00:01Z";
            } else {
                if (month == null) {
                    calendar.set(Calendar.MONTH, 11);
                } else {
                    calendar.set(Calendar.MONTH, month - 1);
                }
                if (day == null) {
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                }
                return dateFormat.format(calendar.getTime()) + "T23:59:59Z";
            }
        }
        return null;
    }

}
