package com.t0mpi9.client.sftp;

/**
 * <br/>
 * Created on 2018/9/25 15:40.
 *
 * @author zhubenle
 */
public interface JschSftpCommand {

    String QUIT = "quit";
    String EXIT = "exit";
    String RE_KEY = "rekey";
    String COMPRESSION = "compression";

    String CD = "cd";
    String LCD = "lcd";

    String RM = "rm";
    String RMDIR = "rmdir";
    String MKDIR = "mkdir";

    String CHGRP = "chgrp";
    String CHOWN = "chown";
    String CHMOD = "chmod";

    String PWD = "pwd";
    String LPWD = "lpwd";

    String LS = "ls";
    String DIR = "dir";

    String LLS = "lls";
    String LDIR = "ldir";

    String GET = "get";
    String GET_RESUME = "get-resume";
    String GET_APPEND = "get-append";

    String PUT = "put";
    String PUT_RESUME = "put-resume";
    String PUT_APPEND = "put-append";

    String LN = "ln";
    String SYMLINK = "symlink";
    String HARDLINK = "hardlink";
    String RENAME = "rename";

    String DF = "df";

    String STAT = "stat";
    String LSTAT = "lstat";

    String READLINK = "readlink";
    String REALPATH = "realpath";
    String VERSION = "version";
}
