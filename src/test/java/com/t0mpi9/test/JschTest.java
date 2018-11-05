package com.t0mpi9.test;

import com.t0mpi9.client.JschClient;
import com.t0mpi9.client.exec.ExecClient;
import com.t0mpi9.client.scp.ScpClient;
import com.t0mpi9.client.shell.ShellClient;
import com.t0mpi9.entity.JschUserInfo;
import com.t0mpi9.util.ChannelUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * <br/>
 * Created on 2018/9/19 11:15.
 *
 * @author zhubenle
 */
public class JschTest {

    @Test
    public void testScp() throws Exception {
        try (ScpClient jschScpClient = new ScpClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new JschUserInfo("Zbl0926"))
                .sessionConnectTimeout(30 * 1000)
                .channelConnectTimeout(30 * 1000)
                .build()) {

            String localFile = "/Users/benlezhu/Downloads/test.html";
            String remoteFile = "/home/zhubenle/test.html";
//            jschScpClient.scp(localFile, remoteFile, false, JschClient.ScpType.SCP_TO);
            jschScpClient.scp(localFile, remoteFile, false, JschClient.ScpType.SCP_FROM);
        }
    }

    @Test
    public void testExec() throws Exception {
        try (ExecClient jschExecClient = new ExecClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new JschUserInfo("Zbl0926"))
                .sessionConnectTimeout(30 * 1000)
                .channelConnectTimeout(30 * 1000)
                .resultStrategy(channel -> {
                    System.out.println(ChannelUtils.read(channel));
                })
                .build()) {

            jschExecClient.exec("ps -ef | grep tomcat");
        }
    }

    public static void main(String[] args) {
        ShellClient shellClient = new ShellClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new JschUserInfo("Zbl0926"))
                .sessionConnectTimeout(30 * 1000)
                .channelConnectTimeout(30 * 1000)
                .resultStrategy(channel -> new Thread(() -> {
                    try {
                        InputStream in = channel.getInputStream();
                        byte[] tmp = new byte[1024];
                        while (true) {
                            while (in.available() > 0) {
                                int i = in.read(tmp, 0, 1024);
                                if (i < 0) {
                                    break;
                                }
                                System.out.print(new String(tmp, 0, i));
                            }
                            if (channel.isClosed()) {
                                if (in.available() > 0) {
                                    continue;
                                }
                                System.out.println("exit-status: " + channel.getExitStatus());
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start())
                .build();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            shellClient.shell(scanner.next());
        }
    }
}
