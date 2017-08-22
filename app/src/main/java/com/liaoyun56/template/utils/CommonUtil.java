package com.liaoyun56.template.utils;

public class CommonUtil {

    public static boolean isEmptyOrNull(String str){
        if(str==null || str.equals("")){
            return true;
        }else {
            return false;
        }
    }

    public static boolean isNotEmptyOrNull(String str){
        if(str==null || str.equals("")){
            return false;
        }else {
            return true;
        }
    }
}