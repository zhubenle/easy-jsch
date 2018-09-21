package com.t0mpi9.client;

import com.jcraft.jsch.Channel;

/**
 * <br/>
 * Created on 2018/9/20 16:12.
 *
 * @author zhubenle
 */
public interface JschClientObtainResultStrategy {

    /**
     * 获取shell交互返回结果
     * @param channel 通道
     */
    void obtainResult(Channel channel);
}
