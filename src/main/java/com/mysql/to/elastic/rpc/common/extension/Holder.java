package com.mysql.to.elastic.rpc.common.extension;

/**
 * @author: cactusli
 * @date: 2022.04.11
 */
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
