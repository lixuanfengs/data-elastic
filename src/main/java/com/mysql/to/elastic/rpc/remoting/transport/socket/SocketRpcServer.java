package com.mysql.to.elastic.rpc.remoting.transport.socket;

import com.mysql.to.elastic.rpc.common.factory.SingletonFactory;
import com.mysql.to.elastic.rpc.common.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import com.mysql.to.elastic.rpc.config.CustomShutdownHook;
import com.mysql.to.elastic.rpc.config.RpcServiceConfig;
import com.mysql.to.elastic.rpc.provider.ServiceProvider;
import com.mysql.to.elastic.rpc.provider.impl.ZkServiceProviderImpl;
import com.mysql.to.elastic.rpc.remoting.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author: cactusli
 * @date: 2022.04.07
 */
@Slf4j
public class SocketRpcServer {

    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, NettyRpcServer.PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }


}
