package com.t0mpi9.test;

import com.t0mpi9.client.JschExecClient;
import com.t0mpi9.client.JschShellClient;
import com.t0mpi9.entity.JschUserInfo;
import org.junit.Test;

import java.io.IOException;

/**
 * <br/>
 * Created on 2018/9/19 11:15.
 *
 * @author zhubenle
 */
public class JschTest {

    @Test
    public void testExec() throws Exception {
        try (JschExecClient jschExecClient = new JschExecClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new JschUserInfo("Zbl0926"))
                .sessionConnectTimeout(30 * 1000)
                .channelConnectTimeout(30 * 1000)
                .build()) {

            System.out.println(jschExecClient.exec("ps -ef | grep 18080"));
        }
    }

    @Test
    public void testShell() throws Exception {
        JschShellClient shellClient = new JschShellClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new JschUserInfo("Zbl0926"))
                .sessionConnectTimeout(30 * 1000)
                .channelConnectTimeout(30 * 1000)
                .resultStrategy((in, channel) -> new Thread(() -> {
                    try {
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
//                                System.out.println("exit-status: " + channel.getExitStatus());
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start())
                .build();

        Thread.sleep(2000);
        shellClient.shell("ls");
        Thread.sleep(2000);
        shellClient.shell("ps -ef | grep 18080");
        Thread.sleep(2000);
    }
}
