package com.t0mpi9.client.shell;

import com.jcraft.jsch.Channel;

import java.io.InputStream;

/**
 * <br/>
 * Created on 2018/9/20 16:12.
 *
 * @author zhubenle
 */
public interface JschShellObtainResultStrategy {

    /**
     * 获取shell交互返回结果
     * @param inputStream 输入流
     * @param channel 通道
     */
    void obtainResult(InputStream inputStream, Channel channel);
}
