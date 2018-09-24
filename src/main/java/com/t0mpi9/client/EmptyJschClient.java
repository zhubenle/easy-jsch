package com.t0mpi9.client;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.util.Objects;

/**
 * 所有方法的空实现<br/>
 * Created on 2018/9/19 14:55.
 *
 * @author zhubenle
 */
public class EmptyJschClient implements JschClient {

    public JSch jsch = new JSch();
    public Session session;

    @Override
    public void exec(String command) throws JSchException, IOException {
        //空实现
    }

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
