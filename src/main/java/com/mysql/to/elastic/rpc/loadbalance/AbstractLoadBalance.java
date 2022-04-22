package com.mysql.to.elastic.rpc.loadbalance;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.mysql.to.elastic.rpc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * Abstract class for a load balancing policy
 *
 * @author: cactusli
 * @date: 2022.04.11
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if(CollectionUtils.isEmpty(serviceAddresses)) {
            return null;
        }
        if(serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest);
}
