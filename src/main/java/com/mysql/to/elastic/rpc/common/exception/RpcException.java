package com.mysql.to.elastic.rpc.common.exception;

import com.mysql.to.elastic.rpc.common.enums.RpcErrorMessageEnum;

/**
 * @author: cactusli
 * @date: 2022.04.11
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
