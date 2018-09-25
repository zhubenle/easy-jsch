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
     * @throws JSchException
     *         JSch异常
     * @throws IOException
     *         IO异常
     */
    void exec(String command) throws JSchException, IOException;

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

    void sftp() throws JSchException, IOException;

    /**
     * scp拷贝文件本地到远程或远程到本地
     *
     * @param localFile
     *         本地文件
     * @param remoteFile
     *         远程文件
     * @param pTimestamp
     *         是否保留文件的原时间戳
     * @param scpType
     *         拷贝类型(本地到远程，远程到本地)
     */
    void scp(String localFile, String remoteFile, boolean pTimestamp, ScpType scpType) throws JSchException, IOException;

    enum ScpType {
        /**
         *
         */
        SCP_TO,
        SCP_FROM
    }

    abstract class AbstractBuilder {

        public String username;
        public UserInfo userInfo;
        public String host;
        public Integer port;
        public Integer sessionConnectTimeout;
        public Integer channelConnectTimeout;
        public JschClientObtainResultStrategy resultStrategy;

        /**
         * 设置用户名
         *
         * @param username
         *         用户名
         *
         * @return 建造者对象
         */
        public AbstractBuilder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * 设置密码
         *
         * @param userInfo
         *         密码对象
         *
         * @return 建造者对象
         */
        public AbstractBuilder userInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        /**
         * 设置ip
         *
         * @param host
         *         ip
         *
         * @return 建造者对象
         */
        public AbstractBuilder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * 设置端口
         *
         * @param port
         *         端口
         *
         * @return 建造者对象
         */
        public AbstractBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        /**
         * 设置会话连接超时时间
         *
         * @param sessionConnectTimeout
         *         超时时间
         *
         * @return 建造者对象
         */
        public AbstractBuilder sessionConnectTimeout(Integer sessionConnectTimeout) {
            this.sessionConnectTimeout = sessionConnectTimeout;
            return this;
        }

        /**
         * 设置通道连接超时时间
         *
         * @param channelConnectTimeout
         *         超时时间
         *
         * @return 建造者对象
         */
        public AbstractBuilder channelConnectTimeout(Integer channelConnectTimeout) {
            this.channelConnectTimeout = channelConnectTimeout;
            return this;
        }

        /**
         * 设置结果处理策略
         *
         * @param resultStrategy
         *         策略
         *
         * @return 建造者对象
         */
        public AbstractBuilder resultStrategy(JschClientObtainResultStrategy resultStrategy) {
            this.resultStrategy = resultStrategy;
            return this;
        }

        /**
         * 构造客户端对象
         *
         * @return 客户端
         */
        public abstract JschClient build();
    }
}
