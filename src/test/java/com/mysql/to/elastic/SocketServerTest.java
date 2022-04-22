package com.mysql.to.elastic;


import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: cactusli
 * @date: 2022.03.31
 */
//@SpringBootTest
@Slf4j
public class SocketServerTest {
    public void start(int port) {
        //1.创建 ServerSocket 对象并且绑定一个端口
        try (ServerSocket server = new ServerSocket(port);) {
            Socket socket;
            //2.通过 accept()方法监听客户端请求
            while ((socket = server.accept()) != null) {
                log.info("client start connected");
                try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                     //3.通过输入流读取客户端发送的请求信息
                    Message m = (Message) objectInputStream.readObject();
                    log.info("server receive message:" + m.getContent());
                    m.setContent("new content .....");
                    //4.通过输出流向客户端发送响应信息
                    objectOutputStream.writeObject(m);
                    objectOutputStream.flush();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    log.error("occur exception:", e);
                }
            }
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }


    public static void main(String[] args) {
        SocketServerTest socketTest = new SocketServerTest();
        socketTest.start(888);
    }
}


