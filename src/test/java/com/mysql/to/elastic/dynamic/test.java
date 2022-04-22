package com.mysql.to.elastic.dynamic;

/**
 * @author: cactusli
 * @date: 2022.04.07
 */
public class test {
    public static void main(String[] args) {
        SmsService smsService = (SmsService) JdkProxyFactory.getProxy(new SmsServiceImpl());
        smsService.send("java");
    }
}
