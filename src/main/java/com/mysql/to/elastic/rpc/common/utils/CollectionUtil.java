package com.mysql.to.elastic.rpc.common.utils;

import java.util.Collection;

/**
 * 集合工具类
 *
 * @author: cactusli
 * @date: 2022.04.11
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
}
