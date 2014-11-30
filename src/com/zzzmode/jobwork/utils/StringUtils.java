package com.zzzmode.jobwork.utils;

/**
 * Created by zl on 2014/11/25.
 */
public class StringUtils {

    private StringUtils(){}


    public static String preConfig(String prefix){
        return ""+prefix+"|";
    }

    public static String delPrefix(String config){


        if(isEmpty(config))
            return "";
        return config.substring(config.indexOf("|")+1);

    }

    public static boolean isEmpty(String s){
        if(s == null || s.length() == 0)
            return true;

        return false;
    }

    public static String toString(Object[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "";
        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append(", ");
        }
    }

}
