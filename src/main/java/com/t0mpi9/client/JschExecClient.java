package com.t0mpi9.client;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.util.IoUtils;

import java.io.IOException;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class JschExecClient extends AbstractJschClient {

    private JschExecClient(Builder builder) {
        try {
            session = jsch.getSession(builder.username, builder.host, builder.port);
            session.setUserInfo(builder.userInfo);
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    public String execute(String command) throws JSchException, IOException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.connect();
        String result = IoUtils.read(channelExec);
        channelExec.disconnect();
        return result;
    }

    public static class Builder {
        private String username;
        private UserInfo userInfo;
        private String host;
        private Integer port;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder userInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public JschExecClient build() {
            return new JschExecClient(this);
        }
    }
}
