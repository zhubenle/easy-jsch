package com.t0mpi9.client.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.client.AbstractJschClient;
import com.t0mpi9.client.JschClientObtainResultStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class JschSftpClient extends AbstractJschClient implements JschSftpCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(JschSftpClient.class);

    private Builder builder;
    private volatile ChannelSftp channelSftp;

    private JschSftpClient(Builder builder) {
        super(builder);
        this.builder = builder;
        sessionConnect();
    }

    private void initChannel() throws JSchException {
        if (Objects.isNull(channelSftp) || !channelSftp.isConnected()) {
            synchronized (this) {
                if (Objects.isNull(channelSftp) || !channelSftp.isConnected()) {
                    channelSftp = (ChannelSftp) session.openChannel("shell");
                    channelSftp.connect(builder.channelConnectTimeout);
                }
            }
        }
    }

    @Override
    public void close() {
        if (Objects.nonNull(channelSftp) && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        super.close();
    }

    @Override
    public void sftp(String command) throws JSchException, SftpException {
        initChannel();
        if (command == null || command.length() == 0) {
            return;
        }
        String cmd = obtainCmd(command);
        String[] ps = obtainParams(command);
        if (QUIT.equals(cmd)) {
            channelSftp.quit();
            return;
        }
        if (EXIT.equals(cmd)) {
            channelSftp.exit();
            return;
        }
        if (RE_KEY.equals(cmd)) {
            return;
        }
        if(CD.equals(cmd)){
            if (ps == null || ps.length < 1) {
                return;
            }
            channelSftp.cd(ps[0]);
        }
        if(LCD.equals(cmd)){
            if (ps == null || ps.length < 1) {
                return;
            }
            channelSftp.lcd(ps[0]);
        }
        LOGGER.warn("sftp command {} not support", command);
    }

    private String[] obtainParams(String command) {
        if (!command.contains(" ")) {
            return null;
        }
        return command.substring(command.indexOf(" ")).split(" ");
    }

    private String obtainCmd(String command) {
        return command.split(" ")[0];
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
