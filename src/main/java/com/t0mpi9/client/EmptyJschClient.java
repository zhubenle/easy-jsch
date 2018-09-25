package com.t0mpi9.client;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.t0mpi9.client.exception.JschClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * 所有方法的空实现<br/>
 * Created on 2018/9/19 14:55.
 *
 * @author zhubenle
 */
public class EmptyJschClient implements JschClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyJschClient.class);

    public JSch jsch = new JSch();
    public Session session;

    public EmptyJschClient(AbstractBuilder builder) {
        try {
            session = jsch.getSession(builder.username, builder.host, builder.port);
            session.setUserInfo(builder.userInfo);
            session.setTimeout(builder.sessionConnectTimeout);
        } catch (JSchException e) {
            LOGGER.error("create jsch client session fail: ", e);
            throw new JschClientException(e);
        }
    }

    public void sessionConnect() {
        try {
            session.connect();
        } catch (JSchException e) {
            LOGGER.error("jsch client session connect fail ", e);
            throw new JschClientException(e);
        }
    }

    @Override
    public void exec(String command) throws JSchException, IOException {
        //空实现
    }

    @Override
    public void shell(String shell) throws JSchException, IOException {
        //空实现
    }

    @Override
    public void sftp(String command) throws JSchException, SftpException {
        //空实现
    }

    @Override
    public void scp(String localFile, String remoteFile, boolean pTimestamp, ScpType scpType) throws JSchException, IOException {
        //空实现
    }

    @Override
    public void close() {
        if (Objects.nonNull(session) && session.isConnected()) {
            session.disconnect();
        }
    }
}
