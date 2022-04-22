package com.mysql.to.elastic.rpc.common.utils;

/** String 工具类
 * @author: cactusli
 * @date: 2022.04.11
 */
public class StringUtil {

    public static boolean isBlank(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
