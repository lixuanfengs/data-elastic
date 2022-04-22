package com.mysql.to.elastic.rpc.common.utils;

/**
 * @author: cactusli
 * @date: 2022.04.12
 */
public class RuntimeUtil {

    /**
     * 获取CPU的核心数
     *
     * @return cpu的核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }

}
