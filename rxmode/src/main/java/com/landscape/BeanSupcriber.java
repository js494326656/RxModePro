package com.landscape;

/**
 * Created by landscape on 2016/8/16.
 */
public interface BeanSupcriber<T> {
    void sendTrigger(T bean);
}
