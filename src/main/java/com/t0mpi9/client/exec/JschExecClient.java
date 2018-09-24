package com.t0mpi9.client.exec;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.client.EmptyJschClient;
import com.t0mpi9.client.JschClientObtainResultStrategy;

import java.io.IOException;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class JschExecClient extends EmptyJschClient {

    private Builder builder;

    public JschExecClient(Builder builder) {
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
    public void exec(String command) throws JSchException, IOException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.connect(builder.sessionConnectTimeout);
        builder.resultStrategy.obtainResult(channelExec);
        channelExec.disconnect();
    }

    public static class Builder extends AbstractBuilder {

        @Override
        public Builder resultStrategy(JschClientObtainResultStrategy resultStrategy) {
            this.resultStrategy = resultStrategy;
            return this;
        }

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
        public JschExecClient build() {
            return new JschExecClient(this);
        }
    }
}
