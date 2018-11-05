package com.t0mpi9.client.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.client.AbstractJschClient;
import com.t0mpi9.client.JschClientObtainResultStrategy;
import com.t0mpi9.client.exception.JschClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class SftpClient extends AbstractJschClient implements SftpCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(SftpClient.class);

    private final static String SPACE = " ";

    private Builder builder;
    private volatile ChannelSftp channelSftp;

    private SftpClient(Builder builder) {
        super(builder);
        this.builder = builder;
        sessionConnect();
        try {
            initChannel();
        } catch (JSchException e) {
            throw new JschClientException(e);
        }
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
    public void sftp(String command) throws SftpException {
        if (command == null || command.length() == 0) {
            return;
        }
        String cmd = obtainCmd(command);
        String[] ps = obtainParams(command);
        if (verifyParams(ps, cmd)) {
            return;
        }
        switch (cmd) {
            case QUIT:
                channelSftp.quit();
                break;
            case EXIT:
                channelSftp.exit();
                break;
            case RE_KEY:
                break;
            case CD:
                channelSftp.cd(ps[0]);
                break;
            case LCD:
                channelSftp.lcd(ps[0]);
                break;
            case RM:
                break;
            case RMDIR:
                break;
            case MKDIR:
                break;

            default:
                LOGGER.warn("sftp command {} not support", command);
                break;
        }
    }

    @Override
    public void reConnect() {
        try {
            initChannel();
        } catch (JSchException e) {
            throw new JschClientException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return channelSftp != null && channelSftp.isConnected();
    }

    @Override
    public void close() {
        if (Objects.nonNull(channelSftp) && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        super.close();
    }

    private boolean verifyParams(String[] ps, String cmd) {
        boolean isInvalid = ((CD.equals(cmd) || LCD.equals(cmd) || RM.equals(cmd) || RMDIR.equals(cmd) || MKDIR.equals(cmd))
                && (ps == null || ps.length < 1))
                || ((CD.equals(cmd) || LCD.equals(cmd)) && (ps == null || ps.length < 2));
        if (isInvalid) {
            LOGGER.error("命令参数错误");
            return false;
        }
        return true;
    }

    private String[] obtainParams(String command) {
        if (!command.contains(SPACE)) {
            return new String[]{};
        }
        return command.substring(command.indexOf(SPACE)).trim().split(SPACE);
    }

    private String obtainCmd(String command) {
        return command.split(SPACE)[0];
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
        public SftpClient build() {
            return new SftpClient(this);
        }
    }
}
