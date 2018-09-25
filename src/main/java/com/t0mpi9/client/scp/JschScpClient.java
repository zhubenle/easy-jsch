package com.t0mpi9.client.scp;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.t0mpi9.client.EmptyJschClient;
import com.t0mpi9.client.JschClientObtainResultStrategy;
import com.t0mpi9.util.ChannelUtils;

import java.io.*;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class JschScpClient extends EmptyJschClient {

    private Builder builder;

    private JschScpClient(Builder builder) {
        super(builder);
        this.builder = builder;
        connect();
    }

    @Override
    public void scp(String localFile, String remoteFile, boolean pTimestamp, ScpType scpType) throws JSchException, IOException {
        switch (scpType) {
            case SCP_TO:
                scpTo(localFile, remoteFile, pTimestamp);
                break;
            case SCP_FROM:
                scpFrom(localFile, remoteFile, pTimestamp);
                break;
            default:
                break;
        }
    }

    private void scpTo(String localFile, String remoteFile, boolean pTimestamp) throws JSchException, IOException {
        String command = "scp " + (pTimestamp ? "-p" : "") + " -t " + remoteFile;
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        OutputStream out = channelExec.getOutputStream();
        InputStream in = channelExec.getInputStream();

        channelExec.connect(builder.channelConnectTimeout);

        if (ChannelUtils.checkAck(in) != 0) {
            return;
        }
        File _localFile = new File(localFile);

        if (pTimestamp) {
            command = "T" + (_localFile.lastModified() / 1000) + " 0";
            command += (" " + (_localFile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (ChannelUtils.checkAck(in) != 0) {
                return;
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long fileSize = _localFile.length();
        command = "C0644 " + fileSize + " ";
        int index = 0;
        if ((index = localFile.lastIndexOf(File.separator)) > 0) {
            command += localFile.substring(index + 1);
        } else {
            command += localFile;
        }
        command += "\n";
        out.write(command.getBytes());
        out.flush();
        if (ChannelUtils.checkAck(in) != 0) {
            return;
        }

        // send a content of lfile
        FileInputStream fis = new FileInputStream(_localFile);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) {
                break;
            }
            out.write(buf, 0, len);
            out.flush();
        }
        fis.close();
        fis = null;

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();
        if (ChannelUtils.checkAck(in) != 0) {
            return;
        }

        channelExec.disconnect();
    }

    private void scpFrom(String localFile, String remoteFile, boolean pTimestamp) throws JSchException, IOException {
        String command = "scp -f " + remoteFile;
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        OutputStream out = channelExec.getOutputStream();
        InputStream in = channelExec.getInputStream();

        channelExec.connect(builder.channelConnectTimeout);

        byte[] buf = new byte[1024];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while (true) {
            int c = ChannelUtils.checkAck(in);
            if (c != 'C') {
                break;
            }

            // read '0644 '
            in.read(buf, 0, 5);

            long fileSize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ') {
                    break;
                }
                fileSize = fileSize * 10L + (long) (buf[0] - '0');
            }

            String fileName = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    fileName = new String(buf, 0, i);
                    break;
                }
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            // read a content of lfile
            FileOutputStream fos = new FileOutputStream(localFile);
            int foo;
            while (true) {
                if (buf.length < fileSize) {
                    foo = buf.length;
                } else {
                    foo = (int) fileSize;
                }
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                fos.write(buf, 0, foo);
                fileSize -= foo;
                if (fileSize == 0L) {
                    break;
                }
            }
            fos.close();
            fos = null;

            if (ChannelUtils.checkAck(in) != 0) {
                return;
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
        }
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
        public JschScpClient build() {
            return new JschScpClient(this);
        }
    }
}
