package com.mysql.to.elastic.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: cactusli
 * @date: 2022.04.07
 */
public class DebugInvocationHandler implements InvocationHandler {

    /**
     * 代理类中的真实对象
     */
    private final Object target;

    public DebugInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        //调用方法之前，我们可以添加自己的操作
        System.out.println("before method " + method.getName());
        Object invoke = method.invoke(target, objects);
        //调用方法之后，我们同样可以添加自己的操作
        System.out.println("after method " + method.getName());
        return invoke;
    }
}
