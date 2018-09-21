package com.t0mpi9.client;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;

import java.io.Closeable;
import java.io.IOException;

/**
 * <br/>
 * Created on 2018/9/19 14:55.
 *
 * @author zhubenle
 */
public interface JschClient extends Closeable {

    /**
     * 远程服务器执令
     *
     * @param command
     *         命令
     *
     * @return 执行返回结果
     *
     * @throws JSchException
     *         JSch异常
     * @throws IOException
     *         IO异常
     */
    String exec(String command) throws JSchException, IOException;

    /**
     * 执行shell
     *
     * @param shell
     *         命令
     *
     * @throws JSchException
     *         JSch异常
     * @throws IOException
     *         IO异常
     */
    void shell(String shell) throws JSchException, IOException;


    abstract class AbstractBuilder {

        public String username;
        public UserInfo userInfo;
        public String host;
        public Integer port;
        public Integer sessionConnectTimeout;
        public Integer channelConnectTimeout;

        /**
         * 设置用户名
         *
         * @param username
         *         用户名
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder username(String username);

        /**
         * 设置密码
         *
         * @param userInfo
         *         密码对象
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder userInfo(UserInfo userInfo);

        /**
         * 设置ip
         *
         * @param host
         *         ip
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder host(String host);

        /**
         * 设置端口
         *
         * @param port
         *         端口
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder port(Integer port);

        /**
         * 设置会话连接超时时间
         *
         * @param sessionConnectTimeout
         *         超时时间
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder sessionConnectTimeout(Integer sessionConnectTimeout);

        /**
         * 设置通道连接超时时间
         *
         * @param channelConnectTimeout
         *         超时时间
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder channelConnectTimeout(Integer channelConnectTimeout);

        /**
         * 构造客户端对象
         *
         * @return 客户端
         */
        public abstract JschClient build();
    }
}
