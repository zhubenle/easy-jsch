package com.t0mpi9.client.sftp;

import com.jcraft.jsch.*;
import com.t0mpi9.client.AbstractJschClient;
import com.t0mpi9.client.JschClientObtainResultStrategy;
import com.t0mpi9.client.exception.JschClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * <br/>
 * Created on 2018/9/18 17:51.
 *
 * @author zhubenle
 */
public class SftpClient extends AbstractJschClient implements SftpCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(SftpClient.class);
    private static String help =
            "      Available commands:\n" +
                    "      * means unimplemented command.\n" +
                    "cd path                       Change remote directory to 'path'\n" +
                    "lcd path                      Change local directory to 'path'\n" +
                    "chgrp grp path                Change group of file 'path' to 'grp'\n" +
                    "chmod mode path               Change permissions of file 'path' to 'mode'\n" +
                    "chown own path                Change owner of file 'path' to 'own'\n" +
                    "df [path]                     Display statistics for current directory or\n" +
                    "                              filesystem containing 'path'\n" +
                    "help                          Display this help text\n" +
                    "get remote-path [local-path]  Download file\n" +
                    "get-resume remote-path [local-path]  Resume to download file.\n" +
                    "get-append remote-path [local-path]  Append remote file to local file\n" +
                    "hardlink oldpath newpath      Hardlink remote file\n" +
                    "*lls [ls-options [path]]      Display local directory listing\n" +
                    "ln oldpath newpath            Symlink remote file\n" +
                    "*lmkdir path                  Create local directory\n" +
                    "lpwd                          Print local working directory\n" +
                    "ls [path]                     Display remote directory listing\n" +
                    "*lumask umask                 Set local umask to 'umask'\n" +
                    "mkdir path                    Create remote directory\n" +
                    "put local-path [remote-path]  Upload file\n" +
                    "put-resume local-path [remote-path]  Resume to upload file\n" +
                    "put-append local-path [remote-path]  Append local file to remote file.\n" +
                    "pwd                           Display remote working directory\n" +
                    "stat path                     Display info about path\n" +
                    "exit                          Quit sftp\n" +
                    "quit                          Quit sftp\n" +
                    "rename oldpath newpath        Rename remote file\n" +
                    "rmdir path                    Remove remote directory\n" +
                    "rm path                       Delete remote file\n" +
                    "symlink oldpath newpath       Symlink remote file\n" +
                    "readlink path                 Check the target of a symbolic link\n" +
                    "realpath path                 Canonicalize the path\n" +
                    "rekey                         Key re-exchanging\n" +
                    "compression level             Packet compression will be enabled\n" +
                    "version                       Show SFTP version\n" +
                    "?                             Synonym for help";
    private final static String SPACE = " ";
    private final static String DOT = ".";
    private final static Pattern PATTERN_NUMBER= Pattern.compile("\\d+");
    private static List<String> LAST_ONE_PARAM = Arrays.asList(CD, LCD, RM, RMDIR, MKDIR, GET, GET_RESUME,
            GET_APPEND, PUT, PUT_RESUME, PUT_APPEND, STAT, LSTAT, READLINK, REALPATH);
    private static List<String> LAST_TWO_PARAM = Arrays.asList(CHGRP, CHMOD, CHOWN, LN, SYMLINK, RENAME, HARDLINK);

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
                    channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect(builder.channelConnectTimeout);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sftp(String command) throws SftpException {
        if (command == null || command.length() == 0) {
            return;
        }
        String cmd = obtainCmd(command);
        String[] ps = obtainParams(command);
        if (!verifyParams(ps, cmd)) {
            return;
        }
        String result = "";
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
                channelSftp.rm(ps[0]);
                break;
            case RMDIR:
                channelSftp.rmdir(ps[0]);
                break;
            case MKDIR:
                channelSftp.mkdir(ps[0]);
                break;
            case CHGRP:
            case CHOWN:
                int foo = Integer.parseInt(ps[0]);
                if (CHGRP.equals(cmd)) {
                    channelSftp.chgrp(foo, ps[1]);
                } else {
                    channelSftp.chown(foo, ps[1]);
                }
                break;
            case CHMOD:
                byte[] bar = ps[0].getBytes();
                foo = 0;
                int k;
                for (byte aBar : bar) {
                    k = aBar;
                    if (k < '0' || k > '7') {
                        foo = -1;
                        break;
                    }
                    foo <<= 3;
                    foo |= (k - '0');
                }
                channelSftp.chmod(foo, ps[1]);
                break;
            case PWD:
                result = channelSftp.pwd();
                break;
            case LPWD:
                result = channelSftp.lpwd();
                break;
            case LS:
            case DIR:
                Vector<ChannelSftp.LsEntry> v = channelSftp.ls(ps.length == 0 ? DOT : ps[0]);
                if (Objects.nonNull(v)) {
                    v.forEach(lsEntry -> builder.resultStrategy.obtainResult(lsEntry.getLongname()));
                }
                return;
            case LLS:
            case LDIR:
                File file = new File(ps.length == 0 ? DOT : ps[0]);
                if (!file.exists()) {
                    System.out.println(ps[0] + ": No such file or directory");
                    break;
                }
                if (file.isDirectory()) {
                    File[] list = file.listFiles();
                    assert list != null;
                    for (File f : list) {
                        builder.resultStrategy.obtainResult(f.getName());
                    }
                }
                return;
            case GET:
            case GET_RESUME:
            case GET_APPEND:
                int mode = ChannelSftp.OVERWRITE;
                if (GET_RESUME.equals(cmd)) {
                    mode = ChannelSftp.RESUME;
                } else if (GET_APPEND.equals(cmd)) {
                    mode = ChannelSftp.APPEND;
                }
                channelSftp.get(ps[0], ps.length > 1 ? ps[1] : DOT, new MyProgressMonitor(), mode);
                break;
            case PUT:
            case PUT_RESUME:
            case PUT_APPEND:
                mode = ChannelSftp.OVERWRITE;
                if (PUT_RESUME.equals(cmd)) {
                    mode = ChannelSftp.RESUME;
                } else if (PUT_APPEND.equals(cmd)) {
                    mode = ChannelSftp.APPEND;
                }
                channelSftp.put(ps[0], ps.length > 1 ? ps[1] : DOT, new MyProgressMonitor(), mode);
                break;
            case LN:
            case SYMLINK:
                channelSftp.symlink(ps[0], ps[1]);
                break;
            case RENAME:
                channelSftp.rename(ps[0], ps[1]);
                break;
            case HARDLINK:
                channelSftp.hardlink(ps[0], ps[1]);
                break;
            case DF:
                SftpStatVFS statVFS = channelSftp.statVFS(ps.length == 0 ? DOT : ps[0]);
                System.out.printf("Size: %d Used: %d Avail %d (root): %d %%Capacity: %d", statVFS.getSize(), statVFS.getAvail(),
                        statVFS.getAvailForNonRoot(), statVFS.getAvail(), statVFS.getCapacity());
                break;
            case STAT:
                SftpATTRS attrs = channelSftp.stat(ps[0]);
                System.out.println(attrs);
                break;
            case LSTAT:
                attrs = channelSftp.lstat(ps[0]);
                System.out.println(attrs);
                break;
            case READLINK:
                result = channelSftp.readlink(ps[0]);
                break;
            case REALPATH:
                result = channelSftp.realpath(ps[0]);
                break;
            case VERSION:
                result = channelSftp.version();
                break;
            case HELP:
            case QUESTION_MARK:
                result = help;
                break;
            default:
                result = help;
                LOGGER.warn("sftp command {} not support", command);
                break;
        }
        builder.resultStrategy.obtainResult(result);
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

    /**
     * 判断参数是否是数字
     * @param v 字符参数
     * @return 是否是数字
     */
    private boolean isNumber(String v) {
        return PATTERN_NUMBER.matcher(v).matches();
    }

    /**
     * 校验参数数量
     * @param ps 参数数组
     * @param cmd 命令
     * @return 是否有效
     */
    private boolean verifyParams(String[] ps, String cmd) {
        boolean isInvalid = (LAST_ONE_PARAM.contains(cmd) && (ps == null || ps.length < 1))
                || (LAST_TWO_PARAM.contains(cmd) && (ps == null || ps.length < 2));
        if (isInvalid) {
            LOGGER.error("命令参数错误");
            return false;
        }
        return true;
    }

    /**
     * 获取输入命令里面的参数数组
     * @param command 输入命令
     * @return 参数数组
     */
    private String[] obtainParams(String command) {
        if (!command.contains(SPACE)) {
            return new String[]{};
        }
        return command.substring(command.indexOf(SPACE)).trim().split(SPACE);
    }

    /**
     * 获取输入命令里面的命令
     * @param command 输入命令
     * @return 命令
     */
    private String obtainCmd(String command) {
        return command.split(SPACE)[0];
    }

    /**
     * 进度监视器
     */
    public class MyProgressMonitor implements SftpProgressMonitor {
        ProgressMonitor monitor;
        long count = 0;
        long max = 0;

        @Override
        public void init(int op, String src, String dest, long max) {
            this.max = max;
            monitor = new ProgressMonitor(null,
                    ((op == SftpProgressMonitor.PUT) ? PUT : GET) + ": " + src,
                    "",
                    0, (int) max);
            count = 0;
            percent = -1;
            monitor.setProgress((int) this.count);
            monitor.setMillisToDecideToPopup(1000);
        }

        private long percent = -1;

        @Override
        public boolean count(long count) {
            this.count += count;

            if (percent >= this.count * 100 / max) {
                return true;
            }
            percent = this.count * 100 / max;

            monitor.setNote("Completed " + this.count + "(" + percent + "%) out of " + max + ".");
            monitor.setProgress((int) this.count);

            return !(monitor.isCanceled());
        }

        @Override
        public void end() {
            monitor.close();
        }
    }

    /**
     * 建造者类
     */
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
