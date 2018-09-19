package com.t0mpi9.client;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.util.ChannelUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class JschClient extends AbstractJschClient {

    private Builder builder;

    private JschClient(Builder builder) {
        try {
            this.builder = builder;
            session = jsch.getSession(builder.username, builder.host, builder.port);
            session.setUserInfo(builder.userInfo);
            session.setTimeout(builder.sessionConnectTimeout);
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String exec(String command) throws JSchException, IOException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.connect(builder.sessionConnectTimeout);
        String result = ChannelUtils.read(channelExec);
        channelExec.disconnect();
        return result;
    }

    @Override
    public void shell(InputStream in, OutputStream out) throws JSchException, IOException {
        ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
        channelShell.setInputStream(in);
        channelShell.setOutputStream(out);
        channelShell.connect(builder.sessionConnectTimeout);
    }

    public static class Builder extends AbstractJschClient.AbstractBuilder {

        @Override
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        @Override
        public Builder userInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        @Override
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        @Override
        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        @Override
        public Builder sessionConnectTimeout(Integer sessionConnectTimeout) {
            this.sessionConnectTimeout = sessionConnectTimeout;
            return this;
        }

        @Override
        public Builder channelConnectTimeout(Integer channelConnectTimeout) {
            this.channelConnectTimeout = channelConnectTimeout;
            return this;
        }

        @Override
        public JschClient build() {
            return new JschClient(this);
        }
    }
}
