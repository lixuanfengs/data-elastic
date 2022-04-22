package com.mysql.to.elastic.rpc.loadbalance;

import com.mysql.to.elastic.rpc.common.extension.SPI;
import com.mysql.to.elastic.rpc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * nterface to the load balancing policy
 *
 * @author: cactusli
 * @date: 2022.04.11
 */
@SPI
public interface LoadBalance {

    /**
     * Choose one from the list of existing service addresses list
     * @param serviceUrlList Service address list
     * @param rpcRequest
     * @return target service address
     */
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
