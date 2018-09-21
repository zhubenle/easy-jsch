package com.t0mpi9.util;

import com.jcraft.jsch.Channel;

import java.io.IOException;
import java.io.InputStream;

/**
 * <br/>
 * Created on 2018/9/19 11:10.
 *
 * @author zhubenle
 */
public class ChannelUtils {

    public static String read(Channel channel) {
        StringBuilder sb = null;
        try {
            InputStream in = channel.getInputStream();
            sb = new StringBuilder();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    sb.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
    //                System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
