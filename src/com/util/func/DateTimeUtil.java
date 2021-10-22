package com.util.func;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class DateTimeUtil {

    private static final Object LOCK = new Object();
    private static DateTimeUtil instance;
    private HashMap<String, SimpleDateFormat> formats;
    private HashMap<String, Pattern> patterns;
    public String DATE_PATTERN_MMyyyy = "MM/yyyy";
    public String DATE_PATTERN_yyyyMMdd = "yyyyMMdd";
    public String DATE_PATTERN_ddMMyyyy = "dd/MM/yyyy";
    public String DATE_PATTERN_HHmmss = "HH:mm:ss";
    public String DATE_PATTERN_ddMMyyyyHHmmss = "dd/MM/yyyy HH:mm:ss";
    public String DATE_PATTERN_yyyy_MM_ddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public String DATE_PATTERN_MMddyyyy = "MM/dd/yyyy";
    public String DATE_PATTERN_ddMMyyyyHHmm = "dd/MM/yyyy HH:mm";
    public String DATE_PATTERN_ddMMyyyyHH24miss = "dd/MM/yyyy HH24:mi:ss";
    public String DATE_PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public String DATE_PATTERN_ddMMyy = "ddMMyy";
    protected String REG_DATE_MMyyyy = "^((0[1-9])|(1[0-2]))\\/\\d{4}$";
    protected String REG_DATE_ddMMyyyy = "^((0[1-9])|([1-2]\\d)|(3[0-1]))\\/((0[1-9])|(1[0-2]))\\/\\d{4}$";
    protected String REG_DATE_ddMMyyyyHHmmss = "^((0[1-9])|([1-2]\\d)|(3[0-1]))\\/((0[1-9])|(1[0-2]))\\/\\d{4}\\s(([0-1]\\d)|(2[0-3])):([0-5]\\d):([0-5]\\d)$";
    protected String REG_DATE_ddMMyyyyHHmm = "^((0[1-9])|([1-2]\\d)|(3[0-1]))\\/((0[1-9])|(1[0-2]))\\/\\d{4}\\s(([0-1]\\d)|(2[0-3])):([0-5]\\d)$";
    protected String REG_DATE_yyyyMMdd = "^\\d{4}((0[1-9])|(1[0-2]))((0[1-9])|([1-2]\\d)|(3[0-1]))$";
    protected String REG_DATE_yyyyMMddHHmmss = "^\\d{4}((0[1-9])|(1[0-2]))((0[1-9])|([1-2]\\d)|(3[0-1]))(([0-1]\\d)|(2[0-3]))([0-5]\\d)([0-5]\\d)$";

    private DateTimeUtil() {
        initialization();
    }

    public static DateTimeUtil getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new DateTimeUtil();
            }
            return instance;
        }
    }

    private void initialization() {
        if (formats == null) {
            formats = new HashMap<String, SimpleDateFormat>();
        }
        if (patterns == null) {
            patterns = new HashMap<String, Pattern>();
        }
    }

    private Pattern getPattern(String pattern) {
        if (!patterns.containsKey(pattern)) {
            patterns.put(pattern, Pattern.compile(pattern));
        }
        return patterns.get(pattern);
    }

    private SimpleDateFormat getFormat(String pattern) {
        if (pattern == null) {
            return null;
        }
        pattern = pattern.trim();
        if (pattern.isEmpty()) {
            return null;
        }
        if (!formats.containsKey(pattern)) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            formats.put(pattern, sdf);
        }
        return formats.get(pattern);
    }

    //<editor-fold defaultstate="collapsed" desc="isDate">
    /**
     * Kiem tra chuoi value co theo dung dinh dang pattern khong
     *
     * @param value
     * @param pattern
     * @return
     */
    public boolean isDate(String value, String pattern) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return !value.isEmpty() && getPattern(pattern).matcher(value).matches();
    }

    public boolean isDateMMyyyy(String value) {
        return isDate(value, REG_DATE_MMyyyy);
    }

    public boolean isDateddMMyyyy(String value) {
        return isDate(value, REG_DATE_ddMMyyyy);
    }

    public boolean isDateddMMyyyyHHmmss(String value) {
        return isDate(value, REG_DATE_ddMMyyyyHHmmss);
    }

    public boolean isDateddMMyyyyHHmm(String value) {
        return isDate(value, REG_DATE_ddMMyyyyHHmm);
    }

    public boolean isDateyyyyMMdd(String value) {
        return isDate(value, REG_DATE_yyyyMMdd);
    }

    public boolean isDateyyyyMMddHHmmss(String value) {
        return isDate(value, REG_DATE_yyyyMMddHHmmss);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toDate">
    /**
     * Convert chuoi value sang kieu Date theo dinh dang pattern
     *
     * @param value
     * @param pattern
     * @return
     * @throws ParseException
     */
    public Date toDate(String value, String pattern) throws ParseException {
        try {
            if (pattern == null || value == null) {
                return null;
            }
            value = value.trim();
            if (value.isEmpty()) {
                return null;
            }
            SimpleDateFormat sdf = getFormat(pattern);
            if (sdf == null) {
                return null;
            }
            return sdf.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    public Date toDateMMyyyy(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_MMyyyy);
    }

    public Date toDateyyyyMMdd(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_yyyyMMdd);
    }

    public Date toDateddMMyyyy(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_ddMMyyyy);
    }

    public Date toDateHHmmss(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_HHmmss);
    }

    public Date toDateddMMyyyyHHmmss(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_ddMMyyyyHHmmss);
    }

    public Date toDateddMMyyyyHHmm(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_ddMMyyyyHHmm);
    }

    public Date toDateyyyyMMddHHmmss(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_yyyyMMddHHmmss);
    }

    public Date toDateyyyy_MM_ddHHmmss(String value) throws ParseException {
        return toDate(value, DATE_PATTERN_yyyy_MM_ddHHmmss);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="formatDate">
    /**
     * Tra ve chuoi format gia tri date theo dinh dang pattern
     *
     * @param date
     * @param pattern
     * @return
     */
    public String format(Object date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        SimpleDateFormat sdf = getFormat(pattern);
        if (sdf == null) {
            return null;
        }
        return sdf.format(date);
    }

    public String formatMMyyyy(Object date) {
        return format(date, DATE_PATTERN_MMyyyy);
    }

    public String formatyyyyMMdd(Object date) {
        return format(date, DATE_PATTERN_yyyyMMdd);
    }

    public String formatddMMyyyy(Object date) {
        return format(date, DATE_PATTERN_ddMMyyyy);
    }
    
    public String formatMMddyyyy(Object date){
        return format(date, DATE_PATTERN_MMddyyyy);
    }

    public String formatddMMyy(Object date) {
        return format(date, DATE_PATTERN_ddMMyy);
    }

    public String formatHHmmss(Object date) {
        return format(date, DATE_PATTERN_HHmmss);
    }

    public String formatddMMyyyyHHmmss(Object date) {
        return format(date, DATE_PATTERN_ddMMyyyyHHmmss);
    }

    public String formatddMMyyyyHHmm(Object date) {
        return format(date, DATE_PATTERN_ddMMyyyyHHmm);
    }

    public String formatyyyyMMddHHmmss(Object date) {
        return format(date, DATE_PATTERN_yyyyMMddHHmmss);
    }

    public String formatyyyy_MM_ddHHmmss(Object date) {
        return format(date, DATE_PATTERN_yyyy_MM_ddHHmmss);
    }
    //</editor-fold>

    public boolean isDateClass(Class clazz) {
        Class tmpClass = clazz;
        while (tmpClass != null && !Object.class.equals(tmpClass)) {
            if (Date.class.equals(tmpClass)) {
                return true;
            }
            tmpClass = tmpClass.getSuperclass();
        }
        return false;
    }

    public Timestamp getTimestamp(Object d) {
        if (d instanceof Timestamp) {
            return (Timestamp) d;
        }
        return d == null ? null : new Timestamp(((Date) d).getTime());
    }

    //<editor-fold defaultstate="collapsed" desc="addTime">
    public Date addSecond(Date date, int second) {
        Date addDate = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.SECOND, second);
            addDate = cal.getTime();
        }
        return addDate;
    }

    public Date addMinute(Date date, int minute) {
        Date addDate = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minute);
            addDate = cal.getTime();
        }
        return addDate;
    }

    public Date addHour(Date date, int hour) {
        Date addDate = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, hour);
            addDate = cal.getTime();
        }
        return addDate;
    }

    public Date addDay(Date date, int day) {
        Date addDate = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, day);
            addDate = cal.getTime();
        }
        return addDate;
    }

    public Date addWeek(Date date, int week) {
        Date addDate = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.WEEK_OF_MONTH, week);
            addDate = cal.getTime();
        }
        return addDate;
    }

    public Date addMonth(Date date, int month) {
        Date addDate = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, month);
            addDate = cal.getTime();
        }
        return addDate;
    }

    public Date addYear(Date date, int year) {
        Date addDate = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, year);
            addDate = cal.getTime();
        }
        return addDate;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getTime">
    public Integer getYear(Date date) {
        Integer result = null;
        if (date == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        result = cal.get(Calendar.YEAR);
        return result;
    }

    public Integer getMonth(Date date) {
        Integer result = null;
        if (date == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        result = cal.get(Calendar.MONTH) + 1;
        return result;
    }

    public Integer getWeek(Date date) {
        Integer result = null;
        if (date == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        result = cal.get(Calendar.WEEK_OF_MONTH);
        return result;
    }

    public Integer getDay(Date date) {
        Integer result = null;
        if (date == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        result = cal.get(Calendar.DAY_OF_MONTH);
        return result;
    }

    public Integer getLastDayOfMonth(Date runTime) throws ParseException {
        return getDay(toDateddMMyyyy(formatddMMyyyy(addDay(toDateMMyyyy(formatMMyyyy(addMonth(runTime, 1))), -1))));
    }
    
    public Date getLastDateOfMonth(Date runTime) throws ParseException {
        return toDateddMMyyyy(formatddMMyyyy(addDay(toDateMMyyyy(formatMMyyyy(addMonth(runTime, 1))), -1)));
    }

    public Integer getHour(Date date) {
        Integer result = null;
        if (date == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        result = cal.get(Calendar.HOUR_OF_DAY);
        return result;
    }

    public Integer getMinute(Date date) {
        Integer result = null;
        if (date == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        result = cal.get(Calendar.MINUTE);
        return result;
    }

    public Integer getSecond(Date date) {
        Integer result = null;
        if (date == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        result = cal.get(Calendar.SECOND);
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getQuarter">
    public Integer getQuarter(Date date) {
        return getQuarter(getMonth(date));
    }

    private Integer getQuarter(Integer month) {
        Integer result = null;
        if (month == null || month <= 0) {
            return result;
        }
        int div = month / 3;
        int mod = month % 3;
        result = div;
        if (mod > 0) {
            result++;
        }
        return result;
    }
    //</editor-fold>

    public Date toFirstDayOfMonth(Date date) {
        Date result = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            result = cal.getTime();
        }
        return result;
    }

    public Date trunc(Date date, String pattern) throws ParseException {
        Date result = toDate(format(date, pattern), pattern);
        return result;
    }

    public double getDayBetween(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            return 0;
        }
        double days = (toDate.getTime() - fromDate.getTime()) / (1000D * 60 * 60 * 24);
        return days;
    }

    public Date setTimeEndDay(Date date) {
        Date result = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            result = cal.getTime();
        }
        return result;
    }

    public Date getDate(Integer day, Integer month, Integer year) {
        Calendar calendar = Calendar.getInstance();
        if (day != null) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        if (month != null) {
            calendar.set(Calendar.MONTH, month);
        }
        if (year != null) {
            calendar.set(Calendar.YEAR, year);
        }
        return calendar.getTime();
    }

    public Date getDate(int day) {
        return getDate(day, null, null);
    }

    public Date getIncrementDate(int day, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        if (month != 0) {
            return addMonth(calendar.getTime(), month);
        } else {
            return calendar.getTime();
        }
    }
    public String getLastDayOfPeriousMonth(int periousMonth) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -periousMonth);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date = cal.getTime();
        return formatddMMyyyy(date);
    }
    
    
    public String textDateMonthYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return " ano " + year + " mes " + month + " dia " + day;
    }

    public String textHourMinuteSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return " horas " + hour + " minuto " + minute + " segundos " + second;
    }
    
    public XMLGregorianCalendar toXMLGDate(Date datte) throws Exception {
        if(datte == null) return null;
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(datte);
        XMLGregorianCalendar changeDateGreg = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        return changeDateGreg;
    }
    
    public Date nvl(Date date, Date nvlDate){
        return date == null ? nvlDate : date; 
    }
}
