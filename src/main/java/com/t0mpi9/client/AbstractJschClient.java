package com.t0mpi9.client;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.Closeable;
import java.util.Objects;

/**
 * <br/>
 * Created on 2018/9/19 14:55.
 *
 * @author zhubenle
 */
public class AbstractJschClient implements Closeable {

    JSch jsch = new JSch();
    Session session;

    @Override
    public void close() {
        if (Objects.nonNull(session) && session.isConnected()) {
            session.disconnect();
        }
    }
}
