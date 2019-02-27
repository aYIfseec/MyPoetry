package utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class InputParamUtil {

    private static final String PHONE_NUM = "[1][1-9]\\d{9}";
    private static final String PASSWORD = "^[a-zA-Z0-9]{6,20}$";//6-20位字母+数字


    public static boolean noInput(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }


    public static boolean isValid(String phoneNum) {
        if (StringUtils.isBlank(phoneNum)) {
            return false;
        }
        return Pattern.matches(PHONE_NUM, phoneNum);
    }

    public static boolean isValidPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        return Pattern.matches(PASSWORD, password);
    }
}
