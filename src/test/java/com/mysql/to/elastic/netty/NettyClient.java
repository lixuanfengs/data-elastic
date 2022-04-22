package com.mysql.to.elastic.netty;

import com.mysql.to.elastic.netty.util.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: cactusli
 * @date: 2022.04.06
 */
@Slf4j
public class NettyClient {

    private final String host;
    private final int port;
    private static final Bootstrap b;


    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //初始化相关资源比如： EventLoopGroup, Boot
    static {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        b = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                // 连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                //  如果 15 秒之内没有发送数据给服务端的话，就发送一次心跳请求
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        /**
                         * 自定义序列化编解码器
                         */
                        //RpcResponse -> ByteBuf
                        socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        // ByteBuf -> RpcRequest
                        socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });

    }

    /**
     * 发送消息到服务端
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    public RpcResponse sendMessage(RpcRequest rpcRequest) {
        try {
            ChannelFuture f = b.connect(host, port).sync();
            log.info("client connect  {}", host + ":" + port);
            Channel futureChannel = f.channel();
            log.info("send message");
            if (futureChannel != null) {
                futureChannel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("client send message: [{}]", rpcRequest.toString());
                    } else {
                        log.error("Send failed:", future.cause());
                    }
                });
                //阻塞等待 ，直到Channel关闭
                futureChannel.closeFuture().sync();
                // 将服务端返回的数据也就是RpcResponse对象取出
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                return futureChannel.attr(key).get();
            }
        } catch (InterruptedException e) {
            log.error("occur exception when connect server:", e);
        }
        return null;
    }

    public static void main(String[] args) {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName("interface")
                .methodName("hello").build();
        NettyClient nettyClient = new NettyClient("192.168.1.216", 8878);
        for (int i = 0; i < 3; i++) {
            nettyClient.sendMessage(rpcRequest);
        }
        RpcResponse rpcResponse = nettyClient.sendMessage(rpcRequest);
        System.out.println(rpcResponse.toString());
    }
}
