package com.t0mpi9.client.exec;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.t0mpi9.client.JschClient;

import java.io.IOException;
import java.util.Objects;

/**
 * <br/>
 * Created on 2018/9/19 14:55.
 *
 * @author zhubenle
 */
public abstract class AbstractJschExecClient implements JschClient {

    public JSch jsch = new JSch();
    Session session;

    @Override
    public void shell(String shell) throws JSchException, IOException {
        //空实现
    }

    @Override
    public void close() {
        if (Objects.nonNull(session) && session.isConnected()) {
            session.disconnect();
        }
    }

}
