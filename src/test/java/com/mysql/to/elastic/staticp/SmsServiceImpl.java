package com.mysql.to.elastic.staticp;

/**
 * @author: cactusli
 * @date: 2022.04.07
 */
public class SmsServiceImpl implements SmsService {
    @Override
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }
}
