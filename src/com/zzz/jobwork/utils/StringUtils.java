package com.zzz.jobwork.utils;

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
}
