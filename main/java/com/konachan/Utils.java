package com.konachan;

public class Utils {

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else {
            return "".equals(str.trim());
        }
    }
}
