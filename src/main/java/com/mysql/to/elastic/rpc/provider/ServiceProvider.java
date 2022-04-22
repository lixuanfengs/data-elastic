package com.mysql.to.elastic.rpc.provider;

import com.mysql.to.elastic.rpc.config.RpcServiceConfig;

/**
 * store and provide service object.
 *
 * @author: cactusli
 * @date: 2022.04.12
 */
public interface ServiceProvider {

    /**
     * @param rpcServiceConfig rpc service related attributes
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceName rpc service name
     * @return service object
     */
    Object getService(String rpcServiceName);

    /**
     * @param rpcServiceConfig rpc service related attributes
     */
    void publishService(RpcServiceConfig rpcServiceConfig);
}
