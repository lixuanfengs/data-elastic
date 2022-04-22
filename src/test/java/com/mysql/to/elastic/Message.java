package com.mysql.to.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: cactusli
 * @date: 2022.04.01
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {

    private String content;

}
