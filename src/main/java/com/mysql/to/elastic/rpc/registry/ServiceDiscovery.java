package com.mysql.to.elastic.rpc.registry;

import com.mysql.to.elastic.rpc.common.extension.SPI;
import com.mysql.to.elastic.rpc.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * service discovery
 *
 * @author: cactusli
 * @date: 2022.04.11
 *
 */
@SPI
public interface ServiceDiscovery {

    /**
     * lookup service by rpcServiceName
     *
     * @param rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
