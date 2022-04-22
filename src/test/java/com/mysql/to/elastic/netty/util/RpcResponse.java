package com.mysql.to.elastic.netty.util;

import lombok.*;

/**
 * @author: cactusli
 * @date: 2022.04.06
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class RpcResponse {
    private String message;
}
