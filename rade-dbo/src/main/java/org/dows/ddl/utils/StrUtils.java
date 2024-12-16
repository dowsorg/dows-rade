package org.dows.ddl.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {

    public static boolean blank(Object str) {
        if (str instanceof String) {
            return !StrUtil.isNotBlank((String) str);
        }
        if (str instanceof String[]) {
            return !Arrays.stream(((String[]) str)).allMatch(StrUtil::isNotBlank);
        }
        return false;
    }


    public static boolean legal(String str) {
        String regex = "^[a-zA-Z]\\w{0,15}$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
