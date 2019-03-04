package utils;

import java.util.Calendar;
import java.util.Date;

public class ChineseDateUtil {

    /** 大写数字 */

    private static final String[] NUMBERS = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };

    private static final String[] data = { "年","月","日"};

    // 日期转化为大小写
    public static String dateToUpper(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        return yearToUpper(year) + monthToUppder(month) + dayToUppder(day);
    }

    public static String yearToUpper(int year) {
        return numToUpper(year) + "年";
    }

    // 将数字转化为大写
    public static String numToUpper(int num) {
        //String u[] = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        String u[] = {"零","一","二","三","四","五","六","七","八","九"};
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }

    // 月转化为大写
    public static String monthToUppder(int month) {
        if(month < 10) {
            return numToUpper(month) + "月";
        } else if(month == 10){
            return "十月";
        } else {
            return "十" + numToUpper(month - 10) + "月";
        }
    }

    // 日转化为大写
    public static String dayToUppder(int day) {
        if(day < 20) {
            return monthToUppder(day) + "日";
        } else {
            char[] str = String.valueOf(day).toCharArray();
            if(str[1] == '0') {
                return numToUpper(Integer.parseInt(str[0] + "")) + "十" + "日";
            }else {
                return numToUpper(Integer.parseInt(str[0] + "")) + "十" + numToUpper(Integer.parseInt(str[1] + "")) + "日";
            }
        }
    }

    public static void main(String args[]) {

        System.out.println(dateToUpper(new Date(1577611182000L)));

    }
}
