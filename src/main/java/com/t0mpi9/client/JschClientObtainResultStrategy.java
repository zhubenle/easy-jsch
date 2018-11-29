package com.t0mpi9.client;

/**
 * <br/>
 * Created on 2018/9/20 16:12.
 *
 * @author zhubenle
 */
public interface JschClientObtainResultStrategy<T> {

    /**
     * 获取shell交互返回结果
     * @param t
     */
    void obtainResult(T t);
}
