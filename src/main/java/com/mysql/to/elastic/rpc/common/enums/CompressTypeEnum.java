package com.mysql.to.elastic.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: cactusli
 * @date: 2022.04.12
 */
@AllArgsConstructor
@Getter
public enum  CompressTypeEnum {

    GZIP((byte) 0x01, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

}
