package com.t0mpi9.test;

import com.t0mpi9.client.JschExecClient;
import com.t0mpi9.entity.CustomUserInfo;
import org.junit.Test;

/**
 * <br/>
 * Created on 2018/9/19 11:15.
 *
 * @author zhubenle
 */
public class JschTest {

    @Test
    public void testJschExec() throws Exception{
        JschExecClient jschExec = new JschExecClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new CustomUserInfo("Zbl0926"))
                .build();

        System.out.println(jschExec.execute("ps -ef | grep 18080"));
        jschExec.close();
    }
}
