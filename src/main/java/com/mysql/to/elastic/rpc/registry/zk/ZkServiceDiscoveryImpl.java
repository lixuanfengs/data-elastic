package com.mysql.to.elastic.rpc.registry.zk;

import com.mysql.to.elastic.rpc.common.enums.RpcErrorMessageEnum;
import com.mysql.to.elastic.rpc.common.exception.RpcException;
import com.mysql.to.elastic.rpc.common.extension.ExtensionLoader;
import com.mysql.to.elastic.rpc.common.utils.CollectionUtil;
import com.mysql.to.elastic.rpc.loadbalance.LoadBalance;
import com.mysql.to.elastic.rpc.registry.ServiceDiscovery;
import com.mysql.to.elastic.rpc.registry.zk.util.CuratorUtils;
import com.mysql.to.elastic.rpc.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * service discovery based on zookeeper
 *
 * @author: cactusli
 * @date: 2022.04.11
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl(LoadBalance loadBalance) {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }


    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
