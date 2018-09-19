package com.t0mpi9.client;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.util.ChannelUtils;

import java.io.IOException;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class JschClient extends AbstractJschClient {

    private JschClient(Builder builder) {
        try {
            session = jsch.getSession(builder.username, builder.host, builder.port);
            session.setUserInfo(builder.userInfo);
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String exec(String command) throws JSchException, IOException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.connect();
        String result = ChannelUtils.read(channelExec);
        channelExec.disconnect();
        return result;
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
        public JschClient build() {
            return new JschClient(this);
        }
    }
}
