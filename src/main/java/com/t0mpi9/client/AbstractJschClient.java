package com.t0mpi9.client;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * <br/>
 * Created on 2018/9/19 14:55.
 *
 * @author zhubenle
 */
public abstract class AbstractJschClient implements Closeable {

    public JSch jsch = new JSch();
    public Session session;

    /**
     * 远程服务器执行命令
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
    public abstract String exec(String command) throws JSchException, IOException;

    /**
     * 执行shell
     * @param shell 命令
     * @throws JSchException JSch异常
     * @throws IOException IO异常
     */
    public abstract void shell(String shell) throws JSchException, IOException;

    @Override
    public void close() {
        if (Objects.nonNull(session) && session.isConnected()) {
            session.disconnect();
        }
    }

    public abstract static class AbstractBuilder {

        /**
         * 基本参数
         */
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
         * 设置密码验证对象
         *
         * @param port
         *         用户名
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder userInfo(UserInfo port);

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
         * 设置session连接超时时间
         *
         * @param sessionConnectTimeout
         *         超时时间(ms)
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder sessionConnectTimeout(Integer sessionConnectTimeout);

        /**
         * 设置channel连接超时时间
         *
         * @param channelConnectTimeout
         *         超时时间(ms)
         *
         * @return 建造者对象
         */
        public abstract AbstractBuilder channelConnectTimeout(Integer channelConnectTimeout);

        /**
         * 创建对象
         *
         * @return 对象
         */
        public abstract AbstractJschClient build();
    }
}
