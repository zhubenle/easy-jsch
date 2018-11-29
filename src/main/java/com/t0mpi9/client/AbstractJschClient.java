package com.t0mpi9.client;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.t0mpi9.client.exception.JschClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 所有方法的空实现<br/>
 * Created on 2018/9/19 14:55.
 *
 * @author zhubenle
 */
public abstract class AbstractJschClient implements JschClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractJschClient.class);

    public JSch jsch = new JSch();
    public Session session;

    public AbstractJschClient(AbstractBuilder builder) {
        try {
            session = jsch.getSession(builder.username, builder.host, builder.port);
            session.setUserInfo(builder.userInfo);
            session.setTimeout(builder.sessionConnectTimeout);
        } catch (JSchException e) {
            LOGGER.error("create jsch client session fail: ", e);
            throw new JschClientException(e);
        }
    }

    /**
     * 会话连接
     */
    public void sessionConnect() {
        try {
            session.connect();
        } catch (JSchException e) {
            LOGGER.error("jsch client session connect fail ", e);
            throw new JschClientException(e);
        }
    }

    /**
     * 判断是否连接
     * @return 是否连接
     */
    public abstract boolean isConnected();

    /**
     * 重连
     */
    public abstract void reConnect();

    @Override
    public void close() {
        if (Objects.nonNull(session) && session.isConnected()) {
            session.disconnect();
        }
    }
}
