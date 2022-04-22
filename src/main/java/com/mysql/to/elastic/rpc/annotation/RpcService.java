package com.mysql.to.elastic.rpc.annotation;

import java.lang.annotation.*;

/**
 * RPC service annotation, marked on the service implementation class
 *
 * @author: cactusli
 * @date: 2022.04.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";

}
