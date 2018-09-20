package com.t0mpi9.client;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class JschShellClient extends AbstractJschClient {

    private Builder builder;
    private volatile ChannelShell channelShell;
    private volatile PrintWriter printWriter;

    private JschShellClient(Builder builder) {
        try {
            this.builder = builder;
            session = jsch.getSession(builder.username, builder.host, builder.port);
            session.setUserInfo(builder.userInfo);
            session.setTimeout(builder.sessionConnectTimeout);
            session.connect();
            initChannel();
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String exec(String command) throws JSchException, IOException {
        //空实现
        return null;
    }

    @Override
    public void shell(String shell) throws JSchException, IOException {
        initChannel();
        printWriter.println(shell);
        printWriter.flush();
    }

    private void initChannel() throws JSchException, IOException{
        if (Objects.isNull(channelShell) || !channelShell.isConnected()) {
            synchronized (this) {
                if (Objects.isNull(channelShell) || !channelShell.isConnected()) {
                    channelShell = (ChannelShell) session.openChannel("shell");
                    builder.resultStrategy.obtainResult(channelShell.getInputStream(), channelShell);
                    printWriter = new PrintWriter(channelShell.getOutputStream());
                    channelShell.connect(builder.channelConnectTimeout);
                }
            }
        }
    }

    @Override
    public void close() {
        if (Objects.nonNull(channelShell) && channelShell.isConnected()) {
            channelShell.disconnect();
        }
        super.close();
    }

    public static class Builder extends AbstractBuilder {

        private JschShellObtainResultStrategy resultStrategy;

        public Builder resultStrategy(JschShellObtainResultStrategy resultStrategy) {
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
        public JschShellClient build() {
            return new JschShellClient(this);
        }
    }
}
