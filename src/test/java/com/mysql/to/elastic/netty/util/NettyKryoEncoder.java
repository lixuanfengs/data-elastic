package com.mysql.to.elastic.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 *  * 自定义编码器。
 *  * <p>
 *  * 网络传输需要通过字节流来实现，ByteBuf 可以看作是 Netty 提供的字节数据的容器，使用它会让我们更加方便地处理字节数据。
 * @author: cactusli
 * @date: 2022.04.06
 */
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {

    private final Serializer serializer;
    private final Class<?> genericClass;


    /**
     * 将对象转换为字节码写入到 ByteBuf 对象中
     * @param channelHandlerContext
     * @param o
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)) {
            //1.将对象转换成 byte
            byte[] body = serializer.serialize(o);
            //2.读取消息的长度
            int datalength = body.length;
            //3.写入消息对应的字节数组长度，WriterIndex 加 4
            byteBuf.writeInt(datalength);
            ///4.将字节数组写入 ByteBuf 对象中
            byteBuf.writeBytes(body);
        }
    }
}
