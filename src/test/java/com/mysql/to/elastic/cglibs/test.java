package com.mysql.to.elastic.cglibs;

/**
 * @author: cactusli
 * @date: 2022.04.07
 */
public class test {
    public static void main(String[] args) {
        AliSmsService aliSmsService = (AliSmsService) CglibProxyFactory.getProxy(AliSmsService.class);
        aliSmsService.send("php");
    }
}
