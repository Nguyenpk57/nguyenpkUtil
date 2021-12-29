package com.util.func;

import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */

public class ValidateUtils {

    private static final Object LOCK = new Object();
    private static ValidateUtils instance;
    private static HashMap<String, ValidateUtils> instances;
    private static HashMap<String, Pattern> patterns;
    private ILogger logger;
    private final String ISDN_REG = "^(\\d|\\+)\\d{6,14}$";
    private final String EMAIL_REG = "^[0-9?A-z0-9?]([a-zA-Z0-9\\-]*(\\.|_)?[a-zA-Z0-9\\-]*)*@([a-zA-Z0-9\\-]{2,}\\.)+[a-zA-Z\\-]{2,}$";
    private final String EMAIL_PLUS_REG = "^((?!((_\\.)|(\\._))).)*$";
    private final String FILE_PATH_REG = "^(.*)(\\.+)(.+)$";

    private final String PASSWORD_REG = "[A-Za-z0-9~!@#$%^&*_+]{6,12}$";
    private final String ARRAY_INDEX = "\\[\\d\\]";
    private final String BINARY_VALIDATE = "[0,1]+";

    private UnsupportedOperationException unsupportedOperationException;

    //<editor-fold defaultstate="collapsed" desc="constructor">
    private ValidateUtils() {
        initialization();
    }

    private ValidateUtils(ILogger logger) {
        this.logger = logger;
        initialization();
    }

    public static ValidateUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ValidateUtils();
            }
            return instance;
        }
    }

    public static ValidateUtils getInstance(ILogger logger) {
        if (logger == null) {
            return getInstance();
        }
        String name = logger.getName();
        if (name == null) {
            return getInstance();
        }
        name = name.trim();
        if (name.isEmpty()) {
            return getInstance();
        }
        synchronized (LOCK) {
            if (instances == null) {
                instances = new HashMap<String, ValidateUtils>();
            }
            if (!instances.containsKey(name)) {
                instances.put(name, new ValidateUtils(logger));
            }
            return instances.get(name);
        }
    }

    private void initialization() {
        if (logger == null) {
            logger = LoggerImpl.getInstance(this.getClass());
        }
        if (patterns == null) {
            patterns = new HashMap<String, Pattern>();
        }
        unsupportedOperationException = new UnsupportedOperationException("Not supported yet.");
    }
    //</editor-fold>

    private Pattern getPattern(String pattern) {
        if (!patterns.containsKey(pattern)) {
            patterns.put(pattern, Pattern.compile(pattern));
        }
        return patterns.get(pattern);
    }

    public boolean isEmail(String value) {
        return matcher(value, EMAIL_REG) && matcher(value, EMAIL_PLUS_REG);
    }

    public boolean isIsdn(String value) {
        return matcher(value, ISDN_REG);
    }

    private boolean matcher(String value, String pattern) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return !value.isEmpty() && getPattern(pattern).matcher(value).matches();
    }

    public UnsupportedOperationException getUnsupportedOperationException() {
        return unsupportedOperationException;
    }

    public boolean isFile(String value) {
        boolean result = matcher(value, FILE_PATH_REG);
        return result;
    }

    public boolean isValidPassWord(String value) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return !value.isEmpty() && getPattern(PASSWORD_REG).matcher(value).matches();
    }

    public boolean isValidBinary(String value) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return !value.isEmpty() && getPattern(BINARY_VALIDATE).matcher(value).matches();
    }

//    public static void main(String[] args) {
//        String pass = "0123ab#A@";
//        String pass1 = "123abA@z";
//        String pass2 = "a123ab#A@";
//        String pass3 = "123ab#A@";
//        String PASSWORD_REG = "^.*(?=.{8,16})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
//        PASSWORD_REG = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
//
//        Pattern pattern = Pattern.compile(PASSWORD_REG);
//        System.out.println("com.viettel.bccs.up. ----: "  + pattern.matcher(pass).matches());
//        System.out.println("com.viettel.bccs.up. ----: "  + pattern.matcher(pass1).matches());
//        System.out.println("com.viettel.bccs.up. ----: "  + pattern.matcher(pass2).matches());
//        System.out.println("com.viettel.bccs.up. ----: "  + pattern.matcher(pass3).matches());
//    }
}