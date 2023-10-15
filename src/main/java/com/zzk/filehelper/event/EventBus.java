package com.zzk.filehelper.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * 自定义的事件总线
 * @Author kelton
 * @Date 2023/10/15 12:11
 * @Version 1.0
 */
public class EventBus {
    private String name;

    private LinkedList<Object> subscribers;

    public EventBus(String name) {
        this.name = name;
        this.subscribers = new LinkedList<>();
    }


    /**
     * 同一个消息将会被发送给所有订阅了该消息的订阅者
     * @param event
     */
    public void post(Object event) {
        // 根据类型， 查看所有的订阅者， 然后调用订阅者的方法
        // 1. 获取所有的订阅者
        for (Object subscriber : subscribers) {
            // 2. 获取订阅者的方法
            // 获取是否有Subscribe注解
            Method[] methods = subscriber.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Subscribe.class)) {
                    // 匹配参数类型
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        continue;
                    }
                    // 只有一个参数
                    if (!parameterTypes[0].isAssignableFrom(event.getClass())) {
                        continue;
                    }
                    // 3. 调用订阅者的方法
                    try {
                        method.invoke(subscriber, event);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }

        }
    }

    public void register(Object object) {
        subscribers.add(object);
    }
    public void unregister(Object object) {
        subscribers.remove(object);
    }
}
