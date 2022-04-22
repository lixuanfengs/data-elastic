package com.mysql.to.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author: cactusli
 * @date: 2022.04.01
 */
//@SpringBootTest
@Slf4j
public class SocketClienTest {

    public Object send(Message message, String host, int port) {
        //1.创建Socket对象并且指定服务器的地址端口号
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //2.通过输出流向服务器端发送请求信息
            objectOutputStream.writeObject(message);
            //3.通过输入流获取服务器响应信息
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        }
        return null;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SocketClienTest socketClienTest = new SocketClienTest();
        Message message = (Message) socketClienTest.send(new Message("content from client 1111"), "192.168.1.216", 888);
        log.info("client receive message: {}", message.getContent());
    }
}

