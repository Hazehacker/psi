package com.zeroone.star.homepage.utils.localDate;

import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;

/**
 * @author Fantasy_Hv
 */
public class LocalDateUtil {
    /**
     * 计算间隔天数，包含开头和结尾,调用前应保证输入合理性
     * @return 天数
     */

    public static int countDays(LocalDate from, LocalDate to){
        if (from.isAfter(to))return 0;
        int days = 0;
        LocalDate cur = from;
        do {
            YearMonth monthContext = YearMonth.of(cur.getYear(), cur.getMonth());
            int daysOfMonth = cur.getMonth()==to.getMonth()&&cur.getYear()==to.getYear() ? to.getDayOfMonth()- cur.getDayOfMonth() : monthContext.lengthOfMonth() - from.getDayOfMonth() ;
            days += daysOfMonth+1;
            cur = cur.plusDays(monthContext.lengthOfMonth()-cur.getDayOfMonth()+1);
        }while ( cur.isBefore(to)|| cur.isEqual(to));
        return days;
    }

    /**
     * 从yyy-mm-dd串中解析日期，不合法返回null
     */
    public static LocalDate parse( String dateString){
        if (dateString==null||!dateString.matches("\\d+-\\d+-\\d+"))
            return null;
        String [] components = dateString.split("-");
        LocalDate date;
        try {
            date = LocalDate.of(
                    Integer.parseInt(components[0])
                    ,Integer.parseInt(components[1])
                    ,Integer.parseInt(components[2]));
        }catch (Exception e){return null;}
        return date;
    }
}
