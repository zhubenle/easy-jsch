package com.t0mpi9.test;

import com.t0mpi9.client.JschClient;
import com.t0mpi9.entity.JschUserInfo;
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
        try (JschClient jschExec = new JschClient
                .Builder()
                .host("192.168.1.250")
                .port(22)
                .username("zhubenle")
                .userInfo(new JschUserInfo("Zbl0926"))
                .build()){
            System.out.println(jschExec.execute("ps -ef | grep 18080"));
        }
    }
}
