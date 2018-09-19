package com.t0mpi9.test;

import com.t0mpi9.client.JschClient;
import com.t0mpi9.entity.JschUserInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * <br/>
 * Created on 2018/9/19 11:15.
 *
 * @author zhubenle
 */
public class JschTest {


    private JschClient jschClient;

    @Before
    public void before() {
        jschClient = new JschClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new JschUserInfo("Zbl0926"))
                .sessionConnectTimeout(30 * 1000)
                .channelConnectTimeout(30 * 1000)
                .build();
    }

    @Test
    public void testExec() throws Exception {
        System.out.println(jschClient.exec("ps -ef | grep 18080"));
        jschClient.close();
    }

    @Test
    public void testShell() throws Exception {
        InputStream in = new ByteArrayInputStream("ls\n".getBytes());
        jschClient.shell(in, new FileOutputStream("/Users/benlezhu/Downloads/test.txt"));
        Thread.sleep(10000);
    }
}
