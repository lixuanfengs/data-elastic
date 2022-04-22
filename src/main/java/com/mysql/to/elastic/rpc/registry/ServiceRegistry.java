package com.mysql.to.elastic.rpc.registry;

import com.mysql.to.elastic.rpc.common.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service registration
 *
 * @author: cactusli
 * @date: 2022.04.11
 */
@SPI
public interface ServiceRegistry {
    /**
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
