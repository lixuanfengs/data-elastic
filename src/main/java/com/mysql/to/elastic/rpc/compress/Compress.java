package com.mysql.to.elastic.rpc.compress;

/**
 * @author: cactusli
 * @date: 2022.04.12
 */
public interface Compress {

    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);
}
