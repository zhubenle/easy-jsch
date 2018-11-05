package com.t0mpi9.client.shell;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.client.AbstractJschClient;
import com.t0mpi9.client.JschClientObtainResultStrategy;
import com.t0mpi9.client.exception.JschClientException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class ShellClient extends AbstractJschClient {

    private Builder builder;
    private volatile ChannelShell channelShell;
    private volatile PrintWriter printWriter;

    private ShellClient(Builder builder) {
        super(builder);
        this.builder = builder;
        sessionConnect();
        try {
            initChannel();
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shell(String shell) {
        if (!channelShell.isConnected()) {
            throw new JschClientException("chanel is not connect");
        }
        printWriter.println(shell);
        printWriter.flush();
    }

    private void initChannel() throws JSchException, IOException{
        if (Objects.isNull(channelShell) || !channelShell.isConnected()) {
            synchronized (this) {
                if (Objects.isNull(channelShell) || !channelShell.isConnected()) {
                    channelShell = (ChannelShell) session.openChannel("shell");
                    channelShell.connect(builder.channelConnectTimeout);
                    builder.resultStrategy.obtainResult(channelShell);
                    printWriter = new PrintWriter(channelShell.getOutputStream());
                }
            }
        }
    }

    @Override
    public void reConnect() {
        try {
            initChannel();
        } catch (JSchException | IOException e) {
            throw new JschClientException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return channelShell != null && channelShell.isConnected();
    }

    @Override
    public void close() {
        if (Objects.nonNull(channelShell) && channelShell.isConnected()) {
            channelShell.disconnect();
        }
        super.close();
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
        public ShellClient build() {
            return new ShellClient(this);
        }
    }
}
