package com.t0mpi9.client.sftp;

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
public class JschSftpClient extends EmptyJschClient {

    private Builder builder;

    private JschSftpClient(Builder builder) {
        super(builder);
        this.builder = builder;
        connect();
    }

    @Override
    public void sftp() throws JSchException, IOException {
    }


    public static class Builder extends AbstractBuilder {

        @Override
        public Builder resultStrategy(JschClientObtainResultStrategy resultStrategy) {
            return (Builder) super.resultStrategy(resultStrategy);
        }

        @Override
        public Builder username(String username) {
            return (Builder) super.username(username);
        }

        @Override
        public Builder userInfo(UserInfo userInfo) {
            return (Builder) super.userInfo(userInfo);
        }

        @Override
        public Builder host(String host) {
            return (Builder) super.host(host);
        }

        @Override
        public Builder port(Integer port) {
            return (Builder) super.port(port);
        }

        @Override
        public Builder sessionConnectTimeout(Integer sessionConnectTimeout) {
            return (Builder) super.sessionConnectTimeout(sessionConnectTimeout);
        }

        @Override
        public Builder channelConnectTimeout(Integer channelConnectTimeout) {
            return (Builder) super.channelConnectTimeout(channelConnectTimeout);
        }

        @Override
        public JschSftpClient build() {
            return new JschSftpClient(this);
        }
    }
}