package com.mysql.to.elastic.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: cactusli
 * @date: 2022.04.11
 */
@AllArgsConstructor
@Getter
public enum  RpcConfigEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;
}
