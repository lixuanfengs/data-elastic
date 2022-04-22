package com.mysql.to.elastic.rpc.loadbalance.loadbalancer;

import com.mysql.to.elastic.rpc.loadbalance.AbstractLoadBalance;
import com.mysql.to.elastic.rpc.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 *  mplementation of random load balancing strategy
 * @author: cactusli
 * @date: 2022.04.11
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        Random random = new Random();

        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
