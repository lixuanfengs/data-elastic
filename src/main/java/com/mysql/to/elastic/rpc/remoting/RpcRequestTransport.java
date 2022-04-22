package com.mysql.to.elastic.rpc.remoting;

import com.mysql.to.elastic.rpc.remoting.dto.RpcRequest;

/**
 * send RpcRequest
 * @author: cactusli
 * @date: 2022.04.07
 */
public interface RpcRequestTransport {
    /**
     * send rpc request to server and get result
     *
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
